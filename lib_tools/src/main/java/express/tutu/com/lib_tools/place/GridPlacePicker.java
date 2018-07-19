package express.tutu.com.lib_tools.place;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import express.tutu.com.lib_tools.R;
import express.tutu.com.lib_tools.utils.CollectionUtil;
import express.tutu.com.lib_tools.utils.DensityUtil;

/**
 * Created by cjlkbxt on 2018/7/17/017.
 */

public class GridPlacePicker extends LinearLayout {
    //必须选市，省市没有不限
    public static final int STYLE_CITY_REQUIRED = 0x02;
    //必须选省，省没有不限
    public static final int STYLE_PROVINCE_REQUIRED = 0x06;
    //省市县都可以不选，即都会出现不限
    public static final int STYLE_NOT_REQUIRED = 0x0E;

    @IntDef({STYLE_NOT_REQUIRED, STYLE_PROVINCE_REQUIRED, STYLE_CITY_REQUIRED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RequiredStyle {
    }

    protected static final int NO_LIMIT_PLACE_CODE = -1;

    private View mBackBtn;
    private TextView mCurrentPlaceTv;
    private RecyclerView mRecyclerView;

    private GridLayoutManager mGridLayoutManager;
    private GridDividerItemDecoration mGridDividerItemDecoration;
    private PlaceAdapter mAdapter;

    private int column;
    private boolean mFilterValidPlace;
    private int mRequiredStyle = STYLE_NOT_REQUIRED;
    private int mPickDeep = Place.DEPTH_TOWN;
    //当前place，列表显示的是mCurrentPlace的children
    private Place mCurrentPlace;
    private List<Place> mSelectedList = new ArrayList<>();

    //处理直辖市问题，比如：已选择上海市，mSelectedLinearList里面会add一个List包含上海省(省级)，上海市(市级)
    private List<List<Place>> mSelectedLinealList = new ArrayList<>();

    private PlaceManager mPlaceManager;
    private OnPickListener mOnPickListener;

    /**
     * 当前列表是属于哪个地方
     */
    public Place getCurrentPlace() {
        return mCurrentPlace;
    }

    public List<Place> getSelectedPlaces() {
        return mSelectedList;
    }

    public GridPlacePicker(Context context) {
        this(context, null);
    }

    public GridPlacePicker(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridPlacePicker(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridPlacePicker);
        column = a.getInt(R.styleable.GridPlacePicker_column, 1);
        a.recycle();

        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.consignor_layout_grid_place_picker, this, true);

        mBackBtn = findViewById(R.id.back_btn);
        mCurrentPlaceTv = (TextView) findViewById(R.id.current_place_tv);
        mRecyclerView = (RecyclerView) findViewById(R.id.place_grid_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mGridLayoutManager = new GridLayoutManager(getContext(), column);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mGridDividerItemDecoration = new GridDividerItemDecoration(getContext(), column, DensityUtil.dip2px(getContext(), 6), android.R.color.transparent, false);
        mRecyclerView.addItemDecoration(mGridDividerItemDecoration);
        mAdapter = new PlaceAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setFocusable(false);

        mBackBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backToUpper();
            }
        });

    }

    public void initData() {
        mPlaceManager = mFilterValidPlace ? ValidPlaceManager.getInstance(getContext()) : PlaceManager.getInstance(getContext());
        Place nationRootPlace = mPlaceManager.getNationRootPlace();
        switchPlaceList(nationRootPlace);
    }

    public void setOnPickListener(OnPickListener onPickListener) {
        this.mOnPickListener = onPickListener;
    }

    public void setColumn(int column) {
        this.column = column;
        mGridDividerItemDecoration.setSpanCount(column);
        mGridLayoutManager.setSpanCount(column);
    }

    public void setIgnoreInvalidPlace(boolean filterValidPlace) {
        this.mFilterValidPlace = filterValidPlace;
    }

    /** 清除所有选中 */
    public void clearSelectedPlaces() {
        mSelectedList.clear();
        switchPlaceList(mCurrentPlace);
    }

    /**
     * 返回省级list
     */
    public void resetPlaceList() {
        switchPlaceList(mPlaceManager.getNationRootPlace());
    }

    public void removeSelectedPlace(Place place) {
        if (place == null) return;
        mSelectedList.remove(place);
        switchPlaceList(mCurrentPlace);
    }

    public void removeSelectedPlaces(List<Place> places) {
        if(CollectionUtil.isEmpty(places)) return;
        mSelectedList.removeAll(places);
        switchPlaceList(mCurrentPlace);
    }

    /** 替换添加 */
    public void pickPlace(Place place) {
        if(place == null) return;
        mSelectedList.clear();
        addSelectedPlace(place);
    }

    /** 替换添加 */
    public void pickPlaces(List<Place> places) {
        if(CollectionUtil.isEmpty(places)) return;
        mSelectedList.clear();
        addSelectedPlaces(places);
    }

    /**
     * 需要处理特殊情况 比如:全国-香港-香港-空，如果传入的是省级香港，会取市级香港
     * @param place
     */
    public void addSelectedPlace(Place place) {
        if (place == null) return;

        Pair<Place, List<Place>> finalChildPair = getFinalChild(place, mPickDeep);
        mSelectedList.add(finalChildPair.first);

        prepareSwitchPlaceList(place, true);
    }

    /**
     * 需要处理特殊情况 比如:全国-香港-香港-空，如果传入的是省级香港，会取市级香港
     * @param places
     */
    public void addSelectedPlaces(List<Place> places) {
        if(CollectionUtil.isEmpty(places)) {
            return;
        }

        List<Place> tempPlaces = new ArrayList<>();
        for (int i = 0; i < places.size(); i++) {
            tempPlaces.add(getFinalChild(places.get(i), mPickDeep).first);
        }
        mSelectedList.addAll(tempPlaces);

        prepareSwitchPlaceList(tempPlaces.get(tempPlaces.size() - 1), true);
    }

    /**
     *
     * @param requiredStyle
     */
    public void setRequiredStyle(@RequiredStyle int requiredStyle) {
        this.mRequiredStyle = requiredStyle;
        switchPlaceList(mCurrentPlace);
    }

    /**
     * 设置选择级别
     * @param pickDeep
     */
    public void setPickDeep(int pickDeep) {
        this.mPickDeep = pickDeep;
    }

    /**
     * 如果place为最后一级，直接则取parent切换List，否则直接切换取Place切换List
     * @param place
     */
    public void prepareSwitchPlaceList(Place place, boolean isForce) {
        Pair<Place, List<Place>> finalChildPair = getFinalChild(place, mPickDeep);

        if(CollectionUtil.isEmpty(finalChildPair.second)) {
            Pair<Place, List<Place>> finalParentPair = getFinalParent(place);
            if(isForce || !finalParentPair.first.equals(getCurrentPlace())) {
                switchPlaceList(finalParentPair.first);
            }
        } else {
            if(isForce || !finalChildPair.first.equals(getCurrentPlace())) {
                switchPlaceList(finalChildPair.first);
            }
        }
    }

    private void switchPlaceList(Place place) {
        if(place == null) return;
        List<Place> children = mPlaceManager.getChildren(place.getCode());
        if(CollectionUtil.isEmpty(children)) return;
        this.mCurrentPlace = place;

        addNoLimitItem(children);
        mAdapter.loadData(children);

        refreshViews();

        if (mOnPickListener != null) {
            mOnPickListener.onDataChange(mCurrentPlace, children);
        }
    }

    private void backToUpper() {
        if (mCurrentPlace.getCode() == 0) return;

        Pair<Place, List<Place>> finalParentPair = getFinalParent(mCurrentPlace);
        if(finalParentPair == null
                || finalParentPair.first == null
                || finalParentPair.second == null
                || finalParentPair.first.getCode() < 0) {
            return;
        }
        mCurrentPlace = finalParentPair.first;
        List<Place> children = finalParentPair.second;
        addNoLimitItem(children);
        mAdapter.loadData(children);

        refreshViews();

        if (mOnPickListener != null) {
            mOnPickListener.onDataChange(mCurrentPlace, children);
        }
    }

    private void addNoLimitItem(List<Place> placeList) {
        if (CollectionUtil.isEmpty(placeList)) return;

        int deep = mCurrentPlace.getDepth();

        if ((mRequiredStyle & (1 << (3 - deep))) != 0) {
            String noLimitPlaceName = "全部";
            switch (deep) {
                case 0:
                    noLimitPlaceName = "全国";
                    break;
                case 1:
                    noLimitPlaceName = "全省";
                    break;
                case 2:
                    noLimitPlaceName = "全市";
                    break;
            }
            placeList.add(0, new Place(NO_LIMIT_PLACE_CODE, mCurrentPlace.getCode(), noLimitPlaceName, noLimitPlaceName, deep + 1, 0.0, 0.0));
        }
    }

    private void notifyDataSetChanged() {
        mSelectedLinealList.clear();
        for (int i = 0; i < mSelectedList.size(); i++) {
            List<Place> linealList = new ArrayList<>();
            Place temp = mSelectedList.get(i);
            List<Place> children;
            do {
                linealList.add(temp);
                temp = mPlaceManager.getParent(temp);
                children = mPlaceManager.getChildren(temp.getCode());
            } while (children != null && children.size() == 1 && children.get(0).getName().equals(temp.getName()));
            mSelectedLinealList.add(linealList);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void refreshViews() {
        notifyDataSetChanged();

        String currentPlaceName = "";
        if (mCurrentPlace != null) {
            currentPlaceName = mCurrentPlace.getShortName();
        }
        mCurrentPlaceTv.setText(currentPlaceName);

        if(mCurrentPlace.getCode() == 0) {
            mBackBtn.setVisibility(View.GONE);
        } else {
            mBackBtn.setVisibility(View.VISIBLE);
        }

        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
                invalidate();
            }
        });
    }

    private Pair<Place, List<Place>> getFinalChild(Place place, int pickDeep) {
        Place temp = place;
        List<Place> children = mPlaceManager.getChildren(temp.getCode());
        if(temp.getDepth() == pickDeep) {
            return new Pair<>(temp, null);
        }
        while (children != null && children.size() == 1 && children.get(0).getName().equals(temp.getName())) {
            temp = children.get(0);
            children = mPlaceManager.getChildren(temp.getCode());
            if(temp.getDepth() == pickDeep) {
                return new Pair<>(temp, null);
            }
        }
        return new Pair<>(temp, children);
    }

    private Pair<Place, List<Place>> getFinalParent(Place place) {
        if(place != null && place.getCode() == 0) {
            return null;
        }
        Place temp = place;
        List<Place> tempChildren;
        do {
            temp = mPlaceManager.getParent(temp);
            tempChildren = mPlaceManager.getChildren(temp.getCode());
        } while (tempChildren != null && tempChildren.size() == 1 && tempChildren.get(0).getName().equals(temp.getName()));

        return new Pair<>(temp, tempChildren);
    }

    private class PlaceAdapter extends RecyclerAdapter<Place, PlaceViewHolder> {

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View placeTv = LayoutInflater.from(getContext()).inflate(R.layout.item_picker_text_view, null);
            return new PlaceViewHolder(placeTv);
        }

        @Override
        public void onBindViewHolder(PlaceViewHolder holder, int position) {
            holder.setData(getItem(position));
        }
    }

    private class PlaceViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private Place place;

        private TextView placeTv;

        private ColorStateList placeTextColor;

        public PlaceViewHolder(View itemView) {
            super(itemView);
            placeTv = (TextView) itemView;
            placeTextColor = ContextCompat.getColorStateList(getContext(), R.color.selector_place_text_color);
            itemView.setOnClickListener(this);
        }

        public void setData(Place place) {
            if (place == null) return;
            this.place = place;
            placeTv.setText(place.getShortName());

            boolean isSelected;
            if (place.getCode() == NO_LIMIT_PLACE_CODE) {
                isSelected = mCurrentPlace != null && mSelectedList.contains(mCurrentPlace);
            } else {
                isSelected = false;
                for (int i = 0; i < mSelectedLinealList.size(); i++) {
                    for (int j = 0; j < mSelectedLinealList.get(i).size(); j++) {
                        if(mSelectedLinealList.get(i).get(j).getCode() == place.getCode()) {
                            isSelected = true;
                        }
                    }
                }
            }
            StateListDrawable mStateListDrawable = (StateListDrawable) ContextCompat.getDrawable(getContext(), R.drawable.selector_place_view_bg);
            if(isSelected){
                placeTv.setTextColor(placeTextColor.getColorForState(new int[]{android.R.attr.state_selected}, 0));
                placeTv.setSelected(true);
                mStateListDrawable.setState(new int[]{android.R.attr.state_selected});
                placeTv.setBackgroundDrawable(mStateListDrawable.getCurrent());
            } else {
                placeTv.setTextColor(placeTextColor.getColorForState(new int[]{android.R.attr.state_empty}, 0));
                mStateListDrawable.setState(new int[]{android.R.attr.state_empty});
                placeTv.setBackgroundDrawable(mStateListDrawable.getCurrent());
            }
        }

        @Override
        public void onClick(View v) {
            if (place.getCode() == NO_LIMIT_PLACE_CODE) {
                if (mOnPickListener != null) {
                    mOnPickListener.onPick(mCurrentPlace);
                }
            } else {
                Pair<Place, List<Place>> finalChildPair = getFinalChild(place, mPickDeep);

                if(CollectionUtil.isEmpty(finalChildPair.second)) {
                    if (mOnPickListener != null) {
                        mOnPickListener.onPick(finalChildPair.first);
                    }
                } else {
                    switchPlaceList(finalChildPair.first);
                }
            }
        }
    }

    public interface OnPickListener {
        void onDataChange(Place parent, List<Place> children);

        void onPick(Place place);
    }
}
