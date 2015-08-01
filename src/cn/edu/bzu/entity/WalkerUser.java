package cn.edu.bzu.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * �û���Ϣʵ����
 * @author monster
 * @date:2015-07-23
 *  *˵������ʵ����̳е�BmobUser���������ű���ΪBmob��ϵͳ�������ڲ����ֶ�
 * �Ѿ�����ã����Բ���Ҫ�ظ����壬����ظ����壬�����³������
 * ��¼���Ѻ��ֶΣ�
 *   		objectId,username,password,email,authData........
 *   ���ԣ��û����������ʵ�����ʱ��ֻ��Ҫ��ӳ����⼸���ֶξ�ok��
 */
@SuppressWarnings("serial")
public class WalkerUser extends BmobUser implements Serializable {
	
	/**������Ϣ**/
	private String motto; //������
	private BmobFile userPhoto;//�û�ͷ��
	private String city;   //�û��ĳ���
	private String nick; //�û����ǳ�
	
	/**������ڵı���**/
	private String stepLength;//����
	private String userHeight; //���
	private String userWeight; //����
	private String userFeatherSport; //�û�Ԥ���˶���(����)
	
	
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMotto() {
		return motto;
	}
	public void setMotto(String motto) {
		this.motto = motto;
	}
	public BmobFile getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(BmobFile userPhoto) {
		this.userPhoto = userPhoto;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStepLength() {
		return stepLength;
	}
	public void setStepLength(String stepLength) {
		this.stepLength = stepLength;
	}
	public String getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(String userHeight) {
		this.userHeight = userHeight;
	}
	public String getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(String userWeight) {
		this.userWeight = userWeight;
	}
	public String getUserFeatherSport() {
		return userFeatherSport;
	}
	public void setUserFeatherSport(String userFeatherSport) {
		this.userFeatherSport = userFeatherSport;
	}
	
}
