package cn.edu.bzu.healthy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import cn.edu.bzu.walker.R;

/**
 *webView---��������
 * @author djh
 * @date 2015-08-01
 */
public class WebView_Two extends Fragment{
	private WebView webView2;
    private ProgressDialog dialog;
    private Activity context; //������
	private ImageView img_top;
	
    @SuppressLint("SetJavaScriptEnabled")
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View rootView = inflater.inflate(R.layout.guide_2, container, false);//���������ļ�  
        webView2 = (WebView) rootView.findViewById(R.id.webView2); 
        img_top=(ImageView) rootView.findViewById(R.id.img_top);
        webView2.loadUrl("http://cms.hxky.cn/wap/jbfz/");
        webView2.getSettings().setJavaScriptEnabled(true);
        /**
         * ��ֹϵͳ�������ҳ��
         */
        webView2.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl(url);
        		return true;
        	}
        });
        /**
         * ���ý�����
         */
        webView2.setWebChromeClient(new WebChromeClient(){
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
                //newProgress 1-100֮�������
				if(newProgress==100)
				{
					//��ҳ������ϣ��ر�ProgressDialog
					closeDialog();
				}
				else
				{
					//��ҳ���ڼ���,��ProgressDialog
					openDialog(newProgress);
				}
			}

			private void closeDialog() {
                  if(dialog!=null&&dialog.isShowing())
                  {
                	     dialog.dismiss();
                	     dialog=null;
                  }
			}

			private void openDialog(int newProgress) {
				if(dialog==null)
				{
					dialog=new ProgressDialog(context);
					dialog.setTitle("���ڼ���");
					dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					dialog.setProgress(newProgress);
					dialog.show();
					
				}
				else
				{
					dialog.setProgress(newProgress);
				}
			
				
			}
		});
        
 img_top.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 if(webView2.canGoBack()){
	                	webView2.goBack();
	                }
			}
		});
        
        
     
        return rootView;  
    }  

    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		context=activity;
	}

}
