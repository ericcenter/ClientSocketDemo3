package ccc.ClientSocketDemo3;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import java.util.Date;

public class Service extends Service {

	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startID) {
		handler.postDelayed(showTime, 1000);
		super.onStart(intent, startID);
	}
	
	@Override
	public void onDestory(){
		handler.removeCallbacks(showTime);
		super.onDestroy();
	}
	
	private Runnable showTime = new Runnable(){
		public void run(){
			try {
                // 以內定(本機電腦端)IP為Server端
                serverIp = InetAddress.getByName("10.0.2.2");
                int serverPort = 5050;
                clientSocket = new Socket(serverIp, serverPort);
 
                // 取得網路輸入串流
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
 
                // 當連線後
                while (clientSocket.isConnected()) {
                    // 取得網路訊息
                    tmp = br.readLine();
 
                    // 如果不是空訊息則
                    if(tmp!=null)
                        // 顯示新的訊息
                        mHandler.post(updateText);
                }
 
            } catch (IOException e) {
 
            }
		}
	}

}
