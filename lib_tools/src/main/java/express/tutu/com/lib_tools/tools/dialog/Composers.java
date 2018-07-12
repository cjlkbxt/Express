package express.tutu.com.lib_tools.tools.dialog;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by cjlkbxt on 2018/7/12/012.
 */

public class Composers {
    public static class DefList implements ItemComposer<ListView> {

        @Override
        public Object compose(@NonNull ListView parent, int index) {
            ListAdapter adapter = parent.getAdapter();
            int headerEnd = parent.getHeaderViewsCount();
            int footerStart = adapter.getCount() - parent.getFooterViewsCount();
            Object item = adapter.getItem(index);
            if (index < headerEnd) {
                return item == null ? "header_" + index : item;
            } else if (index >= footerStart) {
                return item == null ? "footer_" + (index - footerStart) : item;
            }
            return item;
        }
    }

    public static class DefAdapter implements ItemComposer<AdapterView> {

        @Override
        public Object compose(@NonNull AdapterView adapterView, int index) {
            return adapterView.getAdapter().getItem(index);
        }
    }

    public static class DefRecycler implements ItemComposer<RecyclerView> {

        @Override
        public Object compose(@NonNull RecyclerView recyclerView, int index) {
            return "recycler:" + index;
        }
    }

    public static class ViewIDName implements StrComposer<View> {
        @Override
        public String compose(@NonNull View view) {
            int id = view.getId();
            if (id != View.NO_ID && id >= 0x01000000) { // real view id > 0x01000000, added by luke.yujb
                Resources res = view.getResources();
                String entryName = null;
                try {
                    entryName = res.getResourceEntryName(id); // may cause resource not found exception
                } catch (Resources.NotFoundException ignored) {}
                return entryName;
            }
            return view.getClass().getSimpleName();
        }
    }

    public static class FragmentTagName implements StrComposer<Fragment> {

        @Override
        public String compose(@NonNull Fragment fragment) {
            return fragment.getTag();
        }
    }

    public static class ClassName implements StrComposer<Object> {
        @Override
        public String compose(@NonNull Object o) {
            return o.getClass().getName();
        }
    }

    public static class SimpleName implements StrComposer<Object> {

        @Override
        public String compose(@NonNull Object o) {
            return o.getClass().getSimpleName();
        }
    }

    public static class StringOf implements StrComposer<Object> {

        @Override
        public String compose(@NonNull Object o) {
            return String.valueOf(o);
        }
    }

    public static class NULL implements StrComposer<Object> {

        @Override
        public String compose(@NonNull Object o) {
            return null;
        }
    }
}
