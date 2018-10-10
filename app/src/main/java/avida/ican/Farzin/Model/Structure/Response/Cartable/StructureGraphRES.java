package avida.ican.Farzin.Model.Structure.Response.Cartable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2018-10-06 at 11:47 AM
 */

public class StructureGraphRES {

    @SerializedName("@RecordCount")
    int RecordCount;
    @SerializedName("@SelectedPage")
    int SelectedPage;
    @SerializedName("@RecordPerPage")
    int RecordPerPage;

    StructureStartNode StartNode = new StructureStartNode();

    public int getRecordCount() {
        return RecordCount;
    }

    public void setRecordCount(int recordCount) {
        RecordCount = recordCount;
    }

    public int getSelectedPage() {
        return SelectedPage;
    }

    public void setSelectedPage(int selectedPage) {
        SelectedPage = selectedPage;
    }

    public int getRecordPerPage() {
        return RecordPerPage;
    }

    public void setRecordPerPage(int recordPerPage) {
        RecordPerPage = recordPerPage;
    }

    public StructureStartNode getStartNode() {
        return StartNode;
    }

    public void setStartNode(StructureStartNode startNode) {
        StartNode = startNode;
    }


    //_________*********____________<StructureStartNode>___________********____________
    public class StructureStartNode {

        List<StructureNodeRES> Node = new ArrayList<>();

        public List<StructureNodeRES> getNode() {
            return Node;
        }

        public void setNode(List<StructureNodeRES> node) {
            Node = node;
        }
    }
}
