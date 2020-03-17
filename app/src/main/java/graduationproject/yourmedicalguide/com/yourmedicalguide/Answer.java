package graduationproject.yourmedicalguide.com.yourmedicalguide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Itachi on 04/05/2018.
 */
public class Answer {
    @SerializedName("success")
    @Expose
    private String check;
    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }
}
