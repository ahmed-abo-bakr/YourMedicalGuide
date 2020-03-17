package graduationproject.yourmedicalguide.com.yourmedicalguide;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RatingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RatingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "place";
    private static final String ARG_PARAM2 = "dept";
    private static final String ARG_PARAM3 = "deptarab";
    private static final String ARG_PARAM4 = "city";
    private static final String ARG_PARAM5 = "check";
    ListView listView;
    ProgressBar progressBar;
    ImageView refresh;
    TextView noInternet;
    ArrayList<ListItem> data;
    ArrayList<ListItem> listItems;
    DataBase db;
    CheckInternetConnection checkInternetConnection;
    Retrofit retrofit;
    ConnectionAPI connect;
    ArrayList<Ratings> ratings,meanRatings;
    TextView noResult;
    // TODO: Rename and change types of parameters
    private String Place;
    private String Dept;
    private String arabDept;
    private String City;
    private String Check;


    public RatingFragment() {
    }


    public static RatingFragment newInstance(String place, String dept,String arabDept,String city,String check) {
        RatingFragment fragment = new RatingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, place);
        args.putString(ARG_PARAM2, dept);
        args.putString(ARG_PARAM3, arabDept);
        args.putString(ARG_PARAM4, city);
        args.putString(ARG_PARAM5, check);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Place = getArguments().getString(ARG_PARAM1);
            Dept = getArguments().getString(ARG_PARAM2);
            arabDept = getArguments().getString(ARG_PARAM3);
            City = getArguments().getString(ARG_PARAM4);
            Check = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        listView=(ListView)view.findViewById(R.id.listview_top_ratings);
        progressBar=(ProgressBar)view.findViewById(R.id.progressBar_top_rating);
        refresh=(ImageView)view.findViewById(R.id.btn_top_ratings_refresh);
        noInternet=(TextView) view.findViewById(R.id.btn_top_ratings_no_internet);
        noResult=(TextView)view.findViewById(R.id.tv_top_ratings_no_result);
        data =new ArrayList<ListItem>();
        db=new DataBase(RatingFragment.this.getActivity());
        if(Check.equals("Hospitals&Clinics")) {

            data = db.getResult1(Place,Dept,City);

        }else {

            data = db.getResult2(Place,Dept,City);

        }
        listItems=new ArrayList<ListItem>();
        checkInternetConnection = new CheckInternetConnection(RatingFragment.this.getActivity());

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        retrofit = new Retrofit.Builder()

                .baseUrl(ConnectionAPI.baseUrl)

                .client(okHttpClient)

                .addConverterFactory(GsonConverterFactory.create(new Gson()))

                .build();

        connect = retrofit.create(ConnectionAPI.class);

        ratings=new ArrayList<Ratings>();

        meanRatings=new ArrayList<Ratings>();

        return view;
    }
    @Override
    public void onResume() {

        super.onResume();

        ratings.clear();

        meanRatings.clear();

        listItems.clear();

        getAllDocRating();

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                getAllDocRating();
            }
        });

    }
    public void createListView(final ArrayList<ListItem> arrayList){
        Adapter adapter =new Adapter(RatingFragment.this.getContext(),arrayList,"rating");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(RatingFragment.this.getActivity(),Full_Data.class);
                i.putExtra("place",Place);
                i.putExtra("deptarab",arabDept);
                TextView name =(TextView)view.findViewById(R.id.search_row_item_txtview_place_name);
                i.putExtra("name",name.getText().toString());
                listView.setAdapter(null);
                progressBar.setVisibility(View.VISIBLE);
                startActivity(i);
            }
        });
    }
    public void getAllDocRating(){
        if (checkInternetConnection.isConnecting()) {
            connect.getAllDocRating(Place,arabDept).enqueue(new Callback<ArrayList<Ratings>>() {
                @Override
                public void onResponse(Call<ArrayList<Ratings>> call, Response<ArrayList<Ratings>> response) {
                    if (!response.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        refresh.setVisibility(View.VISIBLE);
                        Toast.makeText(RatingFragment.this.getActivity(), "الخدمة غير متوفرة حاليا", Toast.LENGTH_SHORT).show();

                    } else {
                        if (response.body().size() != 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                ratings.add(i, new Ratings(response.body().get(i).getRate(), response.body().get(i).getDocname()));
                            }
                            String doctor = ratings.get(0).getDocname();
                            float rate = ratings.get(0).getRate();
                            int counter = 1;
                            float sum = rate;
                            float mean = 0;
                            for (int i = 1; i < ratings.size(); i++) {
                                if (doctor.equals(ratings.get(i).getDocname())) {
                                    sum = sum + ratings.get(i).getRate();
                                    counter++;
                                } else {
                                    mean = sum / counter;
                                    meanRatings.add(new Ratings(mean, doctor));
                                    doctor = ratings.get(i).getDocname();
                                    sum = ratings.get(i).getRate();
                                    counter = 1;
                                }
                            }
                            mean = sum / counter;
                            meanRatings.add(new Ratings(mean, doctor));
                        }
                        for (int i = 0; i < data.size(); i++) {
                            int count = 0;
                            for (int j = 0; j < meanRatings.size(); j++) {
                                if (data.get(i).Name.equals(meanRatings.get(j).getDocname())) {
                                    listItems.add(i, new ListItem(data.get(i).Name, data.get(i).Desc, meanRatings.get(j).getRate()));
                                    count = 1;
                                    break;
                                }
                            }
                            if (count == 0) {
                                listItems.add(i, new ListItem(data.get(i).Name, data.get(i).Desc, 0));
                            }
                        }
                        listItems = sortRatings(listItems);
                        if(listItems.size() !=0) {
                            createListView(listItems);
                        }else{
                            noResult.setVisibility(View.VISIBLE);
                        }

                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Ratings>> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    refresh.setVisibility(View.VISIBLE);
                    Toast.makeText(RatingFragment.this.getActivity(), "انترنت ضعيف", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            progressBar.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
            Toast.makeText(RatingFragment.this.getActivity(),"لا يوجد اتصال بالانترنت",Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList<ListItem> sortRatings(ArrayList<ListItem> listItems) {
        ArrayList<ListItem> array=new ArrayList<ListItem>();
        ListItem temp = new ListItem("","",0.0F);
        for(int i=0; i < listItems.size(); i++){
            for(int j=1; j < (listItems.size()-i); j++){
                if(listItems.get(j-1).rate < listItems.get(j).rate){
                    temp = listItems.get(j-1);
                    listItems.set(j-1,listItems.get(j));
                    listItems.set(j,temp);
                }
            }
        }
        return listItems;
    }
}
