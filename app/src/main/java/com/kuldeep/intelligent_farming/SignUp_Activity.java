package com.kuldeep.intelligent_farming;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kuldeep.intelligent_farming.Pojo_classes.farmerPojo;
import com.kuldeep.intelligent_farming.Project_classes.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp_Activity extends AppCompatActivity {

    private ImageView logoimage;
    private EditText username;
    private EditText mobilenummber;
    private EditText emailid;
    private EditText password;

    private EditText confirmpassword;
    private RadioGroup radioSexGroup;
    private RadioButton radiomale;
    private RadioButton radiofemale;

    private Button signup;
    private ProgressBar progressBar;
    private farmerPojo fp;


    private String fname, mob, email_id, pass, gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_);
        init();
    }

    private void setvalue() {
        fname = username.getText().toString();
        mob = mobilenummber.getText().toString();
        email_id = emailid.getText().toString();
        pass = password.getText().toString();
        gender = ((RadioButton) findViewById(radioSexGroup.getCheckedRadioButtonId())).getText().toString();

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void init() {

        logoimage = (ImageView) findViewById(R.id.logo_image);
        username = (EditText) findViewById(R.id.user_name);
        mobilenummber = (EditText) findViewById(R.id.mobile_no);
        emailid = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.login_password);
        confirmpassword = (EditText) findViewById(R.id.confirm_password);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radiomale = (RadioButton) findViewById(R.id.radioMale);
        radiofemale = (RadioButton) findViewById(R.id.radioFemale);

        signup = (Button) findViewById(R.id.signipbtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        ((RadioButton) findViewById(radioSexGroup.getCheckedRadioButtonId())).getText();

        logoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator flip = ObjectAnimator.ofFloat(v, "rotationX", 0f, 360f);
                flip.setDuration(3000);
                flip.start();
            }
        });

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
                /*auth.createUserWithEmailAndPassword(email, pass)
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
                        });*/

                fp=new farmerPojo(username.getText().toString(),mobilenummber.getText().toString(),
                        emailid.getText().toString(),password.getText().toString(),
                        ((RadioButton) findViewById(radioSexGroup.getCheckedRadioButtonId())).getText().toString());

                Log.d("name",fp.toString());

               userSignup(fp.getFname(),fp.getMobile(),fp.getEmail(),fp.getPassword(),fp.getGender());

            }
        });

    }

    private void userSignup(final String name, final String mobile, final String email, final String password, final String gender) {

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.userSignuup, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("DEBUG",response);
                    progressBar.setVisibility(View.INVISIBLE);
                    parseSignupResponseMessage(response);
                   // Toast.makeText(getApplicationContext(), "Successfully Registered" , Toast.LENGTH_SHORT).show();
                  //  startActivity(new Intent(getApplicationContext(),Login_Activity.class));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
        }
    }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name", name);
                map.put("mobile", mobile);
                map.put("email", email);
                map.put("password", password);
                map.put("gender", gender);
                return map;
            }
        };
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        10000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
        );
    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
}

    private void parseSignupResponseMessage(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String error = (String) jsonObject.get("error");
            if (error.equals("false")) {
                Toast.makeText(getApplicationContext(), "" +jsonObject.get("message"), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Login_Activity.class));
            } else {
                Toast.makeText(getApplicationContext(), "" +jsonObject.get("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUp_Activity.this, Login_Activity.class));
        finish();

    }
}
