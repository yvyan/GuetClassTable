package top.yvyan.guettable.database.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import top.yvyan.guettable.bean.CourseBean;
import top.yvyan.guettable.bean.ExamBean;
import top.yvyan.guettable.database.DataBaseConstants;
import top.yvyan.guettable.database.MySQLite;

public class ExamDao {
    private static final String TAG = "ExamDao";
    private final MySQLite mySQLite;

    public ExamDao(Context context){
        mySQLite = new MySQLite(context);
    }

    public void insert(ExamBean exam){
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", exam.getNumber());
        values.put("name", exam.getName());
        values.put("teacher", exam.getTeacher());
        values.put("week", exam.getWeek());
        values.put("classNum", exam.getWeek());
        values.put("day", exam.getDay());
        values.put("time", exam.getTime());
        values.put("date", exam.getDate().toString());
        values.put("room", exam.getRoom());

        long rows = db.insert(DataBaseConstants.EXAM_TABLE_NAME, null, values);
        if (rows == -1) {
            Log.d(TAG, "数据插入失败");
        } else {
            Log.d(TAG, "数据插入成功 " + rows + "rows");
        }
        db.close();
    }

    public void delete(ExamBean exam){
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        String where = "number=" + exam.getNumber();
        int rows = db.delete(DataBaseConstants.EXAM_TABLE_NAME, where, null);
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
    public void update(ExamBean exam){
        SQLiteDatabase db = mySQLite.getWritableDatabase();
        String where = "number=" + exam.getNumber();
        ContentValues values = new ContentValues();
        /*
            values.put("xx",course.getXx());
        */
        int rows = db.update(DataBaseConstants.EXAM_TABLE_NAME, values, where, null);
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
        Cursor cursor = db.query(DataBaseConstants.EXAM_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            Log.d(TAG, "id ==" + id + "name = " + name);
        }
        db.close();
    }

    public void query(ExamBean exam) {
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
