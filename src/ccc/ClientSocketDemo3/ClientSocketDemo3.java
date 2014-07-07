package ccc.ClientSocketDemo3;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import ccc.ClientSocketDemo3.R;

// 
public class ClientSocketDemo3 extends Activity {
	public static Handler mHandler = new Handler();
	TextView TextView01; // �����������r�T��
	EditText EditText01; // ���r����
	EditText EditText02; // ���r����
	String tmp; // ���s���r�T��
	Socket clientSocket; // ������socket

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// �q�����������o���}���j�����������r����
		TextView01 = (TextView) findViewById(R.id.TextView01);
		EditText01 = (EditText) findViewById(R.id.EditText01);
		EditText02 = (EditText) findViewById(R.id.EditText02);

		// �H�s������������������
		Thread t = new Thread(readData);

		// ����������
		t.start();

		// �q�����������o���}���j�����������s
		Button button1 = (Button) findViewById(R.id.Button01);

		// �]�w���s������
		button1.setOnClickListener(new Button.OnClickListener() {
			// �����U���s���������o�H�U�����k
			public void onClick(View v) {
				// �p�G�w�s���h
				if (clientSocket.isConnected()) {

					BufferedWriter bw;

					// service
					Intent intent = new Intent(ClientSocketDemo3.this,
							Service.class);
					startService(intent);

					
					
					try {
						// ���o�������X���y
						bw = new BufferedWriter(new OutputStreamWriter(
								clientSocket.getOutputStream()));

						// �g�J�T��
						bw.write(EditText01.getText() + ":"
								+ EditText02.getText() + "\n");

						// ���Y�o�e
						bw.flush();
					} catch (IOException e) {

					}
					// �N���r�����M��
					EditText02.setText("");
				}
			}
		});

	}

	// �������s�T��
	private Runnable updateText = new Runnable() {
		public void run() {
			// �[�J�s�T��������
			TextView01.append(tmp + "\n");
			sendNotification(tmp.toString());
		}
	};

	// ���o��������
	private Runnable readData = new Runnable() {
		public void run() {
			// server����IP
			InetAddress serverIp;

			try {
				// �H���w(�����q����)IP��Server��
				serverIp = InetAddress.getByName("140.112.94.22");
				int serverPort = 5050;
				clientSocket = new Socket(serverIp, serverPort);

				// ���o�������J���y
				BufferedReader br = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));

				// ���s�u��
				while (clientSocket.isConnected()) {
					// ���o�����T��
					tmp = br.readLine();

					// �p�G���O���T���h
					if (tmp != null)
						// �����s���T��
						mHandler.post(updateText);
				}

			} catch (IOException e) {

			}
		}
	};

	public void sendNotification(String message) {

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, message,
				System.currentTimeMillis());
		PendingIntent contentIndent = PendingIntent.getActivity(
				ClientSocketDemo3.this, 0, new Intent(ClientSocketDemo3.this,
						ClientSocketDemo3.class),
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(ClientSocketDemo3.this,
				"Notification ", message, contentIndent);
		notificationManager.notify(1, notification);

	}
}