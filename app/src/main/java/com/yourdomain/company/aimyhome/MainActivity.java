package com.yourdomain.company.aimyhome;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yourdomain.company.aimyhome.Fragment.BathRoomFragment;
import com.yourdomain.company.aimyhome.Fragment.BedroomFragment;
import com.yourdomain.company.aimyhome.Fragment.CameraFragment;
import com.yourdomain.company.aimyhome.Fragment.ChartFragment;
import com.yourdomain.company.aimyhome.Fragment.DiningRoomFragment;
import com.yourdomain.company.aimyhome.Fragment.HomeFragment;
import com.yourdomain.company.aimyhome.Fragment.LivingRoomFragment;
import com.yourdomain.company.aimyhome.Fragment.SettingFragment;
import com.yourdomain.company.aimyhome.databinding.ActivityMainBinding;

import org.checkerframework.checker.units.qual.C;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100 ;

    BottomNavigationView bottomNavigationView;
    private ViewPager2 viewPager2;
    HomeFragment homeFragment;
    ChartFragment chartFragment;
    CameraFragment cameraFragment;
    SettingFragment settingFragment;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Camera Permission
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.frame_layout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_home:
                                Log.d("Huy",  "NavHome");
                                viewPager2.setCurrentItem(0, false);
                                break;
                            case R.id.menu_chart:
                                Log.d("Huy",  "NavChart");
                                viewPager2.setCurrentItem(1, false);
                                break;
                            case R.id.menu_camera:
                                Log.d("Huy",  "NavCam");
                                viewPager2.setCurrentItem(2, false);
                                break;
                            case R.id.menu_setting:
                                Log.d("Huy",  "NavSet");
                                viewPager2.setCurrentItem(3, false);
                                break;
                        }
                        return false;
                    }
                }
        );

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.menu_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.menu_chart).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.menu_camera).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.menu_setting).setChecked(true);
                        break;
                }
            }
        });

        setupViewPager(viewPager2);

    }

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        homeFragment = new HomeFragment();
        chartFragment = new ChartFragment();
        cameraFragment = new CameraFragment();
        settingFragment = new SettingFragment();
        adapter.addFragment(homeFragment);
        adapter.addFragment(chartFragment);
        adapter.addFragment(cameraFragment);
        adapter.addFragment(settingFragment);
        viewPager.setAdapter(adapter);
    }

}
