package com.sanafoundation.sanjaym;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sanafoundation.sanjaym.helper.PrefManager;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class SettingsFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1;

    private PrefManager pref;
    private ImageView profile_image;
    private TextView profile_name;
    private ListView list;
    private String[] values;
    private ArrayAdapter<String> adapter;

    private Handler handler;
    private Runnable r;

    private Bitmap userPicBitmap;

    private String objectId, userName, userFullName, profilePicStr;
    private String encoded, getPicFromBundle;

    Activity mActivity;

    public SettingsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_settings, container, false);

        pref = new PrefManager(getActivity());
        userName = pref.getUserName();
        userFullName = pref.getUserFullName();
        profilePicStr = pref.getProfilePic();

        profile_image = (ImageView) v.findViewById(R.id.profile_image);
        profile_name = (TextView) v.findViewById(R.id.profile_name);

        profile_name.setText(userFullName);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ParseQuery<ParseUser> userPicQuery = ParseUser.getQuery();
                userPicQuery.whereEqualTo("objectId", pref.getObjectId());
                userPicQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        final ParseFile profileImage = ParseUser.getCurrentUser().getParseFile("pic");

                        if (profileImage != null && profileImage.getUrl() != null && profileImage.getUrl().length() > 0) {
                            profileImage.getDataInBackground(new GetDataCallback() {

                                @Override
                                public void done(byte[] bytes, ParseException e) {
                                    if (e == null && bytes != null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0,
                                                bytes.length);

                                        profile_image.setImageBitmap(bmp);
                                        Log.d("profile_image applied", profile_image.toString());

                                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                        bmp.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm pis the bitmap object
                                        byte[] b = baos.toByteArray();

                                        encoded = Base64.encodeToString(b, Base64.DEFAULT);
                                        pref.createProfilePic(encoded);
                                        Log.d("Encoded if :-)", encoded);


                                    } else {
                                        Toast.makeText(getActivity(), "Unable to get image", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            profile_image.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.place_holder));
                        }
                    }
                });
            }
        });

        list = (ListView) v.findViewById(R.id.settings_listview);
        values = getResources().getStringArray(R.array.settings_list);

        if (userPicBitmap != null) {
            profile_image.setImageBitmap(userPicBitmap);
        } else {
            profile_image.setImageDrawable(getResources().getDrawable(R.drawable.place_holder));
        }

        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        list.setAdapter(adapter);

        profile_image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent openChangePicList = new Intent(getActivity(), ChangeProfilePicActivity.class);
                startActivity(openChangePicList);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                return true;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_row = (String) list.getItemAtPosition(position);
                Toast.makeText(getActivity(), selected_row, Toast.LENGTH_LONG).show();

                switch (selected_row) {
                    case "Logout":
                        pref.logout();
                        Intent intent = new Intent(getActivity(), MultiLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                }
            }
        });

        return v;
    }

}
