package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class eachBranchDetail extends AppCompatActivity {

    @BindView(R.id.each_branch_drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.home_nav_view)
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;



    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.each_branch_toolbar)
    Toolbar toolbar;
    @BindView(R.id.each_branch_roll_view)
    RollPagerView rollPagerView;

    @BindView(R.id.eachBranchCubeExpandableLayout)
    ExpandableLayout cubeExpandableLayout;
    @BindView(R.id.eachBranchJugglingExpandableLayout)
    ExpandableLayout jugglingExpandableLayout;
    @BindView(R.id.eachBranchGraphoExpandableLayout)
            ExpandableLayout graphoExpandableLayout;
    @BindView(R.id.eachBranchStackExpandableLayout)
            ExpandableLayout stackExpandableLayout;
    @BindView(R.id.eachBranchCorporateExpandableLayout)
            ExpandableLayout corporateExpandableLayout;
    @BindView(R.id.eachBranchCalligraphyExpandableLayout)
            ExpandableLayout calligraphyExpandableLayout;
    LinearLayoutManager layoutManagerCube;
    RecyclerView cubeRecycle,jugglingRecycle,graphoRecycle,stackRecycle,corporateRecycle,calligraphyRecycle;
    eachBranchRecyclerAdaptor cubeRecyclerAdaptor,jugglingRecycleAdaptor,graphoRecycleAdaptor,stackRecycleAdaptor,corporateRecycleAdaptor,calligraphyRecycleAdaptor;

    init cubeInit,jugglingInit,graphoInit,stackInit,corporateInit,calligraphyInit;
 ArrayList<EachBranchCardCommonClass> cubeList,jugglingList,scientificlist,stackList,corporateList,calligraphyList;


    String centre="Uttarahalli";
    DatabaseReference parent,child,prog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_branch_detail);
        ButterKnife.bind(this);
        collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Demo");
        setSupportActionBar(toolbar);

        rollPagerView.setAdapter(new TestLoopAdapter(rollPagerView));

        parent= FirebaseDatabase.getInstance().getReference("centre");
        child=parent.child(centre);
        prog=child.child("programmes");
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













        cubeExpandableLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cubeExpandableLayout.isOpened())
                {
                    cubeExpandableLayout.hide();
                }
                else
                {
                    cubeExpandableLayout.show();
                    jugglingExpandableLayout.hide();
                    graphoExpandableLayout.hide();
                    stackExpandableLayout.hide();
                    corporateExpandableLayout.hide();
                    calligraphyExpandableLayout.hide();
                }

            }
        });

        jugglingExpandableLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jugglingExpandableLayout.isOpened())
                {
                   jugglingExpandableLayout.hide();
                }
                else
                {
                    cubeExpandableLayout.hide();
                    jugglingExpandableLayout.show();
                    graphoExpandableLayout.hide();
                    stackExpandableLayout.hide();
                    corporateExpandableLayout.hide();
                    calligraphyExpandableLayout.hide();
                }
            }
        });

        graphoExpandableLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(graphoExpandableLayout.isOpened())
                {
                    graphoExpandableLayout.hide();
                }
                else
                {
                    cubeExpandableLayout.hide();
                    jugglingExpandableLayout.hide();
                    graphoExpandableLayout.show();
                    stackExpandableLayout.hide();
                    corporateExpandableLayout.hide();
                    calligraphyExpandableLayout.hide();
                }
            }
        });

        stackExpandableLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stackExpandableLayout.isOpened())
                {
                    stackExpandableLayout.hide();
                }
                else
                {
                    cubeExpandableLayout.hide();
                    jugglingExpandableLayout.hide();
                    graphoExpandableLayout.hide();
                    stackExpandableLayout.show();
                    corporateExpandableLayout.hide();
                    calligraphyExpandableLayout.hide();
                }
            }
        });

        corporateExpandableLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(corporateExpandableLayout.isOpened())
                {
                    corporateExpandableLayout.hide();
                }
                else
                {
                    cubeExpandableLayout.hide();
                    jugglingExpandableLayout.hide();
                    graphoExpandableLayout.hide();
                    stackExpandableLayout.hide();
                    corporateExpandableLayout.show();
                    calligraphyExpandableLayout.hide();
                }
            }
        });

        calligraphyExpandableLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calligraphyExpandableLayout.isOpened())
                {
                    calligraphyExpandableLayout.hide();
                }
                else
                {
                    cubeExpandableLayout.hide();
                    jugglingExpandableLayout.hide();
                    graphoExpandableLayout.hide();
                    stackExpandableLayout.hide();
                    corporateExpandableLayout.hide();
                    calligraphyExpandableLayout.show();
                }
            }
        });

        initializeExpandableLayouts();

    }

    void initializeExpandableLayouts()
    {

        cubeList=new ArrayList<>();
        extractDetails("cube",cubeList);
        RelativeLayout cubeBody= cubeExpandableLayout.getContentRelativeLayout();
        cubeRecycle =(RecyclerView) cubeBody.findViewById(R.id.eachBranchCubeRecyclerView) ;
        TextView cubeText=(TextView) cubeExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        cubeText.setText("Rubik's Cube");
        cubeInit=new init(cubeRecycle,layoutManagerCube,cubeRecyclerAdaptor,cubeList);


        jugglingList=new ArrayList<>();
        extractDetails("juggling",jugglingList);
        RelativeLayout jugglingBody=jugglingExpandableLayout.getContentRelativeLayout();
        jugglingRecycle=(RecyclerView) jugglingBody.findViewById(R.id.eachBranchCubeRecyclerView);
        TextView jugglingText=(TextView) jugglingExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        jugglingText.setText("Juggling");
        jugglingInit=new init(jugglingRecycle,layoutManagerCube,jugglingRecycleAdaptor,jugglingList);


        scientificlist=new ArrayList<>();
        extractDetails("scientific",scientificlist);
        RelativeLayout graphoBody= graphoExpandableLayout.getContentRelativeLayout();
        graphoRecycle =(RecyclerView) graphoBody.findViewById(R.id.eachBranchCubeRecyclerView) ;
        TextView graphoText=(TextView) graphoExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        graphoText.setText("Scientific Handwriting");
        graphoInit=new init(graphoRecycle,layoutManagerCube,graphoRecycleAdaptor,scientificlist);


        stackList=new ArrayList<>();
        extractDetails("stack",stackList);
        RelativeLayout stackBody= stackExpandableLayout.getContentRelativeLayout();
        stackRecycle =(RecyclerView) stackBody.findViewById(R.id.eachBranchCubeRecyclerView) ;
        TextView stackText=(TextView) stackExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        stackText.setText("Speed Stacking");
        stackInit=new init(stackRecycle,layoutManagerCube,stackRecycleAdaptor,stackList);


        corporateList=new ArrayList<>();
        extractDetails("corporate",corporateList);
        RelativeLayout corporateBody= corporateExpandableLayout.getContentRelativeLayout();
        corporateRecycle =(RecyclerView) corporateBody.findViewById(R.id.eachBranchCubeRecyclerView);
        TextView corporateText=(TextView) corporateExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        corporateText.setText("Corporate Training");
        corporateInit=new init(corporateRecycle,layoutManagerCube,corporateRecycleAdaptor,corporateList);


        calligraphyList=new ArrayList<>();
        extractDetails("calligraphy",calligraphyList);
        RelativeLayout calligraphyBody=calligraphyExpandableLayout.getContentRelativeLayout();
        calligraphyRecycle=(RecyclerView)calligraphyBody.findViewById(R.id.eachBranchCubeRecyclerView);
        TextView calligraphyText=(TextView) calligraphyExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        calligraphyText.setText("Calligraphy");
        calligraphyInit=new init(calligraphyRecycle,layoutManagerCube,calligraphyRecycleAdaptor,calligraphyList);
    }


    void extractDetails(final String type, final ArrayList<EachBranchCardCommonClass> list)
    {
        DatabaseReference each=prog.child(type);
        each.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    EachBranchCardCommonClass temp=new EachBranchCardCommonClass();
                    Log.v("fire",type);
                    temp.img=d.child("img").getValue(String.class);
                    temp.cost=d.child("cost").getValue(Integer.class);
                    temp.head=d.child("name").getValue(String.class);
                    list.add(temp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    public class init
    {
        init(RecyclerView recyclerView, LinearLayoutManager layoutManager, eachBranchRecyclerAdaptor adaptor,ArrayList<EachBranchCardCommonClass> list1)
        {

            layoutManager=new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            adaptor=new eachBranchRecyclerAdaptor(list1);
            recyclerView.setAdapter(adaptor);
        }
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



    public class eachBranchRecyclerAdaptor extends RecyclerView.Adapter<eachBranchRecyclerAdaptor.cubeCardHolder>
    {

        DatabaseReference each;

        ArrayList<EachBranchCardCommonClass> eachList;

        public eachBranchRecyclerAdaptor(ArrayList<EachBranchCardCommonClass> list)
        {
            eachList=list;

        }

        @Override
        public cubeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_branch_recycler_card,parent,false);
            return  new cubeCardHolder(itemView);
        }

        @Override
        public void onBindViewHolder(cubeCardHolder holder, int position) {


            Log.v("fire",eachList.get(position).head+"  "+eachList.get(position).cost);
            holder.heading.setText(eachList.get(position).head);
            Glide.with(getApplicationContext())
                    .load(eachList.get(position).img)
                    .thumbnail(Glide.with(getApplicationContext()).load(R.drawable.ring))
                    .into(holder.img);
            holder.cost.setText(eachList.get(position).cost+"");

        }



        @Override
        public int getItemCount() {
           return eachList.size();
        }

        public class cubeCardHolder extends RecyclerView.ViewHolder
        {
            ImageView img;
            TextView heading,day,time,cost;


            public cubeCardHolder(View itemView) {
                super(itemView);

                img=(ImageView)itemView.findViewById(R.id.eachBranchCardImage);
                heading=(TextView)itemView.findViewById(R.id.eachBranchCardHeading);
                day=(TextView)itemView.findViewById(R.id.eachBranchCardDay);
                time=(TextView)itemView.findViewById(R.id.eachBranchCardTime);
                cost=(TextView)itemView.findViewById(R.id.eachBranchCardCost);

            }
        }
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
