package com.example.aileen.androiduav;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aileen.androiduav.RockerView.Direction;
import com.example.aileen.androiduav.RockerView.DirectionMode;
import com.example.aileen.androiduav.RockerView.OnShakeListener;

import java.io.OutputStream;
import java.util.UUID;

public class MainControllerActivity extends BaseActivity {
    private int ActionSign;
    private ImageView imageViewDirectionUp; //方向键 上
    private ImageView imageViewDirectionDown; //方向键 下
    private ImageView imageViewDirectionLeft; //方向键 左
    private ImageView imageViewDirectionRight; //方向键 右
    private ImageView imageViewRise; //升起
    private ImageView imageViewLand; //降落
    private ImageView UavStable; //悬停
    private ImageView Connect; //悬停
    private RockerView rockerView1;
    private RockerView rockerView2;
    private RelativeLayout direction_1;
    private RelativeLayout direction_2;
    private setDirection setDirection;
    private UAVApplication UAV; //application
    private Thread ConnectThread; //连接蓝牙线程
    private byte[] data = new byte[34];
    private OutputStream out;
    private TextView tv;


    /******************************************************

     注意：该活动启动模式为singleTask，再次进入活动时不会再执行onCreate！

     ******************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_controller);
        UAV = (UAVApplication) this.getApplication();
        UavStable = findViewById(R.id.uav_stable);
        //初始化
        initController();

//        tv = (TextView) findViewById(R.id.text);
        //摇杆控件
        initrokerview();

        //返回上一页
        findViewById(R.id.controller_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainControllerActivity.this, MainActivity.class);
                i.putExtra("ActionSign", ActionSign);
                startActivity(i);
            }
        });
    }


    /**
     * 该活动启动模式为singleTask，再次进入活动时不会再执行onCreate
     */
    @Override
    protected void onStart() {
        super.onStart();
    }


    public void initrokerview(){
        //找到RockerView控件
        RockerView roker = (RockerView) findViewById(R.id.rocker_1);
        //实时监测摇动方向
        roker.setOnShakeListener(DirectionMode.DIRECTION_8, new OnShakeListener() {
            //开始摇动时要执行的代码写在本方法里
            @Override
            public void onStart() {

            }
            //结束摇动时要执行的代码写在本方法里
            @Override
            public void onFinish() {
                Toast.makeText(MainControllerActivity.this, "已复位", Toast.LENGTH_SHORT).show();
            }
            //摇动方向时要执行的代码写在本方法里
            @Override
            public void direction(Direction direction) {
                if (direction == RockerView.Direction.DIRECTION_CENTER){
                    Log.d("方向", "中心") ;
                }else if (direction == RockerView.Direction.DIRECTION_DOWN){
                    Log.d("方向", "下") ;
                }else if (direction == RockerView.Direction.DIRECTION_LEFT){
                    Log.d("方向", "左") ;
                }else if (direction == RockerView.Direction.DIRECTION_UP){
                    Log.d("方向", "上") ;
                }else if (direction == RockerView.Direction.DIRECTION_RIGHT){
                    Log.d("方向", "") ;
                }else if (direction == RockerView.Direction.DIRECTION_DOWN_LEFT){
                    Log.d("方向", "左下") ;
                }else if (direction == RockerView.Direction.DIRECTION_DOWN_RIGHT){
                    Log.d("方向", "右下") ;
                }else if (direction == RockerView.Direction.DIRECTION_UP_LEFT){
                    Log.d("方向", "左上") ;
                }else if (direction == RockerView.Direction.DIRECTION_UP_RIGHT){
                    Log.d("方向", "右上") ;
                }

            }
        });
    }

