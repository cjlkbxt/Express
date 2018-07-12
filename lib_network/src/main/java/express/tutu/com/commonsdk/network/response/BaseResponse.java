package express.tutu.com.commonsdk.network.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cjlkbxt on 2018/7/8/008.
 */

public class BaseResponse {
    @SerializedName("result")
    private int result;
    @SerializedName("errorMsg")
    private String errorMsg;

    public BaseResponse() {
    }
}
