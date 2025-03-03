[![](https://jitpack.io/v/ahmedaa6122/Media3DoubleTap.svg)](https://jitpack.io/#ahmedaa6122/Media3DoubleTap)

# Media3DoubleTap
its update of [DoubleTapPlayerView](https://github.com/vkay94/DoubleTapPlayerView) Library for Double tap to fast forward and rewand like YouTube for media3 exoplayer

1.Add the dependency 
```
implementation("com.github.ahmedaa6122:Media3DoubleTap:3.3.2025")
```
# Getting started

To start using the Library, the easiest way is to include it directly 
into your XML layout by replacing `Media3 PlayerView` to `DoubleTapPlayerView` and `YouTubeOverlay` two layout must put together inside `FrameLayout` or same it.

```xml
<FrameLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent" >
    
    <com.ahmedaa612.media3doubletap.dtpv.DoubleTapPlayerView
        android:id="@+id/playerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:dtpv_controller="@+id/youtube_overlay" />

    <com.ahmedaa612.media3doubletap.dtpv.youtube.YouTubeOverlay
        android:id="@+id/youtube_overlay"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:visibility="invisible"
        app:yt_playerView="@+id/playerView" />
</FrameLayout>
```

Then, inside your `Activity` or `Fragment`, you can specify which preparations should be done
before and after the animation, but don't forget to visible `Youtube Overlay` when animation stars and set invisible after animation end like that's:
```
youtubed.performListener(
                new YouTubeOverlay.PerformListener() {
                    @Override
                    public void onAnimationStart() {
                        youtubed.setAlpha(1.0f);
                        youtubed.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd() {
                        youtubed.animate()
                                .alpha(0.0f)
                                .setDuration(300)
                                .setListener(
                                        new AnimatorListenerAdapter() {

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                youtubed.setVisibility(View.GONE);
                                                youtubed.setAlpha(1.0f);
                                            }
                                        });
                    }
                });
```
            

and by doing this steps your implement double tab feature to your `Player` .

___________________________________________________

If you like the project, don't forget to support me ❣ 

[!["Buy Me A Coffee"](https://www.buymeacoffee.com/assets/img/custom_images/orange_img.png)](https://www.buymeacoffee.com/aa6121627o)

___________________________________________________