    /**
     * 初始化
     */
    private void initController () {
        //初始化data数据

        //初始化数据
        UAV.setActionSign(UAV.ACTION_SIGN_START); //设置无人机启动标识

        //获取方向键
        imageViewDirectionUp      = findViewById(R.id.direction_up);
        imageViewDirectionDown    = findViewById(R.id.direction_down);
        imageViewDirectionLeft    = findViewById(R.id.direction_left);
        imageViewDirectionRight   = findViewById(R.id.direction_right);
        imageViewRise             = findViewById(R.id.rise);
        imageViewLand             = findViewById(R.id.land);
        setDirection = new setDirection();

        //监听方向键按下、抬起
        imageViewDirectionUp.setOnTouchListener(setDirection);
        imageViewDirectionDown.setOnTouchListener(setDirection);
        imageViewDirectionLeft.setOnTouchListener(setDirection);
        imageViewDirectionRight.setOnTouchListener(setDirection);
        imageViewRise.setOnTouchListener(setDirection);
        imageViewLand.setOnTouchListener(setDirection);

        //操作控件切换
        findViewById(R.id.replace_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rockerView1 = findViewById(R.id.rocker_1);
                rockerView2 = findViewById(R.id.rocker_2);
                direction_1 = findViewById(R.id.direction_1);
                direction_2 = findViewById(R.id.direction_2);
                if (rockerView1.getVisibility() == View.VISIBLE) {
                    rockerView1.setVisibility(View.GONE);
                    rockerView2.setVisibility(View.GONE);
                    direction_1.setVisibility(View.VISIBLE);
                    direction_2.setVisibility(View.VISIBLE);
                } else {
                    rockerView1.setVisibility(View.VISIBLE);
                    rockerView2.setVisibility(View.VISIBLE);
                    direction_1.setVisibility(View.GONE);
                    direction_2.setVisibility(View.GONE);
                }

            }
        });

        //
        UavStable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UAV.getUavStable() == UAV.UAV_STABLE_SIGN_OPEN) {
                    UAV.setUavStable(UAV.UAV_STABLE_SIGN_CLOSE);
                    UavStable.setImageDrawable(getResources().getDrawable(R.mipmap.uav_stable_close));
                } else if (UAV.getUavStable() == UAV.UAV_STABLE_SIGN_CLOSE) {
                    UAV.setUavStable(UAV.UAV_STABLE_SIGN_OPEN);
                    UavStable.setImageDrawable(getResources().getDrawable(R.mipmap.uav_stable_open));
                }
            }
        });

        /**** 连 接 蓝 牙****/
        //初始化线程
        ConnectThread = new Thread(new ConnetThread());
        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UAV.getActionSign() == 1) {
                    UAV.setActionSign(UAV.ACTION_SIGN_STOP);

                } else if (UAV.getActionSign() == 0) {
                    UAV.setActionSign(UAV.ACTION_SIGN_START);
                    ConnectThread.start(); //开启线程
                }
            }
        });


        /***********************************************
                          控制属性初始化
         ***********************************************/
        //无人机开启状态
        ActionSign = 1;
    }


    /**
     * 方向键按下、抬起
     */
    class setDirection implements View.OnTouchListener{

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d("message", "" + motionEvent.getAction());
            switch (view.getId()) {
                //上
                case  R.id.direction_up:

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirectionUp.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirectionUp.setRotation(180);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        imageViewDirectionUp.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirectionUp.setRotation(180);
                    }
                    break;
                //下
                case  R.id.direction_down:

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirectionDown.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        imageViewDirectionDown.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                    }
                    break;
                //左
                case  R.id.direction_left:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirectionLeft.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirectionLeft.setRotation(90);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        imageViewDirectionLeft.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirectionLeft.setRotation(90);
                    }
                    break;
                //右
                case  R.id.direction_right:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirectionRight.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirectionRight.setRotation(270);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        imageViewDirectionRight.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirectionRight.setRotation(270);
                    }
                    break;
                //上升
                case  R.id.rise:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewRise.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_03));
                        imageViewRise.setMaxWidth(60);
                        imageViewRise.setMaxHeight(60);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        imageViewRise.setImageDrawable(getResources().getDrawable(R.mipmap.rise_land));
                        imageViewLand.setAdjustViewBounds(true);
                        imageViewLand.setMaxWidth(60);
                        imageViewLand.setMaxHeight(55);
                    }
                    break;
                //下降
                case  R.id.land:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewLand.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_03));
                        imageViewLand.setRotation(180);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        imageViewLand.setImageDrawable(getResources().getDrawable(R.mipmap.rise_land));
                        imageViewRise.setAdjustViewBounds(true);
                        imageViewLand.setRotation(180);
                        imageViewLand.setMaxWidth(60);
                        imageViewLand.setMaxHeight(55);
                    }
                    break;
                default:

            }
            return true;
        }
    }

    private class ConnetThread implements Runnable {
        @Override
        public void run() {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();                   //获取适配器
            BluetoothDevice device = adapter.getRemoteDevice(UAVApplication.BLUETOOTHVALUE);  //获取蓝牙设备
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");                //获取UUID（可以不写在线程里）
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);      //连接服务端
                socket.connect();

                out = socket.getOutputStream();    // 获取输出流
                out.write(data);                        //发送数据
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setData() {
        data[0] = (byte) 0xAA;
        data[1] = (byte) 0xAA;
        data[2] = (byte) 0xAA;
        data[3] = (byte) 0xAA;
        data[4] = (byte) 0xAA;
        data[5] = (byte) 0xAA;
        data[6] = (byte) 0xAA;
        data[7] = (byte) 0xAA;
        data[8] = (byte) 0xAA;
        data[9] = (byte) 0xAA;
        data[10] = (byte) 0xAA;
        data[11] = (byte) 0xAA;
        data[12] = (byte) 0xAA;
        data[13] = (byte) 0xAA;
        data[14] = (byte) 0xAA;
        data[15] = (byte) 0xAA;
        data[16] = (byte) 0xAA;
        data[17] = (byte) 0xAA;
        data[18] = (byte) 0xAA;
        data[19] = (byte) 0xAA;
        data[20] = (byte) 0xAA;
        data[21] = (byte) 0xAA;
        data[22] = (byte) 0xAA;
        data[23] = (byte) 0xAA;
        data[24] = (byte) 0xAA;
        data[25] = (byte) 0xAA;
        data[26] = (byte) 0xAA;
        data[27] = (byte) 0xAA;
        data[28] = (byte) 0xAA;
        data[29] = (byte) 0xAA;
        data[30] = (byte) 0xAA;
        data[31] = (byte) 0xAA;
        data[32] = (byte) 0xAA;
        data[33] = (byte) 0xAA;
    }

}
