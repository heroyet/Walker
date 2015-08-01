package cn.edu.bzu.util;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * �������ӹ�����
 * @category ����������ӣ�����������ʱ��ʾ��������Ի���
 * @author monster
 * @date 2015-07-19
 */
public class TestNetWork {
	
	/**
	 * �����������
	 * @param con
	 * @return
	 */
	public boolean isNetworkAvailable(Context con) {
		ConnectivityManager cm = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null)
			return false;
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		if (netinfo == null) {
			return false;
		}
		if (netinfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * ��ʾ�����������ӶԻ���
	 * @param context
	 */
	public void showNetDialog(final Context context){
		 AlertDialog.Builder builder = new AlertDialog.Builder(context);  
         builder.setTitle("����������").setMessage("�Ƿ�������������?");  
           
         builder.setPositiveButton("��", new DialogInterface.OnClickListener() {  
             @Override  
             public void onClick(DialogInterface dialog, int which) {  
                 Intent intent = null;  
                   
                 try {  
                     @SuppressWarnings("deprecation")
					String sdkVersion = android.os.Build.VERSION.SDK;  
                     if(Integer.valueOf(sdkVersion) > 10) {  
                         intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);  
                     }else {  
                         intent = new Intent();  
                         ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");  
                         intent.setComponent(comp);  
                         intent.setAction("android.intent.action.VIEW");  
                     }  
                     context.startActivity(intent);  
                 } catch (Exception e) {  
                     e.printStackTrace();  
                 }  
             }  
         }).setNegativeButton("��", new DialogInterface.OnClickListener() {  
             @Override  
             public void onClick(DialogInterface dialog, int which) {  
                 dialog.cancel();          
             }  
         }).show();  
	}
}
