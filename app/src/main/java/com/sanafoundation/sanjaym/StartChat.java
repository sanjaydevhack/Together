package com.sanafoundation.sanjaym;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pkmmte.view.CircularImageView;
import com.sanafoundation.sanjaym.adapter.ChatListAdapter;
import com.sanafoundation.sanjaym.helper.PrefManager;
import com.sanafoundation.sanjaym.model.Chat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class StartChat extends AppCompatActivity {

    private String FIREBASE_URL = "https://crackling-fire-6726.firebaseio.com/Message";

    private String user1ObjectId, user1Name;
    private String user2ObjectId, user2Name;
    private static String groupId;

    private Firebase mFirebaseRef;
    private ChatListAdapter mChatListAdapter;
    private ValueEventListener mConnectedListener;
    private PrefManager pref;
    private Toolbar mtoolbar;

    private Chat chat;

    private ListView listView;
    private EditText inputText;

    private String input;
    private String recentClassObjectId;
    private ParseObject entryToRecentChat;
    private ParseQuery<ParseUser> user2ProfileImageQuery;
    private CircularImageView user2Icon;
    private TextView user2NameTextView;
    private Bitmap bmp;

    public StartChat() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_chat);

        pref = new PrefManager(getApplicationContext());

        user1ObjectId = pref.getObjectId();
        user1Name = pref.getUserName();

        Intent i = getIntent();
        user2Name = i.getStringExtra("user2Name");
        user2ObjectId = i.getStringExtra("user2ObjectId");
        groupId = i.getStringExtra("groupId");

        mtoolbar = (Toolbar) findViewById(R.id.included_start_chat_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        user2ProfileImageQuery = ParseUser.getQuery();
        user2ProfileImageQuery.whereEqualTo("objectId", user2ObjectId);
        user2ProfileImageQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                ParseFile image = null;

                if (e == null) {
                    for (ParseObject po : list) {

                        final ParseFile postImage;

                        postImage = po.getParseFile("pic");
                        if (postImage != null && postImage.getUrl() != null && postImage.getUrl().length() > 0) {
                            postImage.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                                    user2Icon = (CircularImageView) findViewById(R.id.user2ToolbarLogo);
                                    user2NameTextView = (TextView) findViewById(R.id.user2Name);
                                    user2Icon.setImageBitmap(bmp);
                                    user2NameTextView.setText(user2Name);

                                }
                            });
                        } else {
                            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.place_holder);
                            user2Icon = (CircularImageView) findViewById(R.id.user2ToolbarLogo);
                            user2NameTextView = (TextView) findViewById(R.id.user2Name);
                            user2Icon.setImageDrawable(getResources().getDrawable(R.drawable.place_holder));
                            user2NameTextView.setText(user2Name);

                        }
                    }

                }
            }
        });

        mFirebaseRef = new Firebase(FIREBASE_URL).child(groupId);
        Log.d("FinalGroupId", groupId);

        inputText = (EditText) findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        //input = inputText.getText().toString();
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                sendPush();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        listView = (ListView) findViewById(R.id.list);

        mChatListAdapter = new ChatListAdapter(mFirebaseRef.limit(50), this, R.layout.chat_message, user1Name);
        listView.setAdapter(mChatListAdapter);
        mChatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mChatListAdapter.getCount() - 1);
            }
        });


        // Finally, a little indication of connection status
        mConnectedListener = mFirebaseRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean) dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(StartChat.this, "Connected to ChatRoom", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(StartChat.this, "Disconnected from ChatRoom", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        mFirebaseRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mChatListAdapter.cleanup();
    }

    private void sendMessage() {
        input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            chat = new Chat(groupId, input, user1Name);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            mFirebaseRef.push().setValue(chat);
            inputText.setText("");

            sendPush();
        }

        ParseQuery<ParseObject> groupIdQueryInAddition = ParseQuery.getQuery("Recent");
        groupIdQueryInAddition.getInBackground(recentClassObjectId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("lastMessage", input);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    private void sendPush() {

        if (user2ObjectId != null) {

            /*ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("user", user2ObjectId);
            ParseUser currentUser = ParseUser.getCurrentUser();
            String message = currentUser.getString("name") + " says Hi!";

            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setMessage(message);
            push.sendInBackground();*/

            JSONObject obj;
            try {
                obj = new JSONObject();
                obj.put("customdata", input);

                ParsePush push = new ParsePush();
                ParseQuery query = ParseInstallation.getQuery();


                // Notification for Android users
                query.whereEqualTo("user", user2ObjectId);
                push.setQuery(query);
                push.setData(obj);
                push.sendInBackground();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
