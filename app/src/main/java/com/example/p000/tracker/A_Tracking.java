package com.example.p000.tracker;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;

import java.util.List;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.p000.tracker.C_GlobalHolder.auth;

public class A_Tracking extends AppCompatActivity {

    private TextView tViewUname, tViewCurrentLoc, tViewLblLat, tViewCurrentLat, tViewLblLong, tViewCurrentLong;
    private ImageView imgViewLocIcon;
    private ToggleButton btnStopTime;

    public static boolean isOnline = false;
    private static boolean initUpdateFlg = true;
    public Handler connStsCheckHandler = new Handler();
    public Handler jsonSendHandler = new Handler();
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geocoder;
    private List<Address> street_address;

    public JSONObject location_json = new JSONObject();
    public static MyAppDatabase myAppDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "trackerdb").allowMainThreadQueries().build();
        setContentView(R.layout.activity_a__tracking);
        setEvents();
        startService();
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

    public void  startService() {

        Intent serviceIntent = new Intent(this, TrackerService.class);
        serviceIntent.putExtra("inputExtra", "sampleInputExtra");

        startService(serviceIntent);
    }

    public void stopService() {

        Intent serviceIntent = new Intent(this, TrackerService.class);

        stopService(serviceIntent);
    }

    private void setEvents(){

        tViewUname = findViewById(R.id.txtView_username);
        tViewCurrentLoc = findViewById(R.id.txtView_currentLocation);
        tViewLblLat = findViewById(R.id.txtView_latitude);
        tViewCurrentLat = findViewById(R.id.txtView_currentLatitude);
        tViewLblLong = findViewById(R.id.txtView_longitude);
        tViewCurrentLong = findViewById(R.id.txtView_currentLongitude);
        imgViewLocIcon = findViewById(R.id.imgView_locIcon);
        btnStopTime = findViewById(R.id.imgBtn_stopTime);

        btnStopTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                C_GlobalHolder.end = 1;
                saveLocation();
                if (isOnline) {
                    sendLocation();
                }

                jsonSendHandler.removeCallbacks(sendSaveRunnable);
                stopService();

                SharedPrefManager.getmInstance(A_Tracking.this).setCurrActivity("summary");

                Intent intent = new Intent(A_Tracking.this, A_Summary.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this);

        connStsCheckHandler.post(connectionCheckRunnable);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {

                    C_GlobalHolder.latitude = location.getLatitude();
                    tViewCurrentLat.setText(String.valueOf(C_GlobalHolder.latitude));

                    C_GlobalHolder.longitude = location.getLongitude();
                    tViewCurrentLong.setText(String.valueOf(C_GlobalHolder.longitude));

                    try {
                        street_address = geocoder.getFromLocation(C_GlobalHolder.latitude, C_GlobalHolder.longitude, 1);
                        C_GlobalHolder.street_address = street_address.get(0).getAddressLine(0);
                    } catch (IOException ex) {
                        C_GlobalHolder.street_address = "--";
                    } catch (IndexOutOfBoundsException ex) {
                        C_GlobalHolder.street_address = "--";
                    }
                    tViewCurrentLoc.setText(C_GlobalHolder.street_address);

                    // construct json data
                    /*updateJSONObject();*/

                    // start timer on first callback
                    if (initUpdateFlg) {
                        jsonSendHandler.post(sendSaveRunnable);
                        initUpdateFlg = false;
                    }
                }
            }
        };

        // start location provider client
        startLocationUpdates();
    }

    /*
     * Check wireless connection status every 5 seconds
     */
    private Runnable connectionCheckRunnable = new Runnable() {
        @Override
        public void run() {
            int connectivityStatus = C_Comm.getConnectivityStatusString(A_Tracking.this);
            // end sender timer (?) and switch to native GPS when no wireless network detected
            if (connectivityStatus == C_Comm.NETWORK_STATUS_NOT_CONNECTED) {
                isOnline = false;
            } else {
                if (!isOnline) {
                    isOnline = true;
                }
            }
            connStsCheckHandler.postDelayed(this, C_GlobalHolder.CONNECTION_CHECK_INTERVAL);
        }
    };

    /*
     * Start location updates
     */
    private void startLocationUpdates()
    {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(C_GlobalHolder.UPDATE_INTERVAL);
        locationRequest.setFastestInterval(C_GlobalHolder.FASTEST_INTERVAL);

        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } catch (SecurityException ex) {
            // display permissions error dialog
        }
    }

    /*
     * Send JSON object to server
     */

    private Runnable sendSaveRunnable = new Runnable() {
        @Override
        public void run() {
            Date currentDateTime = Calendar.getInstance().getTime();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            C_GlobalHolder.date_time = fmt.format(currentDateTime);

            if (isOnline) {
                /*location_json.put("is_online", C_GlobalHolder.YES);*/
                C_GlobalHolder.status = C_GlobalHolder.YES;
            } else {
                /*location_json.put("is_online", C_GlobalHolder.NO);*/
                C_GlobalHolder.status = C_GlobalHolder.NO;
            }
            /*location_json.put("date_recorded", C_GlobalHolder.date_time);*/

            // save to local
            saveLocation();
            if (isOnline) {
                sendLocation();
            }

            C_GlobalHolder.start = 0;
            jsonSendHandler.postDelayed(this, C_GlobalHolder.SEND_INTERVAL);
        }
    };

    private void sendLocation() {

        List<C_rLocation> locations = A_Tracking.myAppDatabase.dataAccessObject().sendLocation();

        List<C_Details> details = new ArrayList<>();

        for(C_rLocation rLoc:locations) {

            details.add(new C_Details(
                    rLoc.getLongitude(),
                    rLoc.getLatitude(),
                    rLoc.getAddress(),
                    rLoc.getDatetime(),
                    rLoc.getIsStart(),
                    rLoc.getIsEnd(),
                    rLoc.getStatus()
            ));

        }

        /*Gson gson = new Gson();
        String location = gson.toJson(details);*/

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().sendLocation(auth, details);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200) {

                    System.out.println(response.body().toString());
                    System.out.println(response.code());
                    deleteLocation();

                } else {

                    System.out.println(response.body().toString());
                    System.out.println(response.code());
                    System.out.println("something went wrong, local data not deleted");
                    A_Tracking.myAppDatabase.dataAccessObject().updateLocation();
                    System.out.println("Connection status updated");

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                System.out.println("error in connection, location not deleted locally");
                A_Tracking.myAppDatabase.dataAccessObject().updateLocation();
                System.out.println("Connection status updated");

            }
        });
    }

    private void saveLocation() {

        C_rLocation rLoc = new C_rLocation();
        /*rLoc.setId(1);*/
        rLoc.setLatitude(C_GlobalHolder.latitude);
        rLoc.setLongitude(C_GlobalHolder.longitude);
        rLoc.setAddress(C_GlobalHolder.street_address);
        rLoc.setDatetime(C_GlobalHolder.date_time);
        rLoc.setStatus(C_GlobalHolder.status);
        rLoc.setIsStart(C_GlobalHolder.start);
        rLoc.setIsEnd(C_GlobalHolder.end);

        A_Tracking.myAppDatabase.dataAccessObject().addLocation(rLoc);

        System.out.println("location saved locally");
    }

    private void deleteLocation() {

        A_Tracking.myAppDatabase.dataAccessObject().deleteLocation();
        System.out.println("location deleted locally");
    }

}
