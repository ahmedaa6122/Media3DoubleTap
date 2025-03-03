package com.ahmedaa612.media3doubletap.dtpv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

import androidx.media3.ui.PlayerView;

import com.ahmedaa612.media3doubletap.dtpv.R;

/** Custom player class for Double-Tapping listening. */
public class DoubleTapPlayerView extends PlayerView {

    private final GestureDetectorCompat gestureDetector;
    private final DoubleTapGestureListener gestureListener;
    private PlayerDoubleTapListener controller;
    private int controllerRef = -1;

    /**
     * If this field is set to {@code true} this view will handle double tapping, otherwise it will
     * handle touches the same way as the original {@link PlayerView} does.
     */
    public boolean isDoubleTapEnabled = true;

    public DoubleTapPlayerView(Context context) {
        this(context, null);
    }

    public DoubleTapPlayerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleTapPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Use the root view from the view hierarchy.
        gestureListener = new DoubleTapGestureListener(getRootView());
        gestureDetector = new GestureDetectorCompat(context, gestureListener);

        if (attrs != null) {
            TypedArray a =
                    context.obtainStyledAttributes(attrs, R.styleable.DoubleTapPlayerView, 0, 0);
            controllerRef = a.getResourceId(R.styleable.DoubleTapPlayerView_dtpv_controller, -1);
            a.recycle();
        }
    }

    /** Returns the double tap delay. */
    public long getDoubleTapDelay() {
        return gestureListener.getDoubleTapDelay();
    }

    /** Sets the double tap delay. */
    public void setDoubleTapDelay(long delay) {
        gestureListener.setDoubleTapDelay(delay);
    }

    /**
     * Sets the {@link PlayerDoubleTapListener} which handles the gesture callbacks.
     *
     * <p>Primarily used for {@code YouTubeOverlay}.
     */
    public DoubleTapPlayerView controller(PlayerDoubleTapListener controller) {
        setController(controller);
        return this;
    }

    /** Returns the current state of double tapping. */
    public boolean isInDoubleTapMode() {
        return gestureListener.isDoubleTapping();
    }

    /** Resets the timeout to keep in double tap mode. */
    public void keepInDoubleTapMode() {
        gestureListener.keepInDoubleTapMode();
    }

    /** Cancels double tap mode instantly. */
    public void cancelInDoubleTapMode() {
        gestureListener.cancelInDoubleTapMode();
    }

    public PlayerDoubleTapListener getController() {
        return gestureListener.getControls();
    }

    public void setController(PlayerDoubleTapListener controller) {
        gestureListener.setControls(controller);
        this.controller = controller;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDoubleTapEnabled) {
            gestureDetector.onTouchEvent(ev);
            // Do not trigger original behavior when double tapping,
            // otherwise the controller would show/hide and cause flickering.
            return true;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // If the PlayerView is set by XML then call the corresponding setter method.
        if (controllerRef != -1) {
            try {
                View view = ((View) getParent()).findViewById(controllerRef);
                if (view instanceof PlayerDoubleTapListener) {
                    controller((PlayerDoubleTapListener) view);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(
                        "DoubleTapPlayerView",
                        "controllerRef is either invalid or not PlayerDoubleTapListener: "
                                + e.getMessage());
            }
        }
    }

    /**
     * Gesture Listener for double tapping.
     *
     * <p>For more information which methods are called in certain situations look for {@link
     * GestureDetector#onTouchEvent(MotionEvent)}.
     */
    private static class DoubleTapGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String TAG = ".DTGListener";
        private static final boolean DEBUG = true;

        private final Handler mHandler = new Handler(Looper.getMainLooper());
        private final View rootView;
        private PlayerDoubleTapListener controls;
        private boolean isDoubleTapping = false;
        private long doubleTapDelay = 650;

        private final Runnable mRunnable =
                new Runnable() {
                    @Override
                    public void run() {
                        if (DEBUG) {
                            Log.d(TAG, "Runnable called");
                        }
                        isDoubleTapping = false;
                        if (controls != null) {
                            controls.onDoubleTapFinished();
                        }
                    }
                };

        public DoubleTapGestureListener(View rootView) {
            this.rootView = rootView;
        }

        public long getDoubleTapDelay() {
            return doubleTapDelay;
        }

        public void setDoubleTapDelay(long delay) {
            this.doubleTapDelay = delay;
        }

        public PlayerDoubleTapListener getControls() {
            return controls;
        }

        public void setControls(PlayerDoubleTapListener controls) {
            this.controls = controls;
        }

        public boolean isDoubleTapping() {
            return isDoubleTapping;
        }

        /** Resets the timeout to keep in double tap mode. */
        public void keepInDoubleTapMode() {
            isDoubleTapping = true;
            mHandler.removeCallbacks(mRunnable);
            mHandler.postDelayed(mRunnable, doubleTapDelay);
        }

        /** Cancels double tap mode instantly. */
        public void cancelInDoubleTapMode() {
            mHandler.removeCallbacks(mRunnable);
            isDoubleTapping = false;
            if (controls != null) {
                controls.onDoubleTapFinished();
            }
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if (isDoubleTapping && controls != null) {
                controls.onDoubleTapProgressDown(e.getX(), e.getY());
                return true;
            }
            return super.onDown(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isDoubleTapping && controls != null) {
                controls.onDoubleTapProgressUp(e.getX(), e.getY());
                return true;
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // Ignore this event if double tapping is still active.
            if (isDoubleTapping) return true;
            if (DEBUG) {
                Log.d(TAG, "onSingleTapConfirmed: isDoubleTap = false");
            }
            return rootView.performClick();
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (DEBUG) {
                Log.d(TAG, "onDoubleTap");
            }
            if (!isDoubleTapping) {
                isDoubleTapping = true;
                keepInDoubleTapMode();
                if (controls != null) {
                    controls.onDoubleTapStarted(e.getX(), e.getY());
                }
            }
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            if (e.getActionMasked() == MotionEvent.ACTION_UP
                    && isDoubleTapping
                    && controls != null) {
                if (DEBUG) {
                    Log.d(TAG, "onDoubleTapEvent, ACTION_UP");
                }
                controls.onDoubleTapProgressUp(e.getX(), e.getY());
                return true;
            }
            return super.onDoubleTapEvent(e);
        }
    }
}
