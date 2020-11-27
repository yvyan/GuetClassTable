package top.yvyan.guettable.database.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.List;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.database.DataBaseConstants;
import top.yvyan.guettable.database.MySQLite;


/**
 * 用于Course表的增删查改
 * */

public class CourseDao {
    private static final String TAG = "CourseDao";
    private final MySQLite mySQLite;

    public CourseDao(Context context){
       mySQLite = new MySQLite(context);
    }

    public void insert(CourseBean course){
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();

        /**
         *  将 List<Integer>转化为逗号隔开的字符串
         *  转化回去：List<Integer> result = Arrays.asList(stringName.split(","));
         */
        List<Integer> weekList = course.getWeekList();
        StringBuilder wList = new StringBuilder();
        for (int i = 0; i < weekList.size(); i++) {
            wList.append(weekList.get(i));
            if (i < weekList.size() - 1) {
                wList.append(weekList);
            }
        }

        values.put("id", course.getId());
        values.put("isLab", course.isLab());
        values.put("number", course.getNumber());
        values.put("name", course.getName());
        values.put("libName", course.getLibName());
        values.put("room", course.getRoom());
        values.put("weekStart", course.getWeekStart());
        values.put("weekEnd", course.getWeekEnd());
        values.put("weekList", wList.toString());
        values.put("day", course.getDay());
        values.put("time", course.getTime());
        values.put("teacher", course.getTeacher());
        values.put("remarks", course.getRemarks());

        long rows = db.insert(DataBaseConstants.COURSE_TABLE_NAME, null, values);
        if (rows == -1) {
            Log.d(TAG, "数据插入失败");
        } else {
            Log.d(TAG, "数据插入成功 " + rows + "rows");
        }
        db.close();
    }

    public void delete(CourseBean course){
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        String where = "id=" + course.getId();
        int rows = db.delete(DataBaseConstants.COURSE_TABLE_NAME, where, null);
        if(rows > 0) {
            Log.d(TAG, "数据删除成功 " + rows + "rows");
        } else {
            Log.d(TAG, "数据删除失败");
        }
        db.close();
    }

    /**
     *  更新
     * */
    public void update(CourseBean course){
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        String where = "id=" + course.getId();
        ContentValues values = new ContentValues();
        /*
            values.put("xx",course.getXx());
        */
        int rows = db.update(DataBaseConstants.COURSE_TABLE_NAME, values, where, null);
        if(rows > 0) {
            Log.d(TAG, "数据更新成功 " + rows + "rows");
        } else {
            Log.d(TAG, "数据更新失败");
        }
        db.close();
    }
    /**
     * 查询所有
     */
    public void allQuery() {
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        Cursor cursor = db.query(DataBaseConstants.COURSE_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Log.d(TAG, "id ==" + id + "name = " + name);
        }
        db.close();
    }

    public void query(CourseBean course) {
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        Cursor cursor = db.query(DataBaseConstants.COURSE_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Log.d(TAG, "id ==" + id + "name = " + name);
        }
        db.close();
    }
}
