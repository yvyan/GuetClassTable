package top.yvyan.guettable.Gson;

public class Semester {
    private String schoolYear;
    private String season;

    public String startDate;

    public String endDate;

    public String nameZh;

    public int id;

    public String toString() {
        return schoolYear + (season.equals("AUTUMN") ? "_1" : "_2");
    }
}
