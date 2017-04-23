package com.example.matthew.project15;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Map;

public class InviteEmployees extends AppCompatActivity {

    ListView empListView;
    Button btnInviteEmployees;
    RequestQueue requestQueue;
    //String getEmpUrl = "http://192.168.2.6/androidphp/getEmployees.php";
    //String insertAttendUrl = "http://192.168.2.6/androidphp/insertAttendance.php";
    String getEmpUrl = "http://thewhereto.com/getEmployees.php";
    String insertAttendUrl = "http://thewhereto.com/insertAttendance.php";
    TextView meetTextView;
    SharedPreferences sharedMeetID;
    String strMeetId, strSuccess;
    ArrayList<String> ArrEmpNames;
    int intCntChoice;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_emp_activity);
        ArrEmpNames = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        sharedMeetID = PreferenceManager.getDefaultSharedPreferences(this);
        strMeetId = sharedMeetID.getString("sharedmeetid", "");
        strSuccess = "Success";

        meetTextView = (TextView)findViewById(R.id.empMeetId);
        meetTextView.append(strMeetId);

        empListView = (ListView) findViewById(R.id.list);
        btnInviteEmployees = (Button) findViewById(R.id.btnInviteEmps);


        JsonObjectRequest jsonEmployeeObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getEmpUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonEmpArray = response.getJSONArray("employee");

                    for (int i = 0; i < jsonEmpArray.length(); i++) {

                        JSONObject jsonEmpObject = jsonEmpArray.getJSONObject(i);
                        ArrEmpNames.add(jsonEmpObject.optString("UsrFirst_Name") + " " + jsonEmpObject.optString("UsrLast_Name"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                empListView = (ListView) findViewById(R.id.list);
                ArrayAdapter<String> adapter
                        = new ArrayAdapter<>(InviteEmployees.this,
                        android.R.layout.simple_list_item_multiple_choice,
                        ArrEmpNames);
                empListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                empListView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.append(error.getMessage());
            }

        });
        requestQueue.add(jsonEmployeeObjectRequest);




        btnInviteEmployees.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                long itEmp; //todo: init this with class
                intCntChoice = empListView.getCount();

                if(empListView.getCheckedItemCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Select Employee(s)", Toast.LENGTH_SHORT).show();
                    return;
                }


                SparseBooleanArray sparseBooleanArray = empListView.getCheckedItemPositions();

                for (int i = 0; i < intCntChoice; i++) {
                    if (sparseBooleanArray.get(i)) {

                        itEmp = empListView.getItemIdAtPosition(i + 1); //todo: figure out why...

                        final long finalItEmp = itEmp; //todo: it wants me to reassign itEmp here...
                        StringRequest request = new StringRequest(Request.Method.POST, insertAttendUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.names().get(0).equals("success")) {
                                        Toast.makeText(getApplicationContext(), jsonObject.getString("success"), Toast.LENGTH_LONG).show();
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
                                Map<String, String> parameters = new HashMap<>();
                                parameters.put("Employee_ID", Long.toString(finalItEmp));
                                parameters.put("Meeting_ID", strMeetId);

                                return parameters;
                            }
                        };
                        requestQueue.add(request);
                    }
                }

                //Toast.makeText(InviteEmployees.this,
                        //strSuccess,
                        //Toast.LENGTH_LONG).show();

            }
        });


    }

}