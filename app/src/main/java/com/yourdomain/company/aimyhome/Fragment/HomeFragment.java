package com.yourdomain.company.aimyhome.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yourdomain.company.aimyhome.Fragment.BathRoomFragment;
import com.yourdomain.company.aimyhome.Fragment.BedroomFragment;
import com.yourdomain.company.aimyhome.Fragment.CameraFragment;
import com.yourdomain.company.aimyhome.Fragment.DiningRoomFragment;
import com.yourdomain.company.aimyhome.Fragment.HomeFragment;
import com.yourdomain.company.aimyhome.Fragment.LivingRoomFragment;
import com.yourdomain.company.aimyhome.Fragment.SettingFragment;
import com.yourdomain.company.aimyhome.databinding.ActivityMainBinding;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

//here

import com.yourdomain.company.aimyhome.R;

public class HomeFragment extends Fragment {
    LinearLayout livingRoom, diningRoom, bedRoom, bathRoom, livingRoomOn,livingRoomOff, diningRoomOn,diningRoomOff, bedRoomOn,bedRoomOff,bathRoomOn, bathRoomOff;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        replaceFragment2(new LivingRoomFragment());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_home,container,false);
        livingRoom = (LinearLayout)view.findViewById(R.id.livingRoom);
        diningRoom = (LinearLayout)view.findViewById(R.id.diningRoom);
        bedRoom = (LinearLayout)view.findViewById(R.id.BedRoom);
        bathRoom = (LinearLayout)view.findViewById(R.id.BathRoom);
        livingRoomOn = (LinearLayout)view.findViewById(R.id.livingRoomON);
        livingRoomOff = (LinearLayout)view.findViewById(R.id.livingRoomOFF);
        diningRoomOn = (LinearLayout)view.findViewById(R.id.diningRoomON);
        diningRoomOff = (LinearLayout)view.findViewById(R.id.diningRoomOFF);
        bathRoomOn = (LinearLayout)view.findViewById(R.id.BathRoomON);
        bathRoomOff = (LinearLayout)view.findViewById(R.id.BathRoomOFF);
        bedRoomOn = (LinearLayout)view.findViewById(R.id.BedRoomON);
        bedRoomOff  = (LinearLayout)view.findViewById(R.id.BedRoomOFF);

        //set tab view for room
        livingRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livingRoomOn.setVisibility(View.VISIBLE);
                livingRoomOff.setVisibility(View.GONE);
                diningRoomOn.setVisibility(View.GONE);
                diningRoomOff.setVisibility(View.VISIBLE);
                bedRoomOn.setVisibility(View.GONE);
                bedRoomOff.setVisibility(View.VISIBLE);
                bathRoomOn.setVisibility(View.GONE);
                bathRoomOff.setVisibility(View.VISIBLE);
                replaceFragment2(new LivingRoomFragment());
            }
        });
        diningRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livingRoomOn.setVisibility(View.GONE);
                livingRoomOff.setVisibility(View.VISIBLE);
                diningRoomOn.setVisibility(View.VISIBLE);
                diningRoomOff.setVisibility(View.GONE);
                bedRoomOn.setVisibility(View.GONE);
                bedRoomOff.setVisibility(View.VISIBLE);
                bathRoomOn.setVisibility(View.GONE);
                bathRoomOff.setVisibility(View.VISIBLE);
                replaceFragment2(new DiningRoomFragment());
            }
        });
        bedRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livingRoomOn.setVisibility(View.GONE);
                livingRoomOff.setVisibility(View.VISIBLE);
                diningRoomOn.setVisibility(View.GONE);
                diningRoomOff.setVisibility(View.VISIBLE);
                bedRoomOn.setVisibility(View.VISIBLE);
                bedRoomOff.setVisibility(View.GONE);
                bathRoomOn.setVisibility(View.GONE);
                bathRoomOff.setVisibility(View.VISIBLE);
                replaceFragment2(new BedroomFragment());
            }
        });

        bathRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                livingRoomOn.setVisibility(View.GONE);
                livingRoomOff.setVisibility(View.VISIBLE);
                diningRoomOn.setVisibility(View.GONE);
                diningRoomOff.setVisibility(View.VISIBLE);
                bedRoomOn.setVisibility(View.GONE);
                bedRoomOff.setVisibility(View.VISIBLE);
                bathRoomOn.setVisibility(View.VISIBLE);
                bathRoomOff.setVisibility(View.GONE);
                replaceFragment2(new BathRoomFragment());
            }
        });

        return view;
    }
    private void replaceFragment2(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layout, fragment);
        fragmentTransaction.commit();
    }

}