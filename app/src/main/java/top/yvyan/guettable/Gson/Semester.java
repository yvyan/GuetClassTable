package top.yvyan.guettable.Gson;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class Semester {
    private String schoolYear;
    private String season;

    public String startDate;

    public String endDate;

    private Long parseDateTime(String date) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return Objects.requireNonNull(formatter.parse(date)).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }

    public Long getStartDateTime() {
        return this.parseDateTime(startDate);
    }

    public Long getEndDateTime() {
        return this.parseDateTime(endDate);
    }

    public String nameZh;

    public int id;

    public String toString() {
        return schoolYear + (season.equals("AUTUMN") ? "_1" : "_2");
    }
}
