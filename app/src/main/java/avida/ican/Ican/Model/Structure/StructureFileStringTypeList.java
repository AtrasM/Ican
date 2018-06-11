package avida.ican.Ican.Model.Structure;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AtrasVida on 2018-05-06 at 4:04 PM
 */

public class StructureFileStringTypeList {
    ArrayList<String> encodeBase64ArrayList=new ArrayList<>();
    ArrayList<String> fileNames=new ArrayList<>();
    ArrayList<File> fileArrayList=new ArrayList<>();

    public ArrayList<String> getEncodeBase64ArrayList() {
        return encodeBase64ArrayList;
    }

    public void setEncodeBase64ArrayList(ArrayList<String> encodeBase64ArrayList) {
        this.encodeBase64ArrayList = encodeBase64ArrayList;
    }

    public ArrayList<File> getFileArrayList() {
        return fileArrayList;
    }

    public void setFileArrayList(ArrayList<File> fileArrayList) {
        this.fileArrayList = fileArrayList;
    }

    public ArrayList<String> getFileNames() {
        return fileNames;
    }

    public void setFileNames(ArrayList<String> fileNames) {
        this.fileNames = fileNames;
    }
}
