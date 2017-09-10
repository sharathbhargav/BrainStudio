package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import io.fabric.sdk.android.Fabric;

public class Event_detail extends AppCompatActivity {

    @BindView(R.id.event_detail_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.each_event_toolbar)
    Toolbar toolbar;
    @BindView(R.id.each_event_roll_view)
    RollPagerView rollPagerView;

    @BindView(R.id.eventDetailDate)
    TextView date;
    @BindView(R.id.eventDetailDays)
            TextView days;
    @BindView(R.id.eventDetailDescription)
            TextView description;
    @BindView(R.id.eventDetailTime)
            TextView time;
    @BindView(R.id.eventDetailVenueDescription)
            TextView venue;
    @BindView(R.id.eventDetailRegisterButton)
    Button register;
    String phone;

    TestLoopAdapter testLoopAdapter;
    ArrayList<String> picSlide=new ArrayList<>();

    DatabaseReference parent;
    String timeString;
    SimpleArcDialog mDialog;


    String registerString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        testLoopAdapter=new TestLoopAdapter(rollPagerView);
        mDialog = new SimpleArcDialog(this);

        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);

        Intent fromHome=getIntent();
        String path=fromHome.getStringExtra("path");
        Log.v("detail","received path=="+path);
        parent= FirebaseDatabase.getInstance().getReference(path);
        Log.v("detail",parent.toString());
        mDialog.show();
        parent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                date.setText(dataSnapshot.child("date").getValue(String.class));

                days.setText(dataSnapshot.child("days").getValue(String.class));
                venue.setText(dataSnapshot.child("address").getValue(String.class));
                collapsingToolbarLayout.setTitle(dataSnapshot.child("name").getValue(String.class));
                final String[] time1=dataSnapshot.child("time").getValue(String.class).split(":");
                String t1,t2;
                int t11=Integer.parseInt(time1[0]);
                int t12=Integer.parseInt(time1[2]);
                if(t11>12)
                    t1=t11%12+":"+time1[1]+" pm";
                else
                    t1=t11+":"+time1[1]+" am";

                if(t12>12)
                    t2=t12%12+":"+time1[3]+" pm";
                else
                    t2=t12+":"+time1[3]+" am";
                timeString=t1+"-"+t2;
                time.setText(timeString);
                description.setText(dataSnapshot.child("description").getValue(String.class));
                phone=dataSnapshot.child("phone").getValue(String.class);
                register.setEnabled(true);
                mDialog.dismiss();


                registerString=dataSnapshot.child("register").getValue(String.class);
                DataSnapshot pic=dataSnapshot.child("picslide");
                for(DataSnapshot p1:pic.getChildren())
                {
                    picSlide.add(p1.getValue(String.class));
                    testLoopAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rollPagerView.setAdapter(testLoopAdapter);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(registerString));
                startActivity(i);
            }
        });



    }


    public class TestLoopAdapter extends LoopPagerAdapter {


        public TestLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());

            Glide.with(getApplicationContext())
                    .load(picSlide.get(position))
                    .placeholder(R.drawable.brainstudio)

                    .centerCrop()
                    .into(view);
            //view.setImageResource(displayImageUrls.indexOf(position));

            //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return picSlide.size();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
