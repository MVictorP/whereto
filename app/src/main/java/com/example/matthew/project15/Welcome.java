package com.example.matthew.project15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Welcome extends AppCompatActivity {
    Button btnCreateMeeting, btnLog_out, btnGotoSelMeet;
    TextView emailTextView, fullNameTextView;
    SharedPreferences sharedUserEmail;
    SharedPreferences sharedSelMeetID;
    SharedPreferences.Editor editorSelMeetID;
    SharedPreferences sharedFullName;
    SharedPreferences.Editor editorFullName;
    SharedPreferences sharedUserID;
    SharedPreferences.Editor editorUserID;
    String strUserEmail;
    String getEmpUrl = "http://192.168.2.6/androidphp/getEmployees.php";
    String getInvUrl = "http://192.168.2.6/androidphp/getInvitations.php";
    String strJsonEmail, strJsonMeet;
    Spinner meetsSpinner;

    String strFullEmpName, strEmpID;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        sharedUserEmail = PreferenceManager.getDefaultSharedPreferences(this);
        strUserEmail = sharedUserEmail.getString("shareduseremail", "");

        sharedSelMeetID = PreferenceManager.getDefaultSharedPreferences(this);
        sharedFullName = PreferenceManager.getDefaultSharedPreferences(this);
        sharedUserID = PreferenceManager.getDefaultSharedPreferences(this);


        emailTextView = (TextView) findViewById(R.id.txtuserEmail);
        emailTextView.append(strUserEmail);

        fullNameTextView = (TextView) findViewById(R.id.txtuserFullname);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        final ArrayList<String> ArrEmpMeetings = new ArrayList<>();
        ArrEmpMeetings.add("Please select a meeting...");






        JsonObjectRequest jsonEmployeeObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getEmpUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonEmpArray = response.getJSONArray("employee");

                    for (int i = 0; i < jsonEmpArray.length(); i++) {

                        JSONObject jsonEmpObject = jsonEmpArray.getJSONObject(i);
                        strJsonEmail = jsonEmpObject.optString("email");
                        if (strUserEmail.equals(strJsonEmail)) {

                            strFullEmpName = (jsonEmpObject.optString("UsrFirst_Name") + " " + jsonEmpObject.optString("UsrLast_Name"));

                            editorFullName = sharedFullName.edit();
                            editorFullName.putString("sharedfullname", strFullEmpName);
                            editorFullName.apply();

                            strEmpID = (jsonEmpObject.optString("Employee_ID"));
                            editorUserID = sharedUserID.edit();
                            editorUserID.putString("shareduserid", strEmpID);
                            editorUserID.apply();
                            fullNameTextView.append(strFullEmpName);

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest GetMeetsObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        getInvUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonMeetsArray = response.getJSONArray("invitations");

                            for (int i = 0; i < jsonMeetsArray.length(); i++) {

                                JSONObject jsonMeetsObject = jsonMeetsArray.getJSONObject(i);
                                strJsonMeet = jsonMeetsObject.optString("Employee_ID");
                                if (strEmpID.equals(strJsonMeet)) {
                                    ArrEmpMeetings.add(jsonMeetsObject.optString("Meeting_ID"));

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        meetsSpinner = (Spinner) findViewById(R.id.empMeetsSpinner);
                        meetsSpinner.setAdapter(new ArrayAdapter<>(Welcome.this, android.R.layout.simple_spinner_dropdown_item, ArrEmpMeetings));

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.append(error.getMessage());
                    }

                });
                requestQueue.add(GetMeetsObjectRequest);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonEmployeeObjectRequest);


        btnGotoSelMeet = (Button) findViewById(R.id.btnGotoSelectedMeeting);
        btnGotoSelMeet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(meetsSpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please choose a meeting" , Toast.LENGTH_SHORT).show();
                    return;
                }
                editorSelMeetID = sharedSelMeetID.edit();
                editorSelMeetID.putString("sharedselmeetid", meetsSpinner.getSelectedItem().toString());
                editorSelMeetID.apply();
                startActivity(new Intent(getApplicationContext(), MeetingStatus.class));
            }
        });


        btnCreateMeeting = (Button) findViewById(R.id.btnCreateMeeting);
        btnCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewMeeting.class));
            }
        });


        btnLog_out = (Button) findViewById(R.id.btnLogout);
        btnLog_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}