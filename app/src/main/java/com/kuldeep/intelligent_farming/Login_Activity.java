package com.kuldeep.intelligent_farming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity {

    private TextView createaccount;
    private EditText user_email,user_password;
    private CheckBox show_hide_pass_check;
    private Button loginbtn;
    private String email,password;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        init();


    }
    void init()
    {
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login_Activity.this, MainActivity.class));
            finish();
        }

        user_email = (EditText) findViewById(R.id.login_email);
        user_password = (EditText) findViewById(R.id.login_password);
        show_hide_pass_check = (CheckBox) findViewById(R.id.show_hide_password);
        createaccount=(TextView)findViewById(R.id.createAccount);
        loginbtn = (Button)findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login_Activity.this,SignUp_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString();
                final String pass = user_password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (pass.length() < 6) {
                                        user_password.setError("Password too small");
                                    } else {
                                        Toast.makeText(Login_Activity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
}
