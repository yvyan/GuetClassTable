package top.yvyan.guettable.Gson;

import java.util.List;

public class LabTableOuter {
    private boolean success;
    private long total;
    private List<LabTable> data;

    public boolean isSuccess() {
        return success;
    }

    public long getTotal() {
        return total;
    }

    public List<LabTable> getData() {
        return data;
    }
}
