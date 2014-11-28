package com.androidtuto.streamingMJPEG;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.app.SearchManager.OnCancelListener;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtuto.MJPEG.MjpegInputStream;
import com.androidtuto.MJPEG.MjpegView;

public class MainActivity  extends Activity implements OnCancelListener  {
	private MjpegView mv;
	Thread			thread;
	Handler 		handler;
	boolean 		cancel;
	 
	TextView txt;
	LinearLayout linear; 
	private double  time_unique=0;
	public static  Bitmap test;

	
     OutputStream fOut = null;
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
                //sample public cam
      //  String URL = "http://shibuya.ipcam.jp:60001/nphMotionJpeg?Resolution=320x240&Quality=Standard"; 
        //String URL = "http://192.168.2.113:1500/a.mjpeg";
        String URL="http://mjpeg.sanford.io/count.mjpeg";
        
        final String path = Environment.getExternalStorageDirectory().toString();
        // shibuya.ipcam.jp :: Adresse IP 114.145.160.176 port : 60001
        Toast.makeText(getBaseContext(), URL, 1000).show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        linear = new LinearLayout(this);

        linear.setOrientation(LinearLayout.VERTICAL);

        final ImageView  img=new ImageView (this);
           txt=new TextView (this);
     
      
        linear.addView(img);
        linear.addView(txt);
        setContentView(linear); 
  
    		
    		
        mv = new MjpegView(MainActivity.this);
        linear.addView(mv);
        setContentView(linear);        
        mv.setSource(MjpegInputStream.read(URL));
        mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
        mv.showFps(true);
       
        
        handler = new Handler(){
    		public void handleMessage(Message msg) { 
    			switch(msg.what) {
    			case 0:
    				 time_unique=time_unique + 1; 
    				break;
    				
    			case 1:
    				Toast.makeText(MainActivity.this,"Thread termin�", 1000).show();
    				break;
    			   	
    			}
    		}
        };
        
        thread = new Thread(){
        	public void run(){
        		try {
	        		Thread.sleep(1000);
		        	for(int i=0;i<100;i++){
		        		if(cancel)break;
		        		handler.sendMessage(handler.obtainMessage(0,""+i));		            	
						Thread.sleep(100);
						
		            }		        	
    			} catch (InterruptedException e){    				
    				e.printStackTrace();
    			}
    			handler.sendEmptyMessage(1);    			
        	};
        };
        
			thread.start();
		
        
        //	} 
        
        
        
         
        
	}
	
	public void onPause() {
		super.onPause();
		//mv.stopPlayback();
	}
	public void onCancel(DialogInterface arg0) {
		cancel=true;	
		super.onPause();
		mv.stopPlayback();
	}


    @Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
