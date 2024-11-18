package com.example.comp7031project;

import android.content.Context;
import android.provider.Settings;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.rules.ExternalResource;

public class DisableAnimationsRule extends ExternalResource {

    private float prevWindowAnimationScale;
    private float prevTransitionAnimationScale;
    private float prevAnimatorDurationScale;

    @Override
    protected void before() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        try {
            // Save current animation settings
            prevWindowAnimationScale = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.WINDOW_ANIMATION_SCALE);
            prevTransitionAnimationScale = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.TRANSITION_ANIMATION_SCALE);
            prevAnimatorDurationScale = Settings.Global.getFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE);

            // Set all animations to off (0)
            Settings.Global.putFloat(context.getContentResolver(), Settings.Global.WINDOW_ANIMATION_SCALE, 0);
            Settings.Global.putFloat(context.getContentResolver(), Settings.Global.TRANSITION_ANIMATION_SCALE, 0);
            Settings.Global.putFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, 0);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void after() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        // Restore previous animation settings
        Settings.Global.putFloat(context.getContentResolver(), Settings.Global.WINDOW_ANIMATION_SCALE, prevWindowAnimationScale);
        Settings.Global.putFloat(context.getContentResolver(), Settings.Global.TRANSITION_ANIMATION_SCALE, prevTransitionAnimationScale);
        Settings.Global.putFloat(context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, prevAnimatorDurationScale);
    }
}
