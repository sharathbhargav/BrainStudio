package brainstudio.s4pl.com.brainstudio;

import android.app.Application;
import android.graphics.Typeface;

import cat.ppicas.customtypeface.CustomTypeface;

/**
 * Created by SharathBhargav on 09-09-2017.
 */

public class application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        CustomTypeface.getInstance().registerTypeface("headingRegular", getAssets(), "TangerineRegular.ttf");
        CustomTypeface.getInstance().registerTypeface("headingBold", getAssets(), "TangerineBold.ttf");
        CustomTypeface.getInstance().registerTypeface("subHeadingRegular", getAssets(), "PhilosopherRegular.ttf");
        CustomTypeface.getInstance().registerTypeface("subHeadingBold", getAssets(), "PhilosopherBold.ttf");
    }
}
