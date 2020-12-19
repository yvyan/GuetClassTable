package top.yvyan.guettable.Gson;

import java.util.List;

public class PlannedCoursesOuter {
    private boolean success;
    private int total;
    private List<PlannedCourse> data;
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    public int getTotal() {
        return total;
    }

    public void setData(List<PlannedCourse> data) {
        this.data = data;
    }
    public List<PlannedCourse> getData() {
        return data;
    }
}
