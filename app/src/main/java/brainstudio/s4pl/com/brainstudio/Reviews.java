package brainstudio.s4pl.com.brainstudio;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Parcelable;
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
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import de.mateware.snacky.Snacky;
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
    NiftyDialogBuilder dialogBuilder;
    ViewGroup viewGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
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

        dialogBuilder=NiftyDialogBuilder.getInstance(this);

        dialogBuilder
                .withTitle("Audio Review")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
               // .withMessage("How would you like to give your feedback")                     //.withMessage(null)  no Msg
               // .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)

                .withDuration(700)                                          //def
                                                  //def gone
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                .setCustomView(R.layout.audio_dialog,getBaseContext());       // .setCustomView(View or ResId,context

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
                        String type=d.child("type").getValue(String.class);
                        if(type.equals("audio"))
                            temp.audio=1;
                        else
                            temp.audio=0;
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

        int lengthOfAudio=100;
        Runnable runnable;

        @Override
        public reviewCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card,parent,false);
            return new reviewCardHolder(itemView);
        }



        @Override
        public void onBindViewHolder(final reviewCardHolder holder, final int position) {

            holder.message.setText(reviewDatas.get(position).message);
            holder.course.setText(reviewDatas.get(position).course);
            holder.name.setText(reviewDatas.get(position).name);
            holder.centre.setText(reviewDatas.get(position).centre);

            if(reviewDatas.get(position).audio==0)
                holder.play.setVisibility(View.GONE);
            else
                holder.message.setVisibility(View.GONE);
            holder.eachCardLayout.getHeaderRelativeLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.eachCardLayout.isOpened())
                        holder.eachCardLayout.hide();
                    else
                    {

                                holder.eachCardLayout.show();
                                Log.v("rec","all done");


                    }
                }
            });




           final Handler handler = new Handler();
          final  SeekBar seekBar=(SeekBar) dialogBuilder.findViewById(R.id.audioSeekBar);
            seekBar.setProgress(0);
            final TextView audioName=(TextView)dialogBuilder.findViewById(R.id.audioDialogName);
            final SimpleArcLoader loader=(SimpleArcLoader)dialogBuilder.findViewById(R.id.audioLoader);
        runnable =new Runnable() {

                public void run() {
                    // yourMethod();
                    if (mp.isPlaying())
                    {
                        seekBar.setProgress((int)(((float)mp.getCurrentPosition() / mp.getDuration()) * 100));


                        handler.postDelayed(runnable,1000);
                    }
                }
            };
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.v("progrees","changed=="+progress+" from user"+fromUser);
                    if(fromUser) {
                        handler.removeCallbacks(runnable);
                        float total = mp.getDuration();
                        float finalPosition = total * progress / 100;
                        mp.seekTo((int) (finalPosition));
                        handler.postDelayed(runnable, 1000);
                    }
                    if(progress==100)
                    {
                        dialogBuilder.withButton1Text("Play again");
                        holder.playing=false;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogBuilder.withEffect(Effectstype.Flipv)                                         //def Effectstype.Slidetop
                            .withButton1Text("Pause")                                      //def gone
                            .withButton2Text("Stop and exit");
                    dialogBuilder.isCancelable(false);
                    dialogBuilder.setButton1Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(holder.playing)
                            {
                                dialogBuilder.withButton1Text("Play");
                                holder.playing=false;
                                mp.pause();
                            }
                            else {
                                dialogBuilder.withButton1Text("Pause");
                                holder.playing=true;
                                mp.start();
                                handler.postDelayed(runnable,1000);
                            }

                        }
                    });
                    dialogBuilder.setButton2Click(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mp.stop();
                            dialogBuilder.dismiss();
                        }
                    });
                    dialogBuilder.show();

                    try
                    {
                        audioName.setText("Fetching audio");
                        mp = new MediaPlayer();
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {

                                audioName.setText("Playing");
                                mp.start();
                                lengthOfAudio=mp.getDuration();


                                handler.postDelayed(runnable,1);
                                loader.setVisibility(View.GONE);

                            }
                        });
                        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {

                                Snacky.builder()
                                        .setActivty(Reviews.this)
                                        .setText("Error in playing.")
                                        .setDuration(Snacky.LENGTH_SHORT)
                                        .error()
                                        .show();

                                return false;
                            }
                        });
                        mp.setDataSource("https://firebasestorage.googleapis.com/v0/b/brainstudio-a7a21.appspot.com/o/123.wav?alt=media&token=ea2263db-48f2-4562-a14a-1ea97256c3fc");
                        mp.prepareAsync();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                dialogBuilder.withButton1Text("Play again");
                                holder.playing=false;
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
            ImageView play;

            boolean playing=true;


            public reviewCardHolder(View itemView) {
                super(itemView);
                eachCardLayout=(ExpandableLayout)itemView.findViewById(R.id.reviewCardExpandableLayout);
                eachCardLayout.hide();
                RelativeLayout header=eachCardLayout.getHeaderRelativeLayout();
                RelativeLayout body=eachCardLayout.getContentRelativeLayout();
                name=(TextView) header.findViewById(R.id.reviewCardHeaderName);
                course=(TextView)header.findViewById(R.id.reviewCardHeaderCourse);
                centre=(TextView)header.findViewById(R.id.reviewCardHeaderCentre);
                message=(TextView)body.findViewById(R.id.reviewCardBodyMessage);
                play=(ImageView) body.findViewById(R.id.streamAudio);



            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
