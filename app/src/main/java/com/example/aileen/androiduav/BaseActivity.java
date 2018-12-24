package com.example.aileen.androiduav;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

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
    protected String testVal;
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

    /**
     * 设置旋转动画
     * @return ObjectAnimator
     */
    protected ObjectAnimator setAnimator(ImageView imageView) {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(-1);
        LinearInterpolator lin = new LinearInterpolator();
        animator.setInterpolator(lin);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //setAnimator(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(5000);
        return animator;
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
