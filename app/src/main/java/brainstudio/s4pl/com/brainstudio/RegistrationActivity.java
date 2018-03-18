package brainstudio.s4pl.com.brainstudio;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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
        final String[] select_qualification = {
                "Select Qualification", "10th / Below", "12th", "Diploma", "UG",
                "PG", "Phd"};
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
        final ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
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

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            dateDisplay.setText(dayOfMonth+"/"+month+"/"+year);
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
}
