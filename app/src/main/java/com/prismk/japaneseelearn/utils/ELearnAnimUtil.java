package com.prismk.japaneseelearn.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;

public class ELearnAnimUtil {

    /*上下翻转控件动画*/
    public static void rotateYAnim(final View oldView, final View newView) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationX", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationX", -90, 0);
        animator2.setInterpolator(new OvershootInterpolator(2.0f));
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(170).start();
                newView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(170).start();
    }

    /*左右翻转控件动画*/
    public static void rotateXAnim(final View oldView, final View newView) {

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90, 0);
        animator2.setInterpolator(new OvershootInterpolator(2.0f));
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(170).start();
                newView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.setDuration(170).start();
    }

    public static void setAlphaToVisibleAnim(final View view, final long time) {

        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setVisibility(View.VISIBLE);
                animator.removeAllListeners();
            }

        });
        animator.setDuration(time);
        animator.start();
    }

    public static void setAlphaToGoneAnim(final View view, final long time) {

        final ObjectAnimator animator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                animator.removeAllListeners();
            }
        });
        animator.setDuration(time);
        animator.start();
    }

    /*点击按钮动画*/
    public static ScaleAnimation clickButtonAnim() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(100);
        return scaleAnimation;
    }
}
