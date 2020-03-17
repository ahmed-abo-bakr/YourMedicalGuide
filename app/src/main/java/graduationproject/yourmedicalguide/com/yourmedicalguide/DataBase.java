package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.ArrayList;

public class DataBase extends SQLiteOpenHelper {

//first
    public static final String dbname="data.db";
    public static final String location = Environment.getDataDirectory()+"/data/graduationproject.yourmedicalguide.com.yourmedicalguide/databases/";
    private Context maincontext;
    private SQLiteDatabase myDatabase ;

    public DataBase(Context context) {
        super(context, dbname, null, 1);
        this.maincontext = context;

    }





    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    ////First

    //3
    public void openDataBase (){

        String dbpath = maincontext.getDatabasePath(dbname).getPath();
        if(myDatabase != null && myDatabase.isOpen()){
            return;
        }
        myDatabase = SQLiteDatabase.openDatabase(dbpath,null,SQLiteDatabase.OPEN_READWRITE);

    }

    public void closeDataBase (){

        if (myDatabase != null){
            myDatabase.close();
        }
    }
    ////3

    //4
    public ArrayList<ListItem> getResult1(String place, String dept,String city) {
        ArrayList<ListItem> data = new ArrayList<ListItem>();
        openDataBase();
        Cursor flag = myDatabase.rawQuery("SELECT Name,Description,Latitude,Longitude FROM "+place+" where dept ='"+dept+"' AND City ='"+city+"' ",null);
        flag.moveToFirst();
        while (!flag.isAfterLast()){

            data.add(new ListItem(flag.getString(flag.getColumnIndex("Name")),flag.getString(flag.getColumnIndex("Description")),flag.getDouble(flag.getColumnIndex("Latitude")),flag.getDouble(flag.getColumnIndex("Longitude"))));
            flag.moveToNext();
        }
            flag.close();
            closeDataBase();

        return data;
    }
    public ArrayList<ListItem> getResult2(String place, String dept,String city) {
        ArrayList<ListItem> data = new ArrayList<ListItem>();
        openDataBase();
        Cursor flag = myDatabase.rawQuery("SELECT Name,Description,Latitude,Longitude FROM "+place+" where "+dept+" ='true' and City ='"+city+"' ",null);
        flag.moveToFirst();
        while (!flag.isAfterLast()){
            data.add(new ListItem(flag.getString(flag.getColumnIndex("Name")),flag.getString(flag.getColumnIndex("Description")),flag.getDouble(flag.getColumnIndex("Latitude")),flag.getDouble(flag.getColumnIndex("Longitude"))));
            flag.moveToNext();
        }
        flag.close();
        closeDataBase();

        return data;
    }

    ////4
    //9
    public ArrayList<ListItem> getAllData(String name, String place) {
        ArrayList<ListItem> data = new ArrayList<ListItem>();
        openDataBase();
        Cursor flag = myDatabase.rawQuery("SELECT Name,Address,Phone,Description,Latitude,Longitude FROM "+place+" where Name ='"+name+"'",null);
        flag.moveToFirst();
            String Name=flag.getString(flag.getColumnIndex("Name"));
            String Address=flag.getString(flag.getColumnIndex("Address"));
            String Phone=flag.getString(flag.getColumnIndex("Phone"));
            String Description=flag.getString(flag.getColumnIndex("Description"));
            double latitude=flag.getDouble(flag.getColumnIndex("Latitude"));
            double longitude=flag.getDouble(flag.getColumnIndex("Longitude"));
            data.add(new ListItem(Name,Address,Description,Phone,latitude,longitude));
        flag.close();
        closeDataBase();

        return data;
    }
    public ArrayList getDept(String place,String city) {
        ArrayList data = new ArrayList();
        openDataBase();
        Cursor flag = myDatabase.rawQuery("select distinct Dept from "+place+" where City ='"+city+"' ",null);
        flag.moveToFirst();
        while (!flag.isAfterLast()){
            String Dept=flag.getString(flag.getColumnIndex("Dept"));
            data.add(Dept);
            flag.moveToNext();
        }
        flag.close();
        closeDataBase();

        return data;
    }
    public ArrayList getCity(String place) {
        ArrayList data = new ArrayList();
        openDataBase();
        Cursor flag = myDatabase.rawQuery("select distinct City from "+place+" ",null);
        flag.moveToFirst();
        while (!flag.isAfterLast()){
            String City=flag.getString(flag.getColumnIndex("City"));
            data.add(City);
            flag.moveToNext();
        }
        flag.close();
        closeDataBase();

        return data;
    }
    ////9
}
