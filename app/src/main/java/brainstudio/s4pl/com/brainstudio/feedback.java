package brainstudio.s4pl.com.brainstudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class feedback extends AppCompatActivity {

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
    String course,centre;
    ArrayList<String> centreList=new ArrayList<>();
    ArrayList<String> courseList=new ArrayList<>(Arrays.asList("Rubik's cube","Juggling","Scientific Handwriting","Speed Stacking","Calligraphy","Corporate Training"));
    DatabaseReference parent,feedbackRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Enquiry");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        parent= FirebaseDatabase.getInstance().getReference("centre");
        parent.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d:dataSnapshot.getChildren())
                {
                    centreList.add(d.getKey());
                }
                centreSpinner.setItems(centreList);

                DataSnapshot d=dataSnapshot.child(centreList.get(0));
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


        feedbackRef=FirebaseDatabase.getInstance().getReference("feedback");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    course = courseList.get(courseSpinner.getSelectedIndex());
                    centre = centreList.get(centreSpinner.getSelectedIndex());
                    Log.v("feed", "centre=" + centre);
                    String timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";
                    DatabaseReference each;
                    feedbackRef.child(timeStamp).push();
                    each = feedbackRef.child(timeStamp);

                    each.child("course").push();

                    each.child("course").setValue(course);
                    each.child("centre").push();

                    each.child("centre").setValue(centre);
                    each.child("name").push();
                    each.child("name").setValue(nameEdit.getText().toString());
                    each.child("phone").push();
                    each.child("phone").setValue(phoneEdit.getText().toString());
                    each.child("msg").push();
                    each.child("msg").setValue(messageEdit.getText().toString());
                    each.child("review").push();
                    each.child("review").setValue(0);
                    finish();

            }
        });




    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
