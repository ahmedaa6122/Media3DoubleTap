package com.ahmedaa612.media3doubletap.dtpv;

public interface PlayerDoubleTapListener {

    /**
     * Called when double tapping starts, after double tap gesture
     *
     * @param posX x tap position on the root view
     * @param posY y tap position on the root view
     */
    default void onDoubleTapStarted(float posX, float posY) { }

    /**
     * Called for each ongoing tap (also single tap) (MotionEvent#ACTION_DOWN)
     * when double tap started and still in double tap mode defined
     * by {@link DoubleTapPlayerView#doubleTapDelay}
     *
     * @param posX x tap position on the root view
     * @param posY y tap position on the root view
     */
    default void onDoubleTapProgressDown(float posX, float posY) { }

    /**
     * Called for each ongoing tap (also single tap) (MotionEvent#ACTION_UP}
     * when double tap started and still in double tap mode defined
     * by {@link DoubleTapPlayerView#doubleTapDelay}
     *
     * @param posX x tap position on the root view
     * @param posY y tap position on the root view
     */
    default void onDoubleTapProgressUp(float posX, float posY) { }

    /**
     * Called when {@link DoubleTapPlayerView#doubleTapDelay} is over
     */
    default void onDoubleTapFinished() { }
}