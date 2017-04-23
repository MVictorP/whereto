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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class MeetingStatus extends AppCompatActivity {

    Button btnLogout, btnSubmitInv;
    RequestQueue requestQueue;
    //String getDeptUrl = "http://192.168.2.6/androidphp/getDepartments.php";
    //String getRoomUrl = "http://192.168.2.6/androidphp/getRooms.php";
    //String getEmpUrl = "http://192.168.2.6/androidphp/getEmployees.php";
    //String getRespUrl = "http://192.168.2.6/androidphp/getResponseTypes.php";
    //String getMeetUrl = "http://192.168.2.6/androidphp/getMeetings.php";
    //String updateAttendUrl = "http://192.168.2.6/androidphp/updateAttendance.php";
    String getDeptUrl = "http://thewhereto.com/getDepartments.php";
    String getRoomUrl = "http://thewhereto.com/getRooms.php";
    String getEmpUrl = "http://thewhereto.com/getEmployees.php";
    String getRespUrl = "http://thewhereto.com/getResponseTypes.php";
    String getMeetUrl = "http://thewhereto.com/getMeetings.php";
    String updateAttendUrl = "http://thewhereto.com/updateAttendance.php";
    SharedPreferences sharedSelMeetID, sharedUserEmail, sharedFullName, sharedUserID;
    String strUserEmail, strFullName, strShared_meetId, strJsonRoom, strJsonDept, strJsonMeet, strJsonOrg,
            strMeetingName, strMeetingDesc, strMeetingDateTime, strMeetingDuration,
            strMeetingOrganizerID, strMeetingOrgEmail, strMeetingRoomID, strMeetingDeptID,
            strMeetingDeptName, strMeetingRoomNum, strMeetingOrgName, strDate, strTime, strUserID, strDatetime;
    TextView tvUserFullName, tvUserEmail, tvMeetingName,
            tvMeetingDesc, tvMeetingDate, tvMeetingTime, tvMeetingDuration,
            tvMeetingDept, tvMeetingRoom, tvMeetingOrganizer;
    Spinner responseSpinner;
    StringTokenizer stringTokenizer;
    Date dateToday;
    Calendar calendarCalendar;
    int intResponseID;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetingstatus_activity);

        btnLogout = (Button) findViewById(R.id.submitlogout);
        btnSubmitInv = (Button) findViewById(R.id.btnSubmit);

        sharedSelMeetID = PreferenceManager.getDefaultSharedPreferences(this);
        sharedUserEmail = PreferenceManager.getDefaultSharedPreferences(this);
        sharedFullName = PreferenceManager.getDefaultSharedPreferences(this);
        sharedUserID = PreferenceManager.getDefaultSharedPreferences(this);

        strUserEmail = sharedUserEmail.getString("shareduseremail", "");
        strFullName = sharedFullName.getString("sharedfullname", "");
        strShared_meetId = sharedSelMeetID.getString("sharedselmeetid", "");
        strUserID = sharedUserID.getString("shareduserid", "");

        tvUserFullName = (TextView) findViewById(R.id.txtFullNameUser);
        tvUserFullName.append(strFullName);

        tvUserEmail = (TextView) findViewById(R.id.txtEmailUser);
        tvUserEmail.append(strUserEmail);

        tvMeetingName = (TextView) findViewById(R.id.txtNameMeeting);
        tvMeetingDesc = (TextView) findViewById(R.id.txtDescMeeting);
        tvMeetingDate = (TextView) findViewById(R.id.txtDateMeeting);
        tvMeetingTime = (TextView) findViewById(R.id.txtTimeMeeting);
        tvMeetingDuration = (TextView) findViewById(R.id.txtDurationMeeting);
        tvMeetingDept = (TextView) findViewById(R.id.txtDeptMeeting);
        tvMeetingRoom = (TextView) findViewById(R.id.txtRoomMeeting);
        tvMeetingOrganizer = (TextView) findViewById(R.id.txtOrganizerMeeting);

        final ArrayList<String> respNames = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest jsonResponseObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getRespUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonRespArray = response.getJSONArray("responses");

                    for (int i = 0; i < jsonRespArray.length(); i++) {

                        JSONObject jsonRespObject = jsonRespArray.getJSONObject(i);
                        respNames.add(jsonRespObject.optString("Response_Name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                responseSpinner = (Spinner) findViewById(R.id.respSpinner);
                responseSpinner.setAdapter(new ArrayAdapter<>(MeetingStatus.this, android.R.layout.simple_spinner_dropdown_item, respNames));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonResponseObjectRequest);








        JsonObjectRequest findMeetObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getMeetUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonMeetArray = response.getJSONArray("meetings");
                    for (int i = 0; i < jsonMeetArray.length(); i++) {

                        JSONObject jsonMeetObject = jsonMeetArray.getJSONObject(i);
                        strJsonMeet = jsonMeetObject.optString("Meeting_ID");
                        if (strShared_meetId.equals(strJsonMeet)) {

                            strMeetingName = (jsonMeetObject.optString("Meeting_Name"));
                            strMeetingDesc = (jsonMeetObject.optString("Meeting_Description"));
                            strMeetingDateTime = (jsonMeetObject.optString("Meeting_DateTime"));
                            strMeetingDuration = (jsonMeetObject.optString("Meeting_Duration"));
                            strMeetingDeptID = (jsonMeetObject.optString("Meeting_Dept_ID"));
                            strMeetingRoomID = (jsonMeetObject.optString("Meeting_Room_ID"));
                            strMeetingOrganizerID = (jsonMeetObject.optString("Organizer_ID"));


                            stringTokenizer = new StringTokenizer(strMeetingDateTime);

                            strDate = stringTokenizer.nextToken();
                            strTime = stringTokenizer.nextToken();

                            tvMeetingName.append(strMeetingName);
                            tvMeetingDesc.append(strMeetingDesc);
                            tvMeetingDate.append(strDate);
                            tvMeetingTime.append(strTime);
                            tvMeetingDuration.append(strMeetingDuration);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }







                JsonObjectRequest findRoomObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        getRoomUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonRoomArray = response.getJSONArray("rooms");
                            for (int i = 0; i < jsonRoomArray.length(); i++) {

                                JSONObject jsonRoomObject = jsonRoomArray.getJSONObject(i);
                                strJsonRoom = jsonRoomObject.optString("Room_ID");
                                if (strMeetingRoomID.equals(strJsonRoom)) {

                                    strMeetingRoomNum = (jsonRoomObject.optString("Room_Num"));
                                    tvMeetingRoom.append(strMeetingRoomNum);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.append(error.getMessage());
                    }
                });
                requestQueue.add(findRoomObjectRequest);








                JsonObjectRequest findOrgObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        getEmpUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonOrgArray = response.getJSONArray("employee");
                            for (int i = 0; i < jsonOrgArray.length(); i++) {

                                JSONObject jsonOrgObject = jsonOrgArray.getJSONObject(i);
                                strJsonOrg = jsonOrgObject.optString("Employee_ID");
                                if (strMeetingOrganizerID.equals(strJsonOrg)) {

                                    strMeetingOrgName = (jsonOrgObject.optString("UsrFirst_Name") + " " + jsonOrgObject.optString("UsrLast_Name"));
                                    tvMeetingOrganizer.append(strMeetingOrgName);
                                    strMeetingOrgEmail = (jsonOrgObject.optString("email"));
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.append(error.getMessage());
                    }
                });
                requestQueue.add(findOrgObjectRequest);









                JsonObjectRequest findDeptObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        getDeptUrl, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonDeptArray = response.getJSONArray("departments");
                            for (int i = 0; i < jsonDeptArray.length(); i++) {

                                JSONObject jsonDeptObject = jsonDeptArray.getJSONObject(i);
                                strJsonDept = jsonDeptObject.optString("Meeting_Dept_ID");
                                if (strMeetingDeptID.equals(strJsonDept)) {

                                    strMeetingDeptName = (jsonDeptObject.optString("Meeting_Dept_Name"));
                                    tvMeetingDept.append(strMeetingDeptName);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.append(error.getMessage());
                    }

                });
                requestQueue.add(findDeptObjectRequest);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(findMeetObjectRequest);



        btnSubmitInv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                intResponseID = responseSpinner.getSelectedItemPosition() + 1;
                dateToday = new Date();

                calendarCalendar = Calendar.getInstance();
                calendarCalendar.setTime(dateToday);
                int hour = calendarCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendarCalendar.get(Calendar.MINUTE);
                int second = calendarCalendar.get(Calendar.SECOND);
                int year = calendarCalendar.get(Calendar.YEAR);
                int month = calendarCalendar.get(Calendar.MONTH);
                int dayOfMonth = calendarCalendar.get(Calendar.DAY_OF_MONTH);

                strDatetime = (String.valueOf(year) + "-" + String.valueOf(month)
                        + "-" + String.valueOf(dayOfMonth) + " " + String.valueOf(hour)
                        + ":" + String.valueOf(minute) + ":" + String.valueOf(second));



                StringRequest request = new StringRequest(Request.Method.POST, updateAttendUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
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
                    //TODO
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> parameters = new HashMap<>();

                        parameters.put("Employee_ID", strUserID);
                        parameters.put("Meeting_ID", strShared_meetId);
                        parameters.put("Response_DateTime", strDatetime);
                        parameters.put("Response_Type_ID", Integer.toString(intResponseID));

                        return parameters;
                    }
                };
                requestQueue.add(request);


            }
        });



        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });


    }
}



