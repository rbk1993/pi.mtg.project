package pi.mytourguide.utils;

import android.app.Application;
import android.location.LocationListener;
import android.location.LocationManager;

import com.parse.Parse;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public final class MyApplication extends Application {

    public static boolean requestingMemberPositions=false;

    public static LocationManager locationManager=null;
    public static LocationListener locationListener;
    public static String currentUserId;
    public static ArrayList<HashMap<String, String>> listItem;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "o0vvZbqThRgTotm9VKxeSfl7yaDebOfOa51sLXNc", "PMz0wBtgfmQVSJtINeBP85L1GwwbooeEMGu4tkMc");
        ParseInstallation.getCurrentInstallation().saveInBackground();



    }

}
