package graduationproject.yourmedicalguide.com.yourmedicalguide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Itachi on 29/06/2018.
 */
public class Ratings {
    @SerializedName("RATE")
    @Expose
    private float rate;
    @SerializedName("DOCTOR")
    @Expose
    private String docname;

    public Ratings(float rate, String docname) {
        this.rate = rate;
        this.docname = docname;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getDocname() {
        return docname;
    }

    public void setDocname(String docname) {
        this.docname = docname;
    }
}
