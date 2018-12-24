package com.example.aileen.androiduav;

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

public class MainControllerActivity extends BaseActivity {
    private int ActionSign;
    private ImageView imageViewDirectionUp; //方向键 上
    private ImageView imageViewDirectionDown; //方向键 下
    private ImageView imageViewDirectionLeft; //方向键 左
    private ImageView imageViewDirectionRight; //方向键 右
    private ImageView imageViewRise; //升起
    private ImageView imageViewLand; //降落
    private ImageView UavStable; //悬停
    private RockerView rockerView1;
    private RockerView rockerView2;
    private RelativeLayout direction_1;
    private RelativeLayout direction_2;
    private setDirection setDirection;
    private UAVApplication UAV; //application
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
//                    tv.setText("中心");
                }else if (direction == RockerView.Direction.DIRECTION_DOWN){
                    Log.d("方向", "下") ;
//                    tv.setText("下");
                }else if (direction == RockerView.Direction.DIRECTION_LEFT){
                    Log.d("方向", "左") ;
//                    tv.setText("左");
                }else if (direction == RockerView.Direction.DIRECTION_UP){
                    Log.d("方向", "上") ;
//                    tv.setText("上");
                }else if (direction == RockerView.Direction.DIRECTION_RIGHT){
                    Log.d("方向", "") ;
//                    tv.setText("右");
                }else if (direction == RockerView.Direction.DIRECTION_DOWN_LEFT){
                    Log.d("方向", "左下") ;
//                    tv.setText("左下");
                }else if (direction == RockerView.Direction.DIRECTION_DOWN_RIGHT){
                    Log.d("方向", "右下") ;
//                    tv.setText("右下");
                }else if (direction == RockerView.Direction.DIRECTION_UP_LEFT){
                    Log.d("方向", "左上") ;
//                    tv.setText("左上");
                }else if (direction == RockerView.Direction.DIRECTION_UP_RIGHT){
                    Log.d("方向", "右上") ;
//                    tv.setText("右上");
                }

            }
        });
    }

    /**
     * 初始化
     */
    private void initController () {
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

}
