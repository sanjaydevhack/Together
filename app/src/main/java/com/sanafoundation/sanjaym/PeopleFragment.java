package com.sanafoundation.sanjaym;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sanafoundation.sanjaym.adapter.PeopleAdapter;
import com.sanafoundation.sanjaym.app.AppController;
import com.sanafoundation.sanjaym.helper.PrefManager;
import com.sanafoundation.sanjaym.model.People;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeopleFragment extends Fragment {

    private PrefManager pref;
    com.android.volley.toolbox.ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ListView listview;
    private List<ParseUser> obj;
    private ProgressDialog mProgressDialog;
    private PeopleAdapter adapter;
    protected static List<People> peopleList = new ArrayList<People>();
    Activity mActivity;
    private ParseObject entryToRecentChat;
    private ParseQuery<ParseObject> groupIdQuery;
    private String objectIdStr, user1ObjectId, user1Name, user2ObjectId, user2Name, recentClassObjectId, groupId;

    public static final String gId = "gIdKey";

    public PeopleFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_people, container, false);

        listview = (ListView) v.findViewById(R.id.listview);

        pref = new PrefManager(getActivity());
        objectIdStr = pref.getObjectId();

        new RemoteDataTask().execute();

        return v;
    }

    private class RemoteDataTask extends AsyncTask<List<People>, Void, List<People>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected List<People> doInBackground(List<People>... params) {

            try {

                final ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        ParseFile image = null;

                        if (e == null) {

                            peopleList.clear();

                            obj = objects;
                            for (ParseObject po : objects) {

                                //image = (ParseFile) po.get("pic");
                                final ParseFile postImage;

                                final People people = new People();

                                people.setName(po.getString("username"));
                                people.setLastMessage(po.getString("email"));
                                people.setObjectId(po.getObjectId());
                                user2ObjectId = po.getObjectId();

                                //people.setProfilePic(image.getUrl());

                                postImage = po.getParseFile("pic");
                                if (postImage != null && postImage.getUrl() != null && postImage.getUrl().length() > 0) {
                                    postImage.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] bytes, ParseException e) {
                                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            people.setPic(bmp);
                                            Log.d("ImageCoversionIf", postImage.toString());
                                        }
                                    });
                                } else {
                                    Bitmap icon = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.place_holder);
                                    Log.d("ImageCoversion", icon.toString());
                                    people.setPic(icon);

                                }
                                peopleList.add(people);
                            }
                        } else {
                            Log.d("*****Error", e.getMessage());
                        }
                    }
                });

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return peopleList;
        }

        @Override
        protected void onPostExecute(final List<People> people) {
            super.onPostExecute(people);

            adapter = new PeopleAdapter(getActivity(),
                    peopleList);
            listview.setAdapter(adapter);

            // Locate the listview in fragment_people.xml
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    user2ObjectId = peopleList.get(position).getObjectId();
                    user2Name = peopleList.get(position).getName();

                    creatingGroupId();
                }
            });

            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_people, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_people) {
            Toast.makeText(getActivity(), "Search People", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void creatingGroupId() {
        user1ObjectId = pref.getObjectId();
        user1Name = pref.getUserName();

        groupIdQuery = ParseQuery.getQuery("Recent");
        groupIdQuery.whereContainsAll("members", Arrays.asList(user1ObjectId, user2ObjectId));
        Log.d("objectArray", Arrays.asList(user1ObjectId, user2ObjectId).toString());
        groupIdQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null && list.size() != 0) {
                    for (ParseObject dealsObject : list) {

                        recentClassObjectId = dealsObject.getObjectId();
                        groupId = (String) dealsObject.get("groupId");
                        Log.d("!!!!!GroupId", groupId);
                    }

                    Intent startChat = new Intent(getActivity(), StartChat.class);
                    startChat.putExtra("groupId", groupId);
                    startChat.putExtra("user2Name", user2Name);
                    startChat.putExtra("user2ObjectId", user2ObjectId);
                    startActivity(startChat);

                } else {

                    Toast.makeText(getActivity(), " No Data present", Toast.LENGTH_LONG).show();

                    groupId = user1ObjectId + user2ObjectId;

                    Log.d("!!!!ElseGroupId", groupId);
                    entryToRecentChat = new ParseObject("Recent");
                    entryToRecentChat.put("groupId", groupId);
                    entryToRecentChat.put("lastUser", ParseObject.createWithoutData("_User", user1ObjectId));
                    entryToRecentChat.put("user", ParseObject.createWithoutData("_User", user2ObjectId));
                    entryToRecentChat.addAllUnique("members", Arrays.asList(user1ObjectId, user2ObjectId));
                    entryToRecentChat.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getActivity(), "Value saved", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Value not saved", Toast.LENGTH_LONG).show();
                                Log.d("exception", e.getMessage());
                            }
                        }
                    });
                    Log.d("elseGroupId", groupId);

                    Intent startChat = new Intent(getActivity(), StartChat.class);
                    startChat.putExtra("groupId", groupId);
                    startChat.putExtra("user2Name", user2Name);
                    startChat.putExtra("user2ObjectId", user2ObjectId);
                    Toast.makeText(getActivity(), groupId, Toast.LENGTH_LONG).show();
                    startActivity(startChat);
                }
            }
        });
    }

}
