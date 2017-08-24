package brainstudio.s4pl.com.brainstudio;



import android.app.FragmentTransaction;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;


import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class youTubeList extends YouTubeBaseActivity {
    String[] videoIds = new String[4];
    @BindView(R.id.youtubeThumbNailRecyclerList) RecyclerView youtubeThumbNailRecyclerList;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_list);

        ButterKnife.bind(this);
        videoIds[0] = "MuuBwh4mg6M";
        videoIds[1] = "vaApaA9I7Uk";
        videoIds[2] = "qE1fSp3NVgo";
        videoIds[3] = "NxQgiF56TXA";
        //videoIds[4] = "Eo0trT3Dli4";
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        youtubeThumbNailRecyclerList.setLayoutManager(layoutManager);
        youtubeThumbNailRecyclerList.setAdapter(new thumbNailAdapter());

    }
    public class thumbNailAdapter extends RecyclerView.Adapter<thumbNailAdapter.thumbnailHolder>  {
        @Override
        public thumbnailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnailcard,parent,false);
            return new thumbnailHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final thumbnailHolder holder, final int position)
        {
            holder.thumbNail.setTag(videoIds[position]);
            holder.thumbNail.initialize("AIzaSyARQOgjxXqo7mKlzJIcQ-q3QsF7nBCJ2PQ", new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo((String)youTubeThumbnailView.getTag());
                    holder.thumbNail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(YouTubeStandalonePlayer.createVideoIntent(youTubeList.this,
                                    "AIzaSyARQOgjxXqo7mKlzJIcQ-q3QsF7nBCJ2PQ",videoIds[position],0,true,true));
                        }
                    });
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                    YouTubeInitializationResult youTubeInitializationResult) {
//
                }
            });



        }


        @Override
        public int getItemCount() {
            return videoIds.length;
        }



        public class thumbnailHolder extends RecyclerView.ViewHolder {
            YouTubeThumbnailView thumbNail;
            public thumbnailHolder(View itemView) {
                super(itemView);
                thumbNail=(YouTubeThumbnailView)itemView.findViewById(R.id.thumbnail);
            }
        }
    }


}

