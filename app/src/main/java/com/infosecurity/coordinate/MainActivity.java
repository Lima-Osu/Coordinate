package com.infosecurity.coordinate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        LocationListener {


    /**
     * Represents a geographical location.
     */
    protected Location mLastLocation;

    ArrayAdapter<String> adapter;
    private double longitude = 1.0;
    private double latitude = 1.0;
    // Create arrayList of ids to associate the names with
    ArrayList<String> chatIds = new ArrayList<>();
    // Create the arraylist to display the texts
    ArrayList<String> arrayOfChats = new ArrayList<>();
    // Create mylocation
    LocationManager mLocationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create Chat button
        final Button newChat = (Button)findViewById(R.id.chatButton);
        newChat.setClickable(true);
        newChat.setOnClickListener(MainActivity.this);


        // Create the list view
        final ListView listView = (ListView)findViewById(R.id.listView);


        // ------------------------
        // LANE: MAC address is right here, called macAddress
        // Pass the two doubles latitude and longitude
        // Get Mac address of device
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        final String macAddress = info.getMacAddress();


        // Find GPS location

        ///

        LocationManager mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = LocationManager.GPS_PROVIDER;
        //Location location = mLocationManager.getLastKnownLocation(provider);



        // POST1 - SEND: MAC, LAT, LONG ;
        // RETURN: mac_address, username, id, created_at, updated_at, latitude, longitude
        //final String url = "https://morning-anchorage-16263.herokuapp.com/get_chats";
        final String url = "https://morning-anchorage-16263.herokuapp.com/users/update_location";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                            Log.d("Response", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            //VALUES TO PULL HERE.
                            //-------------
                            String mac_address = jsonObject.getString("mac_address");
                            String latitude = jsonObject.getString("latitude");
                            String longitude = jsonObject.getString("longitude");
                            String id = jsonObject.getString("id");
                            String username = jsonObject.getString("username");
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.wtf("error", e.getMessage());
                            //Test commit changes.
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        System.out.println(error.getMessage());
                        Log.d("Error Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("mac_address", macAddress);
                params.put("latitude", Double.toString(latitude));
                params.put("longitude", Double.toString(longitude));

                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
                //return "application/text/html; charset=UTF-8";
                //return "application/json";
            }
        };
        requestQueue.add(postRequest);


        // POST2 - SEND: MAC
        // RETURN: CHATID, NAME(USER'S), CREATOR, LAST_MESSAGE
        final String url2 = "https://morning-anchorage-16263.herokuapp.com/get_chats";
        RequestQueue chatQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest chatRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("chats");
                            for(int i = 0; i< jsonArray.length(); i++) {
                                //VALUES TO PULL HERE.
                                //-------------
                                JSONObject chat = jsonArray.getJSONObject(i);
                                String chatID = chat.getString("id");
                                String name = chat.getString("name");
                                String creator = chat.getString("creator");
                                String last_message = chat.getString("last_message");
                                // Add name of chats to a list
                                arrayOfChats.add(0, name);
                                // Add name of IDs to list
                                chatIds.add(0, chatID);
                            }
                            // Create the adapter using the available chats
                            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayOfChats);

                            listView.setAdapter(adapter);
                        } catch (Exception e) {
                            // Create the adapter using the available chats
                            String noChats = "There are no chats near you :(";
                            arrayOfChats.add(0, noChats);
                            adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, arrayOfChats);

                            listView.setAdapter(adapter);
                            e.printStackTrace();
                            Log.wtf("error", e.getMessage());
                            //Test commit changes.
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        System.out.println(error.getMessage());
                        //Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                //CHANGE THIS TO ADDRESS NOT "TESTTEST" WHEN READY
                params.put("mac_address", macAddress);
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        chatQueue.add(chatRequest);
        //--------------END OF VOLLEY WORK IN MAINACTIVITY.



        // Open text messages for specified chat
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get right chat id on click
                String chatID = chatIds.get(position);

                // Pass the chat ID to the message activity
                Intent i = new Intent(getApplicationContext(), MessageActivity.class);
                i.putExtra("chatId", chatID);
                startActivity(i);
            }
        });

    }

    @Override
    public void onClick(View v){
        createChat();
    }

    public void createChat(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Create Chat");
        alertDialogBuilder.setMessage("Enter Name of Chat:");

        final EditText editText = new EditText(this);
        alertDialogBuilder.setView(editText);

        alertDialogBuilder.setPositiveButton("Create",new DialogInterface.OnClickListener(){
            final String chatName = editText.getText().toString();
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                // Send chatName to server
                if (chatName.length() == 0){
                    dialog.dismiss();
                }
                // Get position of chat name to retrieve chat id
                int chatIDIndex = arrayOfChats.indexOf(chatName);
                String chatID = chatIds.get(chatIDIndex);

                // ------------------------------------
                // LANE: Create new chat with chatName variable

                // Pass the chat ID to the message activity
                Intent i = new Intent(getApplicationContext(), MessageActivity.class);
                i.putExtra(chatID, true);
                startActivity(i);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }
    void updateLocation(Location location){
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

}
