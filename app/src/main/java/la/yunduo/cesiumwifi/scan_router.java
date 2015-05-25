package la.yunduo.cesiumwifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;





/**
 * Created by 轶晟 on 2015/1/16.
 */
public class scan_router extends ActionBarActivity {

    private WifiManager wifiManager;
    List<ScanResult> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_router);
        init();

    }

    private void init() {
        final WifiAdmin wifiAdmin = new WifiAdmin(this);
        wifiAdmin.openWifi();
        wifiAdmin.startScan();
        List list = wifiAdmin.getWifiList();
        final ListView listView = (ListView) findViewById(R.id.listView);
        if (list == null) {
            Toast.makeText(this, "wifi未打开！什么鬼？！", Toast.LENGTH_LONG).show();
        } else {
            listView.setAdapter(new MyAdapter(this, list));
        }
        if (wifiAdmin.getSSID() != null) {
            final String SSID = wifiAdmin.getSSID();
            Toast.makeText(this, "已连接" + SSID, Toast.LENGTH_LONG).show();
            setTitle("已连接" + SSID);

        } else {
            Toast.makeText(this, "还没连接Wifi呢。。", Toast.LENGTH_LONG).show();
            setTitle("未连接");
        }
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyAdapter wifi = (MyAdapter) listView.getAdapter();
                ScanResult connect = wifi.getScanResult(position);
                final String cSSID = connect.SSID;
                String cCap = connect.capabilities.substring(1, 4);
                final WifiAdmin mConnect = new WifiAdmin(scan_router.this);

                if (connect.SSID.equals(wifiAdmin.getSSID().substring(1,wifiAdmin.getSSID().length()-1))) {

                    int dcid=wifiAdmin.getNetworkId();
                    wifiAdmin.disconnectWifi(dcid);
                    Toast.makeText(scan_router.this, "已断开"+cSSID, Toast.LENGTH_LONG).show();
                    setTitle("未连接");
                    scan_router.this.finish();

                } else {
                    if (cCap.equals("ESS")) {
                        mConnect.addNetwork(mConnect.CreateWifiInfo(cSSID, "", 1));
                        setTitle("已连接" + cSSID);

                    } else if (cCap.equals("WPA")) {
                        Log.d(cSSID,"id");
                        Log.d(wifiAdmin.getSSID(),"gid");
                        LayoutInflater factory = LayoutInflater.from(scan_router.this);
                        final View textEntryView = factory.inflate(R.layout.wifi_pass, null);
                        new AlertDialog.Builder(scan_router.this)
                                .setTitle("输入密码")
                                .setView(textEntryView)
                                .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final EditText password_edit = (EditText) textEntryView.findViewById(R.id.password_edit);
                                        String password = password_edit.getText().toString().trim();
                                        if (!password.equals("")) {
                                            mConnect.addNetwork(mConnect.CreateWifiInfo(cSSID, password, 3));
                                            Toast.makeText(scan_router.this, "连接至" + cSSID + "  。。。", Toast.LENGTH_LONG).show();
                                            setTitle("已连接" + cSSID);
                                            scan_router.this.finish();
                                        } else {
                                            Toast.makeText(scan_router.this, "请输入密码", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                })


                                .create().show();
                    } else if (cCap.equals("WEP")) {
                        LayoutInflater factory = LayoutInflater.from(scan_router.this);
                        final View textEntryView = factory.inflate(R.layout.wifi_pass, null);
                        new AlertDialog.Builder(scan_router.this)
                                .setTitle("输入密码")
                                .setView(textEntryView)
                                .setPositiveButton("连接", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final EditText password_edit = (EditText) textEntryView.findViewById(R.id.password_edit);
                                        String password = password_edit.getText().toString().trim();
                                        if (!password.equals("")) {
                                            mConnect.addNetwork(mConnect.CreateWifiInfo(cSSID, password, 2));
                                            Toast.makeText(scan_router.this, "连接至" + cSSID + "  。。。", Toast.LENGTH_LONG).show();
                                            setTitle("已连接" + cSSID);
                                            scan_router.this.finish();
                                        } else {
                                            Toast.makeText(scan_router.this, "请输入密码", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                    }
                                })


                                .create().show();
                    } else {
                        Log.e("Unknown", "wifi type error");
                    }
                }


            }

        });

    }

    class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;
        List<ScanResult> list;

        public MyAdapter(Context context, List<ScanResult> list) {
            // TODO Auto-generated constructor stub
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public ScanResult getScanResult(int position) {
            return list.get(position);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = null;
            view = inflater.inflate(R.layout.item_wifi_list, null);
            ScanResult scanResult = list.get(position);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(scanResult.SSID);
            TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
            if (scanResult.capabilities.substring(1, 4).equals("WPA")) {
                signalStrenth.setText("WPA/WPA2SK加密");
            } else if (scanResult.capabilities.substring(1, 4).equals("WEP")) {
                signalStrenth.setText("WEP加密");
            } else if (scanResult.capabilities.substring(1, 4).equals("ESS")) {
                signalStrenth.setText("不加密");
            } else {
                signalStrenth.setText("未知类型");
            }


            // signalStrenth.setText(String.valueOf((scanResult.capabilities.substring(1,4))));
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            //判断信号强度，显示对应的指示图标
            if (Math.abs(scanResult.level) > 100) {
                imageView.setImageResource(R.drawable.stat_sys_wifi_signal_0);
            } else if (Math.abs(scanResult.level) > 80) {
                imageView.setImageResource(R.drawable.stat_sys_wifi_signal_1);
            } else if (Math.abs(scanResult.level) > 70) {
                imageView.setImageResource(R.drawable.stat_sys_wifi_signal_1);
            } else if (Math.abs(scanResult.level) > 60) {
                imageView.setImageResource(R.drawable.stat_sys_wifi_signal_2);
            } else if (Math.abs(scanResult.level) > 50) {
                imageView.setImageResource(R.drawable.stat_sys_wifi_signal_3);
            } else {
                imageView.setImageResource(R.drawable.stat_sys_wifi_signal_4);
            }
            return view;
        }

    }
}





