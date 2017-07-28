package com.example.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Handler mMainHandler, mChildHandler;
	private TextView info;
	private Button msgBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		info = (TextView) findViewById(R.id.info);
		msgBtn = (Button) findViewById(R.id.msgBtn);
		mMainHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// �������̵߳���Ϣ
				System.out.println("�������̵߳���Ϣ");
				System.out.println(msg);
				info.setText("���");
			}
		};
		new ChildThread().start();
		msgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChildHandler != null) {
					// ������Ϣ�����߳�
					System.out.println("������Ϣ�����߳�");
					Message childMsg = mChildHandler.obtainMessage();
					childMsg.obj = mMainHandler.getLooper().getThread()
							.getName()
							+ "��������";
					mChildHandler.sendMessage(childMsg);
					
				}
			}
		});
	}
	class ChildThread extends Thread {
		public void run() {
			this.setName("ChildThread");
			System.out.println("��ʼ������");
			// ��ʼ����Ϣѭ�����У���Ҫ��Handler����֮ǰ
			Looper.prepare();
			mChildHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					System.out.println("�����������̵߳���Ϣ");
					System.out.println(msg+"1��");
					try {
						// �����߳��п�����һЩ��ʱ�Ĺ���
						sleep(100);
						System.out.println("���߳����У�������Ϣ�����߳�");
						System.out.println(msg+"2��");
						Message toMain = mMainHandler.obtainMessage();
						toMain.obj = this.getLooper().getThread().getName()
								+ "����Ϣ";
						mMainHandler.sendMessage(toMain);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			System.out.println("�������߳���Ϣѭ������");
			// �������߳���Ϣѭ������
			Looper.loop();
		}
	}
}
