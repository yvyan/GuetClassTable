package top.yvyan.guettable.Gson;

import java.util.List;

public class AvgTextbookOuter {
    private boolean success;
    private int total;
    private List<AvgTextbook> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<AvgTextbook> getData() {
        return data;
    }

    public void setData(List<AvgTextbook> data) {
        this.data = data;
    }
}
