package top.yvyan.guettable.Gson;

public class Semsters {
    private String schoolYear;
    private String season;
    public int id;

    public String toString() {
        return schoolYear + (season.equals("AUTUMN") ? "_1" : "_2");
    }
}
