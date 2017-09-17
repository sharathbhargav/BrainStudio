package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;

import brainstudio.s4pl.com.brainstudio.EachProgramCardData;
import brainstudio.s4pl.com.brainstudio.programmeData;
import brainstudio.s4pl.com.brainstudio.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommonFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    String type;

    private String mParam1;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.eachProgramGridRecycler)
    RecyclerView eachProgramGridRecycler;
    @BindView(R.id.eachProgramBenifits)
    TextView benifits;
    @BindView(R.id.eachProgramInfo)
    TextView info;
    @BindView(R.id.eachProgramHeading)
    TextView heading;

    eachProgramGridRecyclerAdaptor recyclerAdaptor;
    GridLayoutManager gridLayoutManager;

    DatabaseReference level1;
    DataSnapshot benifitsRef,infoRef,imgRef;
    ArrayList<EachProgramCardData> list=new ArrayList<>();


    programmeData data=new programmeData();

    SimpleArcDialog mDialog;
    public CommonFragment() {
        // Required empty public constructor
    }


    public static CommonFragment newInstance(String param1) {
        CommonFragment fragment = new CommonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
     //   LayoutInflater inflater = LayoutInflater.from(getContext());
     //   if (inflater.getFactory() != null) {
     //       inflater = inflater.cloneInContext(getContext());
     //   }
     //  inflater.setFactory(new CustomTypefaceFactory(
     //          getContext(), CustomTypeface.getInstance(), getActivity()));


        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }

        mDialog = new SimpleArcDialog(getContext());

        ArcConfiguration configuration = new ArcConfiguration(getContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);




        Log.v("fragment","in onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_common, container, false);
        ButterKnife.bind(this,v);
        list.clear();
        gridLayoutManager=new GridLayoutManager(getContext(),2);
        eachProgramGridRecycler.setLayoutManager(gridLayoutManager);
        recyclerAdaptor=new eachProgramGridRecyclerAdaptor();
        eachProgramGridRecycler.setAdapter(recyclerAdaptor);
        Log.v("fragment","benifits:::"+data.getBenifits(mParam1));
        heading.requestFocus();

        level1= FirebaseDatabase.getInstance().getReference(mParam1);

        if(data.getBenifits(mParam1)==null) {
            mDialog.show();
            level1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    benifitsRef = dataSnapshot.child("benifits");
                    infoRef = dataSnapshot.child("info");
                    imgRef = dataSnapshot.child("img");


                    data.setData(infoRef.getValue(String.class), benifitsRef.getValue(String.class), dataSnapshot.child("name").getValue(String.class), mParam1);
                    benifits.setText(data.getBenifits(mParam1));
                    info.setText(data.getInfo(mParam1));
                    heading.setText(data.getHead(mParam1));
                    for (DataSnapshot d : imgRef.getChildren()) {
                        EachProgramCardData t = new EachProgramCardData();
                        t.link = d.child("link").getValue(String.class);
                        t.name = d.child("name").getValue(String.class);
                        list.add(t);
                        recyclerAdaptor.notifyDataSetChanged();
                        mDialog.dismiss();
                    }

                    if (mDialog.isShowing())
                        mDialog.dismiss();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        else
        {
            benifits.setText(data.getBenifits(mParam1));
            info.setText(data.getInfo(mParam1));
            heading.setText(data.getHead(mParam1));

            level1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    imgRef = dataSnapshot.child("img");
                    for (DataSnapshot d : imgRef.getChildren()) {
                        EachProgramCardData t = new EachProgramCardData();
                        t.link = d.child("link").getValue(String.class);
                        t.name = d.child("name").getValue(String.class);
                        list.add(t);
                        recyclerAdaptor.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }






        Log.v("fragment","in onCreateView");









        eachProgramGridRecycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });



        return v;
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
        Log.v("fragment","In onAttach");
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


    public class eachProgramGridRecyclerAdaptor extends RecyclerView.Adapter<eachProgramGridRecyclerAdaptor.gridCardHolder>
    {
        @Override
        public gridCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_program_grid_recycler_card,parent,false);
            return new gridCardHolder(itemView);
        }

        @Override
        public void onBindViewHolder(gridCardHolder holder, int position) {

            Glide.with(getContext())
                    .load(list.get(position).link)
                    .thumbnail(Glide.with(getContext()).load(R.drawable.ring))
                    .centerCrop()
                    .into(holder.gridCardImage);
            holder.gridCardText.setText(list.get(position).name);
        }

        @Override
        public int getItemCount() {
            return list.size();
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
