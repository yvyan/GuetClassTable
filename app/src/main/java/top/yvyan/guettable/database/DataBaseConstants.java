package top.yvyan.guettable.database;

public class DataBaseConstants {
    public static final String DATABASE_NAME = "GUETClassTable.db";
    public static final int VERSION_CODE = 1;

    public static final String COURSE_TABLE_NAME = "Course";
    public static final String CREAT_Course= "create table Course ("
            +"id integer primary key autoincrement," //自增id
            +"isLab boolean,"         //是否为实验 0或1
            +"number text,"           //课号
            +"name text,"             //课程名称
            +"libName text,"          //（实验）名称
            +"room text,"             //教室
            +"weekStart text,"        //开始周次
            +"weekEnd text,"          //结束周次
            +"weekList text,"         //周次集合 用","隔开 如 1,2,3,4,5,6,7,8
            +"day integer,"           //周几
            +"time integer,"          //第几节课
            +"teacher text,"          //老师
            +"remarks text)";          //（实验）备注

    public static final String EXAM_TABLE_NAME = "Exam";
    public static final String CREAT_Exam="create table Exam ("
            +"number text,"           //课号
            +"name text,"             //课程名称
            +"teacher text,"          //老师
            +"week text,"             //周次
            +"classNum integer,"       //考试时间
            +"day integer,"           //星期
            +"time integer,"          //考试时间
            +"date date,"              //日期
            +"room text)";            //教室

}
