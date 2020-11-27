package top.yvyan.guettable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLite extends SQLiteOpenHelper {

    private static final String TAG = "MySQLite";

    private Context context;

    public MySQLite(@Nullable Context context) {
        super(context, DataBaseConstants.DATABASE_NAME, null, DataBaseConstants.VERSION_CODE);
        this.context=context;
    }

    /**
     *    注：只在第一次创建数据库时调用
     **/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG,"创建数据库"+DataBaseConstants.DATABASE_NAME+"...");
        //创建Course表
        sqLiteDatabase.execSQL(DataBaseConstants.CREAT_Course);
        sqLiteDatabase.execSQL(DataBaseConstants.CREAT_Exam);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.d(TAG,"升级数据库"+DataBaseConstants.DATABASE_NAME+"...");

        sqLiteDatabase.execSQL("drop table if exists " + DataBaseConstants.COURSE_TABLE_NAME);
        sqLiteDatabase.execSQL("drop table if exists " + DataBaseConstants.EXAM_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
