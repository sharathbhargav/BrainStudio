package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.keiferstone.nonet.Configuration;
import com.keiferstone.nonet.ConnectionStatus;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;
import com.neurenor.permissions.PermissionCallback;
import com.neurenor.permissions.PermissionsHelper;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import de.mateware.snacky.Snacky;
import io.fabric.sdk.android.Fabric;
import julianfalcionelli.magicform.MagicForm;
import julianfalcionelli.magicform.base.FormError;
import julianfalcionelli.magicform.base.FormField;
import julianfalcionelli.magicform.base.ValidationMode;
import julianfalcionelli.magicform.base.ValidatorCallbacks;
import julianfalcionelli.magicform.validation.Validation;
import julianfalcionelli.magicform.validation.ValidationMaxLength;
import julianfalcionelli.magicform.validation.ValidationMinLength;
import julianfalcionelli.magicform.validation.ValidationNotEmpty;
import julianfalcionelli.magicform.validation.ValidationRegex;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;


import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

@Keep
@KeepClassMembers
public class feedback extends AppCompatActivity  {

    @BindView(R.id.feedback_toolbar)
    Toolbar toolbar;

    @BindView(R.id.feedbackSpinnerCentre)
    MaterialSpinner centreSpinner;
    @BindView(R.id.feedbackSpinnerCourse)
    MaterialSpinner courseSpinner;
    @BindView(R.id.feedbackName)
    EditText nameEdit;
    @BindView(R.id.feedbackButton)
    Button submit;
    @BindView(R.id.feedbackPhoneNumber)
    EditText phoneEdit;
    @BindView(R.id.feedbackMessage)
    EditText messageEdit;
    @BindView(R.id.feedbackMessageContainer)
    TextInputLayout messageContainer;
    @BindView(R.id.nextButton)
    Button next;
    @BindView(R.id.feedbackMessageGroup)
    LinearLayout feedbackMessageGroup;



    String course,centre;
    ArrayList<String> centreList=new ArrayList<>();
    ArrayList<String> courseList=new ArrayList<>(Arrays.asList("Rubik's cube","Juggling","Scientific Handwriting","Speed Stacking","Calligraphy","Corporate Training","Handwriting Analysis"));
    DatabaseReference parent,feedbackRef;
    MagicForm magicForm;
    boolean valid=false;
    SimpleArcDialog mDialog;
    int REQUEST_RECORD_AUDIO=111;
    String filePath ;
    int  fileType=0;//1 for audio

    NiftyDialogBuilder dialogBuilder;


    StorageReference mainRef;
    PermissionsHelper helper;

