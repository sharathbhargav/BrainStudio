package brainstudio.s4pl.com.brainstudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reviews extends AppCompatActivity {

    @BindView(R.id.reviewRecycler)
    RecyclerView reviewRecycler;
    LinearLayoutManager layoutManager;

    reviewAdaptor adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);


        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reviewRecycler.setLayoutManager(layoutManager);
        adaptor=new reviewAdaptor();
        reviewRecycler.setAdapter(adaptor);
    }


    public class reviewAdaptor extends RecyclerView.Adapter<reviewAdaptor.reviewCardHolder>
    {
        @Override
        public reviewCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
            return new reviewCardHolder(itemView);
        }

        @Override
        public void onBindViewHolder(reviewCardHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class reviewCardHolder extends RecyclerView.ViewHolder
        {

            TextView name,centre,course,msg;

            public reviewCardHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.reviewCardName);
                centre=(TextView)itemView.findViewById(R.id.reviewCardCentre);
                course=(TextView)itemView.findViewById(R.id.reviewCardCourse);
                msg=(TextView) itemView.findViewById(R.id.reviewCardMessage);


            }
        }
    }
}
