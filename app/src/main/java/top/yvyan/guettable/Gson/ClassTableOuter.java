package top.yvyan.guettable.Gson;

import java.util.List;

public class ClassTableOuter {
    private boolean success;
    private long total;
    private List<ClassTable> data;

    public boolean isSuccess() {
        return success;
    }

    public long getTotal() {
        return total;
    }

    public List<ClassTable> getData() {
        return data;
    }
}
