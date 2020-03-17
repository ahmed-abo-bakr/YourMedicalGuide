package graduationproject.yourmedicalguide.com.yourmedicalguide;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Itachi on 21/05/2018.
 */
public interface ConnectionAPI {
    String baseUrl="https://morganrozza1996.000webhostapp.com/";

    @FormUrlEncoded
    @POST("logIn.php")
    Call<Answer> logIn(@Field("phone") String phone
            ,@Field("password") String password);


    @FormUrlEncoded
    @POST("register.php")
    Call<Answer> register(@Field("firstname") String firstname
            ,@Field("lastname") String lastname
            ,@Field("phone") String phone
            ,@Field("password") String password);


    @FormUrlEncoded
    @POST("addComment.php")
    Call<Answer> addComment(@Field("username") String username
            ,@Field("docname") String docname
            ,@Field("comment") String comment);


    @FormUrlEncoded
    @POST("showCommnts.php")
    Call<ArrayList<Comment>> showComments(@Field("docname") String docname);

    @FormUrlEncoded
    @POST("rate.php")
    Call<Answer> rating(@Field("place") String place,
                        @Field("dept") String dept,
                        @Field("docname") String docname,
                        @Field("username") String username,
                        @Field("rate") float rate);

    @FormUrlEncoded
    @POST("getUserRate.php")
    Call<UserRate> getUserRating(@Field("docname") String docname,
                        @Field("username") String username);

    @FormUrlEncoded
    @POST("getDocRating.php")
    Call<Rate> getDocRating(@Field("docname") String docname);

    @FormUrlEncoded
    @POST("getAllDocRating.php")
    Call<ArrayList<Ratings>> getAllDocRating(@Field("place") String place,
                                             @Field("dept") String dept);

}
