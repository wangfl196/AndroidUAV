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
    public static final int  UAV_STABLE_SIGN_OPEN = 1; //悬停开启标识
    public static final int  UAV_STABLE_SIGN_CLOSE = 0; //悬停关闭标识
    public static final String BLUETOOTHVALUE = "00:0E:0E:0E:31:oB";


    /************************************

     ************************************/
    private int ActionSign; //无人机启动标识
    private int UavStable = UAV_STABLE_SIGN_CLOSE; //无人机悬停标识


    public void setUavStable(int uavStable) {
        UavStable = uavStable;
    }

    public int getUavStable() {
        return UavStable;
    }

    public void setActionSign(int actionSign) {
        ActionSign = actionSign;
    }

    public int getActionSign() {
        return ActionSign;
    }
}
