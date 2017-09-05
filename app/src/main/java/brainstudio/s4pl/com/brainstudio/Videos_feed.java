package brainstudio.s4pl.com.brainstudio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robertsimoes.shareable.Shareable;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
public class Videos_feed extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Videos_feed() {
    }
    public static Videos_feed newInstance(String param1, String param2) {
        Videos_feed fragment = new Videos_feed();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @BindView(R.id.videosRecyclerList) RecyclerView youtubeThumbNailRecyclerList;
    LinearLayoutManager layoutManager;
    //String[] videoIds = new String[4];
    ArrayList<String> videoIds=new ArrayList<>();
    ArrayList<String> videoHeadingsList=new ArrayList<>();
    static String urlPart1 = "http://img.youtube.com/vi/";
    static String urlPart2 = "/0.jpg";
    static String watchActualUrl="https://www.youtube.com/watch?v=";
    int countVideoIds=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View v=inflater.inflate(R.layout.fragment_videos_feed, container, false);
        ButterKnife.bind(this,v);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        youtubeThumbNailRecyclerList.setLayoutManager(layoutManager);
        thumbNailAdapter adapter=new thumbNailAdapter();
        extractVieoIds(adapter);
        youtubeThumbNailRecyclerList.setAdapter(adapter);
        return v;
    }

    void extractVieoIds(final thumbNailAdapter adapter)
    {

        DatabaseReference parentRef= FirebaseDatabase.getInstance().getReference("videos");
        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot noteDataSnapShot:dataSnapshot.getChildren()) {
                    videoIds.add( noteDataSnapShot.getValue(String.class));
                    extractVideoHeadings(noteDataSnapShot.getValue(String.class),adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void extractVideoHeadings(String videoId,thumbNailAdapter adapter)
    {
        String youTubeUrl=watchActualUrl+videoId;
        try {
            if (youTubeUrl != null) {
                URL embededURL = new URL("http://www.youtube.com/oembed?url=" +
                        youTubeUrl + "&format=json"
                );
               // Log.v("test123",embededURL.toString());
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
               // Log.v("test123",new JSONObject(IOUtils.toString(embededURL)).getString("title"));
                videoHeadingsList.add( new JSONObject(IOUtils.toString(embededURL)).getString("title"));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    public class thumbNailAdapter extends RecyclerView.Adapter<thumbNailAdapter.thumbnailHolder>
    {
        public class thumbnailHolder extends RecyclerView.ViewHolder {
            ImageView thumbNail;
            Button shareWatsup,shareFacebook,shareOthers,youtubePlayButton;
            TextView videoHeading;
            RelativeLayout thumbnailContainer;
            public thumbnailHolder(View itemView) {
                super(itemView);
                shareWatsup=(Button)itemView.findViewById(R.id.watsupShare);
                shareFacebook=(Button)itemView.findViewById(R.id.facebookShare);
                shareOthers=(Button)itemView.findViewById(R.id.shareOthers);
                thumbNail=(ImageView)itemView.findViewById(R.id.thumbnail);
                videoHeading=(TextView)itemView.findViewById(R.id.videoHeading);
                thumbnailContainer=(RelativeLayout)itemView.findViewById(R.id.thumbNailContainer);
                youtubePlayButton=(Button)itemView.findViewById(R.id.youtubePlatButton);
            }
        }
        @Override
        public thumbNailAdapter.thumbnailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnailcard,parent,false);
            return new thumbnailHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final thumbNailAdapter.thumbnailHolder holder, final int position)
        {
            Glide.with(Videos_feed.this)
                    .load(urlPart1+videoIds.get(position)+urlPart2)
                    .thumbnail(Glide.with(getContext()).load(R.drawable.ring))
                    .centerCrop()
                    .into(holder.thumbNail);

            holder.thumbnailContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                            "AIzaSyARQOgjxXqo7mKlzJIcQ-q3QsF7nBCJ2PQ",videoIds.get(position),0,true,true));
                }
            });
            holder.videoHeading.setText(videoHeadingsList.get(position));
            Log.v("test123",videoHeadingsList.get(position));
            holder.shareWatsup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT,watchActualUrl+videoIds.get(position));
                    try
                    {
                        getActivity().startActivity(whatsappIntent);
                    }
                    catch (android.content.ActivityNotFoundException ex)
                    {

                    }

                }
            });
            holder.shareFacebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Shareable shareLink=new Shareable.Builder(getActivity().getBaseContext())
                            .message("Brain Studio")
                            .url(watchActualUrl+videoIds.get(position))
                            .socialChannel(Shareable.Builder.FACEBOOK)
                            .build();
                    shareLink.share();
                }
            });
            holder.shareOthers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Shareable shareLink=new Shareable.Builder(getActivity().getBaseContext())
                           .message("Brain Studio")
                           .url(watchActualUrl+videoIds.get(position))
                           .build();
                    shareLink.share();
                }
            });

        }


        @Override
        public int getItemCount() {
            return videoIds.size();
        }


    }
}
