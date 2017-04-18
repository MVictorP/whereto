package com.example.matthew.project15;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewMeeting extends AppCompatActivity {
    TextInputEditText meeting_name, meeting_description, meeting_date, meeting_time, meeting_duration;
    String meeting_department, meeting_room;
    Button create_meeting, log_out;
    RequestQueue requestQueue;
    String insertMeetUrl = "http://192.168.2.6/androidphp/insertMeeting.php";
    String getDeptUrl = "http://192.168.2.6/androidphp/getDepartments.php";
    String getRoomUrl = "http://192.168.2.6/androidphp/getRooms.php";
    Spinner deptSpinner, roomSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmeeting_activity);

        log_out = (Button) findViewById(R.id.btnLogout);
        create_meeting = (Button) findViewById(R.id.btnCreate_Meeting);

        meeting_name = (TextInputEditText) findViewById(R.id.txtMeetingName);
        meeting_description = (TextInputEditText) findViewById(R.id.txtMeetingDesc);
        meeting_date = (TextInputEditText) findViewById(R.id.txtMeetingDate);
        meeting_time = (TextInputEditText) findViewById(R.id.txtMeetingTime);
        meeting_duration = (TextInputEditText) findViewById(R.id.txtMeetingDuration);

        final ArrayList<String> deptNames = new ArrayList<>();
        final ArrayList<String> roomNum = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest jsonDeptObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getDeptUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonDeptArray = response.getJSONArray("departments");

                    for (int i = 0; i < jsonDeptArray.length(); i++) {

                        JSONObject jsonDeptObject = jsonDeptArray.getJSONObject(i);
                        Department dept = new Department();
                        dept.setName(jsonDeptObject.optString("Meeting_Dept_Name"));
                        deptNames.add(jsonDeptObject.optString("Meeting_Dept_Name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                deptSpinner = (Spinner)findViewById(R.id.deptSpinner);
                deptSpinner.setAdapter(new ArrayAdapter<>(NewMeeting.this, android.R.layout.simple_spinner_dropdown_item, deptNames));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonDeptObjectRequest);






        JsonObjectRequest jsonRoomObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getRoomUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonRoomArray = response.getJSONArray("rooms");

                    for (int i = 0; i < jsonRoomArray.length(); i++) {

                        JSONObject jsonRoomObject = jsonRoomArray.getJSONObject(i);
                        Room room = new Room();
                        room.setName(jsonRoomObject.optString("Room_Num"));
                        roomNum.add(jsonRoomObject.optString("Room_Num"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                roomSpinner = (Spinner)findViewById(R.id.roomSpinner);
                roomSpinner.setAdapter(new ArrayAdapter<>(NewMeeting.this, android.R.layout.simple_spinner_dropdown_item, roomNum));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonRoomObjectRequest);








        create_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meeting_department = deptSpinner.getSelectedItem().toString();
                meeting_room = roomSpinner.getSelectedItem().toString();///////////////////////////////////
                StringRequest request = new StringRequest(Request.Method.POST, insertMeetUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();
                        parameters.put("Meeting_Name", meeting_name.getText().toString());
                        parameters.put("Meeting_Description", meeting_description.getText().toString());
                        parameters.put("Meeting_DateTime", meeting_date.getText().toString() + " " + meeting_time.getText().toString());
                        parameters.put("Meeting_Duration", meeting_duration.getText().toString());
                        parameters.put("Meeting_Status_ID", meeting_department);
                        parameters.put("Meeting_Room_ID", meeting_room);


                        return parameters;
                    }
                };
                requestQueue.add(request);
            }
        });
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }


}
