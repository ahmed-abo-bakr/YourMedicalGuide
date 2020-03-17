package graduationproject.yourmedicalguide.com.yourmedicalguide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Itachi on 29/06/2018.
 */
public class Rate {
    @SerializedName("rate")
    @Expose
    private float rate;
    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
