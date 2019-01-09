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
import android.widget.SeekBar;
import android.widget.TextView;

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
    private ImageView imageViewDirection_right_left; //左旋
    private ImageView imageViewDirection_right_right; //右旋
    private ImageView imageViewReplace_btn; // 摇杆切换
    private ImageView imageViewAircraft_take_off; // 起飞图标
    private ImageView imageViewAircraft_land; // 降落图标
    private ImageView imageViewRise; //升起
    private ImageView imageViewLand; //降落
    private ImageView UavStable; //悬停
    private ImageView Connect; //wifi
    private TextView direction_val; //俯仰值
    private TextView throttle_val; //油门值
    private TextView hengg_val; //横滚值
    private TextView hangx_val; //航向值
    private SeekBar directionSeekBar; //滚动条
    private RockerView rockerView1;
    private RockerView rockerView2;
    private RelativeLayout direction_1;
    private RelativeLayout direction_2;
    private setDirection setDirection;
    private UAVApplication UAV; //application
    private Thread ConnectThread; //连接蓝牙线程
//    private byte[] data = new byte[34];
    private OutputStream out;

    public int data3_4;
    public int data5_6;
    public int data7_8;
    public int data9_10;
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
                i.putExtra("ActionSign", UAV.getActionSign());
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
        RockerView rokerLeft = (RockerView) findViewById(R.id.rocker_1);
        RockerView rokerRight = (RockerView) findViewById(R.id.rocker_2);
        //实时监测摇动方向
        rokerLeft.setOnShakeListener(DirectionMode.DIRECTION_8, new OnShakeListener() {
            //开始摇动时要执行的代码写在本方法里
            @Override
            public void onStart() {

            }
            //结束摇动时要执行的代码写在本方法里
            @Override
            public void onFinish() {
//                Toast.makeText(MainControllerActivity.this, "已复位", Toast.LENGTH_SHORT).show();
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
                    Log.d("方向", "右") ;
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

        rokerRight.setOnShakeListener(DirectionMode.DIRECTION_8, new OnShakeListener() {
            @Override
            public void onStart() {

            }

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

            @Override
            public void onFinish() {

            }
        });
    }

    /**
     * 初始化
     */
    private void initController ()  {
        //初始化data数据
        setData();
        //初始化数据
        UAV.setActionSign(UAV.ACTION_SIGN_STOP);  //设置无人机启动标识
        direction_val = findViewById(R.id.direction_val);  //油门值
        throttle_val = findViewById(R.id.throttle_val);   //方向
        hengg_val = findViewById(R.id.hengg_val);   //横滚
        hangx_val = findViewById(R.id.hangx_val);   //航向
        directionSeekBar = findViewById(R.id.throttle_bar); //滚动条
        Connect = findViewById(R.id.connect);
        //初始化显示
        hangx_val.setText("" + data5_6); // 航向
        hengg_val.setText("" + data7_8); // 横滚
        direction_val.setText("" + data9_10); // 俯仰

        imageViewReplace_btn = findViewById(R.id.replace_btn);
        directionSeekBar.setMax(1000);
        directionSeekBar.setProgress(data3_4);
        //油门值视图初始化
        throttle_val.setText("" + data3_4);
        UAV.setActionSign(UAV.ACTION_SIGN_STOP); //设置无人机启动标识

        setSeekBar();
        //获取方向键
        imageViewDirectionUp      = findViewById(R.id.direction_up);
        imageViewDirectionDown    = findViewById(R.id.direction_down);
        imageViewDirectionLeft    = findViewById(R.id.direction_left);
        imageViewDirectionRight   = findViewById(R.id.direction_right);
        imageViewDirection_right_left = findViewById(R.id.direction_right_left);
        imageViewDirection_right_right = findViewById(R.id.direction_right_right);
        setDirection = new setDirection();

        //监听方向键按下、抬起
        imageViewDirectionUp.setOnTouchListener(setDirection);
        imageViewDirectionDown.setOnTouchListener(setDirection);
        imageViewDirectionLeft.setOnTouchListener(setDirection);
        imageViewDirectionRight.setOnTouchListener(setDirection);
        imageViewDirection_right_left.setOnTouchListener(setDirection);
        imageViewDirection_right_right.setOnTouchListener(setDirection);

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
                    imageViewReplace_btn.setImageDrawable(getResources().getDrawable(R.mipmap.replace_02));
                } else {
                    rockerView1.setVisibility(View.VISIBLE);
                    rockerView2.setVisibility(View.VISIBLE);
                    direction_1.setVisibility(View.GONE);
                    direction_2.setVisibility(View.GONE);
                    imageViewReplace_btn.setImageDrawable(getResources().getDrawable(R.mipmap.replace_01));
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
                    //线程结束之后不能再次start 必须重新开启新的线程;
                    Thread SendThread = new Thread(new SendThread());
                    SendThread.start();
                }
            }
        });

        /**** 连 接 蓝 牙****/
        //初始化线程
        ConnectThread = new Thread(new ConnetThread());
        Connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UAV.getActionSign() == 0) {
//                    Connect.setImageDrawable(getResources().getDrawable(R.mipmap.open));
                    UAV.setActionSign(UAV.ACTION_SIGN_START);
                    ConnectThread.start(); //开启线程
                } else if(UAV.getActionSign() == 1) {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
            }
        });

        findViewById(R.id.fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    out.write(UAV.data);                        //发送数据
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.throttle_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data3_4 += 100;
                if (data3_4 > 1000) {
                    data3_4 = 1000;
                }
                UAV.data[3] = (byte) (data3_4>>8);
                UAV.data[4] = (byte) (data3_4&0xff);
                directionSeekBar.setProgress(data3_4);
                throttle_val.setText("" + data3_4);
            }
        });

        findViewById(R.id.throttle_reduce).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data3_4 -= 50;
                if (data3_4 < 0) {
                    data3_4 = 0;
                }
                UAV.data[3] = (byte) (data3_4>>8);
                UAV.data[4] = (byte) ((byte) data3_4&0xff);
                directionSeekBar.setProgress(data3_4);
                throttle_val.setText("" + data3_4);
            }
        });

        /***********************************************
                          控制属性初始化
         ***********************************************/
        //无人机开启状态
    }

    private void setSeekBar() {
        directionSeekBar.setProgress(data3_4);
        directionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                data3_4 = i;
                UAV.data[3] = (byte) (data3_4>>8);
                UAV.data[4] = (byte) (data3_4&0xff);
                throttle_val.setText("" + data3_4);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                        //图标
                        imageViewDirectionUp.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirectionUp.setRotation(180);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //俯仰值
                        data9_10 += 10;
                        if (data9_10 > 3000) {
                            data9_10 = 3000;
                        }
                        UAV.data[9] = (byte) (data9_10>>8);
                        UAV.data[10] = (byte) (data9_10&0xff);
                        direction_val.setText("" + data9_10);
                        //图标
                        imageViewDirectionUp.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirectionUp.setRotation(180);
                    }
                    break;
                //下
                case  R.id.direction_down:

                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirectionDown.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //俯仰值
                        data9_10 -= 10;
                        if (data9_10 < 0) {
                            data9_10 = 0;
                        }
                        UAV.data[9] = (byte) (data9_10>>8);
                        UAV.data[10] = (byte) (data9_10&0xff);
                        direction_val.setText("" + data9_10);
                        //图标
                        imageViewDirectionDown.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                    }
                    break;
                //左
                case  R.id.direction_left:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirectionLeft.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirectionLeft.setRotation(90);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //横滚值
                        data7_8 += 10;
                        if (data7_8 > 3000) {
                            data7_8 = 3000;
                        }
                        UAV.data[7] = (byte) (data7_8>>8);
                        UAV.data[8] = (byte) (data7_8&0xff);
                        hengg_val.setText("" + data7_8);
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
                        //横滚值
                        data7_8 -= 10;
                        if (data7_8 < 0) {
                            data7_8 = 0;
                        }
                        UAV.data[7] = (byte) (data7_8>>8);
                        UAV.data[8] = (byte) (data7_8&0xff);
                        hengg_val.setText("" + data7_8);
                        imageViewDirectionRight.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirectionRight.setRotation(270);
                    }
                    break;
                case R.id.direction_right_left :
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirection_right_left.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirection_right_left.setRotation(90);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //航向值
                        data5_6 += 10;
                        if (data5_6 > 3000) {
                            data5_6 = 3000;
                        }
                        UAV.data[5] = (byte) (data5_6>>8);
                        UAV.data[6] = (byte) (data5_6&0xff);
                        hangx_val.setText("" + data5_6);
                        imageViewDirection_right_left.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirection_right_left.setRotation(90);
                    }
                    break;
                case R.id.direction_right_right:
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        imageViewDirection_right_right.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_15));
                        imageViewDirection_right_right.setRotation(270);
                    } else if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        //航向值
                        data5_6 -= 10;
                        if (data5_6 < 0) {
                            data5_6 = 0;
                        }
                        UAV.data[5] = (byte) (data5_6>>8);
                        UAV.data[6] = (byte) (data5_6&0xff);
                        hangx_val.setText("" + data5_6);
                        imageViewDirection_right_right.setImageDrawable(getResources().getDrawable(R.mipmap.direction_icon_light));
                        imageViewDirection_right_right.setRotation(270);
                    }
                    break;
                default:

            }
            return true;
        }
    }


    /**
     * 蓝牙连接Socket
     */
    private class ConnetThread implements Runnable {
        @Override
        public void run() {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();                   //获取适配器
            BluetoothDevice device = adapter.getRemoteDevice(UAVApplication.BLUETOOTHVALUE);  //获取蓝牙设备
            UUID uuid = UUID.fromString(UAVApplication.UAVUUID);                //获取UUID（可以不写在线程里）
            try {
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);      //连接服务端
                socket.connect();

                out = socket.getOutputStream();    // 获取输出流
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试
     */
    private class SendThread implements Runnable {
        @Override
        public void run() {
            while (UAV.getUavStable()) {
                try {
                    out.write(UAV.data);//发送数据
                    Thread.sleep(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }




    private void setData() {

        //固定值
        UAV.data[0] = (byte) 0xAA;
        UAV.data[1] = (byte) 0xC0;
        UAV.data[2] = (byte) 0x1C;
        //油门
        UAV.data[3] = (byte) (data3_4>>8);
        UAV.data[4] = (byte) (data3_4&0xff);
        //航向
        UAV.data[5] = (byte) (data5_6>>8);
        UAV.data[6] = (byte) (data5_6&0xff);
        //横滚 控制左右
        UAV.data[7] = (byte) (data7_8>>8);
        UAV.data[8] = (byte) (data7_8&0xff);
        //俯仰 控制前后
        UAV.data[9] = (byte) (data9_10>>8);
        UAV.data[10] = (byte) (data9_10&0xff);

//        UAV.data[11] = (byte) 0x00;
//        UAV.data[12] = (byte) 0x00;
//        UAV.data[13] = (byte) 0x00;
//        UAV.data[14] = (byte) 0x00;
//        UAV.data[15] = (byte) 0x00;
//        UAV.data[16] = (byte) 0x00;
//        UAV.data[17] = (byte) 0x00;
//        UAV.data[18] = (byte) 0x00;
//        UAV.data[19] = (byte) 0x00;
//        UAV.data[20] = (byte) 0x00;
//        UAV.data[21] = (byte) 0x00;
//        UAV.data[22] = (byte) 0x00;
//        UAV.data[23] = (byte) 0x00;
//        UAV.data[24] = (byte) 0x00;
//        UAV.data[25] = (byte) 0x00;
//        UAV.data[26] = (byte) 0x00;
//        UAV.data[27] = (byte) 0x00;
//        UAV.data[28] = (byte) 0x00;
//        UAV.data[29] = (byte) 0x00;
//        UAV.data[30] = (byte) 0x00;
        //固定值
        UAV.data[31] = (byte) 0x1C;
        UAV.data[32] = (byte) 0x0D;
        UAV.data[33] = (byte) 0x0A;
    }

}
