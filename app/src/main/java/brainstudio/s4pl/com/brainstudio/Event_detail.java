package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

import butterknife.BindView;
import butterknife.ButterKnife;
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



    DatabaseReference parent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rollPagerView.setAdapter(new TestLoopAdapter(rollPagerView));

        Intent fromHome=getIntent();
        String path=fromHome.getStringExtra("path");
        parent= FirebaseDatabase.getInstance().getReference(path);

        parent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                date.setText(dataSnapshot.child("date").getValue(String.class));
                days.setText(dataSnapshot.child("days").getValue(String.class));
                venue.setText(dataSnapshot.child("address").getValue(String.class));
                time.setText(dataSnapshot.child("time").getValue(String.class));
                description.setText(dataSnapshot.child("description").getValue(String.class));
                phone=dataSnapshot.child("phone").getValue(String.class);
                register.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    .load(R.drawable.background)
                    //.placeholder(R.drawable.nogut)

                    .centerCrop()
                    .into(view);
            //view.setImageResource(displayImageUrls.indexOf(position));

            //view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getRealCount() {
            return 2;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
