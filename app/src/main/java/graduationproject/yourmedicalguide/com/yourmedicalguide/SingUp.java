package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
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

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SingUp extends AppCompatActivity {

    static SingUp activity;
    EditText Fname,Lname,Password,confirmPass,Phone;
    TextView Check1 ,Check2,Check3 ,Check4,Check5;
    LinearLayout l;
    int  check1=0 ,check2=0,check3=0 ,check4=0,check5=0 ,check;
    Button Register;
    String fname,lname,pass="",phone,cpass;
    CheckInternetConnection checkInternetConnection;
    Retrofit retrofit;
    ConnectionAPI connect;
    ProgressBar progressBar;
    SharedPreferences s;
    SharedPreferences.Editor e;
    OkHttpClient okHttpClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        init();
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

        Fname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    fname=Fname.getText().toString();
                    if (fname.length() <3){
                        Check1.setText("يجب ان لا يقل الاسم عن ثلاث احرف");
                        check1=0;
                    }else{
                        Check1.setText("");
                        check1=1;
                    }
                }
            }
        });
        Lname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    lname=Lname.getText().toString();
                    if (lname.length() <3){
                        Check2.setText("يجب ان لا يقل الاسم عن ثلاث احرف");
                        check2=0;
                    }else{
                        Check2.setText("");
                        check2=1;
                    }
                }
            }
        });
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    pass=Password.getText().toString();
                    if (pass.length() <7){
                        Check3.setText("يجب ان لا تقل كلمة المرور عن سبع احرف");
                        check3=0;
                    }else{
                        Check3.setText("");
                        check3=1;
                    }
                }
            }
        });
        confirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    cpass=confirmPass.getText().toString();
                    if (!pass.equals(cpass)){
                        Check4.setText("كلمة المرور غير متطابقة");
                        check4=0;
                    }else{
                        Check4.setText("");
                        check4=1;
                    }
                }
            }
        });
        Phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    phone=Phone.getText().toString();
                    if (phone.length()!= 11){
                        Check5.setText("رقم الهاتف غير صحيح");
                        check5=0;
                    }else{
                        Check5.setText("");
                        check5=1;
                    }
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection.isConnecting()) {
                    View current = getCurrentFocus();
                    if (current != null)
                        current.clearFocus();
                    check = check1+check2+check3+check4+check5;
                    if(check ==5){
                        Register.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        register();
                    }else {
                        Toast.makeText(SingUp.this,"قم بملئ الحقول بطريقة صحيحة",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SingUp.this,"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void register() {
        connect.register(fname,lname,phone,pass).enqueue(new Callback<Answer>() {
            @Override
            public void onResponse(Call<Answer> call, Response<Answer> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(SingUp.this, "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();
                    Register.setEnabled(true);
                    progressBar.setVisibility(View.INVISIBLE);
                }else{
                    if (response.body().getCheck().equals("true")){
                        Intent intent = new Intent(SingUp.this, Search.class);
                        s = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        e = s.edit();
                        e.putString("state", "true");
                        e.putString("userName",fname+" "+lname);
                        e.putString("phone", phone);
                        e.apply();
                        startActivity(intent);
                        MainActivity.getInstance().finish();
                        finish();
                    }else if(response.body().getCheck().equals("user")){
                        Check5.setText("المستخدم موجود مسبقا");
                        Register.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(SingUp.this, "حدث خطأ مفاجئ حاول مرة اخرى", Toast.LENGTH_SHORT).show();
                        Register.setEnabled(true);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Answer> call, Throwable t) {
                Log.e("error",t.getMessage());
                Toast.makeText(SingUp.this, "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                Register.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void init() {
        Fname = (EditText)findViewById(R.id.SingUp_edtxt_fname);
        Lname = (EditText)findViewById(R.id.SingUp_edtxt_lname);
        Password = (EditText)findViewById(R.id.SingUp_edtxt_pass);
        Phone = (EditText)findViewById(R.id.SingUp_edtxt_phone);
        confirmPass = (EditText)findViewById(R.id.SingUp_edtxt_confirm_pass);
        Register = (Button)findViewById(R.id.SingUp_btn);
        progressBar=(ProgressBar)findViewById(R.id.progressBar3);
        Check1 = (TextView)findViewById(R.id.check1);
        Check2 = (TextView)findViewById(R.id.check2);
        Check3 = (TextView)findViewById(R.id.check3);
        Check4 = (TextView)findViewById(R.id.check4);
        Check5 = (TextView)findViewById(R.id.check5);
        l = (LinearLayout) findViewById(R.id.lin1);
        MyClass.setFont(l,MyClass.tf2,this);
        activity =this;
        checkInternetConnection=new CheckInternetConnection(this);
        okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

    }


}
