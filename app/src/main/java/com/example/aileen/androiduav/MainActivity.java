package com.example.aileen.androiduav;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
    private int ActionSign;
    private String Bluetooth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        //关于我们
        findViewById(R.id.first_problem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InfoActivity.class));
            }
        });

        //退出程序
        findViewById(R.id.first_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActionSign == 1) {
                    exitAppliction();
                } else {
                    AtyContainer.getInstance().finishAllActivity();
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void initActivity() {
        try {
            Intent i = getIntent();
            ActionSign = i.getIntExtra("ActionSign", 0);
            Bluetooth = i.getStringExtra("BluetoothValue");
            Toast.makeText(MainActivity.this, Bluetooth, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            ActionSign = 0;
            Bluetooth = "null";
        }

    }

    @Override
    public void onBackPressed() {
        if (ActionSign == 1) {
            exitAppliction();
        } else {
            AtyContainer.getInstance().finishAllActivity();
        }
    }

    private void exitAppliction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.out_tips))
                .setIcon(R.mipmap.dialog_info)
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        AtyContainer.getInstance().finishAllActivity();
                        //MainActivity.this.finish();
                    }
                });
        builder.create().show();
    }
}


