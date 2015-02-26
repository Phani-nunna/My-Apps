package com.paradigmcreatives.audit.camera;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.paradigmcreatives.audit.R;

public class CameraView extends Activity implements SurfaceHolder.Callback{
	private Camera camera;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private boolean previewing = false;
	private LayoutInflater controlInflater = null;
    public static Uri ImagePath;
    private Button buttonTakePicture;


	final int RESULT_SAVEIMAGE = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_view);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		surfaceView = (SurfaceView)findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(CameraView.this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.view, null);
		LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.MATCH_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		buttonTakePicture = (Button)findViewById(R.id.takepicture);

		buttonTakePicture.setOnClickListener(new Button.OnClickListener()	{
			@Override
			public void onClick(View arg0) 
			{
				String tag = "Activity";
				Thread t = new Thread()		{
					public void run()	{
						// TODO Auto-generated method stub
						camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
					}
				};
				t.start();		
				Log.v(tag,"Enter into another activity");
				
			/*	Intent i = new Intent(CameraView.this,Information.class);
				startActivityForResult(i, 1);*/
			}
		});	
	}
	ShutterCallback myShutterCallback = new ShutterCallback(){
		@Override
		public void onShutter() 
		{
			// TODO Auto-generated method stub
		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback()	{
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) 		{
			// TODO Auto-generated method stub
		}
	};

	PictureCallback myPictureCallback_JPG = new PictureCallback()	{
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1){
			// TODO Auto-generated method stub
			Uri uriTarget = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, new ContentValues());
			OutputStream imageFileOS;
			try 	{
				imageFileOS = getContentResolver().openOutputStream(uriTarget);
				imageFileOS.write(arg0);
				imageFileOS.flush();
				imageFileOS.close();
                ImagePath =  uriTarget;
                Intent intent = new Intent();
				intent.setData(uriTarget);
				setResult(Activity.RESULT_OK,intent);
				finish();
				//Toast.makeText(CameraView.this, "Image saved: " + uriTarget.toString(), Toast.LENGTH_LONG).show();
			} 
			catch (FileNotFoundException e) 	{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/**
			 * To display images in the Surface view
			 */
			/*if(mImage != null)
			{
				mImage.setVisibility(View.VISIBLE);
				mImage.setImageURI(uriTarget);
				camera.startPreview();			
			}*/
			camera.startPreview();
		}
	};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 	{
		// TODO Auto-generated method stub
		if(previewing)	{
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null)	{
			try 	{
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} 
			catch (IOException e) 			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 	{
		// TODO Auto-generated method stub
		camera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 	{
		// TODO Auto-generated method stub
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}
}