package brainstudio.s4pl.com.brainstudio;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

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
    @BindView(R.id.enquirySubmit)
    Button submit;


    SimpleArcDialog mDialog;
    MagicForm magicForm;
    boolean valid=false;

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
                        .addValidation(new ValidationMinLength(8).setMessage("please enter valid phone number"))
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
                .withSenderName("Your sender name")
                .withMailTo("tssuhas18@gmail.com")
                .withProcessVisibility(false)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("this is the subject")

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
