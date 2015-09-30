package com.ljaymori.cooxing.common.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.ljaymori.cooxing.R;

import java.util.Timer;
import java.util.TimerTask;

public class LoadingDialogFragment extends DialogFragment {

    public static final String DIALOG_LOADING = "dialog_loading";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());

        // Hide title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // Full Screen
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        // Transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set contentView
        dialog.setContentView(R.layout.dialog_loading);

        init(dialog);

        return dialog;
    }

    @Override
    public void dismiss() {
        if (task != null) {
            task.stop();
        }
        super.dismiss();
    }

    private LoadingAnimationTask task;
    private ImageView ivProgress;
    private void init(Dialog d) {
        ivProgress = (ImageView) d.findViewById(R.id.image_progress_loading_dialog);

        task = new LoadingAnimationTask();
        Timer t = new Timer(false);
        t.schedule(task, 100);
    }

    class LoadingAnimationTask extends TimerTask {
        private AnimationDrawable anim;

        @Override
        public void run() {
            if(ivProgress != null) {
                anim = (AnimationDrawable) ivProgress.getBackground();
                anim.start();
            }
        }

        public void stop() {
            if(anim != null) {
                anim.stop();
            }
        }
    }
}
