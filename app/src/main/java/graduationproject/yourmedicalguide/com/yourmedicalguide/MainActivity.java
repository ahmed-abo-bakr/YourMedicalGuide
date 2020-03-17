package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    static MainActivity activity;
    TextView skip ;
    EditText Username,Password;
    Button signIn;
    TextView Register ;
    TextView Alert;
    LinearLayout l;
    ConnectionAPI connect;
    Retrofit retrofit;
    String phone;
    String pass;
    ProgressBar progressBar;
    CheckInternetConnection checkInternetConnection;
    SharedPreferences s;
    SharedPreferences.Editor e;
    OkHttpClient okHttpClient;

    public static MainActivity getInstance(){
        return   activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        MyClass.setFont(l,MyClass.tf2,this);
        retrofit = new Retrofit.Builder()
                .baseUrl(ConnectionAPI.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
        connect = retrofit.create(ConnectionAPI.class);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Alert.setVisibility(View.GONE);
                if (checkInternetConnection.isConnecting()) {
                    phone = Username.getText().toString();
                    pass = Password.getText().toString();
                    if (phone.trim().isEmpty() || pass.trim().isEmpty()) {
                        Alert.setVisibility(View.VISIBLE);
                        Alert.setText("من فضلك قم بملئ الحقول !");
                    } else {
                        signIn.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        signIn();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signIn() {
        connect.logIn(phone,pass).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();
                    signIn.setEnabled(true);
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (response.body().getCheck().equals("false")) {
                        progressBar.setVisibility(View.GONE);
                        Alert.setVisibility(View.VISIBLE);
                        Alert.setText("رقم الهاتف او كلمة المرور غير صحيحتان");
                        signIn.setEnabled(true);
                    } else {
                        Intent intent = new Intent(MainActivity.this, Search.class);
                        s = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        e = s.edit();
                        e.putString("state", "true");
                        e.putString("userName", response.body().getCheck());
                        e.putString("phone", phone);
                        e.apply();
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.e("error",t.getMessage());
                Toast.makeText(MainActivity.this, " ", Toast.LENGTH_SHORT).show();
                signIn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Search.class);
                startActivity(i);
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,SingUp.class);
                startActivity(i);
            }
        });
    }

    private void init() {
        activity=this;
        checkInternetConnection=new CheckInternetConnection(this);
        signIn =(Button)findViewById(R.id.MainActivity_btn_login);
        Username=(EditText)findViewById(R.id.MainActivity_edtxt_userName);
        Password=(EditText)findViewById(R.id.MainActivity_edtxt_password);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        skip = (TextView)findViewById(R.id.MainActivity_skip);
        Alert =(TextView)findViewById(R.id.alert);
        l = (LinearLayout) findViewById(R.id.lin1);
        Register = (TextView)findViewById(R.id.MainActivity_register);
        okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        Alert.setText("");
        Username.setText("");
        Password.setText("");
    }
}
