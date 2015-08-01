package cn.edu.bzu.note.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ݿ������
 * @author lb
 * @date 2015-07-30
 */
public class NotesDB extends SQLiteOpenHelper{

	/**
	 * ��ʼ��,���� 
	 */
	public NotesDB(Context context) {
		super(context, TABLE_NAME_NOTES, null, 1);
	}

	/**
	 * �������ݿ�
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
	 * ���±�����
	 */
	public static final String TABLE_NAME_NOTES = "notes";
	/**
	 * ��ý�����
	 */
	public static final String TABLE_NAME_MEDIA = "media";
	/**
	 * ���±������ݿ��е�id
	 */
	public static final String COLUMN_NAME_ID = "_id";
	/**
	 * ���±��ı���
	 */
	public static final String COLUMN_NAME_NOTE_NAME = "name";
	/**
	 * ���±�����
	 */
	public static final String COLUMN_NAME_NOTE_CONTENT = "content";
	/**
	 * ���±�����ʱ��
	 */
	public static final String COLUMN_NAME_NOTE_DATE = "date";
	/**
	 * ��ý�屣��·��
	 */
	public static final String COLUMN_NAME_MEDIA_PATH = "path";
	/**
	 * ��ý�������ļ��±�id
	 */
	public static final String COLUMN_NAME_MEDIA_OWNER_NOTE_ID = "note_id";
	
	//TODO
	/**
	 * ��ý���id
	 */
	public static final String COLUMN_NAME_MEDIA_ID = "media_id";


}