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
 * �ռ�������
 * @author lb
 * @date 2015-07-30
 */
public class NoteMain extends ListActivity implements OnClickListener {
	//�ؼ���ʼ��
	//TODO
	private TextView note_list_title_bar_menu_add_note;
	private TextView note_list_title_bar_menu_cancel;

//	private TextView  tvEmpty;


	//�����ʼ��
	private SimpleCursorAdapter adapter = null;

	//���ݿ�
	private NotesDB db;
	private SQLiteDatabase dbRead;
	private SQLiteDatabase dbWrite;

	//������
	public static final int REQUEST_CODE_EDIT_NOTE = 1;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.note_main);

		//TODO��ʼ��
		initialize();

		//��ȡ���ݿ����
		db = new NotesDB(NoteMain.this);
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();
		//��SimpleCursorAdapter�Ļ�ȡ�����ݿ���Ϣ,�벼���еĶ�Ӧ�ؼ���
		adapter = new SimpleCursorAdapter(NoteMain.this, R.layout.note_item, null, 
				new String[]{NotesDB.COLUMN_NAME_NOTE_NAME,NotesDB.COLUMN_NAME_NOTE_DATE}, 
				new int[]{R.id.tvName,R.id.tvDate});
		//ΪlistView��������
		setListAdapter(adapter);
		refreshNotesListView();
	}

	/**
	 *  �ؼ���ʼ��
	 */
	private void initialize(){
		//TODO  �ؼ���ʼ��
		note_list_title_bar_menu_add_note = (TextView) this.findViewById(R.id.note_list_title_bar_menu_add_note);
		note_list_title_bar_menu_cancel = (TextView) this.findViewById(R.id.note_list_title_bar_menu_cancel);
		note_list_title_bar_menu_cancel.setOnClickListener(this);
		note_list_title_bar_menu_add_note.setOnClickListener(this);

		//�б�����,������ʾ��Ϣ����
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				Cursor c = adapter.getCursor();//��ȡCursor����
				c.moveToPosition(position);//�ƶ�����������б���

				Intent i = new Intent(NoteMain.this, AtyEditNote.class);
				//���ݱ������� ��Ӧ��Ϣ
				i.putExtra(AtyEditNote.EXTRA_NOTE_ID, c.getInt(c.getColumnIndex(NotesDB.COLUMN_NAME_ID)));
				i.putExtra(AtyEditNote.EXTRA_NOTE_NAME, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_NAME)));
				i.putExtra(AtyEditNote.EXTRA_NOTE_CONTENT, c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_NOTE_CONTENT)));
				startActivityForResult(i, REQUEST_CODE_EDIT_NOTE);				
			}
		});

		//�����¼�
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, final long id) {//idΪ���ݿ��е�_id,�˴���Ϊ�������ܱ�onClick()��������
				
				new AlertDialog.Builder(NoteMain.this)
				.setTitle("ȷ��Ҫɾ����")
				.setNegativeButton("ȡ��", null)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//ɾ��������־
						dbWrite.delete(NotesDB.TABLE_NAME_NOTES, NotesDB.COLUMN_NAME_ID+"=?", new String[]{id+""});
						//TODOҪɾ����id�µ�����media
						Cursor c = dbRead.query(NotesDB.TABLE_NAME_MEDIA, null, NotesDB.COLUMN_NAME_MEDIA_OWNER_NOTE_ID+"=?", new String[]{id+""}, null, null, null);
						while(c.moveToNext()){
							 String path = c.getString(c.getColumnIndex(NotesDB.COLUMN_NAME_MEDIA_PATH));
							 File deleteFile = new File(path);
							 deleteFile.delete();
						}
						refreshNotesListView();
						Toast.makeText(NoteMain.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
					}
				})
				.show();

				return true;
			}
		});
	}

	
	/**
	 * Activity���غ�ִ��
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_EDIT_NOTE://����ʾ���淵��,ˢ������
			if (resultCode == Activity.RESULT_OK) {
				refreshNotesListView();
				adapter.notifyDataSetChanged();
			}
			break;
		}

	}
	
	/**
	 * ˢ��listView
	 */
	public void refreshNotesListView(){
		/**
		 * ͨ���ı��ѯ���������listView
		 */
		adapter.changeCursor(dbRead.query(NotesDB.TABLE_NAME_NOTES, null, null, null, null, null, null));

	}

	@Override
	public void onClick(View view) {
		// TODO ����������
		switch (view.getId()) {
		case R.id.note_list_title_bar_menu_add_note:
			startActivityForResult(new Intent(NoteMain.this, AtyEditNote.class), REQUEST_CODE_EDIT_NOTE);
			break;
		case R.id.note_list_title_bar_menu_cancel://TODO���˴���
			finish();
			break;
		}

	}

	/**
	 * ��NoteMain������ʱ,�رյ����ݿ�Ķ�д��
	 */
	@Override
	public void onDestroy() {
		dbRead.close();
		dbWrite.close();
		super.onDestroy();
	}

}
