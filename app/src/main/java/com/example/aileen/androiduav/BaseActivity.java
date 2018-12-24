package com.example.aileen.androiduav;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * +--------------------------------------------------------------------
 * |
 * +--------------------------------------------------------------------
 * |  @ProjectName:    AndroidUAV
 * +--------------------------------------------------------------------
 * |      @Package:    com.example.aileen.androiduav
 * +--------------------------------------------------------------------
 * |    @ClassName:    BaseActivity
 * +--------------------------------------------------------------------
 * |  @Description:    功能描述
 * +--------------------------------------------------------------------
 * |       @Author:    王富琳 <894633456@qq.com>
 * +--------------------------------------------------------------------
 * |   @CreateDate:    2018/12/22 11:03
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
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainer.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AtyContainer.getInstance().removeActivity(this);
    }
}

class AtyContainer {

    private AtyContainer() {
    }

    private static AtyContainer instance = new AtyContainer();
    private static List<Activity> activityStack = new ArrayList<Activity>();

    public static AtyContainer getInstance() {
        return instance;
    }

    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

}
