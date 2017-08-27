package brainstudio.s4pl.com.brainstudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
        ButterKnife.bind(this);
        toolbar.setTitle("Enquiry");


    }
}
