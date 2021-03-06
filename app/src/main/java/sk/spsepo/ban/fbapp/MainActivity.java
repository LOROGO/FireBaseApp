package sk.spsepo.ban.fbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionPagerAdapter;
    private TabLayout mTabLayout;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getUid()==null)logout();
        setTitle("Vojs");
        Bundle extras = getIntent().getExtras();
        int pos = 1;
        if (extras!=null){
            pos = extras.getInt("viewPagerPos");
        }




        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
    mSectionPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        //ChatsFragment fragment = (ChatsFragment) getFragmentManager().findFragmentById(R.id.fragment_chats);
       // fragment.<specific_function_name>();
        mViewPager.setAdapter(mSectionPagerAdapter);
    mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
    mTabLayout.setupWithViewPager(mViewPager);
    mViewPager.setCurrentItem(pos);



    }
    public void logout(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);



        if(item.getItemId()== R.id.main_logout_btn){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }else if (item.getItemId() == R.id.main_settings_btn){
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));

        }else if (item.getItemId() == R.id.main_all_btn){
            //startActivity(new Intent(getApplicationContext(), AllUsers.class));
            startActivity(new Intent(getApplicationContext(), AllUsers.class));

        }

        return true;
    }
    public void change2chat(String uid){
        mViewPager.setCurrentItem(1);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}