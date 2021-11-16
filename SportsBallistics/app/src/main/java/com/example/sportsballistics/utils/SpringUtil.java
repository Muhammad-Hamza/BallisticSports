//package com.example.sportsballistics.utils;
//
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.facebook.rebound.SimpleSpringListener;
//import com.facebook.rebound.Spring;
//import com.facebook.rebound.SpringSystem;
//import com.karwatechnologies.karwataxi.animation.SpringyAnimator;
//import com.karwatechnologies.karwataxi.databinding.ListItemRideHistoryBinding;
//import com.karwatechnologies.karwataxi.databinding.NotificationCardBinding;
//import com.karwatechnologies.karwataxi.interfaces.AnimationListener;
//import com.karwatechnologies.karwataxi.ui.listeners.OnSwipeTouchListener;
//
///**
// * Created by Oashraf on 1/4/2018.
// */
//
//public class SpringUtil
//{
//    public static final long BUTTON_DELAY = 750;
//
//    public static void setOnClickListener(final ImageView imageView, final ButtonCallBack callBack)
//    {
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                imageView.setScaleX(mappedValue);
//                imageView.setScaleY(mappedValue);
//
//                if (spring.getCurrentValue() == 0.0)
//                    callBack.onClick();
//            }
//        });
//
//
//        imageView.setOnTouchListener(new View.OnTouchListener()
//        {
//            @Override
//            public boolean onTouch(View v, MotionEvent event)
//            {
//                switch (event.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        mScaleSpring.setEndValue(1);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        mScaleSpring.setEndValue(0);
//                        break;
//                }
//                return true;
//            }
//        });
//    }
//
//    public static void setOnClickListener(final View viewGroup, final ButtonCallBack callBack)
//    {
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                viewGroup.setScaleX(mappedValue);
//                viewGroup.setScaleY(mappedValue);
//
//                if (spring.getCurrentValue() == 0.0)
//                    callBack.onClick();
//            }
//        });
//
//
//        viewGroup.setOnTouchListener((v, event) ->
//        {
//            switch (event.getAction())
//            {
//                case MotionEvent.ACTION_DOWN:
//                    mScaleSpring.setEndValue(1);
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    mScaleSpring.setEndValue(0);
//                    break;
//            }
//            return true;
//        });
//    }
//
//    public static void animateSpringEffect(final View view, Spring mScaleSpring)
//    {
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                view.setScaleX(mappedValue);
//                view.setScaleY(mappedValue);
//            }
//        });
//    }
//
//    public static void setOnClickListener(final View viewGroup, final CallBack callBack)
//    {
//        viewGroup.setClickable(false);
//
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                viewGroup.setScaleX(mappedValue);
//                viewGroup.setScaleY(mappedValue);
//
//                if (spring.getCurrentValue() == 0.0)
//                {
//                    callBack.sprintClicked();
//                    viewGroup.setClickable(true);
//                }
//            }
//        });
//
//
//        viewGroup.setOnTouchListener((v, event) ->
//        {
//            switch (event.getAction())
//            {
//                case MotionEvent.ACTION_DOWN:
//                    mScaleSpring.setEndValue(1);
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    mScaleSpring.setEndValue(0);
//                    break;
//            }
//            return true;
//        });
//    }
//
//    public static void setOnClickListener(final ImageView imageView, final CallBack callBack)
//    {
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                imageView.setScaleX(mappedValue);
//                imageView.setScaleY(mappedValue);
//
//                if (spring.getCurrentValue() == 0.0)
//                    callBack.sprintClicked();
//            }
//        });
//
//
//        imageView.setOnTouchListener((v, event) ->
//        {
//            switch (event.getAction())
//            {
//                case MotionEvent.ACTION_DOWN:
//                    mScaleSpring.setEndValue(1);
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    mScaleSpring.setEndValue(0);
//                    break;
//            }
//            return true;
//        });
//    }
//
//    public static void setOnClickListener(final CallBack callBack, final ImageView... imageViews)
//    {
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//
//                for (ImageView imageView : imageViews)
//                {
//                    imageView.setScaleX(mappedValue);
//                    imageView.setScaleY(mappedValue);
//
//                    if (spring.getCurrentValue() == 0.0)
//                        callBack.sprintClicked();
//                }
//            }
//        });
//
//
//        imageViews[0].setOnTouchListener((v, event) ->
//        {
//            switch (event.getAction())
//            {
//                case MotionEvent.ACTION_DOWN:
//                    mScaleSpring.setEndValue(1);
//                    break;
//                case MotionEvent.ACTION_UP:
//                case MotionEvent.ACTION_CANCEL:
//                    mScaleSpring.setEndValue(0);
//                    break;
//            }
//            return true;
//        });
//    }
//
//    public static void setOnClickListener(final ListItemRideHistoryBinding bookingClickCallback)
//    {
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                bookingClickCallback.getRoot().setScaleX(mappedValue);
//                bookingClickCallback.getRoot().setScaleY(mappedValue);
//            }
//        });
//
//        bookingClickCallback.getRoot().setOnTouchListener(new OnSwipeTouchListener(bookingClickCallback.getRoot().getContext())
//        {
//            @Override
//            public void onClick()
//            {
//                bookingClickCallback.getCallback().onClick(bookingClickCallback.getBooking(), bookingClickCallback.getRoot());
//            }
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent)
//            {
//                switch (motionEvent.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        mScaleSpring.setEndValue(1);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        mScaleSpring.setEndValue(0);
//                        break;
//                }
//
//                return false;
//            }
//        });
//    }
//
//    public static void setOnClickListener(final NotificationCardBinding binding)
//    {
//        final Spring mScaleSpring = SpringSystem.create().createSpring();
//
//        mScaleSpring.addListener(new SimpleSpringListener()
//        {
//            @Override
//            public void onSpringUpdate(Spring spring)
//            {
//                float mappedValue = (float) com.facebook.rebound.SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.75);
//                binding.getRoot().setScaleX(mappedValue);
//                binding.getRoot().setScaleY(mappedValue);
//            }
//        });
//
//        binding.getRoot().setOnTouchListener(new OnSwipeTouchListener(binding.getRoot().getContext())
//        {
//            @Override
//            public void onClick()
//            {
//                binding.getCallback().onClick(binding.getNotification(), binding.getRoot());
//            }
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent)
//            {
//                switch (motionEvent.getAction())
//                {
//                    case MotionEvent.ACTION_DOWN:
//                        mScaleSpring.setEndValue(1);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        mScaleSpring.setEndValue(0);
//                        break;
//                }
//
//                return false;
//            }
//        });
//    }
//
//    public static void setScale(View view, int delay)
//    {
//        setScale(view, SpringyAnimator.SpringAnimationType.SCALEXY, delay);
//    }
//
//    public static void setScale(View view, SpringyAnimator.SpringAnimationType scaleType, int delay)
//    {
//        view.setVisibility(View.VISIBLE);
//        SpringyAnimator helper = new SpringyAnimator(scaleType, 15, 4, 0, 1);
//        helper.setDelay(delay);
//        helper.startSpring(view);
//    }
//
//    public static void springifyView(View view, AnimationListener listener)
//    {
//        /*
//         * animation for recenter icon
//         * */
//        SpringyAnimator iconSpring = new SpringyAnimator(SpringyAnimator.SpringAnimationType.SCALEXY, 4, 2.5, 0, 1);
//        SpringyAnimator iconRotateSpring = new SpringyAnimator(SpringyAnimator.SpringAnimationType.ROTATION, 10, 1.5, 180, 0);
//        iconSpring.setDelay(200);
//        iconRotateSpring.setDelay(200);
//        iconSpring.startSpring(view);
//        iconRotateSpring.startSpring(view);
//        iconRotateSpring.setSpringyListener(new SpringyAnimator.SpringyListener()
//        {
//            @Override
//            public void onSpringStart()
//            {
//
//            }
//
//            @Override
//            public void onSpringStop()
//            {
//                listener.onAnimationEnded();
//            }
//        });
//    }
//
//    public static void animateLogo(View view)
//    {
//        final SpringyAnimator scaleY = new SpringyAnimator(SpringyAnimator.SpringAnimationType.SCALEY, 5, 3, 0.5f, 1);
//        final SpringyAnimator rotate = new SpringyAnimator(SpringyAnimator.SpringAnimationType.ROTATEY, 5, 3, 280, 0);
//        rotate.setDelay(100);
//        scaleY.setDelay(200);
//        rotate.startSpring(view);
//        scaleY.startSpring(view);
//    }
//
//    public static void animatePremiumRide(View view)
//    {
////        final SpringyAnimator scaleY = new SpringyAnimator(SpringyAnimator.SpringAnimationType.SCALEY,5,3,0.5f,1);
//        final SpringyAnimator rotate = new SpringyAnimator(SpringyAnimator.SpringAnimationType.ROTATEY, 5, 3, 280, 0);
//        rotate.setDelay(100);
////        scaleY.setDelay(200);
//        rotate.startSpring(view);
////        scaleY.startSpring(view);
//    }
//
//    public static void springifyView(View view)
//    {
//        SpringyAnimator iconRotateSpring = new SpringyAnimator(SpringyAnimator.SpringAnimationType.ROTATEY, 10, 1.5, 500, 0);
//        iconRotateSpring.startSpring(view);
//    }
//
//    public static void rotate(View view)
//    {
//        final SpringyAnimator rotate = new SpringyAnimator(SpringyAnimator.SpringAnimationType.ROTATEY, 4, 2, 220, 0);
//        rotate.setDelay(200);
//        rotate.startSpring(view);
//    }
//
//    public static void rotateY(View view)
//    {
//        final SpringyAnimator rotate = new SpringyAnimator(SpringyAnimator.SpringAnimationType.ROTATEX, 2, 1, 100, 0);
//        rotate.setDelay(200);
//        rotate.startSpring(view);
//    }
//
//    public interface CallBack
//    {
//        void sprintClicked();
//    }
//}
