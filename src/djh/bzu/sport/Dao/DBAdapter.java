package djh.bzu.sport.Dao;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
  
public class DBAdapter {  
  
    static final String KEY_ROWID = "_id";  
    static final String KEY_NAME = "name";  
    static final String KEY_EMAIL = "email";  
    static final String TAG = "DBAdapter";  
    private String KEY_USER_NAME ="userName";//用户名
    private String KEY_STEP = "step";//步数
    private String KEY_DISTANCE = "distance";//路程
    private String KEY_TIME ="time";
    private String  KEY_CRESTED_TIME="createdTime";
      
    static final String DATABASE_NAME = "sportDB";  
    static final String DATABASE_TABLE = "contacts";  
    static final int DATABASE_VERSION = 1;  
      
    static final String DATABASE_CREATE =   
            "create table contacts( _id integer primary key autoincrement, " +   
            "userName text not null, step text not null, distance text not null, time text not null ,createdtime text not null);";  
    final Context context;  
      
    DatabaseHelper DBHelper;  
    SQLiteDatabase db;  
      
    public DBAdapter(Context cxt)  
    {  
        this.context = cxt;  
        DBHelper = new DatabaseHelper(context);  
    }  
      
    private static class DatabaseHelper extends SQLiteOpenHelper  
    {  
  
        DatabaseHelper(Context context)  
        {  
            super(context, DATABASE_NAME, null, DATABASE_VERSION);  
        }  
        @Override  
        public void onCreate(SQLiteDatabase db) {  
            // TODO Auto-generated method stub  
            try  
            {  
                db.execSQL(DATABASE_CREATE);  
            }  
            catch(SQLException e)  
            {  
                e.printStackTrace();  
            }  
        }  
  
        @Override  
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
            // TODO Auto-generated method stub  
            
            db.execSQL("DROP TABLE IF EXISTS contacts");  
            onCreate(db);  
        }  
    }  
      
    //open the database  
    public DBAdapter open() throws SQLException  
    {  
        db = DBHelper.getWritableDatabase();  
        return this;  
    }  
    //close the database  
    public void close()  
    {  
        DBHelper.close();  
    }  
      
    //insert a contact into the database  
    @SuppressLint("SimpleDateFormat")
	public long insertContact(String userName,String step, String distance, String time)  
    {  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    	Date  createdDate  = new Date(System.currentTimeMillis());
    	String str=sdf.format(createdDate);  
        ContentValues initialValues = new ContentValues();  
        initialValues.put(KEY_USER_NAME, userName);  
        initialValues.put(KEY_STEP, step);
        initialValues.put(KEY_DISTANCE, distance);  
        initialValues.put(KEY_TIME, time); 
        initialValues.put(KEY_CRESTED_TIME, str); 
        return db.insert(DATABASE_TABLE, null, initialValues);  
    }  
    //delete a particular contact  
    public boolean deleteContact(long rowId)  
    {  
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" +rowId, null) > 0;  
    }  
    //retreves all the contacts  
    public Cursor getAllContacts()  
    {  
    	 return db.query(DATABASE_TABLE, new String[]{ KEY_ROWID,KEY_NAME,KEY_EMAIL}, null, null, null, null, null);   
    }  
    //retreves a particular contact  
    public Cursor getContact(String userName) throws SQLException  
    {  
        Cursor mCursor =   
                db.query(DATABASE_TABLE, new String[]{KEY_ROWID,}, KEY_USER_NAME + "=" + userName, null, null, null, null);  
        if (mCursor != null)  
            mCursor.moveToFirst();  
    
        return mCursor;  
    }  
    //updates a contact  
    public boolean updateContact(long rowId, String name, String email)  
    {  
        ContentValues args = new ContentValues();  
        args.put(KEY_USER_NAME, name);  
        args.put(KEY_STEP, email);  
        return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" +rowId, null) > 0;  
    }  
}  