package cn.edu.bzu.entity;

import cn.edu.bzu.walker.R;

/**
 * 照片信息的实体类
 * @author lb
 * @date 2015-07-30
 */
public class MediaListCellData {

	int type = 0;
	int id = -1;
	String path = "";
	int iconId  = 0;

	/**
	 * 
	 * @param path 存储路径
	 * @param id  所属的记事本id
	 */
	public MediaListCellData(String path,int id) {
		this(path);
		this.id = id;
	}

	public MediaListCellData(String path) {
		this.path = path;

		if (path.endsWith(".jpg")) {
			iconId = R.drawable.icon_photo;
			type = MediaType.PHOTO;
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(int iconId) {
		this.iconId = iconId;
	}




	public static class MediaType{
		public static final int PHOTO = 1;
	}
}