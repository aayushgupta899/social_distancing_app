package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeNewUser extends AppCompatActivity {

    private String username;
    private String occupation;
    private String age;
    private String gender;
    private String condition;

    private EditText occupationEditText;
    private EditText ageEditText;
    private EditText genderEditText;
    private EditText conditionEditText;

    private TextView welcomeTextView;

    public static final String AGE = "AGE";
    public static final String OCCUPATION = "OCCUPATION";
    public static final String GENDER = "GENDER";
    public static final String CONDITION = "CONDITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_new_user);
        bindView();
        Intent i = getIntent();
        username = i.getStringExtra(StartingActivity.USERNAME);
        welcomeTextView.setText("Welcome, "+username);
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        welcomeTextView.setText("Welcome, "+username);
    }

    public void bindView()
    {
        occupationEditText = findViewById(R.id.welcome_new_user_occupation_edittext);
        ageEditText = findViewById(R.id.welcome_new_user_age_edittext);
        genderEditText = findViewById(R.id.welcome_new_user_gender_edittext);
        conditionEditText = findViewById(R.id.welcome_new_user_condition_edittext);
        welcomeTextView = findViewById(R.id.welcome_new_greeting_textview);
    }

    public void onNextButtonClick(View view) {

        occupation = occupationEditText.getText().toString();
        age = ageEditText.getText().toString();
        gender = genderEditText.getText().toString();
        condition = conditionEditText.getText().toString();
        if(occupation == null || occupation.equals(""))
        {
            Toast.makeText(this, "Please provide occupation", Toast.LENGTH_SHORT).show();
        }
        else if(age == null || age.equals(""))
        {
            Toast.makeText(this, "Please provide age", Toast.LENGTH_SHORT).show();
        }
        else if(gender == null || gender.equals(""))
        {
            Toast.makeText(this, "Please provide gender", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(StartingActivity.USERNAME, username);
            intent.putExtra(WelcomeNewUser.AGE, age);
            intent.putExtra(WelcomeNewUser.CONDITION, (condition==null || condition.equals("") ? "NA" : condition));
            intent.putExtra(WelcomeNewUser.GENDER, gender);
            intent.putExtra(WelcomeNewUser.OCCUPATION, occupation);
            startActivity(intent);
            finish();
        }
    }


}
