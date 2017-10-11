package com.example.reaganharper.hiittrainer02;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class IntervalSetupActivity extends AppCompatActivity{
    public NumberPicker mMinutes;
    public NumberPicker mSeconds;
    public EditText nName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interval_setup);

        nName = (EditText) findViewById(R.id.name);

        mMinutes = (NumberPicker) findViewById(R.id.minutes);

        mSeconds = (NumberPicker) findViewById(R.id.seconds);

        Button addInterval = (Button) findViewById(R.id.add);
        Button cancel = (Button) findViewById(R.id.cancel);

        addInterval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmInterval();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });
    }

    public void confirmInterval(){
        if(nName.getText().toString().length() == 0) {
            Toast.makeText(this, "Enter a name", Toast.LENGTH_SHORT).show();
        }else if(mMinutes.getValue() == 0 && mSeconds.getValue() == 0){
            Toast.makeText(this, "Choose a duration", Toast.LENGTH_SHORT).show();
        }else{
            Interval interval = new Interval(getName(), getInterval());

            Intent returnIntent = new Intent();
            // returnIntent.putExtra("Interval", interval);
            returnIntent.putExtra("Interval", (Parcelable) interval);
            setResult(RESULT_OK,returnIntent);
            finish();
        }
    }

    public long getInterval(){
        long selectedMinutes = mMinutes.getValue() * 60000;
        long selectedSeconds = mSeconds.getValue() * 1000;
        return selectedMinutes + selectedSeconds;
    }

    public String getName(){
        String name = nName.getText().toString();
        return name;
    }

}
