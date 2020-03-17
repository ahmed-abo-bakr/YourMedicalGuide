package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Search extends AppCompatActivity {

    static Search activity;
    Spinner placeSpinner,deptSpinner,citySpinner;
    ArrayList Dept,analyticalDepts,radiationDepts,hospitals;
    ArrayAdapter arrayAdapter2;
    LinearLayout l;
    DataBase db;
    Button search ;
    TextView textView;

    public static Search getInstance(){
        return   activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
        File dataBase = getApplicationContext().getDatabasePath(DataBase.dbname);
        if( dataBase.exists()==false){
            db.getReadableDatabase();

            if(copyDB(this)){
                Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.activity_spinner_layout,R.id.textView8,hospitals);
        arrayAdapter.setDropDownViewResource(R.layout.activity_spinner_layout);
        placeSpinner.setAdapter(arrayAdapter);

        ArrayList city=db.getCity("Hospitals");
        final ArrayAdapter arrayAdapter1 = new ArrayAdapter(this,R.layout.activity_spinner_layout,R.id.textView8,city);
        arrayAdapter1.setDropDownViewResource(R.layout.activity_spinner_layout);
        citySpinner.setAdapter(arrayAdapter1);


    }

    @Override
    protected void onResume() {
        super.onResume();

        placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(placeSpinner.getSelectedItem().toString().equals("مستشفيات")){
                    Dept =db.getDept("Hospitals",citySpinner.getSelectedItem().toString());
                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,Dept);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);
                }else if (placeSpinner.getSelectedItem().toString().equals("عيادات")){
                    Dept =db.getDept("Clinics",citySpinner.getSelectedItem().toString());
                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,Dept);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);

                }else if (placeSpinner.getSelectedItem().toString().equals("معامل تحاليل")){

                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,analyticalDepts);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);

                }else if(placeSpinner.getSelectedItem().toString().equals("معامل اشعة")){

                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,radiationDepts);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(placeSpinner.getSelectedItem().toString().equals("مستشفيات")){
                    Dept =db.getDept("Hospitals",citySpinner.getSelectedItem().toString());
                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,Dept);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);
                }else if (placeSpinner.getSelectedItem().toString().equals("عيادات")){
                    Dept =db.getDept("Clinics",citySpinner.getSelectedItem().toString());
                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,Dept);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);

                }else if (placeSpinner.getSelectedItem().toString().equals("معامل تحاليل")){

                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,analyticalDepts);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);

                }else if(placeSpinner.getSelectedItem().toString().equals("معامل اشعة")){

                    arrayAdapter2 = new ArrayAdapter(Search.this,R.layout.activity_spinner_layout,R.id.textView8,radiationDepts);
                    arrayAdapter2.setDropDownViewResource(R.layout.activity_spinner_layout);
                    deptSpinner.setAdapter(arrayAdapter2);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Place = placeSpinner.getSelectedItem().toString();
                String deptarab = deptSpinner.getSelectedItem().toString();
                String dept = deptSpinner.getSelectedItem().toString();
                String place="";
                String check="Hospitals&Clinics";
                if(Place.equals("مستشفيات")){
                    check="Hospitals&Clinics";
                    place ="Hospitals";
                }else if(Place.equals("عيادات")){
                    check="Hospitals&Clinics";
                    place="Clinics";
                }else if(Place.equals("معامل تحاليل")){
                    check="Labs";
                    place="Labs";
                    if(dept.equals("بول") || dept.equals("دم") || dept.equals("براز") || dept.equals("سكر") ){
                        dept="common";
                        deptarab="شامل";
                    }else if(dept.equals("باثولجي")){
                        dept="Pathological";
                    }else if(dept.equals("اورام")){
                        dept="Pathology_and_tumors";
                    }else{
                        dept="Pathological_and_Stem_Cells";
                    }

                }else if(Place.equals("معامل اشعة")){
                    check="Labs";
                    place="rayLabs";
                    if(dept.equals("الاشعة المقطعية") || dept.equals("رنين مغناطيسى") || dept.equals("الاشعة السينية")
                            || dept.equals("اشعة الاسنان") || dept.equals("الموجات فوق الصوتيه والدوبلر") || dept.equals("كثافة العظام")
                            || dept.equals("تصوير الثدي الشعاعي")){
                        dept="common";
                        deptarab="شامل";
                    }else if(dept.equals("الاشعة التداخلية")){
                        dept="Intervention_XRay";
                    }else {
                        dept="Nuclear_Medicine";
                    }
                }
                Intent intent = new Intent(Search.this,Search_Result.class);
                intent.putExtra("place",place);
                intent.putExtra("dept",dept);
                intent.putExtra("deptarab",deptarab);
                intent.putExtra("city",citySpinner.getSelectedItem().toString());
                intent.putExtra("check",check);
                startActivity(intent);

            }
        });
    }

    private void init() {
        activity=this;
        search = (Button)findViewById(R.id.Search_btn_search);
        l = (LinearLayout) findViewById(R.id.lin1);
        MyClass.setFont(l,MyClass.tf1,this);
        LayoutInflater inflater=getLayoutInflater();
        View v=inflater.inflate(R.layout.activity_spinner_layout,null);
        textView=(TextView)v.findViewById(R.id.textView8);
        textView.setTextColor(Color.BLUE);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), "font2.ttf");
        textView.setTypeface(tf2);
        db = new DataBase(this);
        radiationDepts = new ArrayList();
        radiationDepts.add("رنين مغناطيسى");
        radiationDepts.add("الاشعة المقطعية");
        radiationDepts.add("الاشعة السينية");
        radiationDepts.add("اشعة الاسنان");
        radiationDepts.add("الموجات فوق الصوتيه والدوبلر");
        radiationDepts.add("كثافة العظام");
        radiationDepts.add("تصوير الثدي الشعاعي");
        radiationDepts.add("الاشعة التداخلية");
        radiationDepts.add("اشعة الطب النووي");

        analyticalDepts = new ArrayList();
        analyticalDepts.add("دم");
        analyticalDepts.add("بول");
        analyticalDepts.add("براز");
        analyticalDepts.add("باثولجي");
        analyticalDepts.add("سكر");
        analyticalDepts.add("خلايا جزعية");
        analyticalDepts.add("اورام");
        placeSpinner= (Spinner)findViewById(R.id.Search_spineer_place);
        deptSpinner=(Spinner)findViewById(R.id.Search_spinner_dept);
        citySpinner=(Spinner)findViewById(R.id.Search_spinner_city);

        hospitals = new ArrayList();
        hospitals.add("مستشفيات");
        hospitals.add("عيادات");
        hospitals.add("معامل تحاليل");
        hospitals.add("معامل اشعة");

    }

    private boolean copyDB(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DataBase.dbname);
            String outFileName = DataBase.location + DataBase.dbname;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte [] buff = new byte[1024];
            int length = 0 ;
            while ((length=inputStream.read(buff))>0){
                outputStream.write(buff,0,length);
            }
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
