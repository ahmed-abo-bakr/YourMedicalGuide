package graduationproject.yourmedicalguide.com.yourmedicalguide;

/**
 * Created by Roza on 11/22/2017.
 */


//4
public class ListItem {
    String Name , Desc ,dept,phone,address;
    float rate;
    double latitude,longitude;
    double distance;

    public ListItem(String name, String desc, float rate) {
        Name = name;
        Desc = desc;
        this.rate = rate;
    }

    public ListItem(String name, String desc, double distance) {
        Name = name;
        Desc = desc;
        this.distance = distance;
    }
    public ListItem(String dept) {
        this.dept = dept;
    }

    public ListItem(String name, String desc,double latitude,double longitude) {
        Name = name;
        Desc = desc;
        this.latitude=latitude;
        this.longitude=longitude;
    }
////4
//10
    public ListItem(String name, String address, String desc, String phone,double latitude,double longitude) {
        Name = name;
        this.address = address;
        this.dept = dept;
        Desc = desc;
        this.phone = phone;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    ////10
}
