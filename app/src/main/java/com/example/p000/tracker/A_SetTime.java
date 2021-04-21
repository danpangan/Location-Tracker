package com.example.p000.tracker;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.common.ConnectionResult;

import static com.example.p000.tracker.C_GlobalHolder.auth;

public class A_SetTime extends AppCompatActivity {
    private TextView tViewUname, tViewIns, tViewLblStartTime, tViewLblEndTime, tViewActualEndTime;
    private ImageView imgViewLocIcon;
    private TextClock tClockActualStartTime;
    private ToggleButton btnStartTime;

    private static int endHourVal = 18;
    private static int endMinVal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a__set_time);
        setEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!SharedPrefManager.getmInstance(this).isLoggedIn()) {

            Intent intent = new Intent(this, A_Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();

//        checkLocationServices();
    }

    private void setEvents(){
        tViewUname = findViewById(R.id.txtView_username);
        tViewIns = findViewById(R.id.txtView_instruction);
        tViewLblStartTime = findViewById(R.id.txtView_startTime);
        tViewLblEndTime = findViewById(R.id.txtView_endTime);
        tViewActualEndTime = findViewById(R.id.txtView_actualEndTime);
        imgViewLocIcon = findViewById(R.id.imgView_locIcon);
        tClockActualStartTime = findViewById(R.id.txtClock_actualStartTime);
        btnStartTime = findViewById(R.id.tBtn_startTime);

        btnStartTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // permission not yet granted
                    if ((ContextCompat.checkSelfPermission(A_SetTime.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                            || (ContextCompat.checkSelfPermission(A_SetTime.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED))
                    {
                        ActivityCompat.requestPermissions(A_SetTime.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                C_GlobalHolder.PERMISSIONS_REQ_ACCESS_FINE_LOC);

                        ActivityCompat.requestPermissions(A_SetTime.this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                C_GlobalHolder.PERMISSIONS_REQ_ACCESS_COARSE_LOC);
                        btnStartTime.setChecked(false);
                        return;
                    }

                    // check if location services is enabled
                    checkLocationServices();

                    SharedPrefManager.getmInstance(A_SetTime.this).setCurrActivity("tracking");

                    // start tracking activity
                    Intent openTrackingPage = new Intent(A_SetTime.this, A_Tracking.class);
                    startActivity(openTrackingPage);

                } else {

                }
            }
        });

        tViewActualEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(A_SetTime.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTimeString(selectedHour, selectedMinute);
                    }
                }, endHourVal, endMinVal, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case C_GlobalHolder.PERMISSIONS_REQ_ACCESS_COARSE_LOC:
            case C_GlobalHolder.PERMISSIONS_REQ_ACCESS_FINE_LOC:
                Toast.makeText(A_SetTime.this, "Location Services: ON", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /*
     * Check if Google Play Services is available in device
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, C_GlobalHolder.PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {

            }
            return false;
        }
        return true;
    }

    public void checkLocationServices()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        boolean isGpsOn = false;
        boolean isNetworkOn = false;

        // get gps status
        try {
            isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (NullPointerException ex) {

        }

        // get connection status
        Integer connSts = C_Comm.getConnectivityStatusString(this);

        // display location services if gps is off
        if (!isGpsOn) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your Location Services seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }

        // display wireless network settings if no network connection detected
        if (connSts == C_Comm.NETWORK_STATUS_NOT_CONNECTED) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Your Network connection seems to be disabled, do you want to enable it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void setTimeString (int selectedHour, int selectedMin) {
        String timeDisplay = "";

        String strAmPm = " ";
        String strHour = "00";
        String strMinutes = "00";

        endHourVal = selectedHour;
        endMinVal = selectedMin;

        if (selectedHour > 12) {
            strAmPm = "PM";
            selectedHour = selectedHour - 12;
        } else if (selectedHour == 12) {
            strAmPm = "PM";
        } else {
            strAmPm = "AM";
        }

        if (selectedHour < 10){
            strHour = "0" + String.valueOf(selectedHour);
        } else {
            strHour = String.valueOf(selectedHour);
        }

        if (selectedMin < 10) {
            strMinutes = "0" + String.valueOf(selectedMin);
        } else {
            strMinutes = String.valueOf(selectedMin);
        }

        tViewActualEndTime.setText( strHour + ":" + strMinutes + " " + strAmPm);
    }
}
