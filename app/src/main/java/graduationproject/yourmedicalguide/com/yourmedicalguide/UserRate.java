package graduationproject.yourmedicalguide.com.yourmedicalguide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Itachi on 29/06/2018.
 */
public class UserRate {
    @SerializedName("Rate")
    @Expose
    private float useRate;
    public float getUserRate() {
        return useRate;
    }

    public void setUserRate(float useRate) {
        this.useRate = useRate;
    }
}
