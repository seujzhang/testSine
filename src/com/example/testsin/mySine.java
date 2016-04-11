package com.example.testsin; 

import android.app.Activity;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;   
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomButton; 

public class mySine extends Activity{  
	
    private TextView frequency=null;   
    private TextView phase=null;   
    private TextView amplifier=null;   
    private Button   btnwave=null;   
    private SineWave sw = null;  
    private LinearLayout sinLayout;
    
    private int mode = 0;
    private float oldDist = 0;
    private float oldDistX = 0;
    private float oldDistY = 0;
       
   @Override  
   protected void onCreate(Bundle savedInstanceState) {   
          super.onCreate(savedInstanceState);   
          sw = new SineWave(this);  
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          setContentView(R.layout.main);   	   
          btnwave = (Button)findViewById(R.id.wave);   
          frequency = (TextView)findViewById(R.id.frequency);   
          phase = (TextView)findViewById(R.id.phase);   
          amplifier = (TextView)findViewById(R.id.amplifier); 
          sinLayout = (LinearLayout) findViewById(R.id.sin_layout);
          btnwave.setOnClickListener(new Button.OnClickListener(){   
                 @Override  
                 public void onClick(View arg0) {   
                        // TODO Auto-generated method stub   
                        sw.Set(Float.parseFloat(amplifier.getText().toString()), 
                        		Float.parseFloat(frequency.getText().toString()), 
                        		Float.parseFloat(phase.getText().toString()));   
                 }   
          }); 
          
          sinLayout.setOnTouchListener(new OnTouchListener() {	//监听触摸事件
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {  
		        case MotionEvent.ACTION_DOWN:  
		            mode = 1;  
		            break;  
		        case MotionEvent.ACTION_UP:  
		            mode = 0;  
		            break;  
		        case MotionEvent.ACTION_POINTER_UP:  
		            mode -= 1;  
		            break;  
		        case MotionEvent.ACTION_POINTER_DOWN:  
		            oldDist = distance(event);  
		            mode += 1;  
		            break;  		  
		        case MotionEvent.ACTION_MOVE:  //当有点在屏幕上移动时触发
		            if (mode >= 2) {  
		                float newDist = distance(event);  
		                if (newDist > oldDist + 1) {  //+1/-1都是在控制灵敏度
		                    zoom(newDist / oldDist);  
		                    oldDist = newDist;  
		                }  
		                if (newDist < oldDist - 1) {  
		                    zoom(newDist / oldDist);  
		                    oldDist = newDist;  
		                }  
		            }  
		            break;  
	            default:
	            	break;
		        }  
		        return true;  
			}
		});
   } 
   
   private float distanceX(MotionEvent event){
	   return Math.abs(event.getX(0)-event.getX(1));
   }
   
   private float distanceY(MotionEvent event){
	   return Math.abs(event.getY(0)-event.getY(1));
   }
   
   private float distance(MotionEvent event){
	   float x = event.getX(0)-event.getX(1);
	   float y = event.getY(0)-event.getY(1);
	   return FloatMath.sqrt(Math.abs(x*x-y*y));
   }
   
   private void zoom(float f){   //这里放大缩小了幅度，有什么卵用？(并且只能缩放Y方向)坐标也应该一起缩小放大?
	   sw.setAmpplifier(sw.GetAmplifier()*f);
	   //sw.setHeight(sw.getHeight()*f);
   }
   
   @Override  
   protected void onStart() {   
          // TODO Auto-generated method stub   
          super.onStart();   
          frequency.setText(Float.toString(sw.GetFrequency()));   
          phase.setText(Float.toString(sw.GetPhase()));   
          amplifier.setText(Float.toString(sw.GetAmplifier()));   
   } 
   
   @Override  
   public boolean onTouchEvent(MotionEvent event) {   
          // TODO Auto-generated method stub   
          //float px = event.getX();   
          //float py = event.getY();   
          //sw.SetXY(px, py);   
          return super.onTouchEvent(event);   
   }   
}  