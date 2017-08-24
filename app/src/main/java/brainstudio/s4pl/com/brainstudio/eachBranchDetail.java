package brainstudio.s4pl.com.brainstudio;

import android.sax.RootElement;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    LinearLayoutManager layoutManagerCube;
    RecyclerView cubeRecycle,jugglingRecycle,graphoRecycle,stackRecycle,corporateRecycle,calligraphyRecycle;
    eachBranchRecyclerAdaptor cubeRecyclerAdaptor,jugglingRecycleAdaptor,graphoRecycleAdaptor,stackRecycleAdaptor,corporateRecycleAdaptor,calligraphyRecycleAdaptor;

    init cubeInit,jugglingInit,graphoInit,stackInit,corporateInit,calligraphyInit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_branch_detail);
        ButterKnife.bind(this);
        collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Demo");
        setSupportActionBar(toolbar);

        rollPagerView.setAdapter(new TestLoopAdapter(rollPagerView));



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
        RelativeLayout cubeBody= cubeExpandableLayout.getContentRelativeLayout();
        cubeRecycle =(RecyclerView) cubeBody.findViewById(R.id.eachBranchCubeRecyclerView) ;
        TextView cubeText=(TextView) cubeExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        cubeText.setText("Rubik's Cube");
        cubeInit=new init(cubeRecycle,layoutManagerCube,cubeRecyclerAdaptor,"cube");


        RelativeLayout jugglingBody=jugglingExpandableLayout.getContentRelativeLayout();
        jugglingRecycle=(RecyclerView) jugglingBody.findViewById(R.id.eachBranchCubeRecyclerView);
        TextView jugglingText=(TextView) jugglingExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        jugglingText.setText("Juggling");
        jugglingInit=new init(jugglingRecycle,layoutManagerCube,jugglingRecycleAdaptor,"juggling");


        RelativeLayout graphoBody= graphoExpandableLayout.getContentRelativeLayout();
        graphoRecycle =(RecyclerView) graphoBody.findViewById(R.id.eachBranchCubeRecyclerView) ;
        TextView graphoText=(TextView) graphoExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        graphoText.setText("Scientific Handwriting");
        graphoInit=new init(graphoRecycle,layoutManagerCube,graphoRecycleAdaptor,"grapho");


        RelativeLayout stackBody= stackExpandableLayout.getContentRelativeLayout();
        stackRecycle =(RecyclerView) stackBody.findViewById(R.id.eachBranchCubeRecyclerView) ;
        TextView stackText=(TextView) stackExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        stackText.setText("Speed Stacking");
        stackInit=new init(stackRecycle,layoutManagerCube,stackRecycleAdaptor,"stack");


        RelativeLayout corporateBody= corporateExpandableLayout.getContentRelativeLayout();
        corporateRecycle =(RecyclerView) corporateBody.findViewById(R.id.eachBranchCubeRecyclerView);
        TextView corporateText=(TextView) corporateExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        corporateText.setText("Corporate Training");
        corporateInit=new init(corporateRecycle,layoutManagerCube,corporateRecycleAdaptor,"corporate");


        RelativeLayout calligraphyBody=calligraphyExpandableLayout.getContentRelativeLayout();
        calligraphyRecycle=(RecyclerView)calligraphyBody.findViewById(R.id.eachBranchCubeRecyclerView);
        TextView calligraphyText=(TextView) calligraphyExpandableLayout.getHeaderRelativeLayout().findViewById(R.id.eachBranchHeaderText);
        calligraphyText.setText("Calligraphy");
        calligraphyInit=new init(calligraphyRecycle,layoutManagerCube,calligraphyRecycleAdaptor,"calligraphy");
    }



    public class init
    {
        init(RecyclerView recyclerView, LinearLayoutManager layoutManager, eachBranchRecyclerAdaptor adaptor,String type)
        {

            layoutManager=new LinearLayoutManager(getApplicationContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            adaptor=new eachBranchRecyclerAdaptor(type);
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
        String type;

        public eachBranchRecyclerAdaptor(String type1)
        {
            type=type1;
        }

        @Override
        public cubeCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_branch_recycler_card,parent,false);
            return  new cubeCardHolder(itemView);
        }

        @Override
        public void onBindViewHolder(cubeCardHolder holder, int position) {

        }



        @Override
        public int getItemCount() {
            return (type.length()-2);
        }

        public class cubeCardHolder extends RecyclerView.ViewHolder
        {


            public cubeCardHolder(View itemView) {
                super(itemView);

            }
        }
    }
}