    //Monitor monitor;
    boolean netLost=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);
        toolbar.setTitle("Feedback");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Feedback");
        helper=new PermissionsHelper(feedback.this);
        mDialog = new SimpleArcDialog(this);
        dialogBuilder=NiftyDialogBuilder.getInstance(this);


        final Configuration config= NoNet.configure()
                .endpoint("https://google.com")
                .timeout(5)
                .connectedPollFrequency(10)
                .disconnectedPollFrequency(3)
                .build();
        NoNet.monitor(this)
                .configure(config)
                .poll()
                .callback(new Monitor.Callback() {
                    @Override
                    public void onConnectionEvent(int connectionStatus) {

                        if(connectionStatus== ConnectionStatus.DISCONNECTED && !netLost)
                        {
                            netLost=true;

                            Intent offLine=new Intent(getApplicationContext(),OfflineActivity.class);
                            startActivity(offLine);

                        }
                        if(connectionStatus==ConnectionStatus.CONNECTED)
                            netLost=false;


                    }
                });
       NoNet.check(this).start();


        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);
        mDialog.setCancelable(false);
        validate();
        mDialog.show();

        parent= FirebaseDatabase.getInstance().getReference("centre");
        mainRef=FirebaseStorage.getInstance().getReferenceFromUrl("gs://brainstudio-a7a21.appspot.com/feedbackAudio");
        parent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren())
                {
                    String cen=d.child("name").getValue(String.class);
                    centreList.add(cen);
                }
                mDialog.dismiss();
                centreSpinner.setItems(centreList);

                //DataSnapshot d=dataSnapshot.child(centreList.get(0));
                submit.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        centreSpinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
            }
        });


        centreSpinner.setSelectedIndex(0);
        courseSpinner.setSelectedIndex(0);

        centreSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                centre=item.toString();
            }
        });





        courseSpinner.setItems(courseList);
        courseSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {

                course=item.toString();
            }
        });

        // final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
        //         .findViewById(android.R.id.content)).getChildAt(0);
        // View itemView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_feedback_dialog,viewGroup);

        feedbackRef=FirebaseDatabase.getInstance().getReference("feedback");
        dialogBuilder
                .withTitle("Choose your preference")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")                              //def
                .withMessage("How would you like to give your feedback")                     //.withMessage(null)  no Msg
                .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                .withDialogColor("#9c27b0")                               //def  | withDialogColor(int resid)

                .withDuration(700)                                          //def
                .withEffect(Effectstype.Flipv)                                         //def Effectstype.Slidetop
                .withButton1Text("Type")                                      //def gone
                .withButton2Text("Voice")                                  //def gone
                .isCancelableOnTouchOutside(false)
                .isCancelable(false)
                //def    | isCancelable(true)
                //   .setCustomView(R.layout.custom_feedback_dialog,itemView.getContext())       // .setCustomView(View or ResId,context)

                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        fileType=0;
                        messageEdit.setVisibility(View.VISIBLE);
                        messageContainer.setVisibility(View.VISIBLE);
                        feedbackMessageGroup.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialogBuilder.dismiss();
                        if(helper.isPermissionGranted(WRITE_EXTERNAL_STORAGE))
                        {
                            if (helper.isPermissionGranted(RECORD_AUDIO))
                            {
                                submit.setVisibility(View.VISIBLE);
                                fileType = 1;
                                startRecording();
                            }
                            else
                            {
                                requestAudioPermission();
                            }
                        }
                        else
                            requestStoragePermission();

                    }
                });


        next.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid=false;
                magicForm.validate();
                if(valid) {

                    dialogBuilder.withButton1Text("Type")                                      //def gone
                            .withButton2Text("Voice")                                  //def gone
                            .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                            //   .setCustomView(R.layout.custom_feedback_dialog,itemView.getContext())       // .setCustomView(View or ResId,context)

                            .show();
                    next.setVisibility(View.GONE);
                }
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                pushData();
            }
        });



    }
    void requestAudioPermission()
    {
        helper.requestPermissions(new String[]{RECORD_AUDIO}, new PermissionCallback() {
            @Override
            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> mapPermissionGrants) {
                PermissionsHelper.PermissionGrant permissionGrant = mapPermissionGrants
                        .get(RECORD_AUDIO);
                switch (permissionGrant) {
                    case GRANTED:
                        submit.setVisibility(View.VISIBLE);
                        fileType = 1;
                        startRecording();
                        break;
                    case DENIED:
                        Snacky.builder()
                                .setActivty(feedback.this)
                                .setText("Permission required to continue.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .error()
                                .show();
                        next.setVisibility(View.VISIBLE);
                        break;
                    case NEVERSHOW:
                        Snacky.builder()
                                .setActivty(feedback.this)
                                .setText("Permission required to continue.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .error()
                                .show();
                        next.setVisibility(View.VISIBLE);
                        break;


                }
            }
        });

    }
    void requestStoragePermission()
    {
        helper.requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, new PermissionCallback() {
            @Override
            public void onResponseReceived(HashMap<String, PermissionsHelper.PermissionGrant> mapPermissionGrants) {
                PermissionsHelper.PermissionGrant permissionGrant = mapPermissionGrants
                        .get(WRITE_EXTERNAL_STORAGE);
                switch (permissionGrant) {
                    case GRANTED:
                        if(helper.isPermissionGranted(RECORD_AUDIO)) {
                            submit.setVisibility(View.VISIBLE);
                            fileType = 1;
                            startRecording();
                        }
                        else
                            requestAudioPermission();
                        break;
                    case DENIED:
                        Snacky.builder()
                                .setActivty(feedback.this)
                                .setText("Permission required to continue.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .error()
                                .show();
                        next.setVisibility(View.VISIBLE);
                        break;
                    case NEVERSHOW:
                        Snacky.builder()
                                .setActivty(feedback.this)
                                .setText("Permission required to continue.")
                                .setDuration(Snacky.LENGTH_LONG)
                                .error()
                                .show();
                        next.setVisibility(View.VISIBLE);
                        break;

                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        /**
         * helper's onRequestPermissionsResult must be called from activity.
         */
        helper.onRequestPermissionsResult(permissions, grantResults);
    }


    void pushData()
    {
        mDialog.show();
        course = courseList.get(courseSpinner.getSelectedIndex());
        centre = centreList.get(centreSpinner.getSelectedIndex());
        Log.v("feed", "centre=" + centre);
        String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
        final DatabaseReference each;
        feedbackRef.child(timeStamp).push();
        each = feedbackRef.child(timeStamp);

        each.child("type").push();
        if(fileType==1)
            each.child("type").setValue("audio");
        else
            each.child("type").setValue("text");
        each.child("course").push();
        each.child("course").setValue(course);
        each.child("centre").push();
        each.child("centre").setValue(centre);
        each.child("name").push();
        each.child("name").setValue(nameEdit.getText().toString());
        each.child("phone").push();
        each.child("phone").setValue(phoneEdit.getText().toString());
        each.child("msg").push();
        each.child("review").push();
        each.child("review").setValue(0);
        if(fileType==0) {
            each.child("msg").setValue(messageEdit.getText().toString());
            String msg=nameEdit.getText().toString()+"\n"+phoneEdit.getText().toString()+"\n"+messageEdit.getText().toString()+"\n"+course+"\n"
                    +centre;
            sendMail(msg,nameEdit.getText().toString());
        }


        else {
            Uri file=Uri.fromFile(new File(filePath));
            StorageReference subref=mainRef.child(timeStamp);


            UploadTask task = subref.putFile(file);

            task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    each.child("msg").setValue(taskSnapshot.getDownloadUrl().toString());
                    Log.v("store", taskSnapshot.getDownloadUrl().toString());

                    String msg=nameEdit.getText().toString()+"\n"+phoneEdit.getText().toString()+"\n"+messageEdit.getText().toString()+"\n"+course+"\n"
                            +centre;


                    sendMail(msg,nameEdit.getText().toString());
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Snacky.builder()
                            .setActivty(feedback.this)
                            .setText("Could not upload. Please check your internet connectivity.")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .error()
                            .show();
                    Log.v("store", e.toString() + "\n" + e.getMessage());
                    mDialog.dismiss();
                }
            });
        }

    }

    void sendMail(String msg,String name)
    {


        BackgroundMail.newBuilder(feedback.this)
                .withUsername("brainstudios4pl@gmail.com")
                .withPassword("aokijikuzan")
                .withSenderName(name)
                .withMailTo("brainstudios4pl@gmail.com")
                .withProcessVisibility(false)

                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Feedback received")

                .withBody(msg)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        mDialog.dismiss();
                        Snacky.builder()
                                .setActivty(feedback.this)
                                .setText("Thank you for your feedback")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .success()
                                .show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            public void run() {
                                // yourMethod();
                                finish();

                            }
                        }, 2000);
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }


    void validate()
    {
        magicForm=new MagicForm(ValidationMode.ON_VALIDATE)
                .addField(new FormField(nameEdit)
                        .addValidation(new ValidationNotEmpty().setMessage("Name required"))
                )
                .addField(new FormField(phoneEdit)
                        .addValidation(new ValidationNotEmpty().setMessage("Phone number required"))
                        .addValidation(new ValidationRegex(Patterns.PHONE).setMessage("Phone number required"))
                        .addValidation(new ValidationMinLength(10).setMessage("Please enter valid phone number"))
                        .addValidation(new ValidationMaxLength(10).setMessage("Please enter valid phone number")))
                .setListener(new ValidatorCallbacks() {
                    @Override
                    public void onSuccess() {
                        valid=true;
                    }

                    @Override
                    public void onFailed(List<FormError> errors) {

                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    void startRecording()
    {

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Brainstudio");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
                return;

        }
        filePath = mediaStorageDir.getPath()+File.separator+"123.wav";
        int color = getResources().getColor(R.color.colorPrimaryDark);
        AndroidAudioRecorder.with(feedback.this)
                // Required
                .setFilePath(filePath)
                .setColor(color)
                .setRequestCode(REQUEST_RECORD_AUDIO)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_8000)
                .setAutoStart(false)

                .setKeepDisplayOn(true)

                // Start recording
                .record();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {
                Snacky.builder()
                        .setActivty(feedback.this)
                        .setText("Sorry something went wrong.")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .warning()
                        .show();


                dialogBuilder.withButton1Text("Type")                                      //def gone
                        .withButton2Text("Voice")                                  //def gone
                        .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                        //   .setCustomView(R.layout.custom_feedback_dialog,itemView.getContext())       // .setCustomView(View or ResId,context)

                        .show();
            }
        }
    }



}
