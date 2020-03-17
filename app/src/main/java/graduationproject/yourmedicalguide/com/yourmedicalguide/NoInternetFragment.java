package graduationproject.yourmedicalguide.com.yourmedicalguide;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoInternetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoInternetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "place";
    private static final String ARG_PARAM2 = "dept";
    private static final String ARG_PARAM3 = "deptarab";
    private static final String ARG_PARAM4 = "city";
    private static final String ARG_PARAM5 = "check";
    ListView listView;
    ArrayList<ListItem> data;
    DataBase db;
    TextView noResult;
    // TODO: Rename and change types of parameters
    private String Place;
    private String Dept;
    private String arabDept;
    private String City;
    private String Check;


    public NoInternetFragment() {
    }


    public static NoInternetFragment newInstance(String place, String dept,String arabDept,String city,String check) {
        NoInternetFragment fragment = new NoInternetFragment();
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
        View view = inflater.inflate(R.layout.fragment_no_internet, container, false);
        listView=(ListView)view.findViewById(R.id.listview_no_internet);
        data =new ArrayList<ListItem>();
        noResult=(TextView)view.findViewById(R.id.tv_no_internet_no_result);
        db=new DataBase(NoInternetFragment.this.getActivity());
        if(Check.equals("Hospitals&Clinics")) {
            data = db.getResult1(Place,Dept,City);
        }else {
            data = db.getResult2(Place,Dept,City);

        }
        if(data.size() !=0) {
            createListView(data);
        }else{
            noResult.setVisibility(View.VISIBLE);
        }
        return view;
    }
    public void createListView(final ArrayList<ListItem> arrayList){
        Adapter adapter =new Adapter(NoInternetFragment.this.getContext(),arrayList,"nointernet");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(NoInternetFragment.this.getActivity(),Full_Data.class);
                i.putExtra("place",Place);
                i.putExtra("deptarab",arabDept);
                TextView name =(TextView)view.findViewById(R.id.search_row_item_txtview_place_name);
                i.putExtra("name",name.getText().toString());
                i.putExtra("fragment","NoInternet");
                startActivity(i);
            }
        });
    }

}
