package com.nanicky.devteam.findcat.utils;


import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import srsdt1.findacat.R;


public class AnimationMotionUtils {
    private static final String TAG = "AnimMotUtils";

    public interface AnimationStageActionListener {
        void afterAnimAction();

        void betweenAnimAction();
    }

    public static void changeLevelDivided(final Context context, final ImageView imageView, final AnimationStageActionListener animationStageActionListener) {
        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.view_fade_out);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                animationStageActionListener.betweenAnimAction();
                Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.view_fade_in);
                loadAnimation.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        animationStageActionListener.afterAnimAction();
                    }
                });
                imageView.startAnimation(loadAnimation);
            }
        });
        imageView.startAnimation(loadAnimation);
    }

    public static void changeLevelCombo(Context context, final ImageView imageView, final ImageView imageView2, final AnimationStageActionListener animationStageActionListener) {
        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.view_fade_out);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                imageView.setVisibility(4);
            }
        });
        Animation loadAnimation2 = AnimationUtils.loadAnimation(context, R.anim.view_fade_in);
        loadAnimation2.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                imageView2.setVisibility(0);
                animationStageActionListener.afterAnimAction();
            }
        });
        imageView.startAnimation(loadAnimation);
        imageView2.startAnimation(loadAnimation2);
    }

    public static void removeMarkWithAnimation(final ImageView imageView, final RelativeLayout relativeLayout, Context context) {
        Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.mark_fade_out);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                try {
                    imageView.setVisibility(8);
                    relativeLayout.postDelayed(new Runnable() {
                        public void run() {
                            relativeLayout.removeView(imageView);
                        }
                    }, 100);
                } catch (Exception e) {
                    Log.e(AnimationMotionUtils.TAG, "Произошла ошибка при удалении метки: " + e.getLocalizedMessage());
                }
            }
        });
        imageView.startAnimation(loadAnimation);
    }
}