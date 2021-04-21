package com.example.p000.tracker;

public class C_GlobalHolder {

    public static final int PERMISSIONS_REQ_ACCESS_FINE_LOC = 1;
    public static final int PERMISSIONS_REQ_ACCESS_COARSE_LOC = 2;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int UPDATE_INTERVAL = 60000;
    public static final int FASTEST_INTERVAL = 5000;
    public static final long SEND_INTERVAL = 10000;
    public static final int DEFAULT_HOUR = 18;
    public static final int DEFAULT_MINUTE = 0;
    public static final int CONNECTION_CHECK_INTERVAL = 5000;

    public static final int YES = 1;
    public static final int NO = 0;



    // user info
    public static String auth = "";
    public static String user_id = "";
    public static String email = " ";
    public static String first_name = " ";
    public static String last_name = " ";

    // URL
    public static final String log_in_URL = "http://192.168.98.34/";
    public static final String send_URL = " ";

    // location data
    public static Double latitude = 0.0;
    public static Double longitude = 0.0 ;
    public static String street_address = " ";
    public static String date_time = " ";
    public static int status = 0;

    public static int start = 1;
    public static int end = 0;

}
