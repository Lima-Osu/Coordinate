package com.infosecurity.coordinate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText messageEditableText;
    ArrayAdapter<String> adapter;
    RequestQueue chatQueue = Volley.newRequestQueue(MessageActivity.this);
    String chatId;
    RequestQueue queue = Volley.newRequestQueue(MessageActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        final ImageButton sendButton = (ImageButton) findViewById(R.id.send_button);
        sendButton.setClickable(true);
        sendButton.setOnClickListener(MessageActivity.this);

        // Create the list view
        final ListView listView = (ListView)findViewById(R.id.textListView);
        // Create the arraylist to display the texts
        final ArrayList<String> messages = new ArrayList<>();

        // ---------------
        // Gets the chatID from last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            chatId = extras.getString("chatId");
        }

        // Pull JSONOBJECT based on chatId value
        
        // -------------
        //PULLS JSONOBJECT
        // GET to /chats/chat_id to get messages for the chosen chat
        final String url = "https://morning-anchorage-16263.herokuapp.com/chats/" + chatId;
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            JSONObject jsonObject = response;
                            //VALUES TO PULL HERE.
                            //-------------
                            String chat_id = jsonObject.getString("chat_id");
                            String creator = jsonObject.getString("creator");
                            JSONArray messages = jsonObject.getJSONArray("messages");
                            //USE THESE VALUES TO AGGREGATE CHAT MESSAGES TO MESSAGEACTIVITY
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
                        Log.d("Error.Response", error.toString());
                    }
                }
        );
        queue.add(getRequest);

        //POST to /messages with params mac_address, content, chat_id [to create a message]

        //---------------


        // Create the adapter using the available chats
        adapter = new ArrayAdapter<>(MessageActivity.this, android.R.layout.simple_list_item_1, messages);

        listView.setAdapter(adapter);

        messageEditableText = (EditText) findViewById(R.id.message_textView);

    }

    @Override
    public void onClick(View v){
        // Get message
        String messsage = this.messageEditableText.getText().toString();
        // Send to server

    }
}
