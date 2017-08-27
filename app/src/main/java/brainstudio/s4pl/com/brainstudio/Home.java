package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;

import android.graphics.drawable.Drawable;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flipview.FlipView;



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
    DatabaseReference refParent,refBranches;
    ArrayList<homeListData> centerList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        refParent= FirebaseDatabase.getInstance().getReference("centre");
        refParent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren())
                {
                    homeListData list=new homeListData();
                    list.name=noteDataSnapshot.child("name").getValue(String.class);
                    list.address=noteDataSnapshot.child("address").getValue(String.class);
                    list.img=noteDataSnapshot.child("mainpic").getValue(String.class);
                    list.phno=noteDataSnapshot.child("phone").getValue(String.class);
                    list.days=noteDataSnapshot.child("days").getValue(String.class);
                    list.time=noteDataSnapshot.child("time").getValue(String.class);
                    centerList.add(list);
                }

             //   recyclerAdaptor=new homeRecyclerAdaptor();
             //   recyclerView.setAdapter(recyclerAdaptor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerAdaptor=new homeRecyclerAdaptor();
        recyclerView.setAdapter(recyclerAdaptor);
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
                        item.setChecked(true);
                        drawer.closeDrawers();
                        break;
                    case R.id.menu_news:
                        item.setChecked(true);
                        drawer.closeDrawers();
                        Intent news=new Intent(getApplicationContext(),youTubeList.class);
                        startActivity(news);
                        break;
                    case R.id.menu_cube:
                        drawer.closeDrawers();
                        Intent toCube=new Intent(getApplicationContext(),eachProgramCommonActivity.class);
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

                }
                return true;
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
        public void onBindViewHolder(homeCardHolder holder, int position) {
            View front=holder.flipView.getFrontLayout();

            TextView name=(TextView) front.findViewById(R.id.home_card_front_name);
            ImageView imageView=(ImageView) front.findViewById(R.id.home_card_front_img);
            TextView phone=(TextView) front.findViewById(R.id.home_card_front_phone);
            //Glide.with(getApplicationContext())
            //        .load(centerList.get(position).img)
            //        .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ring))
            //        .into(imageView);
            //name.setText(centerList.get(position).name);
            //phone.setText(centerList.get(position).phno);


            Glide.with(getApplicationContext())
                    .load(R.drawable.background)
                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ring))
                    .into(imageView);
            name.setText("sdfsd");
            phone.setText("dfsfg");
            holder.flipView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toEachBranch=new Intent(getApplicationContext(),eachBranchDetail.class);
                    startActivity(toEachBranch);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 1;
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
