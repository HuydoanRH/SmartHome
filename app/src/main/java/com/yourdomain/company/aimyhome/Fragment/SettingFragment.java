package com.yourdomain.company.aimyhome.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
import com.yourdomain.company.aimyhome.R;
import com.yourdomain.company.aimyhome.SimilarityClassifier;

public class SettingFragment extends Fragment {
    TextView temp_auto, humi_auto, door_auto;
    boolean startTemp = true, startHumi=true, startDoor = true;
    private Context context;
    String maxTemp;
    String maxHumi;
    String tempState;
    String humiState;
    String doorState;

    com.suke.widget.SwitchButton tempAutoSwitch, humiAutoSwitch, doorAutoSwitch;

    // Initialise it from onAttach()
    @Override
    public void onAttach(Context context1) {
        super.onAttach(context1);
        context = context1;
        tempState = "0";
        humiState = "0";
        doorState = "0";
        maxTemp = "";
        maxHumi = "";
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        temp_auto = (TextView) view.findViewById(R.id.auto_Temp);
        humi_auto = (TextView) view.findViewById(R.id.auto_Humi);
        door_auto = (TextView) view.findViewById(R.id.auto_Door);
        tempAutoSwitch = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_autoTemp);
        humiAutoSwitch = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_autoHumi);
        doorAutoSwitch = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_autoDoor);

        // temp
        tempAutoSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    setMaxTemp();
                }else
                {
                    temp_auto.setText("OFF");
                    maxTemp = "";
                    tempState = "0";

                    // send state and max temp to Home fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("stateTemp", tempState);
                    bundle.putString("maxTemp", maxTemp);
                    getParentFragmentManager().setFragmentResult("dataFromSetting", bundle);
                }
            }
        });

        // humi
        humiAutoSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    setMaxHumi();
                } else {
                    humi_auto.setText("OFF");
                    maxHumi = "";
                    humiState = "0";

                    // send state and max humidity to Home fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("stateHumi", humiState);
                    bundle.putString("maxHumi", maxHumi);
                    getParentFragmentManager().setFragmentResult("dataFromSetting", bundle);
                }
            }
        });

        // door
        doorAutoSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    door_auto.setText("ON");
                    doorState = "1";

                    // send state and max temp to Home fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("stateDoor", doorState);
                    getParentFragmentManager().setFragmentResult("dataFromSetting", bundle);

                }else {
                    door_auto.setText("OFF");
                    doorState = "0";

                    // send state and max temp to Home fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("stateDoor", doorState);
                    getParentFragmentManager().setFragmentResult("dataFromSetting", bundle);
                }
            }
        });
        return view;
    }

    private void setMaxTemp()
    {
            startTemp=false;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Max temperature");

            // Set up the input
            final EditText input = new EditText(context);

            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    maxTemp = input.getText().toString();
                    temp_auto.setText("ON" + "_" + maxTemp + '°' + 'C');
                    startTemp=true;
                    tempState = "1";

                    // send state and max temp to Home fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("stateTemp", tempState);
                    bundle.putString("maxTemp", maxTemp);
                    getParentFragmentManager().setFragmentResult("dataFromSetting", bundle);

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(startTemp){
                        tempAutoSwitch.setChecked(true);
                        dialog.cancel();
                    }
                    else{
                        tempAutoSwitch.setChecked(false);
                        startTemp=false;
                        tempState= "0";
                        dialog.cancel();
                    }
                }
            });
            builder.show();

    }


    private void setMaxHumi()
    {
            startHumi=false;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Enter Max Humidity");

            // Set up the input
            final EditText input = new EditText(context);

            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    maxHumi = input.getText().toString();
                    humi_auto.setText("ON" + "_" + maxHumi + '%');
                    startHumi=true;
                    humiState = "1";

                    // send state and max temp to Home fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("stateHumi", humiState);
                    bundle.putString("maxHumi", maxHumi);
                    getParentFragmentManager().setFragmentResult("dataFromSetting", bundle);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(startHumi){
                        humiAutoSwitch.setChecked(true);
                        dialog.cancel();
                    }
                    else{
                        humiAutoSwitch.setChecked(false);
                        startHumi=false;
                        humiState= "0";
                        dialog.cancel();
                    }
                }
            });
            builder.show();
    }
}