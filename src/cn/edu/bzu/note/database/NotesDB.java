package cn.edu.bzu.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * @author lb
 * @date 2015-07-30
 */
public class NotesDB extends SQLiteOpenHelper{

	/**
	 * 初始化,建表 
	 */
	public NotesDB(Context context) {
		super(context, TABLE_NAME_NOTES, null, 1);
	}

	/**
	 * 创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+TABLE_NAME_NOTES+"(" +
				COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_NAME_NOTE_NAME+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_NOTE_CONTENT+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_NOTE_DATE+" TEXT NOT NULL DEFAULT \"\"" +
				")");
		db.execSQL("CREATE TABLE "+TABLE_NAME_MEDIA+"(" +
				COLUMN_NAME_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
				COLUMN_NAME_MEDIA_PATH+" TEXT NOT NULL DEFAULT \"\"," +
				COLUMN_NAME_MEDIA_OWNER_NOTE_ID+" INTEGER NOT NULL DEFAULT 0" +
				")");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	/**
	 * 记事本表名
	 */
	public static final String TABLE_NAME_NOTES = "notes";
	/**
	 * 多媒体表名
	 */
	public static final String TABLE_NAME_MEDIA = "media";
	/**
	 * 记事本在数据库中的id
	 */
	public static final String COLUMN_NAME_ID = "_id";
	/**
	 * 记事本的标题
	 */
	public static final String COLUMN_NAME_NOTE_NAME = "name";
	/**
	 * 记事本内容
	 */
	public static final String COLUMN_NAME_NOTE_CONTENT = "content";
	/**
	 * 记事本更新时间
	 */
	public static final String COLUMN_NAME_NOTE_DATE = "date";
	/**
	 * 多媒体保存路径
	 */
	public static final String COLUMN_NAME_MEDIA_PATH = "path";
	/**
	 * 多媒体所属的记事本id
	 */
	public static final String COLUMN_NAME_MEDIA_OWNER_NOTE_ID = "note_id";
	
	//TODO
	/**
	 * 多媒体的id
	 */
	public static final String COLUMN_NAME_MEDIA_ID = "media_id";


}