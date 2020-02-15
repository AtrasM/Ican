package avida.ican.Farzin.Model.Structure.Bundle;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by AtrasVida on 2019-08-06 at 4:42 PM
 */

public class StructureZanjireMadrakHeaderBND implements Serializable {
    private String title;
    private Drawable drawable;


    public StructureZanjireMadrakHeaderBND() {
    }

    public StructureZanjireMadrakHeaderBND(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }
}
