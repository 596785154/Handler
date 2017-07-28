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
				// 接收子线程的消息
				System.out.println("接收子线程的消息");
				System.out.println(msg);
				info.setText("你好");
			}
		};
		new ChildThread().start();
		msgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mChildHandler != null) {
					// 发送消息给子线程
					System.out.println("发送消息给子线程");
					Message childMsg = mChildHandler.obtainMessage();
					childMsg.obj = mMainHandler.getLooper().getThread()
							.getName()
							+ "啊啊啊啊";
					mChildHandler.sendMessage(childMsg);
					
				}
			}
		});
	}
	class ChildThread extends Thread {
		public void run() {
			this.setName("ChildThread");
			System.out.println("初始化队列");
			// 初始化消息循环队列，需要在Handler创建之前
			Looper.prepare();
			mChildHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					System.out.println("接收来自主线程的消息");
					System.out.println(msg+"1号");
					try {
						// 在子线程中可以做一些耗时的工作
						sleep(100);
						System.out.println("子线程运行，发送消息给主线程");
						System.out.println(msg+"2号");
						Message toMain = mMainHandler.obtainMessage();
						toMain.obj = this.getLooper().getThread().getName()
								+ "的消息";
						mMainHandler.sendMessage(toMain);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			System.out.println("启动子线程消息循环队列");
			// 启动子线程消息循环队列
			Looper.loop();
		}
	}
}
