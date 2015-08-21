package com.sanafoundation.sanjaym;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sanafoundation.sanjaym.adapter.ChangePicAdapter;
import com.sanafoundation.sanjaym.utils.CaptureFromCamera;
import com.sanafoundation.sanjaym.utils.SelectFromGallery;

import java.io.File;

/**
 * Created by Sanjay on 8/6/15.
 */
public class ChangeProfilePicActivity extends Activity {

    File f = new File(Environment.getExternalStorageDirectory(), "Sanjay M");

    private String[] stringArrayValues = {
            "Take Picture From Camera",
            "Select Picture From Gallery"

    };
    private Integer[] drawableArrayValues = {
            R.drawable.icon_camera,
            R.drawable.icon_gallery
    };

    public ChangeProfilePicActivity() {
    }

    private ListView listView;
    private ChangePicAdapter adapter;

    private int picArray[] = {R.drawable.place_holder, R.drawable.place_holder};
    private String picTypeArray[] = {"Take picture", "Select from gallery"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_pic);

        // Following code define thsi activity to display in bottom of the screen
        Window window = this.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        adapter = new ChangePicAdapter(this, stringArrayValues, drawableArrayValues);

        listView = (ListView) findViewById(R.id.change_profile_pic_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_row = (String) listView.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), selected_row, Toast.LENGTH_LONG).show();

                switch (selected_row) {
                    case "Take Picture From Camera":
                        Intent takeFromCamera = new Intent(getApplicationContext(), CaptureFromCamera.class);
                        startActivity(takeFromCamera);
                        finish();
                        break;
                    case "Select Picture From Gallery":
                        Intent picFromGallery = new Intent(getApplicationContext(), SelectFromGallery.class);
                        startActivity(picFromGallery);
                        finish();
                        break;
                }
            }
        });

    }

}
