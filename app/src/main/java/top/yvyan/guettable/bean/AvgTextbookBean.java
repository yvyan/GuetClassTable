package top.yvyan.guettable.bean;

public class AvgTextbookBean {
    private final String courseName;
    private final String textbookName;
    private String hint;

    public AvgTextbookBean(String courseName, String textbookName, String hint) {
        this.courseName = courseName;
        this.textbookName = textbookName;
        this.hint = hint;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTextbookName() {
        return textbookName;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
