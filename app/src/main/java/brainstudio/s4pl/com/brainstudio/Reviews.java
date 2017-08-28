package brainstudio.s4pl.com.brainstudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Reviews extends AppCompatActivity {

    @BindView(R.id.reviewRecycler)
    RecyclerView reviewRecycler;
    LinearLayoutManager layoutManager;

    @BindView(R.id.review_toolbar)
    Toolbar toolbar;
    reviewAdaptor adaptor;
    DatabaseReference feedbackRef;
    ArrayList<ReviewData> reviewDatas=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        toolbar.setTitle("Reviews");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reviewRecycler.setLayoutManager(layoutManager);
        adaptor=new reviewAdaptor();
        reviewRecycler.setAdapter(adaptor);


        feedbackRef= FirebaseDatabase.getInstance().getReference("feedback");
        feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren())
                {
                    if(d.child("review").getValue(Integer.class)==1)
                    {
                        ReviewData temp=new ReviewData();
                        temp.centre=d.child("centre").getValue(String.class);
                        temp.course=d.child("course").getValue(String.class);
                        temp.name=d.child("name").getValue(String.class);
                        temp.message=d.child("msg").getValue(String.class);
                        reviewDatas.add(temp);
                        adaptor.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

            holder.message.setText(reviewDatas.get(position).message);
            holder.course.setText(reviewDatas.get(position).course);
            holder.name.setText(reviewDatas.get(position).name);
            holder.centre.setText(reviewDatas.get(position).centre);

        }

        @Override
        public int getItemCount() {
            return reviewDatas.size();
        }

        public class reviewCardHolder extends RecyclerView.ViewHolder
        {
            ExpandableLayout eachCardLayout;
            TextView name,course,centre,message;


            public reviewCardHolder(View itemView) {
                super(itemView);
                eachCardLayout=(ExpandableLayout)itemView.findViewById(R.id.reviewCardExpandableLayout);
                RelativeLayout header=eachCardLayout.getHeaderRelativeLayout();
                RelativeLayout body=eachCardLayout.getContentRelativeLayout();
                name=(TextView) header.findViewById(R.id.reviewCardHeaderName);
                course=(TextView)header.findViewById(R.id.reviewCardHeaderCourse);
                centre=(TextView)header.findViewById(R.id.reviewCardHeaderCentre);
                message=(TextView)body.findViewById(R.id.reviewCardBodyMessage);



            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
