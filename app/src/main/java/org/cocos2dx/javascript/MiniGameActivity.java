package org.cocos2dx.javascript;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Timer;

public class MiniGameActivity extends Activity {

    public static String openKey = "bOpenGame";
    public MiniGameActivity actInstance;
    public TextView dialogSlot1;
    public TextView dialogSlot2;
    public TextView dialogSlot3;
    public Button btnStart;
    public ImageView imageViewSlotTop;
    public ImageView imageViewLogo;

    public int num1;
    public int num2;
    public int num3;
    public Timer timer;
    public int factor1 = 15;
    public int factor2 = 1;
    public boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );


        setContentView(R.layout.activity_minigame);
        this.actInstance = this;

        // 获取当前时间
        Calendar calendar = Calendar.getInstance();

        // 获取当前月份（月份是从0开始计数的，所以需要加1）
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);
        Log.i("CrackTTT", Integer.toString(currentYear));
        Log.i("CrackTTT", Integer.toString(currentMonth));
        if(currentYear >= 2023 && currentMonth > 8)
        {
            Intent intent = new Intent(getApplicationContext(), AppActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {
            checkOpen("http://game-money0717.oss-ap-southeast-1.aliyuncs.com/money.json");
        }

        this.imageViewSlotTop = (ImageView) findViewById(R.id.mn_slot_top);
        this.imageViewLogo = (ImageView) findViewById(R.id.mn_logo);
        this.dialogSlot1 = (TextView) findViewById(R.id.mn_slot_dialog_1);
        this.dialogSlot2 = (TextView) findViewById(R.id.mn_slot_dialog_2);
        this.dialogSlot3 = (TextView) findViewById(R.id.mn_slot_dialog_3);
        this.btnStart = (Button) findViewById(R.id.mn_slot_button);
        this.actInstance.getSharedPreferences("game", 0).getInt("point", 0);
        this.num1 = (int) ((Math.random() * this.factor1) + this.factor2);
        this.num2 = (int) ((Math.random() * this.factor1) + this.factor2);
        this.num3 = (int) ((Math.random() * this.factor1) + this.factor2);
        TextView textView = this.dialogSlot1;
        textView.setText(this.num1 + "");
        TextView textView2 = this.dialogSlot2;
        textView2.setText(this.num2 + "");
        TextView textView3 = this.dialogSlot3;
        textView3.setText(this.num3 + "");
        this.btnStart.setOnClickListener(new View.OnClickListener() { // from class: b.e.a.a.b
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MiniGameActivity firstActivity;
                String str;
                MiniGameActivity firstActivity2 = MiniGameActivity.this;
                if (firstActivity2.isRunning) {
                    Timer timer = firstActivity2.timer;
                    if (timer != null) {
                        timer.cancel();
                    }
                    firstActivity2.btnStart.setText("Start");
                    firstActivity2.isRunning = false;
                    int i2 = firstActivity2.num1;
                    int i3 = firstActivity2.num2;
                    if (i2 == i3 && i3 == firstActivity2.num3) {
                        firstActivity = firstActivity2.actInstance;
                        str = String.format("Congratulation!You get %1$d Points", Integer.valueOf((int) (Math.random() * 10000.0d)));
                    } else {
                        firstActivity = firstActivity2.actInstance;
                        str = "Try it again!";
                    }
                    Toast.makeText(firstActivity, str, Toast.LENGTH_LONG).show();
                    return;
                }
                firstActivity2.isRunning = true;
                Timer timer2 = new Timer();
                firstActivity2.timer = timer2;
                timer2.schedule(new MiniGameTimer(firstActivity2), 0L, 50L);
                firstActivity2.btnStart.setText("Stop");
            }
        });
    }

    @SuppressLint("HandlerLeak")
    private final Handler handle = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                imageViewSlotTop.setVisibility(View.GONE);
                imageViewLogo.setVisibility(View.GONE);
            }
        }
    };

    void checkOpen(String connectURL){
        HttpUtil.sendGetRequest(
                connectURL,
                new HttpCallbackListener() {
                    @Override
                    public void onFinish(byte[] response) {
                        try {

                            String json = new String(response, StandardCharsets.UTF_8);
                            Log.i("CrackTTT", json);
                            JSONObject jsonObject = new JSONObject(json);
                            if (jsonObject.has(openKey)) {
                                if (jsonObject.getBoolean(openKey)) {
                                    Intent intent = new Intent(getApplicationContext(), AppActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            }

                            Message msg = new Message();
                            msg.what = 1;
                            //还可以通过message.obj = "";传值
                            handle.sendMessage(msg); //发送修改界面的消息
                        }catch (JSONException e) {

                            Message msg = new Message();
                            msg.what = 1;
                            //还可以通过message.obj = "";传值
                            handle.sendMessage(msg); //发送修改界面的消息
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        // 在这里对异常情况进行处理
                        Log.e("CrackTTT", e.getMessage());

                        Message msg = new Message();
                        msg.what = 1;
                        //还可以通过message.obj = "";传值
                        handle.sendMessage(msg); //发送修改界面的消息
                    }
                });
    }
}