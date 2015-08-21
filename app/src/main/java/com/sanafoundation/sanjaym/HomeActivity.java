package com.sanafoundation.sanjaym;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.sanafoundation.sanjaym.helper.PrefManager;


public class HomeActivity extends AppCompatActivity {

    private static String TAG = HomeActivity.class.getSimpleName();

    private PrefManager pref;

    private ImageView recent_chat, group_chat, people, settings;

    private Toolbar mtoolbar, bottmToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pref = new PrefManager(getApplicationContext());

        mtoolbar = (Toolbar) findViewById(R.id.home_toolbar);
        bottmToolbar = (Toolbar) findViewById(R.id.home_bottom_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentcontainer, new RecentChatFragment());
        ft.commit();
        mtoolbar.setTitle(getResources().getString(R.string.recent_chat));

        recent_chat= (ImageView) findViewById(R.id.recent_chat);
        group_chat = (ImageView) findViewById(R.id.group_chat);
        people = (ImageView) findViewById(R.id.people);
        settings = (ImageView) findViewById(R.id.chat_settings);

        recent_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentcontainer, new RecentChatFragment());
                ft.commit();
                mtoolbar.setTitle(getResources().getString(R.string.recent_chat));
            }
        });
        group_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentcontainer, new GroupChatFragment());
                ft.commit();
                mtoolbar.setTitle(getResources().getString(R.string.group_chat));
            }
        });
        people.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentcontainer, new PeopleFragment());
                ft.commit();
                mtoolbar.setTitle(getResources().getString(R.string.find_people));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fragmentcontainer, new SettingsFragment());
                ft.commit();
                mtoolbar.setTitle(getResources().getString(R.string.chat_settings));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);

    }
}
