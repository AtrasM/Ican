package avida.ican.Farzin.Model.Structure.Database;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import avida.ican.Farzin.Model.Structure.Response.StructureReceiverRES;

/**
 * Created by AtrasVida in 2018-06-19 at 15:25 PM
 */

@DatabaseTable(tableName = "receiver")
public class StructureReceiverDB implements Serializable {
    private static final String Message_ID_FIELD_NAME = "message_id";
    @DatabaseField( canBeNull = false,generatedId = true)
    private int id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = Message_ID_FIELD_NAME)
    private StructureMessageDB message;
    @DatabaseField()
    private int user_id;
    @DatabaseField()
    private int role_id;
    @DatabaseField()
    private String user_name;
    @DatabaseField()
    private String native_id;
    @DatabaseField
    private boolean is_read;
    @DatabaseField
    private String view_date;

    public StructureReceiverDB() {
    }

    public StructureReceiverDB(StructureMessageDB message, StructureReceiverRES structureReceiverRES, StructureUserAndRoleDB structureUserAndRoleDB) {
        this.message = message;
        this.user_id = structureUserAndRoleDB.getUser_ID();
        this.role_id = structureUserAndRoleDB.getRole_ID();
        this.user_name = structureUserAndRoleDB.getUserName();
        this.native_id = structureUserAndRoleDB.getNativeID();
        this.is_read = structureReceiverRES.isRead();
        this.view_date = structureReceiverRES.getViewDate();
    }

    public StructureReceiverDB(StructureMessageDB message, int user_id, int role_id, String user_name, String native_id, boolean is_read, String view_date) {
        this.message = message;
        this.user_id = user_id;
        this.role_id = role_id;
        this.user_name = user_name;
        this.native_id = native_id;
        this.is_read = is_read;
        this.view_date = view_date;
    }

    public StructureMessageDB getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public String getNative_id() {
        return native_id;
    }

    public void setNative_id(String native_id) {
        this.native_id = native_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean Is_read() {
        return is_read;
    }

    public String getView_date() {
        return view_date;
    }
}

