package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
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

import junit.framework.Test;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import io.fabric.sdk.android.Fabric;

public class eachBranchDetail extends AppCompatActivity {

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

    @BindView(R.id.eachBranchDetailLocationButton)
    ImageView location;
    LinearLayoutManager layoutManagerCube;
    RecyclerView cubeRecycle,jugglingRecycle,graphoRecycle,stackRecycle,corporateRecycle,calligraphyRecycle;
    eachBranchRecyclerAdaptor cubeRecyclerAdaptor,jugglingRecycleAdaptor,graphoRecycleAdaptor,stackRecycleAdaptor,corporateRecycleAdaptor,calligraphyRecycleAdaptor;

    init cubeInit,jugglingInit,graphoInit,stackInit,corporateInit,calligraphyInit;
 ArrayList<EachBranchCardCommonClass> cubeList,jugglingList,scientificlist,stackList,corporateList,calligraphyList;

ArrayList<String> picSlide=new ArrayList<>();
    String centre="Uttarahalli",locationString="0,0";
    DatabaseReference parent,child,prog,loc;
    String dayString,timeString;
    TestLoopAdapter testLoopAdapter;

    SimpleArcDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_branch_detail);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent fromHome=getIntent();
        String path=fromHome.getStringExtra("path");

        testLoopAdapter=new TestLoopAdapter(rollPagerView);

        mDialog = new SimpleArcDialog(this);

        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);
        mDialog.setCancelable(false);
        mDialog.show();







        Log.v("home","path received=  "+path);
        child= FirebaseDatabase.getInstance().getReference(path);
        child.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collapsingToolbarLayout.setTitle(dataSnapshot.child("name").getValue(String.class));
                final String[] time=dataSnapshot.child("time").getValue(String.class).split(":");
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
                timeString=t1+"-"+t2;
                dayString=dataSnapshot.child("days").getValue(String.class);



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
        prog=child.child("programmes");
        loc=child.child("location");
        loc.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                locationString=dataSnapshot.getValue(String.class);
                location.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+locationString));
                startActivity(intent);
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
                    if(mDialog.isShowing())
                        mDialog.dismiss();
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
                    .centerCrop()
                    .into(holder.img);
            holder.day.setText(dayString);
            holder.time.setText(timeString);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
