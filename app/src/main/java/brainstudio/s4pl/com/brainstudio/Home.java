package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flipview.FlipView;
import io.fabric.sdk.android.Fabric;
import rm.com.clocks.ClockDrawable;
import rm.com.clocks.ClockImageView;
import rm.com.clocks.Stroke;


public class Home extends AppCompatActivity {


    @BindView(R.id.home_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.home_toolbar)
            Toolbar toolbar;
    @BindView(R.id.home_nav_view)
    NavigationView navigationView;
     ActionBarDrawerToggle toggle;

    LinearLayoutManager layoutManager;
    homeRecyclerAdaptor recyclerAdaptor;
    @BindView(R.id.home_recycler_view)
    RecyclerView recyclerView;


    SimpleArcDialog mDialog;

    DatabaseReference refParent,refBranches;
    ArrayList<homeListData> centerList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdaptor=new homeRecyclerAdaptor();
        recyclerView.setAdapter(recyclerAdaptor);
        mDialog = new SimpleArcDialog(this);

        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);
        extractCenterData();

        toolbar.setTitle("Home");
        final ActionBar actionBar = getSupportActionBar();
       toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawer.addDrawerListener(toggle);
                toggle.syncState();
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        };
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.menu_home:

                        drawer.closeDrawers();
                        break;
                    case R.id.menu_news:

                        drawer.closeDrawers();
                        Intent news=new Intent(getApplicationContext(),Images_VideosList.class);
                        startActivity(news);
                        break;
                    case R.id.menu_programmes:
                        drawer.closeDrawers();
                        Intent toCube=new Intent(getApplicationContext(),Programmes.class);
                        startActivity(toCube);
                        break;
                    case R.id.menu_feedback:
                        drawer.closeDrawers();
                        Intent toFeedback=new Intent(getApplicationContext(),feedback.class);
                        startActivity(toFeedback);
                        break;
                    case R.id.menu_enquiry:
                        drawer.closeDrawers();
                        Intent toEnquiry=new Intent(getApplicationContext(),Enquiry.class);
                        startActivity(toEnquiry);
                        break;
                    case R.id.menu_contact_us:
                        drawer.closeDrawers();
                        Intent contact=new Intent(getApplicationContext(),ContactUs.class);
                        startActivity(contact);
                        break;
                    case R.id.menu_reviews:
                        drawer.closeDrawers();
                        Intent toReview =new Intent(getApplicationContext(),Reviews.class);
                        startActivity(toReview);
                        break;
                    case R.id.menu_about_us:
                        drawer.closeDrawers();
                        Intent toAbout=new Intent(getApplicationContext(),AboutUs.class);
                        startActivity(toAbout);
                        break;

                }
                return true;
            }
        });



// Using this configuration with Dialog


