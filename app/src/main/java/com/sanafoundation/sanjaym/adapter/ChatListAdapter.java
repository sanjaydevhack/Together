package com.sanafoundation.sanjaym.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.client.Query;
import com.sanafoundation.sanjaym.R;
import com.sanafoundation.sanjaym.model.Chat;

/**
 * Created by IM028 on 7/13/15.
 */
public class ChatListAdapter extends FirebaseListAdapter<Chat> {

    // The mUsername for this client. We use this to indicate which messages originated from this user
    private String mUsername;
    Activity activity;
    LayoutInflater inflater;

    public ChatListAdapter(Query ref, Activity activity, int layout, String mUsername) {
        super(ref, Chat.class, layout, activity);
        this.mUsername = mUsername;
        this.activity = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Bind an instance of the <code>Chat</code> class to our view. This method is called by <code>FirebaseListAdapter</code>
     * when there is a data change, and we are given an instance of a View that corresponds to the layout that we passed
     * to the constructor, as well as a single <code>Chat</code> instance that represents the current data to bind.
     *
     * @param view A view instance corresponding to the layout we passed to the constructor.
     * @param chat An instance representing the current state of a chat message
     */
    @Override
    protected void populateView(View view, Chat chat) {

        String author = chat.getAuthor();
        TextView authorText = (TextView) view.findViewById(R.id.author);
        TextView messageText = (TextView) view.findViewById(R.id.message);

        authorText.setText(author + ": ");
        // If the message was sent by this user, design it differently
        if (author != null && author.equals(mUsername)) {

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.RIGHT;

            authorText.setTextColor(view.getResources().getColor(R.color.lblFromName));
            authorText.setLayoutParams(params);

            messageText.setText(chat.getMessage());
            messageText.setBackground(view.getResources().getDrawable(R.drawable.snd_msg_bubble));
            messageText.setTextColor(Color.parseColor("#FFFFFF"));
            messageText.setLayoutParams(params);

        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params.weight = 1.0f;
            params.gravity = Gravity.LEFT;

            authorText.setTextColor(view.getResources().getColor(R.color.lblFromName));
            authorText.setLayoutParams(params);

            messageText.setText(chat.getMessage());
            messageText.setLayoutParams(params);
            messageText.setBackground(view.getResources().getDrawable(R.drawable.recieve_msg_bubble));
            messageText.setTextColor(Color.parseColor("#000000"));
        }

    }

}
