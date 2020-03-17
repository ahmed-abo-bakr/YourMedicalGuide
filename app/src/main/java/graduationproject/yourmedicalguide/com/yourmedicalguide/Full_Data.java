package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//8
public class Full_Data extends AppCompatActivity implements OnMapReadyCallback {
    TextView name,address,phone,dept,description,noComments;
    ImageView refreshComments;
    ProgressBar progressBar;
    String deptarab,place,fragment;
    float rating;
    EditText eComment;
    Button Save,Cancel,GoToLogIn,Logout,Guides;
    ImageView Share,sendComment;
    RatingBar Rating,RatingViewer;
    SharedPreferences s;
    ArrayList<ListItem> data;
    String userName;
    ArrayList<CommentItem> comments;
    ListView Comments;
    ScrollView mainScrollView;
    CheckInternetConnection checkInternetConnection;
    Retrofit retrofit;
    ConnectionAPI connect;
    AlertDialog.Builder B;
    AlertDialog dialog;
    AlertDialog.Builder B1;
    AlertDialog dialog1;
    LinearLayout l;
    OkHttpClient okHttpClient;
    int colorKill;
    int darkBlue;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full__data);
        init();
        Typeface tf1 = Typeface.createFromAsset(getAssets(), "font1.ttf");
        Save.setTypeface(tf1);
        Cancel.setTypeface(tf1);

        if (!place.equals("Labs")){
            Guides.setVisibility(View.GONE);
        }
        name.setText(data.get(0).Name);
        dept.setText(deptarab);
        address.setText(data.get(0).address);
        phone.setText(data.get(0).phone);
        description.setText(data.get(0).Desc);
        userName=s.getString("userName","NoUser");
        if (userName.equals("NoUser")){
            eComment.setVisibility(View.GONE);
            sendComment.setVisibility(View.GONE);
            GoToLogIn.setVisibility(View.VISIBLE);
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionAPI.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        connect = retrofit.create(ConnectionAPI.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showComments();
        getDocRating();
        Guides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });
        RatingViewer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (!userName.equals("NoUser")) {
                        Save.setEnabled(false);
                        Save.setBackgroundResource(R.drawable.button_disabled);
                        dialog.show();
                        getUserRating();
                    }else {
                        Toast.makeText(Full_Data.this,"سجل اولا لكي تتمكن من التقييم",Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        GoToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Full_Data.this,MainActivity.class);
                startActivity(i);
                Search_Result.getInstance().finish();
                Search.getInstance().finish();
                MainActivity.getInstance().finish();
                finish();
            }
        });
        if(userName.equals("NoUser")){
            Logout.setEnabled(false);
        }
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor e = s.edit();
                e.putString("state", "false");
                e.putString("userName", "NoUser");
                e.putString("phone","");
                e.apply();
                Intent intent = new Intent(Full_Data.this,MainActivity.class);
                Search_Result.getInstance().finish();
                Search.getInstance().finish();
                finish();
                startActivity(intent);
            }
        });
        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PackageManager pm = getPackageManager();
                    ApplicationInfo ai = pm.getApplicationInfo(getPackageName(), 0);
                    File srcFile = new File(ai.publicSourceDir);
                    Intent share = new Intent();
                    share.setAction(Intent.ACTION_SEND);
                    share.setType("application/vnd.android.package-archive");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
                    startActivity(Intent.createChooser(share, "PersianCoders"));
                } catch (Exception e) {
                    Log.e("ShareApp", e.getMessage());
                }
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection.isConnecting()) {
                    if (rating != Rating.getRating()) {
                        setUserRating();
                    }else{
                        Toast.makeText(Full_Data.this, "لقد قيمت بهذا التقييم مسبقا", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Full_Data.this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rating.setRating(0);
                dialog.cancel();
            }
        });

        refreshComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshComments.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                showComments();
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInternetConnection.isConnecting()) {
                    sendComment.setEnabled(false);
                    if (!eComment.getText().toString().trim().isEmpty()) {
                        sendComment();
                    } else {
                        Toast.makeText(Full_Data.this, "من فضلك قم بملئ الحقل التالى", Toast.LENGTH_SHORT).show();
                        sendComment.setEnabled(true);
                    }
                }else{
                    Toast.makeText(Full_Data.this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
                }
            }

        });
        enableNestedScroll();
    }
    public void getUserRating(){
        if (checkInternetConnection.isConnecting()) {

            connect.getUserRating(data.get(0).Name,userName).enqueue(new Callback<UserRate>() {
                @Override
                public void onResponse(Call<UserRate> call, Response<UserRate> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(Full_Data.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();

                    } else {
                        if (response.body().getUserRate()==6){
                            rating=response.body().getUserRate();
                            Rating.setRating(0);
                            Save.setEnabled(true);
                            Save.setBackgroundResource(R.drawable.btn_bk2);
                        }else {
                            Rating.setRating(response.body().getUserRate());
                            rating=response.body().getUserRate();
                            Save.setEnabled(true);
                            Save.setBackgroundResource(R.drawable.btn_bk2);
                        }
                    }
                }

                @Override
                public void onFailure(Call<UserRate> call, Throwable t) {
                    Log.e("errorrrrr",t.getMessage());
                    Toast.makeText(Full_Data.this, "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                    getUserRating();
                }
            });
        }else{
            Toast.makeText(Full_Data.this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
        }
    }
    public void getDocRating(){
        if (checkInternetConnection.isConnecting()) {
            connect.getDocRating(data.get(0).Name).enqueue(new Callback<Rate>() {
                @Override
                public void onResponse(Call<Rate> call, Response<Rate> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(Full_Data.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();
                    } else {
                        if (response.body().getRate()==6){
                            RatingViewer.setRating(0);
                        }else {
                            RatingViewer.setRating(response.body().getRate());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Rate> call, Throwable t) {
                    Log.e("error",t.getMessage());
                    Toast.makeText(Full_Data.this, "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            if (!fragment.equals("NoInternet")) {
                Toast.makeText(Full_Data.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void showComments(){
        if (checkInternetConnection.isConnecting()) {
            connect.showComments(data.get(0).Name).enqueue(new Callback<ArrayList<Comment>>() {
                @Override
                public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(Full_Data.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        if (response.body().size() != 0) {
                            comments.clear();
                            for (int i = 0; i < response.body().size(); i++) {
                                comments.add(i, new CommentItem(response.body().get(i).getUsername(),
                                        response.body().get(i).getComment(),
                                        response.body().get(i).getDate()));
                            }
                            Adapter adapter = new Adapter(comments);
                            Comments.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }else{
                            noComments.setVisibility(View.VISIBLE);
                            noComments.setText("لا يوجد تعليقات لهذا المنشأ الطبي");
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                    Toast.makeText(Full_Data.this, "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                    refreshComments.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else{
            noComments.setVisibility(View.VISIBLE);
            noComments.setText("لا يوجدتعليقات لعدم الاتصال بالانترنت");
            if (!fragment.equals("NoInternet")) {
                Toast.makeText(Full_Data.this, "لا يوجد اتصال بالانترنت", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setUserRating(){
        Save.setEnabled(false);
        Save.setBackgroundResource(R.drawable.button_disabled);
        connect.rating(place, deptarab, data.get(0).Name, userName, Rating.getRating()).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Full_Data.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();
                    Save.setEnabled(true);
                    Save.setBackgroundResource(R.drawable.btn_bk2);
                } else {
                    if (response.body().getCheck().equals("true")) {
                        Toast.makeText(Full_Data.this, "تم التقييم بنجاح", Toast.LENGTH_SHORT).show();
                        Save.setEnabled(true);
                        Save.setBackgroundResource(R.drawable.btn_bk2);
                        dialog.cancel();
                        getDocRating();
                    } else {
                        Toast.makeText(Full_Data.this, "لم يتم التقييم", Toast.LENGTH_SHORT).show();
                        Save.setEnabled(true);
                        Save.setBackgroundResource(R.drawable.btn_bk2);
                    }
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.e("error", t.getMessage());
                Toast.makeText(Full_Data.this, "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                Save.setEnabled(true);
                Save.setBackgroundResource(R.drawable.btn_bk2);
            }
        });
    }

    private void sendComment(){
        connect.addComment(userName, data.get(0).Name, eComment.getText().toString()).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Full_Data.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();
                    sendComment.setEnabled(true);
                } else {
                    if (response.body().getCheck().equals("true")) {
                        Toast.makeText(Full_Data.this, "تم اضافة التعليق بنجاح", Toast.LENGTH_SHORT).show();
                        sendComment.setEnabled(true);
                        eComment.setText("");
                        noComments.setVisibility(View.GONE);
                        showComments();
                    } else {
                        Toast.makeText(Full_Data.this, "لم يتم اضافة التعليق", Toast.LENGTH_SHORT).show();
                        sendComment.setEnabled(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Toast.makeText(Full_Data.this, "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                sendComment.setEnabled(true);
            }
        });
    }

    private void enableNestedScroll(){
        Comments.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if(Comments.getChildCount() != 0) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
            }
        });
    }

    private void init() {
        eComment = (EditText)findViewById(R.id.comment_etxt);
        sendComment = (ImageView)findViewById(R.id.send_comment);
        GoToLogIn = (Button)findViewById(R.id.gologin);
        Logout = (Button)findViewById(R.id.logout);
        Guides = (Button)findViewById(R.id.guides);
        Share = (ImageView)findViewById(R.id.share);
        l = (LinearLayout) findViewById(R.id.lin1);
        refreshComments=(ImageView)findViewById(R.id.refresh);
        progressBar=(ProgressBar) findViewById(R.id.progressBar_comments);
        MyClass.setFont(l,MyClass.tf1,this);
        checkInternetConnection = new CheckInternetConnection(this);
        Intent intent =getIntent();
        place=intent.getStringExtra("place");
        deptarab=intent.getStringExtra("deptarab");
        fragment=intent.getStringExtra("fragment");
        name=(TextView)findViewById(R.id.Full_Data_docName);
        dept=(TextView)findViewById(R.id.Full_Data_dept);
        address=(TextView)findViewById(R.id.Full_Data_address);
        phone=(TextView)findViewById(R.id.Full_Data_phone);
        description=(TextView)findViewById(R.id.Full_Data_information);
        noComments=(TextView)findViewById(R.id.no_comments);
        noComments.setVisibility(View.GONE);
        Comments =(ListView)findViewById(R.id.comments_listview);
        mainScrollView =(ScrollView)findViewById(R.id.scroll);
        RatingViewer =(RatingBar) findViewById(R.id.ratingBar);
        comments=new ArrayList<CommentItem>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        DataBase db =new DataBase(this);
        data=db.getAllData(intent.getStringExtra("name"),intent.getStringExtra("place"));
        s = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        B=new AlertDialog.Builder(Full_Data.this);
        View v=getLayoutInflater().inflate(R.layout.rating_dialog,null);
        Rating=(RatingBar)v.findViewById(R.id.ratingBar1);
        Rating.setNumStars(5);
        Rating.setStepSize(1);
        Save=(Button) v.findViewById(R.id.rate_save);
        Cancel=(Button) v.findViewById(R.id.rate_cancel);
        B.setView(v);
        dialog=B.create();
        B1=new AlertDialog.Builder(Full_Data.this);
        View v1=getLayoutInflater().inflate(R.layout.guides,null);
        LinearLayout l5 = (LinearLayout)v1.findViewById(R.id.lin1);
        MyClass.setFont(l5,MyClass.tf2,this);
        B1.setView(v1);
        dialog1=B1.create();
        colorKill = Color.parseColor("#406686");
        darkBlue = Color.parseColor("#004A7D");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        LatLng location = new LatLng(data.get(0).latitude,data.get(0).longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(data.get(0).Name)).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    class Adapter extends BaseAdapter {

        ArrayList<CommentItem> array = new ArrayList<CommentItem>();
        public Adapter( ArrayList<CommentItem> array) {

            this.array = array;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public Object getItem(int position) {
            return array.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View v = layoutInflater.inflate(R.layout.comment_row,null);
            TextView UserName = (TextView)v.findViewById(R.id.comment_row_username);
            TextView Comment = (TextView)v.findViewById(R.id.comment);
            TextView Date = (TextView)v.findViewById(R.id.comment_date);
            UserName.setText(array.get(position).userName);
            Comment.setText(array.get(position).comment);
            Date.setText(array.get(position).date);
            return v;
        }
    }
}
