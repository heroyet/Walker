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
 *webView---健康新知
 * @author djh
 * @date 2015-08-01
 */
public class WebView_One extends Fragment{
	private WebView webView;
	private ImageView img_top;
	
    private ProgressDialog dialog;
    private Activity context; //上下文
    @SuppressLint("SetJavaScriptEnabled")
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View rootView = inflater.inflate(R.layout.guide_1, container, false);//关联布局文件  
        webView = (WebView) rootView.findViewById(R.id.webView); 
        img_top=(ImageView) rootView.findViewById(R.id.img_top);
        webView.loadUrl("http://cms.hxky.cn/wap/jkxz/");
        webView.getSettings().setJavaScriptEnabled(true);
        
        /**
         * 禁止系统浏览器打开页面
         */
        webView.setWebViewClient(new WebViewClient(){
        	@Override
        	public boolean shouldOverrideUrlLoading(WebView view, String url) {
        		view.loadUrl(url);
        		return true;
        	}
        });
        /**
         * 设置进度条
         */
        webView.setWebChromeClient(new WebChromeClient(){
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
                //newProgress 1-100之间的整数
				if(newProgress==100)
				{
					//网页加载完毕，关闭ProgressDialog
					closeDialog();
				}
				else
				{
					//网页正在加载,打开ProgressDialog
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
					dialog.setTitle("正在加载");
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
				 if(webView.canGoBack()){
	                	webView.goBack();
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
