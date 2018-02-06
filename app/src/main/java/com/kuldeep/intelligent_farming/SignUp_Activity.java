package com.kuldeep.intelligent_farming;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kuldeep.intelligent_farming.Pojo_classes.farmerPojo;

import static com.kuldeep.intelligent_farming.R.drawable.mobile;

public class SignUp_Activity extends AppCompatActivity {

    private ImageView logoimage;
    private EditText username;
    private EditText mobilenummber;
    private EditText emailid;
    private EditText password;
    private EditText confirmpassword;
    private EditText dob;
    private EditText address;
    private EditText pincode;
    private RadioGroup radiosex;
    private RadioButton radiomale;
    private RadioButton radiofemale;
    private Button signup;
    private ProgressBar progressBar;

    public String userId;

    private String fname,gender,doB,add,pinc,cid,sid,coid,mob,email_id;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);

        init();

    }

    private void setvalue() {
        fname=username.getText().toString();
        gender="";
        doB=dob.getText().toString();
        add=address.getText().toString();
        pinc=pincode.getText().toString();
        cid="1";sid="1";coid="1";
        mob=mobilenummber.getText().toString();
        email_id=emailid.getText().toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void init()
    {
        logoimage=(ImageView)findViewById(R.id.logo_image);
        username=(EditText)findViewById(R.id.user_name);
        mobilenummber=(EditText)findViewById(R.id.mobile_no);
        emailid=(EditText)findViewById(R.id.email_id);
        password=(EditText)findViewById(R.id.login_password);
        confirmpassword=(EditText)findViewById(R.id.confirm_password);
        dob=(EditText)findViewById(R.id.dob);
        address=(EditText)findViewById(R.id.address);
        pincode=(EditText)findViewById(R.id.pin_code);
        radiosex=(RadioGroup)findViewById(R.id.radioSex);
        radiomale=(RadioButton)findViewById(R.id.radioMale);
        radiofemale=(RadioButton)findViewById(R.id.radioFemale);
        signup=(Button)findViewById(R.id.signipbtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        logoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator flip = ObjectAnimator.ofFloat(v, "rotationX", 0f, 360f);
                flip.setDuration(3000);
                flip.start();
            }
        });

        auth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ObjectAnimator flip = ObjectAnimator.ofFloat(v, "rotationX", 0f, 360f);
                flip.setDuration(3000);
                flip.start();

                String email = emailid.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(SignUp_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                setvalue();
                                Toast.makeText(SignUp_Activity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                FirebaseDatabase connection = FirebaseDatabase.getInstance();
                                DatabaseReference farmerRef= connection.getReference("farmer");
                                farmerPojo user = new farmerPojo(fname, gender, doB, add, pinc, cid, sid, coid, mob, email_id);
                                // pushing user to 'users' node using the userId
                                farmerRef.child(auth.getCurrentUser().getUid()).setValue(user);

                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(SignUp_Activity.this, Login_Activity.class));
                                finish();
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUp_Activity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp_Activity.this, Login_Activity.class));
        finish();

    }
}
