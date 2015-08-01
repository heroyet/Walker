package cn.edu.bzu.note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bzu.adapter.MediaAdapter;
import cn.edu.bzu.entity.MediaListCellData;
import cn.edu.bzu.entity.MediaListCellData.MediaType;
import cn.edu.bzu.note.database.NotesDB;
import cn.edu.bzu.walker.R;


/**
 * 编辑日志界面的activity 
 * @author lb
 * @date 2015-07-30
 */
public class AtyEditNote extends ListActivity implements OnItemLongClickListener{

	/**
	 * 适配器
	 */
	private MediaAdapter adapter;
	/**
	 * 记事本在数据库中的id
	 */
	public static final String EXTRA_NOTE_ID = "noteId";
	public static final String EXTRA_NOTE_NAME = "noteName";
	public static final String EXTRA_NOTE_CONTENT = "noteContent";
	/**
	 * 获取图片的请求码
	 */
	public static final int REQUEST_CODE_GET_PHOTO = 1;

	/**
	 * 数据库
	 */
	private int noteId = -1;//判断数据库中是否存在该数据
	private NotesDB db;
	private SQLiteDatabase dbRead,dbWrite;
	private String currentPath = null;
	/**
	 * 控件
	 */
	private TextView edit_notes_title_bar_menu_complete;//完成
	private TextView edit_notes_title_bar_menu_photo;//拍照
	private TextView edit_notes_title_bar_menu_cancel;//回退
	
