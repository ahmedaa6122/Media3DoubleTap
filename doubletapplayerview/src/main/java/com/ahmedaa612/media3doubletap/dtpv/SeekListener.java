package com.ahmedaa612.media3doubletap.dtpv;

public interface SeekListener {
    /**
     * Called when video start reached during rewinding
     */
    void onVideoStartReached();

    /**
     * Called when video end reached during forwarding
     */
    void onVideoEndReached();
}
