package proj.excellaglobal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sai on 3/10/17.
 */

public class TokenObject {
    @SerializedName("token")
    private String token;
    public TokenObject(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}
