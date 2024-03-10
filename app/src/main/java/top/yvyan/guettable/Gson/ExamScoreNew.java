package top.yvyan.guettable.Gson;

import java.util.List;
import java.util.Map;

public class ExamScoreNew {
    public Map<String, List<ScoreDetail>> semesterId2studentGrades;

    public static class ScoreDetail implements Comparable<ScoreDetail> {
        public int id;
        public int semesterId;
        public double credits;
        public String semesterName;
        public String courseCode;
        public String courseName;
        public String lessonCode;
        public String courseType;
        public String gaGrade;
        public boolean passed;
        public String gradeDetail;
        public String fillAGrace;

        @Override
        public int compareTo(ScoreDetail score) {
            if(this.semesterId==score.semesterId) {
                return this.id-score.id;
            } else {
                return this.semesterId-score.semesterId;
            }
        }
    }
}
