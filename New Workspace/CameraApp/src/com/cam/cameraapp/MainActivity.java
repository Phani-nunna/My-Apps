package com.cam.cameraapp;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private Camera camera;
	  private int cameraId = 0;
	  private final static String DEBUG_TAG = "MakePhotoActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/*
		
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Log.d("No of cameras",Camera.getNumberOfCameras()+"");
        for (int camNo = 0; camNo < Camera.getNumberOfCameras(); camNo++) {
            CameraInfo camInfo = new CameraInfo();
            Camera.getCameraInfo(camNo, camInfo);
           
            if (camInfo.facing==(Camera.CameraInfo.CAMERA_FACING_FRONT)) {
                camera = Camera.open(camNo);
            }
        }
        if (camera == null) {
           // no front-facing camera, use the first back-facing camera instead.
           // you may instead wish to inform the user of an error here...
        	camera = Camera.open();
        }*/
        
		/*if (!getPackageManager()
		        .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
		      Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
		          .show();
		    } else {
		      cameraId = findFrontFacingCamera();
		      if (cameraId < 0) {
		        Toast.makeText(this, "No front facing camera found.",
		            Toast.LENGTH_LONG).show();
		      } else {
		    	  System.out.println("phan frnt cam found"+cameraId);
		        camera = Camera.open();
		      }
		    }
		camera.takePicture(null, null,
		        new PhotoHandler(getApplicationContext()));*/
		//startCamera();
		Intent intent = new Intent(this,CameraService.class);
		//startService(intent);
		openFrontFacingCamera();
	}
	
	private Camera openFrontFacingCamera() {
	    Camera camera = null;
	     
	    // Look for front-facing camera, using the Gingerbread API.
	    // Java reflection is used for backwards compatibility with pre-Gingerbread APIs.
	    try {
	        Class<?> cameraClass = Class.forName("android.hardware.Camera");
	        Object cameraInfo = null;
	        Field field = null;
	        int cameraCount = 0;
	        Method getNumberOfCamerasMethod = cameraClass.getMethod( "getNumberOfCameras" );
	        if ( getNumberOfCamerasMethod != null ) {
	            cameraCount = (Integer) getNumberOfCamerasMethod.invoke( null, (Object[]) null );
	        }
	        Class<?> cameraInfoClass = Class.forName("android.hardware.Camera$CameraInfo");
	        if ( cameraInfoClass != null ) {
	            cameraInfo = cameraInfoClass.newInstance();
	        }
	        if ( cameraInfo != null ) {
	            field = cameraInfo.getClass().getField( "facing" );
	        }
	        Method getCameraInfoMethod = cameraClass.getMethod( "getCameraInfo", Integer.TYPE, cameraInfoClass );
	        if ( getCameraInfoMethod != null && cameraInfoClass != null && field != null ) {
	            for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
	                getCameraInfoMethod.invoke( null, camIdx, cameraInfo );
	                int facing = field.getInt( cameraInfo );
	                if ( facing == 1 ) { // Camera.CameraInfo.CAMERA_FACING_FRONT 
	                    try {
	                        Method cameraOpenMethod = cameraClass.getMethod( "open", Integer.TYPE );
	                        if ( cameraOpenMethod != null ) {
	                            camera = (Camera) cameraOpenMethod.invoke( null, camIdx );
	                        }
	                    } catch (RuntimeException e) {
	                        Log.e("", "Camera failed to open: " + e.getLocalizedMessage());
	                    }
	                }
	            }
	        }
	    }
	    // Ignore the bevy of checked exceptions the Java Reflection API throws - if it fails, who cares.
	    catch ( ClassNotFoundException e        ) {Log.e("", "ClassNotFoundException" + e.getLocalizedMessage());}
	    catch ( NoSuchMethodException e         ) {Log.e("", "NoSuchMethodException" + e.getLocalizedMessage());}
	    catch ( NoSuchFieldException e          ) {Log.e("", "NoSuchFieldException" + e.getLocalizedMessage());}
	    catch ( IllegalAccessException e        ) {Log.e("", "IllegalAccessException" + e.getLocalizedMessage());}
	    catch ( InvocationTargetException e     ) {Log.e("", "InvocationTargetException" + e.getLocalizedMessage());}
	    catch ( InstantiationException e        ) {Log.e("", "InstantiationException" + e.getLocalizedMessage());}
	    catch ( SecurityException e             ) {Log.e("", "SecurityException" + e.getLocalizedMessage());}
	 
	    if ( camera == null ) {
	        // Try using the pre-Gingerbread APIs to open the camera.
	        try {
	            camera = Camera.open();
	        } catch (RuntimeException e) {
	            Log.e("", "Camera failed to open: " + e.getLocalizedMessage());
	        }
	    }
	 
	    return camera;
	}
	private int findFrontFacingCamera() {
	    int cameraId = -1;
	    // Search for the front facing camera
	    int numberOfCameras = Camera.getNumberOfCameras();
	    for (int i = 0; i < numberOfCameras; i++) {
	      CameraInfo info = new CameraInfo();
	      Camera.getCameraInfo(i, info);
	      if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
	        Log.d(DEBUG_TAG, "Camera found");
	        cameraId = i;
	        break;
	      }
	    }
	    return cameraId;
	  }

	  @Override
	  protected void onPause() {
	    if (camera != null) {
	      camera.release();
	      camera = null;
	    }
	    super.onPause();
	  }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void startCamera()
    {
       Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
         intent.putExtra(MediaStore.EXTRA_OUTPUT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
         intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
         startActivityForResult(intent, 1);
       }
}
