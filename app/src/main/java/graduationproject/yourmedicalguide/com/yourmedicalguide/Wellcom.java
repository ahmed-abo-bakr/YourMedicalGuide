package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Wellcom extends AppCompatActivity {

    Button btnsub;
    LinearLayout l1;
    RelativeLayout l2;
    Animation uptodown,downtoup;
    SharedPreferences s;
    Typeface tf1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcom);
        init();
        l1.setAnimation(uptodown);
        l2.setAnimation(downtoup);
        btnsub.setTypeface(tf1);
        btnsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = s.getString("state","false");
                if (state.equals("true")){
                    Intent i = new Intent(Wellcom.this, Search.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(Wellcom.this, MainActivity.class);
                    startActivity(i);
                }
                finish();
            }
        });
    }

    private void init() {
        btnsub = (Button) findViewById(R.id.buttonsub);
        l1 = (LinearLayout) findViewById(R.id.l1);
        l2 = (RelativeLayout) findViewById(R.id.l2);
        tf1 = Typeface.createFromAsset(getAssets(), "font1.ttf");
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        s = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

}
