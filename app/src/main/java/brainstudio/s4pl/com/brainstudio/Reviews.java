package brainstudio.s4pl.com.brainstudio;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.expandablelayout.library.ExpandableLayout;
import com.crashlytics.android.Crashlytics;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

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
        Fabric.with(this, new Crashlytics());
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


        MediaPlayer mp;
        ProgressDialog pd;
        int lengthOfAudio=100;
        Runnable runnable;
        @Override
        public reviewCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
            return new reviewCardHolder(itemView);
        }



        @Override
        public void onBindViewHolder(final reviewCardHolder holder, int position) {

            holder.message.setText(reviewDatas.get(position).message);
            holder.course.setText(reviewDatas.get(position).course);
            holder.name.setText(reviewDatas.get(position).name);
            holder.centre.setText(reviewDatas.get(position).centre);


           final Handler handler = new Handler();
        runnable =new Runnable() {

                public void run() {
                    // yourMethod();
                    if (mp.isPlaying())
                    {
                        holder.seekBar.setProgress((int)(((float)mp.getCurrentPosition() / mp.getDuration()) * 100));
                     //   holder.seekBar.setVisibility(View.GONE);

                        Log.v("mp","current=="+mp.getCurrentPosition()+"   duration===="+mp.getDuration());
                        handler.postDelayed(runnable,1000);
                    }
                }
            };
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        pd = new ProgressDialog(Reviews.this);

                        pd.setMessage("Buffering.....");
                        pd.show();
                        mp = new MediaPlayer();
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                pd.setMessage("Playing....."+mp.getDuration()/1000);
                                mp.start();
                                lengthOfAudio=mp.getDuration();
                                pd.dismiss();
                                holder.audioLayout.setVisibility(View.VISIBLE);
                                handler.postDelayed(runnable,1000);


                            }
                        });
                        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                Toast.makeText(getApplicationContext(),"Error in streaming",Toast.LENGTH_LONG).show();
                                return false;
                            }
                        });
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/brainstudio-a7a21.appspot.com/o/123.wav?alt=media&token=ea2263db-48f2-4562-a14a-1ea97256c3fc");
                        mp.prepareAsync();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                Toast.makeText(getApplicationContext(),"Playing complete",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    catch(Exception e)
                    {
                        Log.e("StreamAudioDemo", e.getMessage());
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return reviewDatas.size();
        }

        public class reviewCardHolder extends RecyclerView.ViewHolder
        {
            ExpandableLayout eachCardLayout;
            TextView name,course,centre,message;
            Button play;
            LinearLayout audioLayout;
            SeekBar seekBar;


            public reviewCardHolder(View itemView) {
                super(itemView);
                eachCardLayout=(ExpandableLayout)itemView.findViewById(R.id.reviewCardExpandableLayout);
                RelativeLayout header=eachCardLayout.getHeaderRelativeLayout();
                RelativeLayout body=eachCardLayout.getContentRelativeLayout();
                name=(TextView) header.findViewById(R.id.reviewCardHeaderName);
                course=(TextView)header.findViewById(R.id.reviewCardHeaderCourse);
                centre=(TextView)header.findViewById(R.id.reviewCardHeaderCentre);
                message=(TextView)body.findViewById(R.id.reviewCardBodyMessage);
                play=(Button)body.findViewById(R.id.streamAudio);
                audioLayout=(LinearLayout)body.findViewById(R.id.audioLayout);
                seekBar=(SeekBar)body.findViewById(R.id.audio_play_seek_bar);

            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
