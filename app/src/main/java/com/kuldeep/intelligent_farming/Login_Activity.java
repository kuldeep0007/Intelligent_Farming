package com.kuldeep.intelligent_farming;

import android.app.Activity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kuldeep.intelligent_farming.Project_classes.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login_Activity extends AppCompatActivity {

    private TextView createaccount;
    private EditText user_email,user_password;
    private CheckBox show_hide_pass_check;
    private Button loginbtn;
    private String email,password;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        init();


    }
    void init()
    {
        /*auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login_Activity.this, MainActivity.class));
            finish();
        }*/

        user_email = (EditText) findViewById(R.id.login_email);
        user_password = (EditText) findViewById(R.id.login_password);
        show_hide_pass_check = (CheckBox) findViewById(R.id.show_hide_password);
        createaccount=(TextView)findViewById(R.id.createAccount);
        loginbtn = (Button)findViewById(R.id.loginBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        /*auth = FirebaseAuth.getInstance();*/
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

                userLogin(user_email.getText().toString(),user_password.getText().toString());

                /*auth.signInWithEmailAndPassword(email, pass)
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
                        });*/
            }
        });
    }

    private void userLogin(final String email, final String password) {

        StringRequest request = new StringRequest(Request.Method.POST, URLHelper.userProfile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG",response);
                        progressBar.setVisibility(View.INVISIBLE);
                        parseResponseString(response);
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
                map.put("email", email);
                map.put("password", password);
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

    private void parseResponseString(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String error = (String) jsonObject.get("error");
            JSONObject farmer = (JSONObject) jsonObject.get("farmer");
            Log.d("DEBUG",jsonArray.toString()+"\n"+jsonObject.toString()+"\n"+farmer.toString()+"\n"+error);
            if (error.equals("false")) {
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("farmingSharedPreference", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                editor.putString("name", (String) farmer.get("farmer_name"));
                editor.putString("mobile", (String) farmer.get("farmer_mobile"));
                editor.putString("email", (String) farmer.get("farmer_email"));
                editor.putString("password",user_password.getText().toString());
                editor.putString("gender", (String) farmer.get("farmer_gender"));
                editor.apply();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "" +error, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
