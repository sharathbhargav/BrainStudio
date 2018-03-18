package brainstudio.s4pl.com.brainstudio;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.keiferstone.nonet.Configuration;
import com.keiferstone.nonet.ConnectionStatus;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.mateware.snacky.Snacky;

public class OfflineActivity extends AppCompatActivity {
    @BindView(R.id.goBackOnline)
    Button goBackOnline;
    @BindView(R.id.offlineExit)
            Button exit;
    boolean backPressed=false;
    Monitor monitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);
        ButterKnife.bind(this);
        Log.v("offline","in offline activity");

       NoNet.monitor(this)

                .callback(new Monitor.Callback() {
                    @Override
                    public void onConnectionEvent(int connectionStatus) {

                        if(connectionStatus== ConnectionStatus.CONNECTED)
                        {

                           goBackOnline.setVisibility(View.VISIBLE);
                            exit.setVisibility(View.GONE);
                        }

                    }
                });

        NoNet.check(this).start();

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);

                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            }
        });
        goBackOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);

        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NoNet.destroy();
    }
}
