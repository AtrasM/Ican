package avida.ican.Ican.View.Custom;

import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import avida.ican.R;


/**
 * Created by AtrasVida on 10/22/2016
 */
public class Animator {
    private Animation animation;
    private Context context;
    final Handler HANDLER = new Handler();

    public Animator(Context context) {
        this.context = context;
    }


    public void slideInFromRight(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right);
        viewToAnimate.startAnimation(animation);

    }public void slideInFromRightFast(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_right_fast);
        viewToAnimate.startAnimation(animation);
    }


    public void slideOutToRight(final View viewToAnimate) {
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_to_right);
        viewToAnimate.startAnimation(animation);

        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewToAnimate.setVisibility(View.GONE);
            }
        }, 500);
    }

    public void slideOutToLeft(final View viewToAnimate) {
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_to_left);
        viewToAnimate.startAnimation(animation);
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewToAnimate.setVisibility(View.GONE);
            }
        }, 500);
    }

    public void slideOutToDown(final View viewToAnimate) {
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_up);
        viewToAnimate.startAnimation(animation);

        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewToAnimate.setVisibility(View.GONE);
            }
        }, 500);
    }

    public void slideOutToUp(final View viewToAnimate) {
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_out_up);
        viewToAnimate.startAnimation(animation);

        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewToAnimate.setVisibility(View.GONE);
            }
        }, 500);
    }

    public void slideInFromLeft(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_left);
        viewToAnimate.startAnimation(animation);
    }

    public void slideInFromDown(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_down);
        viewToAnimate.startAnimation(animation);
    }
    public void slideInFromDownFast(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_down_fast);
        viewToAnimate.startAnimation(animation);
    }public void slideInFromUpFast(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_up_fast);
        viewToAnimate.startAnimation(animation);
    }


    public void testAnim(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.scale_x_left);
        viewToAnimate.startAnimation(animation);
    }


    public void blink(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.blink);
        viewToAnimate.startAnimation(animation);

    }

    public void bounce(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.bounce);
        viewToAnimate.startAnimation(animation);

    }

    public void fadeIn(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        viewToAnimate.startAnimation(animation);

    }
    public void fadeInSplash(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_splash);
        viewToAnimate.startAnimation(animation);

    }
    public void fadeInFast(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_fast);
        viewToAnimate.startAnimation(animation);

    }

    public void fadeOut(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        viewToAnimate.startAnimation(animation);

    }
    public void fadeOutFast(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.fade_out_fast);
        viewToAnimate.startAnimation(animation);

    }

    public void move(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.move);
        viewToAnimate.startAnimation(animation);

    }

    public void rotate(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        viewToAnimate.startAnimation(animation);

    }

    public void sequential(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.sequential);
        viewToAnimate.startAnimation(animation);

    }

    public void together(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.together);
        viewToAnimate.startAnimation(animation);

    }

    public void zoomIn(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        viewToAnimate.startAnimation(animation);

    }

    public void zoomOut(View viewToAnimate) {
        viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        viewToAnimate.startAnimation(animation);

    }


    public void setGoneVisibility(final View view) {
        final int height = view.getHeight();
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), 100);

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);

            }
        });
        anim.setDuration(1000);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);

                view.setVisibility(View.GONE);
            }

        });
    }


    public void setVisibleFromDownVisibility(final View view) {

        ValueAnimator anim = ValueAnimator.ofInt(-100, -view.getMeasuredHeight());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);

                view.setVisibility(View.VISIBLE);
                slideInFromDown(view);

            }
        });
    }

    public void setVisibleFromTopVisibility(final View view) {

        ValueAnimator anim = ValueAnimator.ofInt(-100, -view.getMeasuredHeight());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.VISIBLE);
                //slideInFromTop(view);

            }
        });
    }
    public void slideInFromTop(final View view) {
    /*    viewToAnimate.setVisibility(View.VISIBLE);
        animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_from_top);
        viewToAnimate.startAnimation(animation);*/
        ValueAnimator anim = ValueAnimator.ofInt(-100, view.getMeasuredHeight());

        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = val;
                view.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        view.setVisibility(View.VISIBLE);
        anim.start();
    }



}
