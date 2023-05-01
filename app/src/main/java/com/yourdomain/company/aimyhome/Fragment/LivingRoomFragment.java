package com.yourdomain.company.aimyhome.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suke.widget.SwitchButton;
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
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yourdomain.company.aimyhome.MQTTHelper;
import com.yourdomain.company.aimyhome.R;

import java.nio.charset.Charset;
import java.util.Objects;


public class LivingRoomFragment extends Fragment {
    TextView temp, humi;
    TextView light_text, fan_text, air_text, door_text;
    com.suke.widget.SwitchButton light, fan, air, door_lock;
    MQTTHelper mqttHelper;
    String m_maxTemp;
    String m_maxHumi;
    String m_tempState;
    String m_humiState;
    String m_doorState;

    Boolean autoTemp;
    Boolean autoHumi;
    String faceValue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        m_tempState = "0";
        m_humiState = "0";
        m_doorState = "0";
        m_maxTemp = "";
        m_maxHumi = "";

        autoTemp = false;
        autoHumi = false;
        faceValue = "0";

        startMQTT();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_living_room,container,false);
        temp = (TextView) view.findViewById(R.id.temp);
        humi = (TextView) view.findViewById(R.id.humidity);
        light = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_light);
        fan = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_fan);
        door_lock = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_door);
        air = (com.suke.widget.SwitchButton) view.findViewById(R.id.switch_air);

        light_text = (TextView)view.findViewById(R.id.light_text);
        fan_text = (TextView)view.findViewById(R.id.fan_text);
        air_text = (TextView)view.findViewById(R.id.air_text);
        door_text = (TextView)view.findViewById(R.id.door_text);

        light.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    light_text.setText("ON");
                    sendDataMQTT("DucHuy/feeds/livingroom_lightbutton","1");
                }else {
                    light_text.setText("OFF");
                    sendDataMQTT("DucHuy/feeds/livingroom_lightbutton","0");
                }
            }
        });

        fan.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    fan_text.setText("ON");
                    sendDataMQTT("DucHuy/feeds/fan-button","1");
                }else {
                    fan_text.setText("OFF");
                    sendDataMQTT("DucHuy/feeds/fan-button","0");
                }
            }
        });

        air.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    air_text.setText("ON");
                    sendDataMQTT("DucHuy/feeds/air","1");
                }else {
                    air_text.setText("OFF");
                    sendDataMQTT("DucHuy/feeds/air","0");
                }
            }
        });

        door_lock.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (view.isChecked())
                {
                    door_text.setText("ON");
                    sendDataMQTT("DucHuy/feeds/door-button","1");
                }else {
                    door_text.setText("OFF");
                    sendDataMQTT("DucHuy/feeds/door-button","0");
                }
            }
        });

        getParentFragmentManager().setFragmentResultListener("dataFromCam", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle resultFace) {
                faceValue = resultFace.getString("checkFace");
                Log.w("Huy", faceValue);

                if(Objects.equals(m_doorState,"1") && Objects.equals(faceValue,"1")){
                    Log.w("Huy", "autoDoorOn");
                    door_lock.setChecked(true);
                }

            }
        });

        getParentFragmentManager().setFragmentResultListener("dataFromSetting", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                m_tempState = result.getString("stateTemp");
                m_maxTemp = result.getString("maxTemp");

                m_humiState = result.getString("stateHumi");
                m_maxHumi = result.getString("maxHumi");

                m_doorState = result.getString("stateDoor");
//                faceValue = result.getString("checkFace");

//                Log.w("Huy", m_tempState);
//                Log.w("Huy", m_maxTemp);
//
//                Log.w("Huy", m_humiState);
//                Log.w("Huy", m_maxHumi);
//
//                Log.w("Huy", m_doorState);
//                Log.w("Huy", faceValue);

                if (Objects.equals(m_tempState,"0") && autoTemp ){
                    Log.w("Huy", "autoTempOff");
                    fan.setChecked(false);
                    autoTemp = false;
                }

                if (Objects.equals(m_humiState,"0") && autoHumi ){
                    Log.w("Huy", "autoHumiOff");
                    air.setChecked(false);
                    autoHumi = false;
                }

            }
        });
        return view;
    }

    public void startMQTT(){
        mqttHelper = new MQTTHelper(this.getContext());
        Log.w("connect", "not ok");

        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("TEST", topic + "***" + message.toString());
                if(topic.contains("temperature")){
                    temp.setText(message.toString() + '°' + 'C');
                    if(Objects.equals(m_tempState, "1")){
                        Log.w("Huy", "autoTempOn");
                        autoTemp = true;
                        int valueTemp = Integer.parseInt(message.toString());
                        int maxValueTemp = Integer.parseInt(m_maxTemp);
                        if (valueTemp >= maxValueTemp){
                            fan.setChecked(true);
                        }else {
                            fan.setChecked(false);
                        }
                    }

                }else if(topic.contains("humidity")){
                    humi.setText(message.toString() + '%');
                    if(Objects.equals(m_humiState, "1")){
                        Log.w("Huy", "autoHumiOn");
                        autoHumi = true;
                        int valueHumi = Integer.parseInt(message.toString());
                        int maxValueHumi = Integer.parseInt(m_maxHumi);
                        if (valueHumi >= maxValueHumi){
                            air.setChecked(true);
                        }else {
                            air.setChecked(false);
                        }
                    }

                }else if(topic.contains("livingroom-lightbutton")){
                    Log.d("Huy", topic + "light");
                    if(message.toString().equals("1")){
                        light_text.setText("ON");
                        light.setChecked(true);
                    }else{
                        light_text.setText("OFF");
                        light.setChecked(false);
                    }
                }else if(topic.contains("fan-button")) {
                    Log.d("Huy", topic + "fan");
                    if (message.toString().equals("1")) {
                        fan_text.setText("ON");
                        fan.setChecked(true);
                    } else {
                        fan_text.setText("OFF");
                        fan.setChecked(false);
                    }
                }else if(topic.contains("air")) {
                    Log.d("Huy", topic + "air");
                    if (message.toString().equals("1")) {
                        air_text.setText("ON");
                        air.setChecked(true);
                    } else {
                        air_text.setText("OFF");
                        air.setChecked(false);
                    }
                }else  if(topic.contains("door-button")){
                    Log.d("Huy", topic + "door");
                    if (message.toString().equals("1")) {
                        door_text.setText("ON");
                        door_lock.setChecked(true);
                    } else {
                        door_text.setText("OFF");
                        door_lock.setChecked(false);
                    }
                }

            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
    public void sendDataMQTT(String topic, String value){
        MqttMessage msg = new MqttMessage();
        msg.setId(1234);
        msg.setQos(0);
        msg.setRetained(false);

        byte[] b = value.getBytes(Charset.forName("UTF-8"));
        msg.setPayload(b);

        try {
            mqttHelper.mqttAndroidClient.publish(topic, msg);
        } catch (MqttException e){

        }
    }

}
