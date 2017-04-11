package com.infosecurity.coordinate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText messageEditableText;
    ArrayAdapter<String> adapter;
    RequestQueue chatQueue = Volley.newRequestQueue(MessageActivity.this);
    String chatId;

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

        //
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            chatId = extras.getString("chatId");
        }

        // -------------
        //PULLS JSONOBJECT
        // GET to /chats/chat_id to get messages for the chosen chat

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
