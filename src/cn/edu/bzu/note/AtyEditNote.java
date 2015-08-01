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
 * �༭��־�����activity 
 * @author lb
 * @date 2015-07-30
 */
public class AtyEditNote extends ListActivity implements OnItemLongClickListener{

	/**
	 * ������
	 */
	private MediaAdapter adapter;
	/**
	 * ���±������ݿ��е�id
	 */
	public static final String EXTRA_NOTE_ID = "noteId";
	public static final String EXTRA_NOTE_NAME = "noteName";
	public static final String EXTRA_NOTE_CONTENT = "noteContent";
	/**
	 * ��ȡͼƬ��������
	 */
	public static final int REQUEST_CODE_GET_PHOTO = 1;

	/**
	 * ���ݿ�
	 */
	private int noteId = -1;//�ж����ݿ����Ƿ���ڸ�����
	private NotesDB db;
	private SQLiteDatabase dbRead,dbWrite;
	private String currentPath = null;
	/**
	 * �ؼ�
	 */
	private TextView edit_notes_title_bar_menu_complete;//���
	private TextView edit_notes_title_bar_menu_photo;//����
	private TextView edit_notes_title_bar_menu_cancel;//����
	
	private EditText etTitle, etContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
		setContentView(R.layout.aty_edit_note);
		initialize();
		db = new NotesDB(this);//��ȡ���ݿ�ʵ��
		dbRead = db.getReadableDatabase();
		dbWrite = db.getWritableDatabase();

		adapter = new MediaAdapter(this);
		setListAdapter(adapter);

		noteId = getIntent().getIntExtra(EXTRA_NOTE_ID, -1);

		if (noteId>-1) {
			//���ݴ����id��ȡ��Ϣ
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

		edit_notes_title_bar_menu_complete = (TextView) findViewById(R.id.edit_notes_title_bar_menu_complete);//���
		edit_notes_title_bar_menu_photo = (TextView) findViewById(R.id.edit_notes_title_bar_menu_photo);//����
		edit_notes_title_bar_menu_cancel = (TextView) findViewById(R.id.edit_notes_title_bar_menu_cancel);//����
		
		// ���ü�����
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
			case R.id.edit_notes_title_bar_menu_photo://����

				i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//String photoName = System.currentTimeMillis()+".jpg";//TODO ͼƬ����,�����ڶ�ý���б�����ʾ
				file = new File(getMediaDir(), System.currentTimeMillis()+".jpg");//TODO��Ҫ��ȡͼƬ������
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				currentPath = file.getAbsolutePath();
				/**
				 * ������ӵ���Ϣ�������ݿ���
				 */
				i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
				startActivityForResult(i, REQUEST_CODE_GET_PHOTO);
				break;
			case R.id.edit_notes_title_bar_menu_complete://����
				saveMedia(saveNote());
				setResult(RESULT_OK);
				//TODO��ô����ˢ����
//				xinlvActivity m =new xinlvActivity();
//				m.refreshView();
				finish();
				break;
			case R.id.edit_notes_title_bar_menu_cancel://ȡ��
				setResult(RESULT_CANCELED);
				finish();
				break;
			}
		}
	};
	
	
	/**
	 * �б�����
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
	 * ����ʼ�,����ֵΪ�����������ݿ��е�id 
	 */
	@SuppressLint("SimpleDateFormat")
	public int saveNote(){
		String etName = etTitle.getText().toString();

		ContentValues cv = new ContentValues();
		cv.put(NotesDB.COLUMN_NAME_NOTE_NAME, etName);
		cv.put(NotesDB.COLUMN_NAME_NOTE_CONTENT, etContent.getText().toString());
		cv.put(NotesDB.COLUMN_NAME_NOTE_DATE, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));

		if (noteId>-1) {//ԭ������,�����
			dbWrite.update(NotesDB.TABLE_NAME_NOTES, cv, NotesDB.COLUMN_NAME_ID+"=?", new String[]{noteId+""});
			return noteId;
		}else{//ԭ��������,�Ͳ���
			return (int) dbWrite.insert(NotesDB.TABLE_NAME_NOTES, null, cv);
		}

	}
	/**
	 *	��ȡ��ý��Ĵ洢·��
	 */
	public File getMediaDir(){
		//File(String parent, String child) --->���� parent ·�����ַ����� child ·�����ַ�������һ���� File ʵ����
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
			final int position, final long id) {//idΪ���ݿ��е�_id,�˴���Ϊ�������ܱ�onClick()��������
		// TODO Auto-generated method stub
		
		new AlertDialog.Builder(AtyEditNote.this)
		.setTitle("ȷ��Ҫɾ����")
		.setNegativeButton("ȡ��", null)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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
				//Log.e("---->>>><<<<----", path);//  TODO ���Եõ�·��
				//TODOɾ���ڴ��е�ͼƬ
				File deleteFile = new File(path);
				deleteFile.delete();//ɾ���ڴ��е�ͼƬ
				dbWrite.delete(NotesDB.TABLE_NAME_MEDIA, NotesDB.COLUMN_NAME_ID+"=?", new String[]{id+""});//ɾ�����ݿ��е�ͼƬ
				//ɾ��adapter�󶨵�listView������
				adapter.delete(position);
				adapter.notifyDataSetChanged();
				
				Toast.makeText(AtyEditNote.this, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
			}
		})
		.show();
		return true;
	}

	

}