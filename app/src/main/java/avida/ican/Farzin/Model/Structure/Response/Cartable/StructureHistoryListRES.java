package avida.ican.Farzin.Model.Structure.Response.Cartable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AtrasVida on 2018-10-06 at 10:19 AM
 */

public class StructureHistoryListRES {
    StructureHistoryListResult GetHistoryListResult = new StructureHistoryListResult();
    private String StrErrorMsg;

    public StructureHistoryListResult getGetHistoryListResult() {
        return GetHistoryListResult;
    }

    public void setGetHistoryListResult(StructureHistoryListResult getHistoryListResult) {
        GetHistoryListResult = getHistoryListResult;
    }

    public String getStrErrorMsg() {
        return StrErrorMsg;
    }

    public void setStrErrorMsg(String strErrorMsg) {
        StrErrorMsg = strErrorMsg;
    }


    //_________*********____________<GetHistoryListResult>___________********____________
    public class StructureHistoryListResult {
        StructureGraphs Graphs = new StructureGraphs();

        public StructureGraphs getGraphs() {
            return Graphs;
        }

        public void setGraphs(StructureGraphs graphs) {
            Graphs = graphs;
        }
    }


    //_________*********____________<StructureGraphs>___________********____________
    public class StructureGraphs {
        List<StructureGraphRES> Graph = new ArrayList<>();

        public List<StructureGraphRES> getGraph() {
            return Graph;
        }

        public void setGraph(List<StructureGraphRES> graph) {
            Graph = graph;
        }
    }


}
