package graduationproject.yourmedicalguide.com.yourmedicalguide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Itachi on 26/05/2018.
 */
public class Comment {
    @SerializedName("USERNAME")
    @Expose
    private String username;

    @SerializedName("COMMENT")
    @Expose
    private String comment;

    @SerializedName("DATE")
    @Expose
    private String date;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
