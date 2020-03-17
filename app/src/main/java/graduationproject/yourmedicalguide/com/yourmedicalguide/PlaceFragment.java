package graduationproject.yourmedicalguide.com.yourmedicalguide;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "place";
    private static final String ARG_PARAM2 = "dept";
    private static final String ARG_PARAM3 = "deptarab";
    private static final String ARG_PARAM4 = "city";
    private static final String ARG_PARAM5 = "check";
    ListView listView;
    ArrayList<ListItem> data;
    ArrayList<ListItem> listItems;
    DataBase db;
    Location current;
    Button enableLocation;
    TextView noResult;
    ProgressBar progressBar;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback locationCallback;
    // TODO: Rename and change types of parameters
    private String Place;
    private String Dept;
    private String arabDept;
    private String City;
    private String Check;
    public PlaceFragment() {
    }

    public static PlaceFragment newInstance(String place, String dept, String arabDept, String city, String check) {
        PlaceFragment fragment = new PlaceFragment();
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
        View view = inflater.inflate(R.layout.fragment_place, container, false);
        listView = (ListView) view.findViewById(R.id.listview_nearest_places);
        noResult = (TextView) view.findViewById(R.id.tv_nearest_places_no_result);
        data = new ArrayList<ListItem>();
        listItems = new ArrayList<ListItem>();
        db = new DataBase(PlaceFragment.this.getActivity());
        current = new Location("location");
        enableLocation = (Button) view.findViewById(R.id.enable_Location);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar_nearest_places);
        if (Check.equals("Hospitals&Clinics")) {

            data = db.getResult1(Place, Dept, City);

        } else {

            data = db.getResult2(Place, Dept, City);

        }
        enableLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocationManager manager=(LocationManager) PlaceFragment.this.getContext().getSystemService(Service.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ){
            enableLocation.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }else {
            enableLocation.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
        listItems.clear();
        createListView(listItems);
        getLocation();


    }

    public void createListView(final ArrayList<ListItem> arrayList) {
        Adapter adapter = new Adapter(PlaceFragment.this.getContext(), arrayList, "place");
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(PlaceFragment.this.getActivity(), Full_Data.class);
                i.putExtra("place", Place);
                i.putExtra("deptarab", arabDept);
                TextView name = (TextView) view.findViewById(R.id.search_row_item_txtview_place_name);
                i.putExtra("name", name.getText().toString());
                listView.setAdapter(null);
                startActivity(i);
            }
        });
    }

    private ArrayList<ListItem> sortPlaces(ArrayList<ListItem> listItems) {
        ArrayList<ListItem> array = new ArrayList<ListItem>();
        ListItem temp = new ListItem("", "", 0);
        for (int i = 0; i < listItems.size(); i++) {
            for (int j = 1; j < (listItems.size() - i); j++) {
                if (listItems.get(j - 1).distance > listItems.get(j).distance) {
                    temp = listItems.get(j - 1);
                    listItems.set(j - 1, listItems.get(j));
                    listItems.set(j, temp);
                }
            }
        }
        return listItems;
    }

    private void getLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(PlaceFragment.this.getContext());
        if (ActivityCompat.checkSelfPermission(PlaceFragment.this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PlaceFragment.this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlaceFragment.this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    500);
            return;
        }
        LocationRequest locationRequest=new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(PlaceFragment.this.getContext());
        settingsClient.checkLocationSettings(locationSettingsRequest);
        locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // do work here
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        mFusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        progressBar.setVisibility(View.GONE);
        current=location;
        for (int i=0;i<data.size();i++){
            Location placeLocation=new Location("location");
            placeLocation.setLatitude(data.get(i).latitude);
            placeLocation.setLongitude(data.get(i).longitude);
            double distance=current.distanceTo(placeLocation);
            listItems.add(new ListItem(data.get(i).Name,data.get(i).Desc,distance));
        }
        listItems=sortPlaces(listItems);
        if(listItems.size() !=0) {
            createListView(listItems);
        }else{
            noResult.setVisibility(View.VISIBLE);
        }

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            Log.e("oooooooo", "oooooooo");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            Log.e("kkkk", "kkkkk");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==500 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }
}
