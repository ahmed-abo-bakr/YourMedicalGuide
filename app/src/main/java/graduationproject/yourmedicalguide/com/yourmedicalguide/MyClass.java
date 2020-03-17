package graduationproject.yourmedicalguide.com.yourmedicalguide;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Itachi on 04/07/2018.
 */


public class MyClass {
    public static Typeface tf1, tf2,tf3,tf4,tf5;


    public static void  setFont(ViewGroup group, Typeface font, Context c) {
        tf1 = Typeface.createFromAsset(c.getAssets(), "font1.ttf");
        tf2 = Typeface.createFromAsset(c.getAssets(), "font2.ttf");
        tf3 = Typeface.createFromAsset(c.getAssets(), "font3.ttf");
        tf4 = Typeface.createFromAsset(c.getAssets(), "font4.ttf");
        tf5 = Typeface.createFromAsset(c.getAssets(), "font5.ttf");
        int count = group.getChildCount();
        View v;
        for (int i = 0; i < count; i++) {
            v = group.getChildAt(i);
            if (v instanceof TextView || v instanceof EditText || v instanceof Button) {
                ((TextView) v).setTypeface(font);
            } else if (v instanceof ViewGroup)
                setFont((ViewGroup) v, font,c);
        }
    }

}
