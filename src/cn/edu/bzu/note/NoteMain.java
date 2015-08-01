package cn.edu.bzu.note;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.bzu.note.database.NotesDB;
import cn.edu.bzu.walker.R;

/**
 * 日记主界面
 * @author lb
 * @date 2015-07-30
 */
public class NoteMain extends ListActivity implements OnClickListener {
	//控件初始化
	//TODO
	private TextView note_list_title_bar_menu_add_note;
	private TextView note_list_title_bar_menu_cancel;

//	private TextView  tvEmpty;


	//所需初始化
	private SimpleCursorAdapter adapter = null;

	//数据库
	private NotesDB db;
	private SQLiteDatabase dbRead;
	private SQLiteDatabase dbWrite;

	//请求码
	public static final int REQUEST_CODE_EDIT_NOTE = 1;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.note_main);

		//TODO初始化
		initialize();

		//获取数据库对象
		db = new NotesDB(NoteMain.this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();
		//将SimpleCursorAdapter的获取的数据库信息,与布局中的对应控件绑定
		adapter = new SimpleCursorAdapter(NoteMain.this, R.layout.note_item, null, 
				new String[]{NotesDB.COLUMN_NAME_NOTE_NAME,NotesDB.COLUMN_NAME_NOTE_DATE}, 
				new int[]{R.id.tvName,R.id.tvDate});
		//为listView绑定适配器
		setListAdapter(adapter);
		refreshNotesListView();
	}

	/**
	 *  控件初始化
	 */
	private void initialize(){
		//TODO  控件初始化
		note_list_title_bar_menu_add_note = (TextView) this.findViewById(R.id.note_list_title_bar_menu_add_note);
		note_list_title_bar_menu_cancel = (TextView) this.findViewById(R.id.note_list_title_bar_menu_cancel);
		note_list_title_bar_menu_cancel.setOnClickListener(this);
		note_list_title_bar_menu_add_note.setOnClickListener(this);

		//列表项被点击,启动显示信息界面
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Cursor c = adapter.getCursor();//获取Cursor对象
				c.moveToPosition(position);//移动至被点击的列表项

				Intent i = new Intent(NoteMain.this, AtyEditNote.class);
				//传递被点击项的 对应信息
				i.putExtra(AtyEditNote.EXTRA_NOTE_ID, c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
				i.putExtra(AtyEditNote.EXTRA_NOTE_NAME, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
				i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT)));
				startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);				
			}
		});

		//长按事件
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, final long id) {//id为数据库中的_id,此处需为常量才能被onClick()方法调用
				
				new AlertDialog.Builder(NoteMain.this)
				.setTitle("确定要删除吗")
				.setNegativeButton("取消", null)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//删除文字日志
						dbWrite.delete(NotesDB.TABLE_NAME_NOTES, NotesDB.COLUMN_NAME_ID+"=?", new String[]{id+""});
						//TODO要删除该id下的所有media
						Cursor c = dbRead.query(NotesDB.TABLE_NAME_MEDIA, null, NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID+"=?", new String[]{id+""}, null, null, null);
						while(c.moveToNext()){
							 String path = c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH));
							 File deleteFile = new File(path);
							 deleteFile.delete();
						}
						refreshNotesListView();
						Toast.makeText(NoteMain.this, "删除成功", Toast.LENGTH_SHORT).show();
					}
				})
				.show();

				return true;
			}
		});
	}

	
	/**
	 * Activity返回后执行
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_EDIT_NOTE://从显示界面返回,刷新数据
			if (resultCode == Activity.RESULT_OK) {
				refreshNotesListView();
				adapter.notifyDataSetChanged();
			}
			break;
		}

	}
	
	/**
	 * 刷新listView
	 */
	public void refreshNotesListView(){
		/**
		 * 通过改变查询结果来更新listView
		 */
		adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null, null, null, null, null));

	}

	@Override
	public void onClick(View view) {
		// TODO 标题点击处理
		switch (view.getId()) {
		case R.id.note_list_title_bar_menu_add_note:
			startActivityForResult(new Intent(NoteMain.this, AtyEditNote.class), REQUEST_CODE_EDIT_NOTE);
			break;
		case R.id.note_list_title_bar_menu_cancel://TODO后退处理
			finish();
			break;
		}

	}

	/**
	 * 在NoteMain被销毁时,关闭掉数据库的读写器
	 */
	@Override
	public void onDestroy() {
		dbRead.close();
		dbWrite.close();
		super.onDestroy();
	}

}
