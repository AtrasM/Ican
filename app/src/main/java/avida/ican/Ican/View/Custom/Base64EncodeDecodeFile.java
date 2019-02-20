package avida.ican.Ican.View.Custom;


import android.net.Uri;
import android.util.Base64;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import avida.ican.Ican.Model.Structure.StructureFileStringTypeList;


/**
 * Created by AtrasVida on 2018-04-10 at 11:00 AM
 */

public class Base64EncodeDecodeFile {
    private String encoded = "";
    private byte[] decoded = null;

    //___________________==========File===========____________________________________

    public String EncodeFileToBase64(File file) {

        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return encoded = Base64.encodeToString(bytes, 0);
    }


    public ArrayList<String> EncodeFilesToBase64(ArrayList<File> files) {
        ArrayList<String> encodFiles = new ArrayList<>();
        for (File file : files) {
            byte[] bytes = new byte[0];
            try {
                bytes = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            encodFiles.add(Base64.encodeToString(bytes, 0));
        }
        return encodFiles;
    }

    //===================____________________________===================================


    //___________________========String==========____________________________________

    String EncodeFileToBase64(String filePath) {
        Uri uri = Uri.parse(filePath);
        File file = new File(uri.getPath());
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encoded = Base64.encodeToString(bytes, 0);
    }

    public ArrayList<String> EncodeFilesPathToBase64(ArrayList<String> filesPath) {
        ArrayList<String> encodFiles = new ArrayList<>();
        for (String filePath : filesPath) {
            Uri uri = Uri.parse(filePath);
            File file = new File(uri.getPath());
            byte[] bytes = new byte[0];
            try {
                bytes = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            encodFiles.add(Base64.encodeToString(bytes, 0));
        }
        return encodFiles;
    }

    StructureFileStringTypeList EncodeFilesPathToFileAndBase64(ArrayList<String> filesPath) {
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> encodFiles = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();
        StructureFileStringTypeList structureFileStringTypeList = new StructureFileStringTypeList();
        for (String filePath : filesPath) {
            Uri uri = Uri.parse(filePath);
            File file = new File(uri.getPath());
            byte[] bytes = new byte[0];
            try {
                bytes = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            encodFiles.add(Base64.encodeToString(bytes, 0));
            fileNames.add(CustomFunction.getFileName(filePath));
            fileNames.add(CustomFunction.getFileName(filePath));
            files.add(file);

        }
        structureFileStringTypeList.setFileNames(fileNames);
        structureFileStringTypeList.setEncodeBase64ArrayList(encodFiles);
        structureFileStringTypeList.setFileArrayList(files);
        return structureFileStringTypeList;
    }

    StructureFileStringTypeList EncodeFilesPathToFileAndBase64(String[] filesPath) {
        ArrayList<String> fileNames = new ArrayList<>();
        ArrayList<String> encodFiles = new ArrayList<>();
        ArrayList<File> files = new ArrayList<>();
        StructureFileStringTypeList structureFileStringTypeList = new StructureFileStringTypeList();
        for (String filePath : filesPath) {
            Uri uri = Uri.parse(filePath);
            File file = new File(uri.getPath());
            byte[] bytes = new byte[0];
            try {
                bytes = FileUtils.readFileToByteArray(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

            encodFiles.add(Base64.encodeToString(bytes, 0));
            fileNames.add(new CustomFunction().getFileName(filePath));
            files.add(file);

        }
        structureFileStringTypeList.setFileNames(fileNames);
        structureFileStringTypeList.setEncodeBase64ArrayList(encodFiles);
        structureFileStringTypeList.setFileArrayList(files);
        return structureFileStringTypeList;
    }

    //===================____________________________===================================


    public byte[] DecodeBase64ToByte(String encodedBase64File) {
        return decoded = Base64.decode(encodedBase64File, 0);
    }
    public byte[] DecodeBase64ToByte(byte[] bytes) {
        return decoded = Base64.decode(bytes, Base64.NO_WRAP);
    }

    public String EncodeByteArrayToString(byte[] encodedBase64File) {
        if (encodedBase64File == null) {
            return "";
        } else {
            return Base64.encodeToString(encodedBase64File, 0);

        }

    }
}
  /*  try
    {
        File file2 = new File("fileName.wav");
        FileOutputStream os = new FileOutputStream(file2, true);
        os.write(decoded);
        os.close();
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }*/






