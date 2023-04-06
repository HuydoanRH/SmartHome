//package com.yourdomain.company.aimyhome;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.os.Handler;
//import android.os.Message;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.view.animation.TranslateAnimation;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.suke.widget.SwitchButton;
//import com.yourdomain.company.aimyhome.Fragment.FanSpeed;
//import com.yourdomain.company.aimyhome.databinding.ActivityMainBinding;
//
//import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
//import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Set;
//import java.util.UUID;
//
//import jp.hamcheesedev.outlinedtextview.CompatOutlinedTextView;
//import me.itangqi.waveloadingview.WaveLoadingView;
//
//
//public class MainActivity2 extends AppCompatActivity {
//
//    //    int a = 0;
////    ListView listView;
////    int int_value = 0;
////    String rev_value = null;
//////    BluetoothAdapter bluetoothAdapter;
//////    BluetoothDevice[] btArray;
////    //    SendReceive sendReceive;
////    View menuView;
////    WaveLoadingView mWaveLoadingView;
////    LinearLayout connect, fail, connecting;
////    boolean isUp;
////    static final int STATE_LISTENING = 1;
////    static final int STATE_CONNECTING = 2;
////    static final int STATE_CONNECTED = 3;
////    static final int STATE_CONNECTION_FAILED = 4;
////    static final int STATE_MESSAGE_RECEIVED = 5;
////    int REQUEST_ENABLE_BLUETOOTH = 1;
////    private static final String APP_NAME = "MyHome";
////    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
////    String action;
////    int check_connect = 0;
////    int back_press = 0;
////    LinearLayout notify_on, notify_off, power_on, power_off, ai_off, ai_on, sensor_on, sensor_off;
//    TextView temp, humidity;
//    //    String fan_value = "0";
//////    CompatOutlinedTextView fan_speed;
//////    com.suke.widget.SwitchButton light, fan, wifi, tv, door_lock, air;
//////    int switch_control_value = 0;
//////    TextView recv, date_view, day_view;
//////    TextView light_text, tv_text, wifi_text, air_text, door_text;
//////    int switch_off_value = 0;
//////    int fan_change_value = 0;
//////    int light_tt = 0, wifi_tt = 0, tv_tt = 0, air_tt = 0, door_tt = 0, fan_tt = 0;
////
////    //
//    MQTTHelper mqttHelper;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
////        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
////
////        IntentFilter filter = new IntentFilter();
////        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
////        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
////        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
////        this.registerReceiver(broadcastReceiver, filter);
//
////        mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
//
////        connect = (LinearLayout) findViewById(R.id.connect);
////        fail = (LinearLayout) findViewById(R.id.fail);
////        connecting = (LinearLayout) findViewById(R.id.connecting);
//
////        notify_off = (LinearLayout) findViewById(R.id.notification_off);
////        notify_on = (LinearLayout) findViewById(R.id.notification_on);
////        power_on = (LinearLayout) findViewById(R.id.power_on);
////        power_off = (LinearLayout) findViewById(R.id.power_off);
////        ai_off = (LinearLayout) findViewById(R.id.ai_off);
////        ai_on = (LinearLayout) findViewById(R.id.ai_on);
////        sensor_off = (LinearLayout) findViewById(R.id.sensor_off);
////        sensor_on = (LinearLayout) findViewById(R.id.sensor_on);
//
//        temp = (TextView) findViewById(R.id.temp);
////        fan_speed = (CompatOutlinedTextView) findViewById(R.id.fan_speed);
////        fahren = (TextView) findViewById(R.id.fahren);
//        humidity = (TextView) findViewById(R.id.humidity);
////        recv = (TextView) findViewById(R.id.recv);
////        date_view = (TextView) findViewById(R.id.mounth_value);
////        day_view = (TextView) findViewById(R.id.day_value);
////
////        light_text = (TextView) findViewById(R.id.light_text);
////        wifi_text = (TextView) findViewById(R.id.wifi_text);
////        tv_text = (TextView) findViewById(R.id.tv_text);
////        air_text = (TextView) findViewById(R.id.air_text);
////        door_text = (TextView) findViewById(R.id.door_text);
////
////        light = (com.suke.widget.SwitchButton) findViewById(R.id.switch_light);
////        fan = (com.suke.widget.SwitchButton) findViewById(R.id.switch_fan);
////        wifi = (com.suke.widget.SwitchButton) findViewById(R.id.switch_wifi);
////        tv = (com.suke.widget.SwitchButton) findViewById(R.id.switch_tv);
////        door_lock = (com.suke.widget.SwitchButton) findViewById(R.id.switch_door);
////        air = (com.suke.widget.SwitchButton) findViewById(R.id.switch_air);
//
//        startMQTT();
//
//    }
//
//    public void startMQTT(){
//        mqttHelper = new MQTTHelper(this);
//        Log.w("connect", "not ok");
//        mqttHelper.setCallback(new MqttCallbackExtended() {
//            @Override
//            public void connectComplete(boolean reconnect, String serverURI) {
//
//            }
//
//            @Override
//            public void connectionLost(Throwable cause) {
//
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) throws Exception {
//                Log.d("TEST", topic + "***" + message.toString());
//                if(topic.contains("temperature")){
//                    temp.setText(message.toString());
//                }else if(topic.contains("humidity")){
//                    humidity.setText(message.toString() + '%');
//                }
//
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken token) {
//
//            }
//        });
//    }
//
//}
//
//
//
