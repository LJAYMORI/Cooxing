package com.ljaymori.cooxing.common.utils;

import android.graphics.Bitmap;
import android.net.Uri;

import com.ljaymori.cooxing.common.CooxingApplication;
import com.ljaymori.cooxing.write.camera.ImageUtility;
import com.svenkapudija.imageresizer.ImageResizer;
import com.svenkapudija.imageresizer.operations.ResizeMode;

import java.io.File;

public class ResizeUtils {

    public static synchronized String getResizedPath(String path) {
        Bitmap bitmap = ImageResizer.resize(new File(path), 384, 384, ResizeMode.AUTOMATIC);
        Uri uri = ImageUtility.savePicture(CooxingApplication.getContext(), bitmap);

        return uri.getPath();
    }
}
