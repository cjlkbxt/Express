package express.tutu.com.express.network.api;

import express.tutu.com.commonsdk.network.RetrofitManager;
import express.tutu.com.commonsdk.network.request.EmptyRequest;
import express.tutu.com.express.network.okservice.UserInfoOkService;
import express.tutu.com.express.network.response.UserInfoResponse;
import retrofit2.Call;

/**
 * Created by cjlkbxt on 2018/7/8/008.
 */

public class UserInfoApi {

    public static Call<UserInfoResponse> getUserInfo(){
        return RetrofitManager.getInstance().getService(UserInfoOkService.class).getUserInfo(new EmptyRequest());
    }
}