	private EditText etTitle, etContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.aty_edit_note);
		initialize();
		db = new NotesDB(this);//获取数据库实例
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();

		adapter = new MediaAdapter(this);
		setListAdapter(adapter);

		noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

		if (noteId>-1) {
			//根据传入的id获取信息
			Cursor cInput = dbRead.query(NotesDB.TABLE_NAME_NOTES, null, NotesDB.COLUMN_NAME_ID+"=?", new String[]{noteId+""}, null, null, null);
			if(cInput.moveToFirst()){
				String title = cInput.getString(cInput.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME));
				
				etTitle.setText(title);
				String content = cInput.getString(cInput.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT));
				etContent.setText(content);	
			}
			

			Cursor c = dbRead.query(NotesDB.TABLE_NAME_MEDIA, null, NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID+"=?", new String[]{noteId+""}, null, null, null);
			while(c.moveToNext()){
				adapter.add(new MediaListCellData(c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH)),c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID))));
			}
			adapter.notifyDataSetChanged();
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		System.out.println(data);

		switch (requestCode) {
		case REQUEST_CODE_GET_PHOTO:
			if (resultCode==RESULT_OK) {
				adapter.add(new MediaListCellData(currentPath));
				adapter.notifyDataSetChanged();
			}
			break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private void initialize(){
		etContent = (EditText) findViewById(R.id.etContent);
		etTitle =  (EditText) findViewById(R.id.etTitle);

		edit_notes_title_bar_menu_complete = (TextView) findViewById(R.id.edit_notes_title_bar_menu_complete);//完成
		edit_notes_title_bar_menu_photo = (TextView) findViewById(R.id.edit_notes_title_bar_menu_photo);//拍照
		edit_notes_title_bar_menu_cancel = (TextView) findViewById(R.id.edit_notes_title_bar_menu_cancel);//回退
		
		// 设置监听器
		edit_notes_title_bar_menu_complete.setOnClickListener(btnClickHandler);
		edit_notes_title_bar_menu_photo.setOnClickListener(btnClickHandler);
		edit_notes_title_bar_menu_cancel.setOnClickListener(btnClickHandler);
		
		getListView().setOnItemLongClickListener(this);
	}

	private OnClickListener btnClickHandler=new OnClickListener() {

		Intent i;File file;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.edit_notes_title_bar_menu_photo://拍照

				i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//String photoName = System.currentTimeMillis()+".jpg";//TODO 图片名字,用以在多媒体列表中显示
				file = new File(getMediaDir(), System.currentTimeMillis()+".jpg");//TODO需要获取图片的名字
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				currentPath = file.getAbsolutePath();
				/**
				 * 将新添加的消息放入数据库中
				 */
				i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(i, REQUEST_CODE_GET_PHOTO);
				break;
			case R.id.edit_notes_title_bar_menu_complete://保存
				saveMedia(saveNote());
				setResult(RESULT_OK);
				//TODO怎么做到刷新呢
//				xinlvActivity m =new xinlvActivity();
//				m.refreshView();
				finish();
				break;
			case R.id.edit_notes_title_bar_menu_cancel://取消
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
		}
	};
	
	
	/**
	 * 列表项被点击
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		MediaListCellData data = adapter.getItem(position);
		Intent i;
		
		switch (data.getType()) {
		case MediaType.PHOTO:
			i = new Intent(this, AtyPhotoViewer.class);
			i.putExtra(AtyPhotoViewer.EXTRA_PATH, data.getPath());
			startActivity(i);
			break;
		}
		
		super.onListItemClick(l, v, position, id);
	}

	/**
	 * 保存笔记,返回值为该数据在数据库中的id 
	 */
	@SuppressLint("SimpleDateFormat")
	public int saveNote(){
		String etName = etTitle.getText().toString();

		ContentValues cv = new ContentValues();
		cv.put(NotesDB.COLUMN_NAME_NOTE_NAME, etName);
		cv.put(NotesDB.COLUMN_NAME_NOTE_CONTENT, etContent.getText().toString());
		cv.put(NotesDB.COLUMN_NAME_NOTE_DATE, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

		if (noteId>-1) {//原本存在,则更新
			dbWrite.update(NotesDB.TABLE_NAME_NOTES, cv, NotesDB.COLUMN_NAME_ID+"=?", new String[]{noteId+""});
			return noteId;
		}else{//原本不存在,就插入
			return (int) dbWrite.insert(NotesDB.TABLE_NAME_NOTES, null, cv);
		}

	}
	/**
	 *	获取多媒体的存储路径
	 */
	public File getMediaDir(){
		//File(String parent, String child) --->根据 parent 路径名字符串和 child 路径名字符串创建一个新 File 实例。
		File dir = new File(Environment.getExternalStorageDirectory(), "NotesMedia");
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return dir;
	}


	@Override
	protected void onDestroy() {
		dbRead.close();
		dbWrite.close();
		super.onDestroy();
	}


	public void saveMedia(int noteId){

		MediaListCellData data;
		ContentValues cv;

		for (int i = 0; i < adapter.getCount(); i++) {
			data = adapter.getItem(i);

			if (data.getId()<=-1) {
				cv = new ContentValues();
				cv.put(NotesDB.COLUMN_NAME_MEDIA_PATH, data.getPath());
				cv.put(NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID, noteId);
				dbWrite.insert(NotesDB.TABLE_NAME_MEDIA, null, cv);
			}
		}

	}


	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			final int position, final long id) {//id为数据库中的_id,此处需为常量才能被onClick()方法调用
		// TODO Auto-generated method stub
		
		new AlertDialog.Builder(AtyEditNote.this)
		.setTitle("确定要删除吗")
		.setNegativeButton("取消", null)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO
				String path="";
				MediaListCellData data = adapter.getItem(position);
				
				switch (data.getType()) {
				case MediaType.PHOTO:
					path = data.getPath();
					break;
				}
				//Log.e("---->>>><<<<----", path);//  TODO 可以得到路径
				//TODO删除内存中的图片
				File deleteFile = new File(path);
				deleteFile.delete();//删除内存中的图片
				dbWrite.delete(NotesDB.TABLE_NAME_MEDIA, NotesDB.COLUMN_NAME_ID+"=?", new String[]{id+""});//删除数据库中的图片
				//删除adapter绑定的listView的数据
				adapter.delete(position);
				adapter.notifyDataSetChanged();
				
				Toast.makeText(AtyEditNote.this, "删除成功", Toast.LENGTH_SHORT).show();
			}
		})
		.show();
		return true;
	}

	

}