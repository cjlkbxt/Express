package express.tutu.com.express.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cjlkbxt on 2018/7/8/008.
 */

public class UserInfoResponse extends BaseResponse {
    @SerializedName("userName")
    private String userName;
}
