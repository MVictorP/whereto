package com.example.matthew.project15;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText TextInputEmail, TextInputPassword;
    RequestQueue requestQueue;
    //String checkUserUrl = "http://192.168.2.6/androidphp/user_control.php";
    String checkUserUrl = "http://thewhereto.com/user_control.php";
    StringRequest stringRequest;
    Button btnSignIn;
    SharedPreferences sharedUserEmail;
    SharedPreferences.Editor editorUserID;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEmail = (TextInputEditText) findViewById(R.id.loginemail);
        TextInputPassword = (TextInputEditText) findViewById(R.id.password);
        btnSignIn = (Button) findViewById(R.id.email_sign_in_button);
        sharedUserEmail = PreferenceManager.getDefaultSharedPreferences(this);

        requestQueue = Volley.newRequestQueue(this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String checkEmail = TextInputEmail.getText().toString();
                String checkPassword = TextInputPassword.getText().toString();
                if(TextUtils.isEmpty(checkEmail)) {
                    TextInputEmail.setError("Enter your Email");

                }
                if(TextUtils.isEmpty(checkPassword)) {
                    TextInputPassword.setError("Enter your Password");

                }

                stringRequest = new StringRequest(Request.Method.POST, checkUserUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Welcome.class));
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Error: " + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        HashMap<String, String> parameters = new HashMap<>();
                        parameters.put("email", TextInputEmail.getText().toString());
                        parameters.put("password", TextInputPassword.getText().toString());

                        editorUserID = sharedUserEmail.edit();
                        editorUserID.putString("shareduseremail", TextInputEmail.getText().toString());
                        editorUserID.apply();

                        return parameters;
                    }
                };

                requestQueue.add(stringRequest);

            }
        });

    }

}