package top.yvyan.guettable.Gson;

import java.util.Date;

import top.yvyan.guettable.bean.ExamBean;

public class ExamInfo {
    private String croomno;
    private String croomname;
    private String tch;
    private String tch1;
    private String tch2;
    private String js;
    private String js1;
    private String js2;
    private String stid1;
    private String stid2;
    private int roomrs;
    private int pc;
    private String term;
    private String grade;
    private String dpt;
    private String spno;
    private String spname;
    private String courseid;
    private String courseno;
    private String labno;
    private String labname;
    private String dptno;
    private String teacherno;
    private String name;
    private String xf;
    private String cname;
    private String sctcnt;
    private String stucnt;
    private String scoretype;
    private String examt;
    private String kctype;
    private String typeno;
    private Date examdate;
    private String examtime;
    private int examstate;
    private String exammode;
    private String xm;
    private String refertime;
    private int zc;
    private int xq;
    private String ksjc;
    private String jsjc;
    private String bkzt;
    private String kssj;
    private String comm;
    private String rooms;
    private String lsh;
    private int zone;
    private String checked1;
    private String postdate;
    private String operator;
    private String pk;

    public ExamBean toExamBean() {
        return new ExamBean(courseno, cname, name, zc, xq, Integer.valueOf(ksjc), kssj, examdate, croomno);
    }

    public void setCroomno(String croomno) {
        this.croomno = croomno;
    }
    public String getCroomno() {
        return croomno;
    }

    public void setCroomname(String croomname) {
        this.croomname = croomname;
    }
    public String getCroomname() {
        return croomname;
    }

    public void setTch(String tch) {
        this.tch = tch;
    }
    public String getTch() {
        return tch;
    }

    public void setTch1(String tch1) {
        this.tch1 = tch1;
    }
    public String getTch1() {
        return tch1;
    }

    public void setTch2(String tch2) {
        this.tch2 = tch2;
    }
    public String getTch2() {
        return tch2;
    }

    public void setJs(String js) {
        this.js = js;
    }
    public String getJs() {
        return js;
    }

    public void setJs1(String js1) {
        this.js1 = js1;
    }
    public String getJs1() {
        return js1;
    }

    public void setJs2(String js2) {
        this.js2 = js2;
    }
    public String getJs2() {
        return js2;
    }

    public void setStid1(String stid1) {
        this.stid1 = stid1;
    }
    public String getStid1() {
        return stid1;
    }

    public void setStid2(String stid2) {
        this.stid2 = stid2;
    }
    public String getStid2() {
        return stid2;
    }

    public void setRoomrs(int roomrs) {
        this.roomrs = roomrs;
    }
    public int getRoomrs() {
        return roomrs;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }
    public int getPc() {
        return pc;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    public String getTerm() {
        return term;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getGrade() {
        return grade;
    }

    public void setDpt(String dpt) {
        this.dpt = dpt;
    }
    public String getDpt() {
        return dpt;
    }

    public void setSpno(String spno) {
        this.spno = spno;
    }
    public String getSpno() {
        return spno;
    }

    public void setSpname(String spname) {
        this.spname = spname;
    }
    public String getSpname() {
        return spname;
    }

    public void setCourseid(String courseid) {
        this.courseid = courseid;
    }
    public String getCourseid() {
        return courseid;
    }

    public void setCourseno(String courseno) {
        this.courseno = courseno;
    }
    public String getCourseno() {
        return courseno;
    }

    public void setLabno(String labno) {
        this.labno = labno;
    }
    public String getLabno() {
        return labno;
    }

    public void setLabname(String labname) {
        this.labname = labname;
    }
    public String getLabname() {
        return labname;
    }

    public void setDptno(String dptno) {
        this.dptno = dptno;
    }
    public String getDptno() {
        return dptno;
    }

    public void setTeacherno(String teacherno) {
        this.teacherno = teacherno;
    }
    public String getTeacherno() {
        return teacherno;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }
    public String getXf() {
        return xf;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
    public String getCname() {
        return cname;
    }

    public void setSctcnt(String sctcnt) {
        this.sctcnt = sctcnt;
    }
    public String getSctcnt() {
        return sctcnt;
    }

    public void setStucnt(String stucnt) {
        this.stucnt = stucnt;
    }
    public String getStucnt() {
        return stucnt;
    }

    public void setScoretype(String scoretype) {
        this.scoretype = scoretype;
    }
    public String getScoretype() {
        return scoretype;
    }

    public void setExamt(String examt) {
        this.examt = examt;
    }
    public String getExamt() {
        return examt;
    }

    public void setKctype(String kctype) {
        this.kctype = kctype;
    }
    public String getKctype() {
        return kctype;
    }

    public void setTypeno(String typeno) {
        this.typeno = typeno;
    }
    public String getTypeno() {
        return typeno;
    }

    public void setExamdate(Date examdate) {
        this.examdate = examdate;
    }
    public Date getExamdate() {
        return examdate;
    }

    public void setExamtime(String examtime) {
        this.examtime = examtime;
    }
    public String getExamtime() {
        return examtime;
    }

    public void setExamstate(int examstate) {
        this.examstate = examstate;
    }
    public int getExamstate() {
        return examstate;
    }

    public void setExammode(String exammode) {
        this.exammode = exammode;
    }
    public String getExammode() {
        return exammode;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }
    public String getXm() {
        return xm;
    }

    public void setRefertime(String refertime) {
        this.refertime = refertime;
    }
    public String getRefertime() {
        return refertime;
    }

    public void setZc(int zc) {
        this.zc = zc;
    }
    public int getZc() {
        return zc;
    }

    public void setXq(int xq) {
        this.xq = xq;
    }
    public int getXq() {
        return xq;
    }

    public void setKsjc(String ksjc) {
        this.ksjc = ksjc;
    }
    public String getKsjc() {
        return ksjc;
    }

    public void setJsjc(String jsjc) {
        this.jsjc = jsjc;
    }
    public String getJsjc() {
        return jsjc;
    }

    public void setBkzt(String bkzt) {
        this.bkzt = bkzt;
    }
    public String getBkzt() {
        return bkzt;
    }

    public void setKssj(String kssj) {
        this.kssj = kssj;
    }
    public String getKssj() {
        return kssj;
    }

    public void setComm(String comm) {
        this.comm = comm;
    }
    public String getComm() {
        return comm;
    }

    public void setRooms(String rooms) {
        this.rooms = rooms;
    }
    public String getRooms() {
        return rooms;
    }

    public void setLsh(String lsh) {
        this.lsh = lsh;
    }
    public String getLsh() {
        return lsh;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }
    public int getZone() {
        return zone;
    }

    public void setChecked1(String checked1) {
        this.checked1 = checked1;
    }
    public String getChecked1() {
        return checked1;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }
    public String getPostdate() {
        return postdate;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getOperator() {
        return operator;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
    public String getPk() {
        return pk;
    }
}
