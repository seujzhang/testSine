package com.example.testsin;   

import android.content.Context;   
import android.graphics.Canvas;   
import android.graphics.Color;   
import android.graphics.Paint;   
import android.graphics.Path;   
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;   
import android.view.View;   
import java.lang.Math;  

public class SineWave extends View implements Runnable{   
	
    private  Paint mPaint = null;   
    private  static float amplifier = 100.0f;   
    private  static float frequency = 2.0f;    //2Hz   
    private  static float phase = 45.0f;         //相位    
    private  int height = 0;   
    private  int width = 0;   
    private  static float px=-1,py=-1;   
    private  boolean sp=false;  
    
    public SineWave(Context context){   
       super(context);   
       mPaint = new Paint();   
       new Thread(this).start();          
    }   
    
    //如果不写下面的构造函数，则会报错：custom view SineWave is not using the 2- or 3-argument View constructors   
    public SineWave(Context context, AttributeSet attrs){   
       super(context,attrs);   
       mPaint = new Paint();   
       new Thread(this).start();      
    } 
    
    public SineWave(Context context,float amplifier,float frequency,float phase){   
       super(context);   
       this.frequency = frequency;   
       this.amplifier = amplifier;   
       this.phase     = phase;   
       mPaint = new Paint();   
       new Thread(this).start();   
    } 
    
    public void setAmpplifier(float amplifier){
    	this.amplifier = amplifier;
    }
    
    public void setFrequency(float frequency){
    	this.frequency = frequency;
    }
    
    public void setPhase(float phase){
    	this.phase = phase;
    }
    
    
    public float GetAmplifier(){   
       return amplifier;   
    }  
    
    public float GetFrequency(){   
       return frequency;   
    } 
    
    public float GetPhase(){   
       return phase;   
    }  
    
    public void Set(float amplifier,float frequency,float phase){     	
       this.frequency = frequency;   
       this.amplifier = amplifier;   
       this.phase     = phase;   
    }  
    
    public void SetXY(float px,float py){
    	this.px = px;   
        this.py = py;
    }  
    
    public void onDraw(Canvas canvas){  
       super.onDraw(canvas);   
       canvas.drawColor(Color.WHITE);   
       height = this.getHeight();   
       width  = this.getWidth();   
       mPaint.setAntiAlias(true);  //抗锯齿，图像边缘相对清晰 
       mPaint.setColor(Color.BLACK);  
       
       canvas.drawText("幅度值", 2, 15,  mPaint);
	   canvas.drawText("原点(0,0)", 2, getHeight()/2, mPaint);
	   canvas.drawText("时间)",getWidth()-40, getHeight()/2,  mPaint);
	   //canvas.drawLine(2, 0, 0, height, mPaint);
	   //canvas.drawLine(2, getHeight()/2, getWidth(), getHeight()/2, mPaint);
	   canvas.drawLine(0, 0, 0, height, mPaint);
	   canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, mPaint);	   
		
       amplifier = (amplifier*2>height)?(height/2):amplifier;  
       Log.d("SineWave", "width= "+width);
       Log.d("SineWave", "height= "+height);
       Log.d("SineWave", "amplifier= "+amplifier);
       mPaint.setAlpha(200);   //设置透明度
       mPaint.setStrokeWidth(5);   
       float cy = height/2;   
       //float py=this.py-this.getTop();   
       for(int i=0;i<width-1;i++){
    	   mPaint.setColor(Color.GREEN);
    	   canvas.drawLine((float)i, cy-amplifier*(float)(Math.sin(phase*2*(float)Math.PI/360.0f+2*Math.PI*frequency*i/width)), 
    			   (float)(i+1), cy-amplifier*(float)(Math.sin(phase*2*(float)Math.PI/360.0f+2*Math.PI*frequency*(i+1)/width)), mPaint);
    	   float point = cy-amplifier*(float)(Math.sin(phase*2*(float)Math.PI/360.0f+2*Math.PI*frequency*i/width));   
           if((py>=(point-2.5f))&&(py<=(point+2.5f))&&(px>=i-2.5f)&&(px<=i+2.5f))   
              sp = true;
       }        
       if(sp){   
           mPaint.setColor(Color.RED);   
           mPaint.setTextSize(20);   
           canvas.drawText("(x="+Float.toString(px)+",y="+Float.toString(py)+")", 60, 20, mPaint);             
           sp = false;   
       }         
       mPaint.setColor(Color.BLUE);   
       mPaint.setTextSize(20);   
       canvas.drawText("(x="+Float.toString(px)+",y="+Float.toString(py)+")", 20, this.getHeight()-20, mPaint);   
    }  
    
    @Override  
    public boolean onTouchEvent(MotionEvent event) {   
       // TODO Auto-generated method stub   
       float px = event.getX();   
       float py = event.getY();   
       this.SetXY(px, py);   
       return super.onTouchEvent(event);   
    }   
    
    @Override  
    public void run() {   
       // TODO Auto-generated method stub   
       while(!Thread.currentThread().isInterrupted())  //无限判断当前线程状态,如果没有中断,就一直执行while内容
       {   
           try{   
              Thread.sleep(1000);   //防止占用过多CPU资源
           }catch(InterruptedException e)   
           {   
              Thread.currentThread().interrupt();   
           }   
           postInvalidate(); //postInvalidate()是重绘的，也就是调用postInvalidate()后系统会重新调用onDraw方法画一次 
       }   
    }   
}  