package top.yvyan.guettable.Gson;

import java.util.List;

public class AvgTextbookFormGetOuter {
    private boolean success;
    private int total;
    private List<AvgTextbookFormGet> data;

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

    public List<AvgTextbookFormGet> getData() {
        return data;
    }

    public void setData(List<AvgTextbookFormGet> data) {
        this.data = data;
    }
}
