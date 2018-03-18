package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.fenjuly.library.ArrowDownloadButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.keiferstone.nonet.Configuration;
import com.keiferstone.nonet.ConnectionStatus;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import de.mateware.snacky.Snacky;
import io.fabric.sdk.android.Fabric;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import static java.security.AccessController.getContext;

@Keep
@KeepClassMembers
public class AboutUs extends AppCompatActivity {


    @BindView(R.id.about_us_toolbar)
    Toolbar toolbar;

    @BindView(R.id.aboutUsWeb)
    WebView webView;
    int progress=0;

    SimpleArcDialog mDialog;
    Monitor monitor;
    boolean netLost=false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        mDialog = new SimpleArcDialog(this);

        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);



        webView.loadUrl("https://sites.google.com/view/brainstudios4pl/home");
        configuration.setText("Please wait..");

        mDialog.setConfiguration(configuration);
       mDialog.setCancelable(false);
        final Configuration config= NoNet.configure()
                .endpoint("https://google.com")
                .timeout(5)
                .connectedPollFrequency(10)
                .disconnectedPollFrequency(3)
                .build();
       NoNet.monitor(this)
                .configure(config)
                .poll()
                .callback(new Monitor.Callback() {
                    @Override
                    public void onConnectionEvent(int connectionStatus) {

                        if(connectionStatus== ConnectionStatus.DISCONNECTED && !netLost)
                        {
                            netLost=true;

                            Intent offLine=new Intent(getApplicationContext(),OfflineActivity.class);
                            startActivity(offLine);

                        }
                        if(connectionStatus==ConnectionStatus.CONNECTED)
                            netLost=false;


                    }
                });
        monitor= NoNet.check(this).start();



        Fabric.with(this, new Crashlytics());
        toolbar.setTitle("About us");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);





    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }




}
