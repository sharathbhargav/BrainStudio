package brainstudio.s4pl.com.brainstudio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class feedback extends AppCompatActivity {


    @BindView(R.id.feedbackSpinnerCentre)
    MaterialSpinner centreSpinner;
    @BindView(R.id.feedbackSpinnerCourse)
    MaterialSpinner courseSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        courseSpinner.setItems("Rubik's cube","Juggling","Scientific Handwriting","Speed Stacking","Calligraphy","Corporate Training");
        courseSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_LONG).show();
            }
        });


        centreSpinner.setItems("Uttarahalli road","V.V.Puram");
        centreSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toast.makeText(getApplicationContext(),item.toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
}
