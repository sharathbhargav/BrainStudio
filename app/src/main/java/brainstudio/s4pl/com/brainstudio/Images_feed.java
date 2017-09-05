package brainstudio.s4pl.com.brainstudio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fenjuly.library.ArrowDownloadButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.robertsimoes.shareable.Shareable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Images_feed.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Images_feed#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Images_feed extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Images_feed() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Images_feed.
     */
    // TODO: Rename and change types and number of parameters
    public static Images_feed newInstance(String param1, String param2) {
        Images_feed fragment = new Images_feed();
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
    @BindView(R.id.imageListRecycler)
    RecyclerView imageList;
    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;
    FirebaseStorage storage;
    ArrayList<String> urlsList=new ArrayList<>( );
    int progress=0;

    ProgressDialog progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_images_feed, container, false);
        ButterKnife.bind(this,v);
       //layoutManager = new LinearLayoutManager(getContext());
       //layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
       //imageList.setLayoutManager(layoutManager);

        gridLayoutManager=new GridLayoutManager(getContext(),2);
        imageList.setLayoutManager(gridLayoutManager);

        storage = FirebaseStorage.getInstance();
        //Log.v("test123",storage.getReferenceFromUrl(urlsList.get(0)).toString());
        progressBar=new ProgressDialog(getActivity());
        imageListAdapter adapter=new imageListAdapter();
        extractImageUrls(adapter);
        imageList.setAdapter(adapter);
        return v;
    }

    void extractImageUrls(final imageListAdapter adapter)
    {
        DatabaseReference parentRef= FirebaseDatabase.getInstance().getReference("images");
        parentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot eachDatasnapShaot:dataSnapshot.getChildren())
                {
                    urlsList.add(eachDatasnapShaot.getValue(String.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public class imageListAdapter extends RecyclerView.Adapter<imageListAdapter.imageListHolder>
    {
        @Override
        public imageListHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.imagelist_card,parent,false);
            return new  imageListHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final imageListHolder holder, final int position)
        {
            Glide.with(Images_feed.this)
                    .load(urlsList.get(position))
                    .thumbnail(Glide.with(getContext()).load(R.drawable.ring))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.imageListCardImageView);

            holder.imageListCardImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent tophotoSliderIntent=new Intent(getContext(),PhotoSliderFullView.class);
                    tophotoSliderIntent.putExtra("Position",position);
                    tophotoSliderIntent.putExtra("Urlslist",urlsList);
                    startActivity(tophotoSliderIntent);

                }
            });



        }

        @Override
        public int getItemCount() {
            return urlsList.size();
        }

        public class imageListHolder extends RecyclerView.ViewHolder
        {
            ImageView imageListCardImageView;

            public imageListHolder(View itemView) {
                super(itemView);
                imageListCardImageView=(ImageView)itemView.findViewById(R.id.imageListImageView);

            }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
