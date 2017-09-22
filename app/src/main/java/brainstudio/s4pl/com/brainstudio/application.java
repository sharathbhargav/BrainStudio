package brainstudio.s4pl.com.brainstudio;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import cat.ppicas.customtypeface.CustomTypeface;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * Created by SharathBhargav on 09-09-2017.
 */

@Keep
@KeepClassMembers
public class application extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    //    MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();


        CustomTypeface.getInstance().registerTypeface("headingRegular", getAssets(), "BreeSerif-Regular.ttf");
        CustomTypeface.getInstance().registerTypeface("headingBold", getAssets(), "BreeSerif-Regular.ttf");
        CustomTypeface.getInstance().registerTypeface("subHeadingRegular", getAssets(), "PhilosopherRegular.ttf");
        CustomTypeface.getInstance().registerTypeface("subHeadingBold", getAssets(), "PhilosopherRegular.ttf");

    }
}
