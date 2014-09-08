package org.videolan.vlc.gui.video;

import android.os.Environment;
import org.videolan.libvlc.LibVLC;

import java.io.File;

/**
 * @author: archko 2014/9/8 :19:22
 */
public class RecordUtil {

    public static void takeSnapshot(LibVLC libVLC, int videoWidth, int videoHeight) {
        String path="/sdcard/Android"/*Environment.getExternalStorageDirectory().getPath()*/+File.separator+System.currentTimeMillis()+".png";
        System.out.println("snap:"+videoWidth+" height:"+videoHeight+" path:"+path);
        if (null!=libVLC) {
            libVLC.takeSnapShot(path, videoWidth, videoHeight);
        }
    }

    public static void startRecord(LibVLC libVLC) {
        if (null!=libVLC) {
            String path="/sdcard/Android"/*Environment.getExternalStorageDirectory().getPath()*/+File.separator+System.currentTimeMillis();
            System.out.println("startRecord:"+path);
            libVLC.videoRecordStart(path);
        }
    }

    public static void stopRecord(LibVLC libVLC) {
        if (null!=libVLC) {
            libVLC.videoRecordStop();
        }
    }
}
