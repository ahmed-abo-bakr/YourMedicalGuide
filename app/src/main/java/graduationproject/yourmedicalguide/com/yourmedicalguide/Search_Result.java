package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//6
public class Search_Result extends AppCompatActivity {
    static Search_Result activity;
    Button topRatings,nearestPlaces,noInternet;
    String place,dept,arabDept,city,check;
    NoInternetFragment noInternetFragment;
    RatingFragment ratingFragment;
    PlaceFragment placeFragment;
    FragmentManager manager;
    LinearLayout l;
    int darkBlue;

    public static Search_Result getInstance(){
        return   activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__result);
        init();
        noInternetFragment=NoInternetFragment.newInstance(place,dept,arabDept,city,check);
        ratingFragment=RatingFragment.newInstance(place,dept,arabDept,city,check);
        placeFragment=PlaceFragment.newInstance(place,dept,arabDept,city,check);
        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, noInternetFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        topRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topRatings.setBackgroundResource(R.drawable.fbuttonwhiet);
                topRatings.setTextColor(darkBlue);
                noInternet.setBackgroundResource(R.drawable.fbuttondarkblue);
                noInternet.setTextColor(Color.WHITE);
                nearestPlaces.setBackgroundResource(R.drawable.fbuttondarkblue);
                nearestPlaces.setTextColor(Color.WHITE);
                manager.beginTransaction().replace(R.id.container, ratingFragment).commit();
            }
        });

        noInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternet.setBackgroundResource(R.drawable.fbuttonwhiet);
                noInternet.setTextColor(darkBlue);
                topRatings.setBackgroundResource(R.drawable.fbuttondarkblue);
                topRatings.setTextColor(Color.WHITE);
                nearestPlaces.setBackgroundResource(R.drawable.fbuttondarkblue);
                nearestPlaces.setTextColor(Color.WHITE);
                manager.beginTransaction().replace(R.id.container, noInternetFragment).commit();
            }
        });

        nearestPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearestPlaces.setBackgroundResource(R.drawable.fbuttonwhiet);
                nearestPlaces.setTextColor(darkBlue);
                topRatings.setBackgroundResource(R.drawable.fbuttondarkblue);
                topRatings.setTextColor(Color.WHITE);
                noInternet.setBackgroundResource(R.drawable.fbuttondarkblue);
                noInternet.setTextColor(Color.WHITE);
                manager.beginTransaction().replace(R.id.container, placeFragment).commit();
            }
        });
    }

    private void init() {
        topRatings=(Button)findViewById(R.id.btn_top_ratings);
        nearestPlaces=(Button)findViewById(R.id.btn_nearest_places);
        noInternet=(Button)findViewById(R.id.btn_no_internet);
        l = (LinearLayout) findViewById(R.id.lin1);
        MyClass.setFont(l,MyClass.tf1,this);
        darkBlue = Color.parseColor("#004A7D");
        activity=this;
        Intent intent = getIntent();
        place=intent.getStringExtra("place");
        dept=intent.getStringExtra("dept");
        arabDept=intent.getStringExtra("deptarab");
        city=intent.getStringExtra("city");
        check = intent.getStringExtra("check");
    }
}
