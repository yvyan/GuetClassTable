package top.yvyan.guettable.Gson;

public class Semsters {
    private String schoolYear;
    private String season;

    public String startDate;

    public String endDate;

    public int id;

    public String toString() {
        return schoolYear + (season.equals("AUTUMN") ? "_1" : "_2");
    }
}
