package com.example.gloria.myapplication.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.example.gloria.myapplication.R;

public class SignUp4Activity extends AppCompatActivity {
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private RadioButton radioButton7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up4);

        Intent gintent = getIntent();
        final String mail = gintent.getStringExtra("mail");
        final String passwd = gintent.getStringExtra("passwd");
        final String name = gintent.getStringExtra("name");
        final String univers = gintent.getStringExtra("university");
        final String major = gintent.getStringExtra("major");

        radioButton1 = (RadioButton)findViewById(R.id.radioButton2);
        radioButton2 = (RadioButton)findViewById(R.id.radioButton3);
        radioButton3 = (RadioButton)findViewById(R.id.radioButton4);
        radioButton4 = (RadioButton)findViewById(R.id.radioButton5);
        radioButton5 = (RadioButton)findViewById(R.id.radioButton6);
        radioButton6 = (RadioButton)findViewById(R.id.radioButton7);
        radioButton7 = (RadioButton)findViewById(R.id.radioButton8);

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton1.isChecked()){
                    String grade = (String)radioButton1.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton2.isChecked()){
                    String grade = (String)radioButton2.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });

        radioButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton3.isChecked()){
                    String grade = (String)radioButton3.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });

        radioButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton4.isChecked()){
                    String grade = (String)radioButton4.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });

        radioButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton5.isChecked()){
                    String grade = (String)radioButton5.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });

        radioButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton6.isChecked()){
                    String grade = (String)radioButton6.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });

        radioButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(radioButton7.isChecked()){
                    String grade = (String)radioButton7.getText();
                    Intent intent = new Intent(SignUp4Activity.this, SignUp5Activity.class);
                    intent.putExtra("mail",mail);
                    intent.putExtra("passwd", passwd);
                    intent.putExtra("name", name);
                    intent.putExtra("university", univers);
                    intent.putExtra("major", major);
                    intent.putExtra("grade", grade);
                    startActivity(intent);
                }
            }
        });
    }
}

