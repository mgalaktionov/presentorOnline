package com.example.mikhail.mobilepresentor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText editText;
    private String code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view){
        switch (view.getId()) {
            case R.id.codeSendButton:
                editText = findViewById(R.id.codeEditText);
                code = editText.getText().toString();
                if(code.isEmpty()) {
                    Toast.makeText(this, "you didn't type code", Toast.LENGTH_LONG).show();
                }else if(code.length()!=4){
                    Toast.makeText(this, "code must contain 4 digits", Toast.LENGTH_LONG).show();
                }else {
                    CodeSendTask codeSendTask = new CodeSendTask(code);
                    codeSendTask.execute();
                    Intent mainIntent = new Intent(this, MainActivity.class);
                    mainIntent.putExtra("code", code);
                    startActivity(mainIntent);
                }

        }
    }
}
