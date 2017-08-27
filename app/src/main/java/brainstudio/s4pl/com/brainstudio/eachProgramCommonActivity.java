package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class eachProgramCommonActivity extends AppCompatActivity {

    @BindView(R.id.each_program_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.home_nav_view)
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    @BindView(R.id.program_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.each_program_toolbar)
    Toolbar toolbar;
    @BindView(R.id.each_program_roll_view)
    RollPagerView rollPagerView;
    @BindView(R.id.eachProgramGridRecycler)
    RecyclerView eachProgramGridRecycler;

    eachProgramGridRecyclerAdaptor recyclerAdaptor;
    GridLayoutManager gridLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_program_common);
        ButterKnife.bind(this);

        collapsingToolbarLayout.setTitle("Demo");
        setSupportActionBar(toolbar);

        rollPagerView.setAdapter(new TestLoopAdapter(rollPagerView));

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








        gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        eachProgramGridRecycler.setLayoutManager(gridLayoutManager);
        recyclerAdaptor=new eachProgramGridRecyclerAdaptor();
        eachProgramGridRecycler.setAdapter(recyclerAdaptor);

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


    public class eachProgramGridRecyclerAdaptor extends RecyclerView.Adapter<eachProgramGridRecyclerAdaptor.gridCardHolder>
    {
        @Override
        public gridCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_program_grid_recycler_card,parent,false);
            return new gridCardHolder(itemView);
        }

        @Override
        public void onBindViewHolder(gridCardHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class gridCardHolder extends RecyclerView.ViewHolder
        {
            ImageView gridCardImage;
            TextView gridCardText;
            public gridCardHolder(View itemView) {
                super(itemView);
                gridCardImage=(ImageView)itemView.findViewById(R.id.eachProgramGridRecyclerCardImage);
                gridCardText=(TextView)itemView.findViewById(R.id.eachProgramGridRecyclerCardText);
            }
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
