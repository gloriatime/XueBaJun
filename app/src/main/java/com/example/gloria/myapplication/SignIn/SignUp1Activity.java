package com.example.gloria.myapplication.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.gloria.myapplication.R;

public class SignUp1Activity extends AppCompatActivity {
    private EditText mail, passwd, name;
    private Button next;
    private RadioButton radioButton;
    private String mail_string, passwd_string, name_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);

        mail = (EditText)findViewById(R.id.mail);
        passwd = (EditText)findViewById(R.id.password);
        name = (EditText)findViewById(R.id.name);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        next = (Button)findViewById(R.id.button3);

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mail_string = mail.getText().toString();
                passwd_string = passwd.getText().toString();
                name_string = name.getText().toString();
                if(mail_string.length()<11){
                    Toast.makeText(SignUp1Activity.this, "请输入正确的手机号/邮箱",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd_string.length()<6){
                    Toast.makeText(SignUp1Activity.this, "请输入正确的密码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(name_string.length()<0){
                    Toast.makeText(SignUp1Activity.this, "请输入正确的昵称",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(radioButton.isChecked()){
                    Intent intent = new Intent(SignUp1Activity.this, SignUp2Activity.class);
                    intent.putExtra("mail",mail_string);
                    intent.putExtra("passwd", passwd_string);
                    intent.putExtra("name", name_string);
                    startActivity(intent);
                }
            }
        });
    }
}


