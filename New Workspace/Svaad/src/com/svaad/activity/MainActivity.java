package com.svaad.activity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.svaad.BaseActivity;
import com.svaad.R;
import com.svaad.adapter.SearchAdapter.ViewHolder;
import com.svaad.utils.Constants;
import com.svaad.utils.MediaHandeler;
import com.svaad.utils.Utils;

public class MainActivity extends BaseActivity implements OnClickListener,
		OnItemClickListener, SurfaceHolder.Callback {

	ImageButton captureBtn = null;
	final int CAMERA_CAPTURE = 1;
	private Uri picUri;
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private GridView grid;
	private List<String> listOfImagesPath;
	Camera camera;
	SurfaceView surfaceView;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	public static final String GridViewDemo_ImagePath = Environment
			.getExternalStorageDirectory().toString()
			+ "/Pictures/Screenshots/";

	String mediaPath;

	// public static final String GridViewDemo_ImagePath =
	// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getWindow().setFormat(PixelFormat.UNKNOWN);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setSupportProgressBarIndeterminateVisibility(false);

		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		captureBtn = (ImageButton) findViewById(R.id.capture_btn1);
		captureBtn.setOnClickListener(this);
		surfaceView.setOnClickListener(this);
		grid = (GridView) findViewById(R.id.gridviewimg);
		grid.setOnItemClickListener(this);
		listOfImagesPath = null;
		listOfImagesPath = RetriveCapturedImagePath();
		if (listOfImagesPath != null) {
			grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
		}
	}

	@Override
	public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// try {
		// // use standard intent to capture an image
		// Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// // we will handle the returned data in onActivityResult
		// startActivityForResult(captureIntent, CAMERA_CAPTURE);
		// } catch (ActivityNotFoundException anfe) {
		// // display an error message
		// String errorMessage =
		// "Whoops - your device doesn't support capturing images!";
		// Toast toast = Toast
		// .makeText(this, errorMessage, Toast.LENGTH_SHORT);
		// toast.show();
		// }

		mediaPath = Utils.getMediaPath();
		MediaHandeler.captureImage(MainActivity.this, mediaPath);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (resultCode == RESULT_OK) {
		// // user is returning from capturing an image using the camera
		// if (requestCode == CAMERA_CAPTURE) {
		// Bundle extras = data.getExtras();
		// Bitmap thePic = extras.getParcelable("data");
		// String imgcurTime = dateFormat.format(new Date());
		// File imageDirectory = new File(GridViewDemo_ImagePath);
		// imageDirectory.mkdirs();
		// String _path = GridViewDemo_ImagePath + imgcurTime + ".jpg";
		// try {
		// FileOutputStream out = new FileOutputStream(_path);
		// thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);
		// out.close();
		// } catch (FileNotFoundException e) {
		// e.getMessage();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// listOfImagesPath = null;
		// listOfImagesPath = RetriveCapturedImagePath();
		// if (listOfImagesPath != null) {
		// grid.setAdapter(new ImageListAdapter(this, listOfImagesPath));
		// }
		// Intent in = new Intent(MainActivity.this, Ateit1_activity.class);
		// in.putExtra("path", _path);
		// startActivity(in);
		//
		// }
		// }
		if (resultCode == RESULT_OK) {
			if (requestCode == Constants.CAPTURE_IMAGE) {
				// Bitmap bitmap = Utils.compressImage(mediaPath, this);
				// uploadpic.setImageBitmap(bitmap);

				Intent in = new Intent(MainActivity.this, Ateit1_activity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				in.putExtra("path", mediaPath);
				startActivity(in);

			}
		}
	}

	private List<String> RetriveCapturedImagePath() {
		List<String> tFileList = new ArrayList<String>();
		// File f = new File(GridViewDemo_ImagePath);
		File f = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM");
		// File f = new
		// File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Camera");
		if (f.exists()) {
			File[] files = f.listFiles();
			Arrays.sort(files);

			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (!file.isDirectory())
					tFileList.add(file.getPath());
			}
		}
		return tFileList;
	}

	// public class ImageListAdapter extends BaseAdapter {
	// private Context context;
	// private List<String> imgPic;
	//
	// public ImageListAdapter(Context c, List<String> thePic) {
	// context = c;
	// imgPic = thePic;
	// }
	//
	// public int getCount() {
	// if (imgPic != null)
	// return imgPic.size();
	// else
	// return 0;
	// }
	//
	// // ---returns the ID of an item---
	// public Object getItem(int position) {
	// return position;
	// }
	//
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// // ---returns an ImageView view---
	// public View getView(final int position, View convertView,
	// ViewGroup parent) {
	// ImageView imageView;
	// BitmapFactory.Options bfOptions = new BitmapFactory.Options();
	// bfOptions.inDither = false; // Disable Dithering mode
	// bfOptions.inPurgeable = true; // Tell to gc that whether it needs
	// // free memory, the Bitmap can be
	// // cleared
	// bfOptions.inInputShareable = true; // Which kind of reference will
	// // be used to recover the Bitmap
	// // data after being clear, when
	// // it will be used in the future
	// bfOptions.inTempStorage = new byte[32 * 1024];
	// if (convertView == null) {
	// imageView = new ImageView(context);
	// imageView.setLayoutParams(new GridView.LayoutParams(
	// ViewGroup.LayoutParams.MATCH_PARENT,
	// ViewGroup.LayoutParams.MATCH_PARENT));
	// imageView.setPadding(0, 0, 0, 0);
	// } else {
	// imageView = (ImageView) convertView;
	// }
	// FileInputStream fs = null;
	// Bitmap bm;
	// try {
	// fs = new FileInputStream(new File(imgPic.get(position)
	// .toString()));
	//
	// if (fs != null) {
	// bm = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
	// bfOptions);
	// imageView.setImageBitmap(bm);
	// imageView.setId(position);
	// imageView.setScaleType(ScaleType.FIT_XY);
	// imageView.setLayoutParams(new GridView.LayoutParams(
	// ViewGroup.LayoutParams.MATCH_PARENT, 200));
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// if (fs != null) {
	// try {
	// fs.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	// return imageView;
	// }
	// }

	public class ImageListAdapter extends BaseAdapter {
		private Context context;
		private List<String> imgPic;
		private LayoutInflater inflater;

		public ImageListAdapter(Context c, List<String> thePic) {
			context = c;
			imgPic = thePic;
		}

		public int getCount() {
			if (imgPic != null)
				return imgPic.size();
			else
				return 0;
		}

		// ---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// ---returns an ImageView view---
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder holder;
			// final ImageView imageView;
			// if (convertView == null) {
			// imageView = (ImageView) getLayoutInflater().inflate(
			// R.layout.item_grid_image, parent, false);
			// } else {
			// imageView = (ImageView) convertView;
			// }

			if (convertView == null) {
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.item_grid_image,
						parent, false);

				holder = new ViewHolder();

				holder.image = (ImageView) convertView.findViewById(R.id.image);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			File photoFile = new File(imgPic.get(position).toString());
			Picasso.with(context).load(photoFile).placeholder(R.drawable.temp)
					.error(R.drawable.temp).resize(200, 200).into(holder.image);

			return convertView;
		}
	}

	public class ViewHolder {
		private ImageView image;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// // TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "touched", Toast.LENGTH_LONG)
				.show();
		Intent in = new Intent(MainActivity.this, Ateit1_activity.class);
		in.putExtra("path", listOfImagesPath.get(arg2));
		startActivity(in);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}