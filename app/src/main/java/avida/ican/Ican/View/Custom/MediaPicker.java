package avida.ican.Ican.View.Custom;

import android.app.Activity;
import android.content.Intent;


import com.rahul.media.main.MediaFactory;

import java.util.ArrayList;

import avida.ican.Ican.Model.Structure.StructureFileStringTypeList;
import avida.ican.Ican.View.Interface.MediaPickerListener;

/**
 * Created by AtrasVida on 2018-05-06 at 3:00 PM
 */

public class MediaPicker {

    /**
     * ِgradle dependency is:  implementation 'com.crop.multiple:multipleImagePickerLib:1.4.4'
     */

    private Activity context;
    private MediaFactory.MediaBuilder mediaBuilder;
    private MediaFactory mediaFactory;
    private int pickCount = 10;
    boolean isSquareCrop = false;
    private MediaPickerListener mediaPickerListener;

    public MediaPicker(Activity context) {
        this.context = context;
    }

    public MediaPicker setPickCount(int pickcount) {
        /**
         * defult is 10
         */
        this.pickCount = pickcount;
        return this;
    }

    public MediaPicker isSquareCrop(boolean isSquareCrop) {
        /**
         * defult is true
         */
        this.isSquareCrop = isSquareCrop;
        return this;
    }

    public MediaPicker setOnListener(MediaPickerListener mediaPickerListener) {
        this.mediaPickerListener = mediaPickerListener;
        return this;
    }

    //==============================P==I==C==K=============================================
    public void showSinglePickFromGallery() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .setPickCount(1)
                        .isSquareCrop(isSquareCrop)
                        .doCropping()
                        .fromGallery();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    public void showSinglePickFromCamera() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .isSquareCrop(isSquareCrop)
                        .doCropping()
                        .fromCamera();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    public void showMultyPickFromGallery() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .setPickCount(pickCount)
                        .isSquareCrop(isSquareCrop)
                        .doCropping()
                        .fromGallery();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    public void showMultyPickFromCamera() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .setPickCount(pickCount)
                        .isSquareCrop(isSquareCrop)
                        .doCropping()
                        .fromCamera();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }
    //==============================P==I==C==K=============================================


    //==============================V==I==D==E==O===========================================

    public void showSingleVideoFromGallery() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .takeVideo()
                        .isSquareCrop(isSquareCrop)
                        .fromGallery();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    public void showSingleVideoFromCamera() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .takeVideo()
                        .isSquareCrop(isSquareCrop)
                        .fromCamera();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    public void showMultyVideoFromGallery() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .takeVideo()
                        .setPickCount(pickCount)
                        .isSquareCrop(isSquareCrop)
                        .fromGallery();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    public void showMultyVideoFromCamera() {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mediaBuilder = new MediaFactory.MediaBuilder(context)
                        .takeVideo()
                        .setPickCount(pickCount)
                        .isSquareCrop(isSquareCrop)
                        .fromCamera();
                mediaFactory = MediaFactory.create().start(mediaBuilder);
            }
        });

    }

    //==============================V==I==D==E==O===========================================


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ArrayList<String> all_path = mediaFactory.onActivityResult(requestCode, resultCode, data);
        StructureFileStringTypeList structureFileStringTypeList = new Base64EncodeDecodeFile().EncodeFilesPathToFileAndBase64(all_path);
        if (structureFileStringTypeList.getEncodeBase64ArrayList().size() > 0) {
            mediaPickerListener.onSuccess(structureFileStringTypeList.getFileArrayList(), structureFileStringTypeList.getEncodeBase64ArrayList(),structureFileStringTypeList.getFileNames());
        } else {
            mediaPickerListener.onFailed();
        }
    }
}
