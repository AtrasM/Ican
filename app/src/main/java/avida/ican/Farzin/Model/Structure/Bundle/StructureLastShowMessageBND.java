package avida.ican.Farzin.Model.Structure.Bundle;

import java.io.Serializable;
import avida.ican.Farzin.Model.Enum.Type;

/**
 * Created by AtrasVida on 2019-05-25 at 4:06 PM
 */

public class StructureLastShowMessageBND implements Serializable {
    private int id=-1;
    private int main_id=-1;
    private int position=-1;
    private Type type;

    public StructureLastShowMessageBND() {
    }

    public StructureLastShowMessageBND(int id, int main_id, int position, Type type) {
        this.id = id;
        this.main_id = main_id;
        this.position = position;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMain_id() {
        return main_id;
    }

    public void setMain_id(int main_id) {
        this.main_id = main_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
