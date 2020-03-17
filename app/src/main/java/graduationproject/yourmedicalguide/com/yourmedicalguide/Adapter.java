package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Itachi on 02/07/2018.
 */
public class Adapter extends BaseAdapter {
    ArrayList<ListItem> array = new ArrayList<ListItem>();
    Context c;
    String fragment;
    public Adapter(Context context,ArrayList<ListItem> arrayList,String fragment){
        this.c=context;
        this.array=arrayList;
        this.fragment=fragment;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int i) {
        return array.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflat = LayoutInflater.from(c);
        View v = inflat.inflate(R.layout.row_item, viewGroup, false);
        TextView placeName = (TextView)v.findViewById(R.id.search_row_item_txtview_place_name);
        TextView description = (TextView)v.findViewById(R.id.search_row_item_txtview_description);
        placeName.setTypeface(MyClass.tf1);
        description.setTypeface(MyClass.tf2);
        RatingBar ratingBar = (RatingBar)v.findViewById(R.id.search_row_item_RatingBar);
        placeName.setText(array.get(i).Name);
        description.setText(array.get(i).Desc);
        if(!fragment.equals("rating"))
            ratingBar.setVisibility(View.GONE);
        ratingBar.setRating(array.get(i).rate);

        return v;
    }
}
