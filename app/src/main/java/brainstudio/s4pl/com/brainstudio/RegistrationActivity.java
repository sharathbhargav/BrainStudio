package brainstudio.s4pl.com.brainstudio;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
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
public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.registrationNameEdit)
    EditText name;
    @BindView(R.id.registrationStandardEdit)
    EditText standard;
    @BindView(R.id.registrationSchoolEdit)
    EditText school;
    @BindView(R.id.registrationAccept)
    Button accept;
    @BindView(R.id.registrationEmailEdit)
    EditText email;
    @BindView(R.id.registrationFatherNameEdit)
    EditText fatherName;
    @BindView(R.id.registrationFatherPhoneEdit)
    EditText fatherPhone;
    @BindView(R.id.registrationFatherProfessionEdit)
    EditText fatherProfession;
    @BindView(R.id.registrationMotherNameEdit)
    EditText motherName;
    @BindView(R.id.registrationMotherPhoneEdit)
    EditText motherPhone;
    @BindView(R.id.registrationMotherProfessionEdit)
    EditText motherProfession;
    @BindView(R.id.registrationResedentialPhoneEdit)
    EditText resedentialPhone;
    @BindView(R.id.registrationGender)
    MaterialSpinner genderSpinner;
    @BindView(R.id.registrationDateSelectButton)
    Button calenderButton;
    @BindView(R.id.registrationDateDisplay)
    TextView dateDisplay;
    @BindView(R.id.registrationSpinnerCentre)
    MaterialSpinner centreSpinner;
    @BindView(R.id.registrationSkillsSpinner)
    Spinner skills;
    @BindView(R.id.registration_toolbar)
    Toolbar toolbar;

    MagicForm magicForm;
    boolean valid=false;
    SimpleArcDialog mDialog;


    DatabaseReference parent,registration,mainRef;
    ArrayList<String> centreList;
    ArrayList<StateVO> listVOs;
    ArrayList<String> courseList=new ArrayList<>(Arrays.asList("Choose Skills","Rubik's cube","Juggling","Scientific Handwriting","Speed Stacking","Calligraphy","Corporate Training","Handwriting Analysis"));
    boolean netLost=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Registration");


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

        mDialog = new SimpleArcDialog(this);
        ArcConfiguration configuration = new ArcConfiguration(getApplicationContext());
        configuration.setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        configuration.setText("Please wait..");
        mDialog.setConfiguration(configuration);
        mDialog.setCancelable(false);
        validate();
        mDialog.show();

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,R.style.datepicker,RegistrationActivity.this, year, month, day);
        Calendar max= Calendar.getInstance();
        max.set(year-4,0,1);
        Calendar min = Calendar.getInstance();
        min.set(year-75,0,1);
        datePickerDialog.getDatePicker().setMaxDate(max.getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(min.getTimeInMillis());

        centreList=new ArrayList<>();

        parent= FirebaseDatabase.getInstance().getReference("centre");
        mainRef= FirebaseDatabase.getInstance().getReference("registration");
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        genderSpinner.setItems(new ArrayList<String>(Arrays.asList("Male","Female")));
        centreSpinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
            }
        });


        centreSpinner.setSelectedIndex(0);



        listVOs = new ArrayList<>();

        for (int i = 0; i < courseList.size(); i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(courseList.get(i));
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(RegistrationActivity.this, 0,
                listVOs);

        skills.setAdapter(myAdapter);
        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });




        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid=false;
                magicForm.validate();
                if(valid==true)
                {
                    mDialog.show();
                    pushData();

                }
                else {
                    Snacky.builder()
                            .setActivty(RegistrationActivity.this)
                            .setText("Invalid entry.Please check again.")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning()
                            .show();
                }
            }
        });


    }


    void pushData()
    {
        String skills="";

        for(int i =0;i<listVOs.size();i++)
        {
            if(listVOs.get(i).isSelected())
                skills = skills +(listVOs.get(i).getTitle())+",";
        }
        String centre = centreList.get(centreSpinner.getSelectedIndex());
        String gender = genderSpinner.getSelectedIndex() ==0 ? "Male":"Female";
        String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "R" + (int)(Math.random()*1000);
        final DatabaseReference each;
        mainRef.child(timeStamp).push();
        each= mainRef.child(timeStamp);

        each.child("name").push();
        each.child("name").setValue(name.getText().toString());

        each.child("school").push();
        each.child("school").setValue(school.getText().toString());
        each.child("gender").push();
        each.child("gender").setValue(gender);

        each.child("class").push();
        each.child("class").setValue(standard.getText().toString());

        each.child("birthday").push();
        each.child("birthday").setValue(dateDisplay.getText().toString());

        each.child("fatherName").push();
        each.child("fatherName").setValue(fatherName.getText().toString());

        each.child("fatherProfession").push();
        each.child("fatherProfession").setValue(fatherProfession.getText().toString());

        each.child("fatherPhone").push();
        each.child("fatherPhone").setValue(fatherPhone.getText().toString());

        each.child("motherName").push();
        each.child("motherName").setValue(motherName.getText().toString());

        each.child("motherProfession").push();
        each.child("motherProfession").setValue(motherProfession.getText().toString());

        each.child("motherPhone").push();
        each.child("motherPhone").setValue(motherPhone.getText().toString());

        if(!TextUtils.isEmpty(resedentialPhone.getText().toString())) {
            each.child("residentialPhone").push();
            each.child("residentialPhone").setValue(resedentialPhone.getText().toString());
        }
        if(!TextUtils.isEmpty(email.getText().toString())) {
            each.child("email").push();
            each.child("email").setValue(email.getText().toString());
        }


        each.child("center").push();
        each.child("center").setValue(centre);

        each.child("skills").push();
        each.child("skills").setValue(skills);


        mDialog.dismiss();


        Snacky.builder()
                .setActivty(RegistrationActivity.this)
                .setText("Your registration was successful.")
                .setDuration(Snacky.LENGTH_SHORT)
                .success()
                .show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            public void run() {
                // yourMethod();
                finish();

            }
        }, 2100);









    }











    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateDisplay.setText(dayOfMonth+"/"+(month+1)+"/"+year);
    }


    public class StateVO {
        private String title;
        private boolean selected;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }


    public class MyAdapter extends ArrayAdapter<StateVO> {
        private Context mContext;
        private ArrayList<StateVO> listState;
        private MyAdapter myAdapter;
        private boolean isFromView = false;

        public MyAdapter(Context context, int resource, List<StateVO> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.listState = (ArrayList<StateVO>) objects;
            this.myAdapter = this;
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(final int position, View convertView,
                                  ViewGroup parent) {

            final ViewHolder holder;
            if (convertView == null) {
                LayoutInflater layoutInflator = LayoutInflater.from(mContext);
                convertView = layoutInflator.inflate(R.layout.registration_skills_spinner_layout, null);
                holder = new ViewHolder();
                holder.mTextView = (TextView) convertView
                        .findViewById(R.id.text);
                holder.mCheckBox = (CheckBox) convertView
                        .findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextView.setText(listState.get(position).getTitle());

            // To check weather checked event fire from getview() or user input
            //isFromView = true;
            //holder.mCheckBox.setChecked(listState.get(position).isSelected());
            //isFromView = false;

            if ((position == 0)) {
                holder.mCheckBox.setVisibility(View.INVISIBLE);
            } else {
                holder.mCheckBox.setVisibility(View.VISIBLE);
            }
            holder.mCheckBox.setTag(position);
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();
                    listState.get(getPosition).setSelected(isChecked);


                }
            });
            return convertView;
        }

        private class ViewHolder {
            private TextView mTextView;
            private CheckBox mCheckBox;
        }
    }


    void validate()
    {
        magicForm=new MagicForm(ValidationMode.ON_VALIDATE)
                .addField(new FormField(name)
                        .addValidation(new ValidationNotEmpty().setMessage("Name required"))
                )
                .addField(new FormField(fatherName)
                        .addValidation(new ValidationNotEmpty().setMessage("Father's name required"))
                )
                .addField(new FormField(fatherProfession)
                        .addValidation(new ValidationNotEmpty().setMessage("Father's profession required"))
                )
                .addField(new FormField(motherName)
                        .addValidation(new ValidationNotEmpty().setMessage("Mother's name required"))
                )
                .addField(new FormField(motherProfession)
                        .addValidation(new ValidationNotEmpty().setMessage("Mother's profession required"))
                )
                .addField(new FormField(standard)
                        .addValidation(new ValidationNotEmpty().setMessage("Class studying required"))
                )
                .addField(new FormField(school)
                        .addValidation(new ValidationNotEmpty().setMessage("School name required"))
                )

                .addField(new FormField(dateDisplay)
                        .addValidation(new ValidationNotEmpty().setMessage("Date of birth required"))
                )
                .addField(new FormField(fatherPhone)
                        .addValidation(new ValidationNotEmpty().setMessage("Father's phone number required"))
                        .addValidation(new ValidationRegex(Patterns.PHONE).setMessage("Phone number required"))
                        .addValidation(new ValidationMinLength(10).setMessage("Please enter valid phone number"))
                        .addValidation(new ValidationMaxLength(10).setMessage("Please enter valid phone number")))
                .addField(new FormField(motherPhone)
                        .addValidation(new ValidationNotEmpty().setMessage("Mother's phone number required"))
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
}