// Using this configuration with ArcLoader




    }
    void extractCenterData()
    {
        refParent=FirebaseDatabase.getInstance().getReference("events");
        mDialog.show();
        refParent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    if(noteDataSnapshot.child("show").getValue(String.class)!=null) {
                        homeListData list = new homeListData();
                        list.name = noteDataSnapshot.child("name").getValue(String.class);
                        list.address = noteDataSnapshot.child("address").getValue(String.class);
                        list.img = noteDataSnapshot.child("mainpic").getValue(String.class);
                        list.phno = noteDataSnapshot.child("phone").getValue(String.class);
                        list.days = noteDataSnapshot.child("days").getValue(String.class);
                        list.time = noteDataSnapshot.child("time").getValue(String.class);
                        list.event = 1;

                        centerList.add(list);
                        recyclerAdaptor.notifyDataSetChanged();
                        mDialog.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        refParent= FirebaseDatabase.getInstance().getReference("centre");
        refParent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren())
                {
                    homeListData list=new homeListData();
                    list.ref=noteDataSnapshot.getKey();
                    list.name=noteDataSnapshot.child("name").getValue(String.class);
                    list.address=noteDataSnapshot.child("address").getValue(String.class);
                    list.img=noteDataSnapshot.child("mainpic").getValue(String.class);
                    list.phno=noteDataSnapshot.child("phone").getValue(String.class);
                    list.days=noteDataSnapshot.child("days").getValue(String.class);
                    list.time=noteDataSnapshot.child("time").getValue(String.class);
                    centerList.add(list);
                     recyclerAdaptor.notifyDataSetChanged();
                    if(mDialog.isShowing())
                        mDialog.dismiss();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    public class homeRecyclerAdaptor extends RecyclerView.Adapter<homeRecyclerAdaptor.homeCardHolder>
    {


        @Override
        public homeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_flip_card,parent,false);
            return new homeCardHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final homeCardHolder holder, final int position) {
            View front=holder.flipView.getFrontLayout();
            View back=holder.flipView.getRearLayout();

            holder.flipView.setFlipInterval(3000+position*500);
            TextView name=(TextView) front.findViewById(R.id.home_card_front_name);
            ImageView imageView=(ImageView) front.findViewById(R.id.home_card_front_img);
            TextView phone=(TextView) front.findViewById(R.id.home_card_front_phone);
            TextView address=(TextView)back.findViewById(R.id.home_card_back_address);


            ImageView sun=(ImageView)back.findViewById(R.id.home_back_sunday);
            ImageView mon=(ImageView)back.findViewById(R.id.home_back_monday);
            ImageView tue=(ImageView)back.findViewById(R.id.home_back_tuesday);
            ImageView wed=(ImageView)back.findViewById(R.id.home_back_wednesday);
            ImageView thu=(ImageView)back.findViewById(R.id.home_back_thursday);
            ImageView fri=(ImageView)back.findViewById(R.id.home_back_friday);
            ImageView sat=(ImageView)back.findViewById(R.id.home_back_saturday);



            final ClockImageView clocks=(ClockImageView) back.findViewById(R.id.clock1);
            final ClockImageView clocks2=(ClockImageView)back.findViewById(R.id.clock2);

            TextView clockText1=(TextView) back.findViewById(R.id.home_card_clock1_text);
            TextView clockText2=(TextView)back.findViewById(R.id.home_card_clock2_text);
            clocks.setClockColor(R.color.colorPrimary);
            clocks2.setClockColor(R.color.colorPrimary);


            final String[] time=centerList.get(position).time.split(":");
            String t1,t2;
            int t11=Integer.parseInt(time[0]);
            int t12=Integer.parseInt(time[2]);
            if(t11>12)
                t1=t11%12+":"+time[1]+" pm";
            else
                t1=t11+":"+time[1]+" am";

            if(t12>12)
                t2=t12%12+":"+time[3]+" pm";
            else
                t2=t12+":"+time[3]+" am";

            clockText1.setText(t1);
            clockText2.setText(t2);
        holder.flipView.setOnFlippingListener(new FlipView.OnFlippingListener() {
            @Override
            public void onFlipped(FlipView flipView, boolean checked) {
                final  boolean j=checked;
                clocks.start();
                clocks2.start();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    public void run() {
                        // yourMethod();
                        clocks.stop();
                        clocks2.stop();
                        if(j) {
                            clocks.animateToTime(Integer.parseInt(time[0]), Integer.parseInt(time[1]));
                            clocks2.animateToTime(Integer.parseInt(time[2]),Integer.parseInt(time[3]));
                        }
                    }
                }, 500);


            }
        });


            //clocks.setHours(12);
            //clocks.setMinutes(23);
            int event=centerList.get(position).event;
                if(event==1) {
                    sun.setVisibility(View.GONE);
                    mon.setVisibility(View.GONE);
                    tue.setVisibility(View.GONE);
                    wed.setVisibility(View.GONE);
                    thu.setVisibility(View.GONE);
                    fri.setVisibility(View.GONE);
                    sat.setVisibility(View.GONE);
                }


            name.setText(centerList.get(position).name);
            phone.setText(centerList.get(position).phno);
            address.setText(centerList.get(position).address);
            String temp=centerList.get(position).days;
            String[] d=temp.split(",");
            for(int i=0;i<d.length;i++)
            {
                switch (d[i])
                {
                    case "sun":
                        sun.setVisibility(View.VISIBLE);
                        sun.setImageResource(R.drawable.sunday_green);
                        break;
                    case "mon":
                        mon.setVisibility(View.VISIBLE);
                        mon.setImageResource(R.drawable.monday_green);
                        break;
                    case "tue":
                        tue.setVisibility(View.VISIBLE);
                        tue.setImageResource(R.drawable.tuesday_green);
                        break;
                    case "wed":
                        wed.setVisibility(View.VISIBLE);
                        wed.setImageResource(R.drawable.wednesday_green);
                        break;
                    case "thu":
                        thu.setVisibility(View.VISIBLE);
                        thu.setImageResource(R.drawable.thursday_green);
                        break;
                    case "fri":
                        fri.setVisibility(View.VISIBLE);
                        fri.setImageResource(R.drawable.friday_green);
                        break;
                    case "sat":
                        sat.setVisibility(View.VISIBLE);
                        sat.setImageResource(R.drawable.saturday_green);
                        break;

                }
            }

            Glide.with(getApplicationContext())
                    .load(centerList.get(position).img)
                    .centerCrop()
                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ring))
                    .into(imageView);



            holder.flipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toEachBranch=new Intent(getApplicationContext(),eachBranchDetail.class);
                    String path;
                    if(centerList.get(position).event==0) {
                        path = "centre/" + centerList.get(position).ref;
                        toEachBranch.putExtra("path",path);
                        startActivity(toEachBranch);
                    }
                    else {
                        Intent toEvent=new Intent(getApplicationContext(),Event_detail.class);

                        path = "events/" + centerList.get(position).ref;
                        toEvent.putExtra("path",path);
                        startActivity(toEvent);

                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return centerList.size();
        }

        public class homeCardHolder extends RecyclerView.ViewHolder
        {

            FlipView flipView;


            public homeCardHolder(View itemView) {
                super(itemView);

                flipView=(FlipView)itemView.findViewById(R.id.home_flip_view_card);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.home_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


}
