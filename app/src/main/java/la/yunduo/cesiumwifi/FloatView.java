package la.yunduo.cesiumwifi;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import la.yunduo.monitor_service.Traffic_Service;

public class FloatView extends View {
	
	WindowManager mWManger;
	WindowManager.LayoutParams mWManParams;
	
	public View view;
	

	private float startX;
	private float startY;
	

	private float x;
	private float y;
	

	private float mTouchSatrtX;
	private float mTouchStartY;
	

	public ImageView img_folat,img_close;
	public TextView tv_show;
	
	Context mContext;
	
	public FloatView(Context context) {
		super(context);
		this.mContext = context;
	}

	public void show(){
		mWManger = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mWManParams = new WindowManager.LayoutParams();
		

		mWManParams.type = 2002;
		mWManParams.flags |= 8;

		mWManParams.gravity = Gravity.TOP|Gravity.LEFT;
		

		mWManParams.x = 0;
		mWManParams.y = 0;
		

		mWManParams.width = 80;
		mWManParams.height = 40;
		
		mWManParams.format = -3;
		

		view = LayoutInflater.from(mContext).inflate(R.layout.traffic_view, null);
		
		
		mWManger.addView(view, mWManParams);
		
		
		view.setOnTouchListener(new OnTouchListener() {
			/**
			 * �ı�������λ��
			 */
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				x = event.getRawX();
				y = event.getRawY() - 25;

				
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					startX = x;
					startY = y;
					

					mTouchSatrtX = event.getX();
					mTouchStartY = event.getY();
					

					
					break;
					
				case MotionEvent.ACTION_MOVE:
					updatePosition();
					
					break;
					
				case MotionEvent.ACTION_UP:
					updatePosition();
					
					show_img_close();
					
					mTouchSatrtX = mTouchStartY =0;
					
					break;
				}
				
				
				
				return true;
			}
		});

		img_close = (ImageView) view.findViewById(R.id.img_close);

		img_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
                /*
				Intent intent = new Intent(mContext,Traffic_Service.class);
				mContext.stopService(intent);
				view.setVisibility(View.GONE);
				*/
			}
		});

		
		
		tv_show = (TextView) view.findViewById(R.id.tv_show);
		
		
		
	}
	

	public void updatePosition(){
		mWManParams.x = (int) (x - mTouchSatrtX);
		mWManParams.y = (int) (y - mTouchStartY);
		
		mWManger.updateViewLayout(view, mWManParams);
	}

	public void show_img_close() {
		if (Math.abs(x - startX) < 1.5 && Math.abs(y - startY) < 1.5
				&& !img_close.isShown()) {
			img_close.setVisibility(View.VISIBLE);
		} else if (img_close.isShown()) {
			img_close.setVisibility(View.GONE);
		}
	}
    public void img_gone() {
            img_close.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            Intent intent = new Intent(mContext,Traffic_Service.class);
            mContext.stopService(intent);
        }


	
}
