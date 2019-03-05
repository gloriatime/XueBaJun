package com.example.gloria.myapplication.sign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.gloria.myapplication.R;

public class SignInPage1Activity extends AppCompatActivity {
    private EditText mail, passwd, name;
    private Button next;
    private RadioButton radioButton;
    private String mail_string, passwd_string, name_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page1);

    }
}
