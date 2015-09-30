package com.ljaymori.cooxing.write;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.utils.FontUtils;

public class DescAddDialog extends DialogFragment implements View.OnClickListener {

    private Dialog dialog;

    private View viewAlbum;
    private View viewCamera;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = new Dialog(getActivity());
        // 제목 숨기기
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // 전체 화면
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_album_camera_description_write);
        // 배경을 투명하게하기
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        viewAlbum = dialog.findViewById(R.id.view_album_description_write_dialog);
        viewCamera = dialog.findViewById(R.id.view_camera_description_write_dialog);
        viewAlbum.setOnClickListener(this);
        viewCamera.setOnClickListener(this);

        setFonts();

        return dialog;
    }

    private void setFonts() {
        TextView tvNotice = (TextView) dialog.findViewById(R.id.text_notice_dialog_album_camera);
        TextView tvAlbum = (TextView) dialog.findViewById(R.id.text_album_dialog_album_camera);
        TextView tvCamera = (TextView) dialog.findViewById(R.id.text_camera_dialog_album_camera);

        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNotice, tvAlbum, tvCamera);
    }

    @Override
    public void onClick(View v) {
        if(dialogListener != null) {
            switch (v.getId()) {
                case R.id.view_album_description_write_dialog: {
                    dialogListener.onAlbumClick();
                    break;
                }
                case R.id.view_camera_description_write_dialog: {
                    dialogListener.onCameraClick();
                    break;
                }
            }

        }
        dismiss();
    }


    public interface OnDescAddDalogEventListener {
        void onAlbumClick();
        void onCameraClick();
    }
    private OnDescAddDalogEventListener dialogListener;
    public void setOnDescAddDalogEventListener(OnDescAddDalogEventListener listener) {
        dialogListener = listener;
    }
}
