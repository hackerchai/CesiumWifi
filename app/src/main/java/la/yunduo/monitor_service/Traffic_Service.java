package la.yunduo.monitor_service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import la.yunduo.cesiumwifi.FloatView;


public class Traffic_Service extends Service {

	private Handler mHandler;
    FloatView view;


	public final String DEV_FILE = "/proc/self/net/dev";


	String[] ethData = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0" };
	String[] gprsData = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0" };
	String[] wifiData = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0", "0", "0", "0", "0", "0" };


	String[] data = { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
			"0" };


	final String ETHLINE = "eth0";

	final String WIFILINE = "wlan0";

	final String GPRSLINE = "rmnet0";


	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			refresh();
			mHandler.postDelayed(mRunnable, 3000);
		}
	};


	@Override
	public void onStart(Intent intent, int startId) {
		mHandler.postDelayed(mRunnable, 0);
	};


	@Override
	public void onDestroy() {
		mHandler.removeCallbacks(mRunnable);
        view.img_gone();
		super.onDestroy();
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();


        view = new FloatView(this);
        view.show();
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					// System.out.println((float) (msg.arg1 / 1024) + "kb/s");
					view.tv_show.setText((float) (msg.arg1 / (1024 * 3))
							+ "k/s");

				}
			}
		};

	}


	public void readDev() {
		FileReader fr = null;
		try {
			fr = new FileReader(DEV_FILE);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "系统流量文件读取失败", Toast.LENGTH_SHORT).show();
		}

		BufferedReader bufr = new BufferedReader(fr, 500);
		String line;
		String[] data_temp;
		String[] netData;
		int k;
		int j;


		try {
			while ((line = bufr.readLine()) != null) {
				data_temp = line.trim().split(":");
				if (line.contains(ETHLINE)) {
					netData = data_temp[1].trim().split(" ");
					for (k = 0, j = 0; k < netData.length; k++) {
						if (netData[k].length() > 0) {
							ethData[j] = netData[k];
							j++;
						}
					}
				} else if (line.contains(GPRSLINE)) {
					netData = data_temp[1].trim().split(" ");
					for (k = 0, j = 0; k < netData.length; k++) {
						if (netData[k].length() > 0) {
							gprsData[j] = netData[k];
							j++;
						}
					}
				} else if (line.contains(WIFILINE)) {
					netData = data_temp[1].trim().split(" ");
					for (k = 0, j = 0; k < netData.length; k++) {
						wifiData[j] = netData[k];
						j++;
					}
				}
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void refresh() {


		readDev();


		int[] delta = new int[12];
		delta[0] = Integer.parseInt(ethData[0]) - Integer.parseInt(data[0]);
		delta[1] = Integer.parseInt(ethData[1]) - Integer.parseInt(data[1]);
		delta[2] = Integer.parseInt(ethData[8]) - Integer.parseInt(data[2]);
		delta[3] = Integer.parseInt(ethData[9]) - Integer.parseInt(data[3]);
		delta[4] = Integer.parseInt(gprsData[0]) - Integer.parseInt(data[4]);
		delta[5] = Integer.parseInt(gprsData[1]) - Integer.parseInt(data[5]);
		delta[6] = Integer.parseInt(gprsData[8]) - Integer.parseInt(data[6]);
		delta[7] = Integer.parseInt(gprsData[9]) - Integer.parseInt(data[7]);
		delta[8] = Integer.parseInt(wifiData[0]) - Integer.parseInt(data[8]);
		delta[9] = Integer.parseInt(wifiData[1]) - Integer.parseInt(data[9]);
		delta[10] = Integer.parseInt(wifiData[8]) - Integer.parseInt(data[10]);
		delta[11] = Integer.parseInt(wifiData[9]) - Integer.parseInt(data[11]);

		data[0] = ethData[0];
		data[1] = ethData[1];
		data[2] = ethData[8];
		data[3] = ethData[9];
		data[4] = gprsData[0];
		data[5] = gprsData[1];
		data[6] = gprsData[8];
		data[7] = gprsData[9];
		data[8] = wifiData[0];
		data[9] = wifiData[1];
		data[10] = wifiData[8];
		data[11] = wifiData[9];


		int traffic_data = delta[0] + delta[4] + delta[8];

		Message msg = mHandler.obtainMessage();
		msg.what = 1;
		msg.arg1 = traffic_data;
		mHandler.sendMessage(msg);
	}
}
