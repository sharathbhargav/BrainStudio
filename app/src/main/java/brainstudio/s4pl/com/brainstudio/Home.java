package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

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

                }
                return true;
            }
        });



        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerAdaptor=new homeRecyclerAdaptor();
        recyclerView.setAdapter(recyclerAdaptor);


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

            TextView t=(TextView) front.findViewById(R.id.home_card_front_name);
            ImageView imageView=(ImageView) front.findViewById(R.id.home_card_front_img);
            Glide.with(getApplicationContext())
                    .load(R.drawable.background)
                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ring))
                    .into(imageView);
            t.setText("Changed");
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
            return 3;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //if (id == R.id.action_right_menu) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
          //  return true;
       // }

        return super.onOptionsItemSelected(item);
    }
}
