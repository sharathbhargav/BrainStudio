package brainstudio.s4pl.com.brainstudio;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.keiferstone.nonet.Configuration;
import com.keiferstone.nonet.ConnectionStatus;
import com.keiferstone.nonet.Monitor;
import com.keiferstone.nonet.NoNet;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.ppicas.customtypeface.CustomTypeface;
import cat.ppicas.customtypeface.CustomTypefaceFactory;
import de.mateware.snacky.Snacky;
import io.fabric.sdk.android.Fabric;
import julianfalcionelli.magicform.MagicForm;
import julianfalcionelli.magicform.base.FormError;
import julianfalcionelli.magicform.base.FormField;
import julianfalcionelli.magicform.base.ValidationMode;
import julianfalcionelli.magicform.base.ValidatorCallbacks;
import julianfalcionelli.magicform.validation.ValidationMaxLength;
import julianfalcionelli.magicform.validation.ValidationMinLength;
import julianfalcionelli.magicform.validation.ValidationNotEmpty;
import julianfalcionelli.magicform.validation.ValidationRegex;
import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@Keep
@KeepClassMembers
public class Enquiry extends AppCompatActivity {

    @BindView(R.id.enquiry_toolbar)
    Toolbar toolbar;
    @BindView(R.id.enquiryLocationEdit)
    EditText location;
    @BindView(R.id.enquiryMailEdit)
    EditText mail;
    @BindView(R.id.enquiryNameEdit)
    EditText name;
    @BindView(R.id.enquiryPhoneEdit)
    EditText phone;
    @BindView(R.id.enquiryMessageEdit)
    EditText message;
    @BindView(R.id.enquiryMessageWrapper)
    TextInputLayout messageWrapper;
    @BindView(R.id.enquirySubmit)
    Button submit;

    @BindView(R.id.enquirySpinner)
    MaterialSpinner spinner;
    ArrayList<String> courseList=new ArrayList<>(Arrays.asList("How to solve different puzzles in life?(Rubik's cube)",
                    "Are you curious to learn juggling 3 balls cascade?(Juggling)",
                    "Do you want to improve your character by handwriting?(Scientific Handwriting)",
                    "How to improve your speed and alertness?(Speed Stacking)",
                    "How to discover your creativity?(Calligraphy)",
                    "Can I excel in the world of corporate?(Corporate training)",
                    "Can I discover my true inner personality(Handwriting Analysis)",
                    "Type what you are curious about"));


    SimpleArcDialog mDialog;
    MagicForm magicForm;
    boolean valid=false;

    Monitor monitor;
    boolean netLost=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getLayoutInflater().setFactory(new CustomTypefaceFactory(
                this, CustomTypeface.getInstance()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDialog = new SimpleArcDialog(this);




        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);
        mDialog.setCancelable(false);

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
        monitor= NoNet.check(this).start();



        spinner.setItems(courseList);
        spinner.setSelectedIndex(0);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position==7) {
                    message.setText("");
                    message.setVisibility(View.VISIBLE);
                    messageWrapper.setVisibility(View.VISIBLE);
                }
                else {
                    message.setVisibility(View.GONE);
                    messageWrapper.setVisibility(View.GONE);
                    message.setText(item.toString());
                }
            }
        });

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("enq","on click spinner");
                try  {
                    InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                } catch (Exception e) {

                }
            }
        });
        validate();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid=false;

                magicForm.validate();
                if(valid)
                {
                    mDialog.show();
                    sendMail();

                }
            }
        });

    }


    void validate()
    {
        magicForm = new MagicForm(ValidationMode.ON_VALIDATE)
                .addField(new FormField(name)
                        .addValidation(new ValidationNotEmpty().setMessage("Name required")))
                .addField(new FormField(phone)
                        .addValidation(new ValidationNotEmpty().setMessage("Phone number required"))
                        .addValidation(new ValidationRegex(Patterns.PHONE).setMessage("Phone number required"))
                        .addValidation(new ValidationMinLength(10).setMessage("please enter valid phone number"))
                        .addValidation(new ValidationMaxLength(10).setMessage("Please enter valid phone number")))
                .addField(new FormField(mail)
                        .addValidation(new ValidationRegex(Patterns.EMAIL_ADDRESS).setMessage("Please enter valid email id")))
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

    void sendMail()
    {

        String msg=name.getText().toString()+"\n"+phone.getText().toString()+"\n"+mail.getText().toString()+"\n"+location.getText().toString()+"\n"+message.getText().toString()
                    +"\n";
        BackgroundMail.newBuilder(Enquiry.this)
                .withUsername("brainstudios4pl@gmail.com")
                .withPassword("aokijikuzan")
                .withSenderName(name.getText().toString())
                .withMailTo("brainstudios4pl@gmail.com")
                .withProcessVisibility(false)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Enquiry")

                .withBody(msg)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        mDialog.dismiss();
                        Snacky.builder()
                                .setActivty(Enquiry.this)
                                .setText("Your Enquiry has been sent")
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


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
