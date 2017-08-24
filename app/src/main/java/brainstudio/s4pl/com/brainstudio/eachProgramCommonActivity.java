package brainstudio.s4pl.com.brainstudio;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class eachProgramCommonActivity extends AppCompatActivity {


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
}
