package com.valdez.burnedcaloriescalculator;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.StringReader;
import java.text.NumberFormat;

public class BurnedCaloriesCalculatorActivity extends Activity
        implements TextView.OnEditorActionListener, SeekBar.OnSeekBarChangeListener {

    //define variables
    private TextView weight;
    private EditText weightEntered;
    private TextView milesRan;
    private TextView totalMilesRan;
    private TextView caloriesBurned;
    private TextView totalCaloriesBurned;
    private TextView height;
    private Spinner feet;
    private Spinner inches;
    private TextView bmi;
    private TextView totalBmi;
    private TextView name;
    private EditText nameEntered;
    private SeekBar milesSeekBar;

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables that should be saved
    private String weightString = "";
    private String nameString = "";
    private int milesString = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        // get references to the widgets
        weight = (TextView) findViewById(R.id.weight);
        weightEntered = (EditText) findViewById(R.id.weightEntered);
        milesRan = (TextView) findViewById(R.id.milesRan);
        totalMilesRan = (TextView) findViewById(R.id.totalMilesRan);
        caloriesBurned = (TextView) findViewById(R.id.caloriesBurned);
        totalCaloriesBurned = (TextView) findViewById(R.id.totalCaloriesBurned);
        height = (TextView) findViewById(R.id.height);
        feet = (Spinner) findViewById(R.id.feet);
        inches = (Spinner) findViewById(R.id.inches);
        bmi = (TextView) findViewById(R.id.bmi);
        totalBmi = (TextView) findViewById(R.id.totalBmi);
        name = (TextView) findViewById(R.id.name);
        nameEntered = (EditText) findViewById(R.id.nameEntered);

        // set array adapter for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.split_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        feet.setAdapter(adapter);
        inches.setAdapter(adapter);

        // set the listeners
        weightEntered.setOnEditorActionListener(this);
        nameEntered.setOnEditorActionListener(this);
        milesSeekBar.setOnSeekBarChangeListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);


    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("weight", String.valueOf(weightString));
        editor.putString("name", String.valueOf(nameString));
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get the instance variables
        weightString = savedValues.getString("weightEntered", "");
        milesString = savedValues.getInt("milesRan", 0);

        // set the weight on its widget
        weightEntered.setText(weightString);

        // set the on its widget
        int progress = Math.round(milesString * 10);
        milesSeekBar.setProgress(progress);

        // calculate and display
        calculateAndDisplay();
    }

    public void calculateAndDisplay() {

        // get weight
        String weightEnteredString = weightEntered.getText().toString();
        float weight;
        if (weightEnteredString.equals("")) {
            weight = 0;
        }
        else {
            weight = Float.parseFloat(weightEnteredString);
        }

        //
        int progress = milesSeekBar.getProgress();
        milesSeekBar = (float) progress / 10;

        // get name
        String nameEnteredString = nameEntered.getText().toString();
        String name;
        if (nameEnteredString.equals("")) {
            name = "Enter Name...";
        }
        else {
            name = String.format(nameEnteredString);
        }

        // display the other results with formatting
        //NumberFormat numbers = NumberFormat.getNumberInstance();
        //weight.setText(numbers.format());

        NumberFormat percent = NumberFormat.getPercentInstance();
        milesSeekBar.setText(percent.format(milesSeekBar));

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    //*****************************************************
    // Event handler for the SeekBar
    //*****************************************************
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        milesRan.setText(progress + "%");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        calculateAndDisplay();
    }
}
