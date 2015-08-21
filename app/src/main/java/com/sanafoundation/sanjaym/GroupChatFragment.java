package com.sanafoundation.sanjaym;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sanafoundation.sanjaym.adapter.GroupAdapter;
import com.sanafoundation.sanjaym.model.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupChatFragment extends Fragment {

    private ListView listview;
    private List<ParseObject> obj;
    private ProgressDialog mProgressDialog;
    private GroupAdapter adapter;
    private List<Group> groupList = null;

    Activity mActivity;

    public GroupChatFragment() {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_group_chat, container, false);

        new RemoteDataTask().execute();

        return v;
    }

    private class RemoteDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            groupList = new ArrayList<Group>();

            try {
                // Locate the class table named "Country" in Parse.com
                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Group");
                // Locate the column named "No" in Parse.com and order list
                // by ascending
                query.orderByAscending("no");
                obj = query.find();
                for (ParseObject groupObj : obj) {
                    // Locate images in flag column
                    //ParseFile image = (ParseFile) group.get("group_icon");
                    final ParseFile postImage;

                    final Group group = new Group();
                    group.setGroup_name((String) groupObj.get("group_title"));
                    //group.setGroup_icon(image.getUrl());

                    postImage = groupObj.getParseFile("pic");
                    if (postImage != null && postImage.getUrl() != null && postImage.getUrl().length() > 0) {
                        postImage.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, ParseException e) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                group.setPic(bmp);
                                Log.d("ImageCoversionIf", postImage.toString());

                            }
                        });
                    } else {
                        Bitmap icon = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.place_holder);
                        Log.d("ImageCoversion", icon.toString());
                        group.setPic(icon);

                    }
                    groupList.add(group);
                }

            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            // Locate the listview in listview_main.xml
            listview = (ListView) getActivity().findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new GroupAdapter(getActivity(),
                    groupList);
            // Binds the Adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
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
        inflater.inflate(R.menu.menu_group, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_group) {
            Toast.makeText(getActivity(), "Group Created", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
}
