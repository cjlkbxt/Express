package express.tutu.com.express.presenter;

import android.content.Context;

import express.tutu.com.express.network.api.UserInfoApi;
import express.tutu.com.express.network.response.UserInfoResponse;
import express.tutu.com.express.presenterinterface.IUserInfoPresenter;
import express.tutu.com.express.viewinterface.IUserInfoViewInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cjlkbxt on 2018/7/8/008.
 */

public class UserInfoPresenter implements IUserInfoPresenter{

    private Context mContext;
    private IUserInfoViewInterface mViewInterface;

    public UserInfoPresenter(Context context, IUserInfoViewInterface viewInterface) {
        this.mContext = context;
        this.mViewInterface = viewInterface;
    }
    @Override
    public void getUserInfo() {
        UserInfoApi.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {

            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {

            }
        });
    }
}
