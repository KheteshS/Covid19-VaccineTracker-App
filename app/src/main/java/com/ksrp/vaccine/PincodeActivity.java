package com.ksrp.vaccine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ksrp.vaccine.api.CenterData;
import com.ksrp.vaccine.api.CountryData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PincodeActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText pinCodeEdit;
    private RecyclerView centerRV;
    private ProgressBar loadingPB;
    private List<CenterData> centerList;
    private CenterAdapter centerRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincode);

        searchButton = findViewById(R.id.idBtnSearch);
        pinCodeEdit = findViewById(R.id.idEditPinCode);
        centerRV = findViewById(R.id.idRVCenters);
        loadingPB = findViewById(R.id.idPBLoading);
        centerList = new ArrayList<>();

        centerRV.setLayoutManager(new LinearLayoutManager(this));
        centerRVAdapter = new CenterAdapter(this, centerList);
        centerRV.setAdapter(centerRVAdapter);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinCode = pinCodeEdit.getText().toString();

                if (pinCode.length()!=6){
                    Toast.makeText(PincodeActivity.this, "Please enter a valid pincode", Toast.LENGTH_SHORT).show();
                } else {
                    centerList.clear();
                    Calendar c = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(PincodeActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                            int year, month, day;
                            loadingPB.setVisibility(View.VISIBLE);
                            year = selectedYear;
                            month = selectedMonth;
                            day = selectedDayOfMonth;

                            String dateStr = ""+day+"-"+(month+1)+"-"+year+"";

                            getAppointmentDetails(pinCode, dateStr);
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

                    dpd.show();
                }
            }
        });
    }

    private void getAppointmentDetails(String pinCode, String dateStr) {
        String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode="+ pinCode + "&date=" + dateStr;
        RequestQueue queue = Volley.newRequestQueue(PincodeActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingPB.setVisibility(View.GONE);
                        try {
                            JSONArray centerArray = response.getJSONArray("centers");
                            if (centerArray.length() == 0){
                                Toast.makeText(PincodeActivity.this, "No Vaccination Centers Available", Toast.LENGTH_SHORT).show();
                            }
                            for (int i=0;i<centerArray.length();i++){
                                JSONObject centerObj = centerArray.getJSONObject(i);
                                String centerName = centerObj.getString("name");
                                String centerAddress = centerObj.getString("address");
                                String centerFromTime = centerObj.getString("from");
                                String centerToTime = centerObj.getString("to");
                                String fee_type = centerObj.getString("fee_type");
                                JSONObject sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0);
                                int availableCapacity = sessionObj.getInt("available_capacity");
                                int ageLimit = sessionObj.getInt("min_age_limit");
                                String vaccineName = sessionObj.getString("vaccine");

                                CenterData center = new CenterData(centerName,
                                        centerAddress,
                                        centerFromTime,
                                        centerToTime,
                                        fee_type,
                                        ageLimit,
                                        vaccineName,
                                        availableCapacity
                                );

                                centerList.add(center);
                            }

                            centerRVAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PincodeActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }


    private Toast backToast;
    private long backPressedTime;

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            PincodeActivity.super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to go back", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}