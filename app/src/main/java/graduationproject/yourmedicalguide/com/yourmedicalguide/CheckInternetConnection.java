package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Roza on 2/28/2018.
 */
public class CheckInternetConnection {

    private Context context;
    public CheckInternetConnection(Context context){

        this.context=context;
    }

    public boolean isConnecting(){

        ConnectivityManager connectivityManager =(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager !=null){
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();

            return info != null && info.isConnected();
        }

        return false;
    }
}
