package brainstudio.s4pl.com.brainstudio;

import android.content.Intent;
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
import android.widget.Toast;

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
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;





import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cafe.adriel.androidaudiorecorder.AndroidAudioRecorder;
import cafe.adriel.androidaudiorecorder.model.AudioChannel;
import cafe.adriel.androidaudiorecorder.model.AudioSampleRate;
import cafe.adriel.androidaudiorecorder.model.AudioSource;
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
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

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



    String course,centre;
    ArrayList<String> centreList=new ArrayList<>();
    ArrayList<String> courseList=new ArrayList<>(Arrays.asList("Rubik's cube","Juggling","Scientific Handwriting","Speed Stacking","Calligraphy","Corporate Training"));
    DatabaseReference parent,feedbackRef;
    MagicForm magicForm;
    boolean valid=false;
    SimpleArcDialog mDialog;
    int REQUEST_RECORD_AUDIO=111;
    String filePath ;
    int  fileType=0;//1 for audio

    NiftyDialogBuilder dialogBuilder;


    StorageReference mainRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDialog = new SimpleArcDialog(this);
        dialogBuilder=NiftyDialogBuilder.getInstance(this);

        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);
        validate();

        parent= FirebaseDatabase.getInstance().getReference("centre");
        mainRef=FirebaseStorage.getInstance().getReferenceFromUrl("gs://brainstudio-a7a21.appspot.com");
        parent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren())
                {
                    centreList.add(d.getKey());
                }
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
                Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_LONG).show();
                centre=item.toString();
            }
        });





        courseSpinner.setItems(courseList);
        courseSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_LONG).show();
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
                .withDialogColor("#FFE74C3C")                               //def  | withDialogColor(int resid)
                .withIcon(R.drawable.aboutus)
                .withDuration(700)                                          //def
                .withEffect(Effectstype.Flipv)                                         //def Effectstype.Slidetop
                .withButton1Text("Type")                                      //def gone
                .withButton2Text("Voice")                                  //def gone
                .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
             //   .setCustomView(R.layout.custom_feedback_dialog,itemView.getContext())       // .setCustomView(View or ResId,context)

                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), "i'm btn1", Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                            fileType=0;
                        messageEdit.setVisibility(View.VISIBLE);
                        messageContainer.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"i'm btn2",Toast.LENGTH_SHORT).show();
                        dialogBuilder.dismiss();
                        submit.setVisibility(View.VISIBLE);
                        fileType=1;
                        startRecording();
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

                    dialogBuilder.show();
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
            sendMail(msg);
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


                     sendMail(msg);
                }
            });
            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.v("store", e.toString() + "\n" + e.getMessage());
                    mDialog.dismiss();
                }
            });
        }

    }

    void sendMail(String msg)
    {


        BackgroundMail.newBuilder(feedback.this)
                .withUsername("brainstudios4pl@gmail.com")
                .withPassword("aokijikuzan")
                .withSenderName("Your sender name")
                .withMailTo("tssuhas18@gmail.com")
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
                            .addValidation(new ValidationMinLength(8).setMessage("Please enter valid phone number"))
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
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();


                dialogBuilder.withButton1Text("Type")                                      //def gone
                        .withButton2Text("Voice")                                  //def gone
                        .isCancelableOnTouchOutside(false)                           //def    | isCancelable(true)
                        //   .setCustomView(R.layout.custom_feedback_dialog,itemView.getContext())       // .setCustomView(View or ResId,context)

                       .show();
            }
        }
    }



}
