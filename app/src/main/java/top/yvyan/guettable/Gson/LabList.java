package top.yvyan.guettable.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabList {
    public Result result;

    public static class Result {
        public List<Lab> records;

        public static class Lab {
            public String courseId;
            public String courseNumber;
        }
    }

    // id 反查课号 map;
    public Map<String, String> toMap() {
        Map<String, String> out = new HashMap<>();
        for (Result.Lab lab : result.records) {
            if (!out.containsKey(lab.courseId)) {
                out.put(lab.courseId, lab.courseNumber);
            }
        }
        return out;
    }
}
