package com.example.aileen.androiduav;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {
    private int ActionSign;
    private String Bluetooth;
    private ImageView imageViewLogo;
    private UAVApplication uavApplication; //application

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uavApplication = (UAVApplication) this.getApplication();
        imageViewLogo = findViewById(R.id.first_logo);
        //初始化
        initActivity();


        //点击first_logo 跳转操作页面
        findViewById(R.id.first_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MainControllerActivity.class));
            }
        });

        //用户设置跳转
        findViewById(R.id.user_setup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(new Intent(MainActivity.this, UserSetupActivity.class));
                i.putExtra("BluetoothValue", Bluetooth);
                startActivity(i);
            }
        });

        //关于我们跳转
        findViewById(R.id.first_problem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });

        //退出程序
        findViewById(R.id.first_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActionSign == 1) {
                    exitAppliction();
                } else {
                    setUavInitData();
                    AtyContainer.getInstance().finishAllActivity();
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void initActivity() {


        //接收Intent
        try {
            Intent i = getIntent();
            ActionSign = i.getIntExtra("ActionSign", 0); //无人机启动标识
            Bluetooth = i.getStringExtra("BluetoothValue"); //蓝牙地址
        } catch (Exception e) {
            ActionSign = 0;
            Bluetooth = "null";
        }

        if (ActionSign == UAVApplication.ACTION_SIGN_START) {
            //开启动画
            setAnimator(imageViewLogo).start();
        } else if (ActionSign == UAVApplication.ACTION_SIGN_STOP) {
            //关闭动画
            setAnimator(imageViewLogo).cancel();
        }

    }

    @Override
    public void onBackPressed() {
        setUavInitData();
        if (ActionSign == 1) {
            exitAppliction();
        } else {
            setUavInitData();
            AtyContainer.getInstance().finishAllActivity();
        }
    }

    private void setUavInitData() {

    }

    /**
     * 退出程序
     */
    private void exitAppliction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.out_tips))
                .setIcon(R.mipmap.dialog_info)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作
                    }
                })
                .setNegativeButton("坠机", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        setUavInitData();
                        AtyContainer.getInstance().finishAllActivity();
                    }
                });
        builder.create().show();
    }



}


