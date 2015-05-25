package la.yunduo.cesiumwifi;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import la.yunduo.monitor_service.Traffic_Service;


public class MainActivity extends ActionBarActivity {
    Switch switchWifi = null;
    Button button2;
    Button button1;
    Switch switchW = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switchWifi = (Switch) findViewById(R.id.switch1);
        switchW = (Switch) findViewById(R.id.switch2);




        button2 =(Button)findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent openWifiList= new Intent(MainActivity.this ,scan_router.class);
                                           startActivity(openWifiList);
                                       }
                                   }

        );
        button1 =(Button)findViewById(R.id.button);
        button1.setOnClickListener(new OnClickListener() {
                                       @Override
                                       public void onClick(View v){
                                           addYunduo();

                                       }
                                   }

        );
        checkWifi();
        switchWifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    ToggleWiFi(true);
                    button1.setEnabled(true);
                    button2.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "WiFi已开启!",
                            Toast.LENGTH_LONG).show();
                } else {
                    ToggleWiFi(false);
                    button1.setEnabled(false);
                    button2.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "WiFi已关闭!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        switchW.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(MainActivity.this, Traffic_Service.class);
                    startService(intent);
                    Toast.makeText(getApplicationContext(), "实时网速悬浮窗已开启!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent sintent = new Intent(MainActivity.this, Traffic_Service.class);
                    stopService(sintent);

                    Toast.makeText(getApplicationContext(), "实时网速悬浮窗已关闭!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkWifi() {
        WifiManager checkWifi =(WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if(checkWifi.isWifiEnabled())
        {
            switchWifi.setChecked(true);
            button1.setEnabled(true);
            button2.setEnabled(true);

        }
        else
        {
            switchWifi.setChecked(false);
            button1.setEnabled(false);
            button2.setEnabled(false);

        }

    }
    private void addYunduo()
    {
        WifiAdmin addYunduo =new WifiAdmin(this);
        int crouter=(int)(Math.round(Math.random()*1+1));
       // int crouter =1;
        String router ="Yunduo_0"+String.valueOf(crouter);
        Log.d(router, "name");
        addYunduo.addNetwork(addYunduo.CreateWifiInfo(router, "neychn0502_1402", 3));
        Toast.makeText(MainActivity.this, "连接至" + router + "  。。。", Toast.LENGTH_LONG).show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Intent about = new Intent(MainActivity.this,web_view.class);
            startActivity(about);
        }
        if(id == R.id.abouty){
            Intent abouty = new Intent(MainActivity.this,web_view_y.class);
            startActivity(abouty);
        }


        return super.onOptionsItemSelected(item);
    }
   @Override
   protected void onResume ()
   {
       super.onResume();
       checkWifi();
   }


    public void ToggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }


}
