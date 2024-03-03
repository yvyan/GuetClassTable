package top.yvyan.guettable.Gson;

import android.se.omapi.Session;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CurrentSemester {
    public String schoolYear;
    public int id;
    public Season season;
    public Date startDate;
    private static class Date {
        public int[] values;
        public String toString() {
            return String.format("%04d-%02d-%02d",values[0],values[1],values[2]);
        }
    }

    public Date endDate;

    private static class Season {
        public String $name;
    }
    public String toString() {
        return schoolYear + (season.$name.equals("AUTUMN") ? "_1" : "_2");
    }

    public Long getStartTime() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.parse(startDate.toString()).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }
}
