package express.tutu.com.express.network.okservice;

import express.tutu.com.express.network.request.EmptyRequest;
import express.tutu.com.express.network.response.UserInfoResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by cjlkbxt on 2018/7/8/008.
 */

public interface UserInfoOkService {
    @POST("/ymm-cargoorder-app/freightPaymentRelation/queryFreightPaymentRelationByCondition")
    Call<UserInfoResponse> getUserInfo(@Body EmptyRequest request);

}
