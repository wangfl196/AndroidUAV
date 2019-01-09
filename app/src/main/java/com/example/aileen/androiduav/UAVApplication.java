package com.example.aileen.androiduav;

import android.app.Application;

/**
 * +--------------------------------------------------------------------
 * |
 * +--------------------------------------------------------------------
 * |  @ProjectName:    AndroidUAV
 * +--------------------------------------------------------------------
 * |      @Package:    com.example.aileen.androiduav
 * +--------------------------------------------------------------------
 * |    @ClassName:    UAVApplication
 * +--------------------------------------------------------------------
 * |  @Description:    Application
 * +--------------------------------------------------------------------
 * |       @Author:    王富琳 <894633456@qq.com>
 * +--------------------------------------------------------------------
 * |   @CreateDate:    2018/12/24 10:55
 * +--------------------------------------------------------------------
 * |   @UpdateUser:    修改人 < email >
 * +--------------------------------------------------------------------
 * |   @UpdateDate:    修改时间
 * +--------------------------------------------------------------------
 * | @UpdateRemark:    更新说明
 * +--------------------------------------------------------------------
 * |      @Version:    v1.0.0
 * +--------------------------------------------------------------------
 */
public class UAVApplication extends Application {
    /************************************
                  自定义常量
     ************************************/
    public static final int  ACTION_SIGN_START = 1; //启动标识
    public static final int  ACTION_SIGN_STOP = 0; //关闭标识
    public static final Boolean  UAV_STABLE_SIGN_OPEN = true; //悬停开启标识
    public static final Boolean  UAV_STABLE_SIGN_CLOSE = false; //悬停关闭标识
    public static final String UAVUUID = "00001101-0000-1000-8000-00805F9B34FB";
    public static final String BLUETOOTHVALUE = "00:0E:0E:15:84:F0";

    /************************************

     ************************************/
    private boolean UAV_ACTION_UP = false;//上
    private boolean UAV_ACTION_DOWN = false;//下
    private boolean UAV_ACTION_RIGHT = false;//左
    private boolean UAV_ACTION_LEFT = false;//右
    private int ActionSign; //无人机启动标识
    private Boolean UavStable = UAV_STABLE_SIGN_CLOSE; //无人机悬停标识


    public void setUavStable(Boolean uavStable) {
        UavStable = uavStable;
    }

    public Boolean getUavStable() {
        return UavStable;
    }

    public void setActionSign(int actionSign) {
        ActionSign = actionSign;
    }

    public int getActionSign() {
        return ActionSign;
    }

    /**
     *  3、4 油门
     *
     */
}
