package djh.bzu.tongji.ui.proggress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;


/**
 * 杜锦华
 * 2015.7.21
 * 这个类主要用来绘制进度条
 * */

public class CircleProgressBar extends View {

	private int maxProgress = 100;
	private int progress = 0;
	private int progressStrokeWidth = 2;
	private int marxArcStorkeWidth = 16;
	
	RectF oval;
	Paint paint;

	public CircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		oval = new RectF();
		paint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();

		width = (width > height) ? height : width;
		height = (width > height) ? height : width;

		paint.setAntiAlias(true); 
		paint.setColor(Color.WHITE); 
		canvas.drawColor(Color.TRANSPARENT); 
		paint.setStrokeWidth(progressStrokeWidth); 
		paint.setStyle(Style.STROKE);

		oval.left = marxArcStorkeWidth / 2;
		oval.top = marxArcStorkeWidth / 2; 
		oval.right = width - marxArcStorkeWidth / 2; 
		oval.bottom = height - marxArcStorkeWidth / 2; 

		canvas.drawArc(oval, -90, 360, false, paint); 
		paint.setColor(Color.rgb(0x57, 0x87, 0xb6));
		paint.setStrokeWidth(marxArcStorkeWidth);
		canvas.drawArc(oval, -90, ((float) progress / maxProgress) * 360,
				false, paint); 

		paint.setStrokeWidth(1);
		String text = progress + "%";
		int textHeight = height / 4;
		paint.setTextSize(textHeight);
		int textWidth = (int) paint.measureText(text, 0, text.length());
		paint.setStyle(Style.FILL);
		canvas.drawText(text, width / 2 - textWidth / 2, height / 2
				+ textHeight / 2, paint);

	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	
	public void setProgress(int progress, View view) {
		this.progress = progress;
		view.setAnimation(pointRotationAnima(0,
				(int) (((float) 360 / maxProgress) * progress)));
		this.invalidate();
	}

	
	public void setProgressNotInUiThread(int progress, View view) {
		this.progress = progress;
		view.setAnimation(pointRotationAnima(0,
				(int) (((float) 360 / maxProgress) * progress)));
		this.postInvalidate();
	}

	
	private Animation pointRotationAnima(float fromDegrees, float toDegrees) {
		int initDegress = 306;
		RotateAnimation animation = new RotateAnimation(fromDegrees,
				initDegress + toDegrees, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(1);
		animation.setRepeatCount(1);
		animation.setFillAfter(true);
		return animation;
	}

}