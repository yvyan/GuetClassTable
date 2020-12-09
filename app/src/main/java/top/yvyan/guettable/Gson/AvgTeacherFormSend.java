package top.yvyan.guettable.Gson;

import android.util.Log;

import top.yvyan.guettable.util.AppUtil;

public class AvgTeacherFormSend {
    private int afz;
    private int bfz;
    private int cfz;
    private int dfz;
    private int efz;
    private String courseid;
    private String courseno;
    private String dja;
    private String djb;
    private String djc;
    private String djd;
    private String dje;
    private int lb = 1;
    private int lsh;
    private String nr;
    private double qz;
    private int score = 100;
    private String teacherno;
    private String term;
    private String zbnh;
    private int zp = 0;

    public AvgTeacherFormSend(AvgTeacherFormGet avgTeacherFormGet, String courseId, String courseNo, String teacherNo, String term) {
        this.afz = avgTeacherFormGet.getAfz();
        this.bfz = avgTeacherFormGet.getBfz();
        this.cfz = avgTeacherFormGet.getCfz();
        this.dfz = avgTeacherFormGet.getDfz();
        this.efz = avgTeacherFormGet.getEfz();
        this.courseid = courseId;
        this.courseno = courseNo;
        this.dja = AppUtil.encode(avgTeacherFormGet.getDja());
        this.djb = AppUtil.encode(avgTeacherFormGet.getDjb());
        this.djc = AppUtil.encode(avgTeacherFormGet.getDjc());
        this.djd = AppUtil.encode(avgTeacherFormGet.getDjd());
        this.dje = AppUtil.encode(avgTeacherFormGet.getDje());
        this.lsh = avgTeacherFormGet.getLsh();
        String str = avgTeacherFormGet.getNr();
        if (str.contains("(")) {
            str = str.substring(1, str.length() - 1);
        }
        this.nr = str;
        this.qz = avgTeacherFormGet.getQz();
        this.teacherno = teacherNo;
        this.term = term;
        this.zbnh = AppUtil.encode(avgTeacherFormGet.getZbnh());
        Log.d("testNr", nr);
    }

    public AvgTeacherFormSend(int afz, int bfz, int cfz, int dfz, int efz, String courseid, String courseno, String dja, String djb, String djc, String djd, String dje, int lsh, String nr, double qz, String teacherno, String term, String zbnh) {
        this.afz = afz;
        this.bfz = bfz;
        this.cfz = cfz;
        this.dfz = dfz;
        this.efz = efz;
        this.courseid = courseid;
        this.courseno = courseno;
        this.dja = dja;
        this.djb = djb;
        this.djc = djc;
        this.djd = djd;
        this.dje = dje;
        this.lsh = lsh;
        this.nr = nr;
        this.qz = qz;
        this.teacherno = teacherno;
        this.term = term;
        this.zbnh = zbnh;
    }

    public String getTerm() {
        return term;
    }

    public String getCourseno() {
        return courseno;
    }

    public String getTeacherno() {
        return teacherno;
    }

    public String getCourseid() {
        return courseid;
    }

    @Override
    public String toString() {
        return "AvgTeacherFormSend{" +
                "afz=" + afz +
                ", bfz=" + bfz +
                ", cfz=" + cfz +
                ", dfz=" + dfz +
                ", efz=" + efz +
                ", courseid='" + courseid + '\'' +
                ", courseno='" + courseno + '\'' +
                ", dja='" + dja + '\'' +
                ", djb='" + djb + '\'' +
                ", djc='" + djc + '\'' +
                ", djd='" + djd + '\'' +
                ", dje='" + dje + '\'' +
                ", lb=" + lb +
                ", lsh=" + lsh +
                ", nr='" + nr + '\'' +
                ", qz=" + qz +
                ", score=" + score +
                ", teacherno='" + teacherno + '\'' +
                ", term='" + term + '\'' +
                ", zbnh='" + zbnh + '\'' +
                ", zp=" + zp +
                '}';
    }
}
