package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class legal extends AppCompatActivity {

    @BindView(R.id.legal_toolbar)
    Toolbar toolbar;

    @BindView(R.id.legalRecycler)
    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;
    legalRecyclerAdaptor adaptor;

    @BindView(R.id.legalDev)
            TextView dev;
    NiftyDialogBuilder dialogBuilder;
    ArrayList<data> apiList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Legal");
        addData();
        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adaptor=new legalRecyclerAdaptor();
        recyclerView.setAdapter(adaptor);
        dialogBuilder=NiftyDialogBuilder.getInstance(this);
        dialogBuilder
                .withTitle("Contact Developers")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("How would you like to contact developers")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#9c27b0")                               //def  | withDialogColor(int resid)

                .withDuration(700)                                          //def
                .withEffect(Effectstype.Flipv)                                         //def Effectstype.Slidetop
                .withButton1Text("Call")                                      //def gone
                .withButton2Text("Mail")                                  //def gone
                                //def    | isCancelable(true)
                //   .setCustomView(R.layout.custom_feedback_dialog,itemView.getContext())       // .setCustomView(View or ResId,context)

                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:+919738441708"));
                        startActivity(intent);
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                 "mailto", "eternalslumber1@gmail.com", null));
                                         emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hi, I am contacting from brain studio application");
                                         startActivity(Intent.createChooser(emailIntent, null));
                                     }
                                 }
                );



        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.withButton1Text("Call")                                      //def gone
                        .withButton2Text("Mail");
                dialogBuilder.show();



            }
        });





    }

    void addData()
    {
        apiList.add(new data("Butter Knife","https://github.com/JakeWharton/butterknife#butter-knife","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("davideas/FlipView","https://github.com/davideas/FlipView"
                                ,"Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("Glide","https://github.com/bumptech/glide","Custom"));
        apiList.add(new data("ArcNavigationView","https://github.com/rom4ek/ArcNavigationView","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("RollViewPager","https://github.com/Jude95/RollViewPager","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("ExpandableLayout","https://github.com/traex/ExpandableLayout","The MIT License"));
        apiList.add(new data("MaterialSpinner","https://github.com/jaredrummler/MaterialSpinner","Licensed under the Apache License, Version 2.0 (the \"License\")"));

        apiList.add(new data("Shareable","https://github.com/robertsimoes/Shareable","Shareable is open-sourced under MIT-License."));
        apiList.add(new data("GmailBackground","https://github.com/luongvo/GmailBackground","Licensed to the Apache Software Foundation"));
        apiList.add(new data("MagicForm","https://github.com/julianfalcionelli/MagicForm","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("ArrowDownloadButton","https://github.com/fenjuly/ArrowDownloadButton","LICENSE UNDER MIT"));
        apiList.add(new data("SimpleArcLoader","https://github.com/generic-leo/SimpleArcLoader","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("Snacky","https://github.com/matecode/Snacky","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("Animated-clock-icon","https://github.com/alxrm/animated-clock-icon","MIT License"));
        apiList.add(new data("AndroidAudioRecorder","https://github.com/adrielcafe/AndroidAudioRecorder","The MIT License"));
        apiList.add(new data("NiftyDialogEffects","https://github.com/sd6352051/NiftyDialogEffects","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("PhotoView","https://github.com/chrisbanes/PhotoView","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("custom-typeface","https://github.com/ppicas/custom-typeface","Licensed under the Apache License, Version 2.0 (the \"License\")"));
        apiList.add(new data("PermissionHelper","https://github.com/nirav-tukadiya/PermissionHelper","Licensed under the Apache License, Version 2.0 (the \"License\")"));
    }

    public class legalRecyclerAdaptor extends RecyclerView.Adapter<legalRecyclerAdaptor.legalViewHolder>
    {

        @Override
        public legalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.legal_recycler_card,parent,false);
            return new legalViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(legalViewHolder holder, final int position) {
            holder.head.setText(apiList.get(position).apiName);
            holder.type.setText(apiList.get(position).type);

            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(apiList.get(position).link));
                    startActivity(viewIntent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return apiList.size();
        }

        class legalViewHolder extends RecyclerView.ViewHolder
        {
            TextView head,type;
            CardView card;

            public legalViewHolder(View itemView) {
                super(itemView);
                head=(TextView)itemView.findViewById(R.id.legalText);
                type=(TextView)itemView.findViewById(R.id.legalType);
                card=(CardView)itemView.findViewById(R.id.legalCard);
            }
        }


    }

   public class data
    {
        String apiName,link,type;
        data(String s1,String s2,String s3)
        {
            apiName=s1;
            link=s2;
            type=s3;
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
