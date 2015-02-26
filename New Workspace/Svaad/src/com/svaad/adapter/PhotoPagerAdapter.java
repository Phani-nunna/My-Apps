package com.svaad.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.Dto.BranchDishIdDto;
import com.svaad.Dto.FeedDetailDto;
import com.svaad.activity.DishCollectionActivity;
import com.svaad.utils.Utils;

public class PhotoPagerAdapter extends PagerAdapter implements OnClickListener {

	private Context context;
	private LayoutInflater inflater;
	private List<FeedDetailDto> photoDetailDtos;
	private List<FeedDetailDto> feedbackList;
	private String uname,resname,image_url;

	public PhotoPagerAdapter(Context context,
			List<FeedDetailDto> photoDetailDtos,List<FeedDetailDto> feedbackList,String uname,String resname,String image_url) {
		this.context = context;
		this.photoDetailDtos = photoDetailDtos;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		this.feedbackList = feedbackList;
		this.uname=uname;
		this.resname=resname;
		this.image_url=image_url;
	}

	public List<FeedDetailDto> getPhotoDetailDtos() {
		return photoDetailDtos;
	}

	public void setPhotoDetailDtos(List<FeedDetailDto> photoDetailDtos) {
		this.photoDetailDtos = photoDetailDtos;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public int getCount() {
		return photoDetailDtos.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, int position)
	{
		View imageLayout = inflater.inflate(R.layout.item_pager_image, view,false);
		assert imageLayout != null;
		ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
		TextView tvComment = (TextView) imageLayout.findViewById(R.id.tvComment);
		TextView tvSeeMoreDishes = (TextView) imageLayout.findViewById(R.id.tvSeeMoreDishes);
		TextView dishNameTextView = (TextView) imageLayout.findViewById(R.id.dishNameTextView);
		TextView tvDishTag = (TextView) imageLayout.findViewById(R.id.tvDishTag);
		
		TextView tvCount = (TextView) imageLayout.findViewById(R.id.tvCount);
		
//		if(position==4)
//		{
//		if(position==photoDetailDtos.size()-1)
//		{
//			tvSeeMoreDishes.setVisibility(View.VISIBLE);
//			tvDishTag.setVisibility(View.GONE);
//			tvComment.setVisibility(View.GONE);
//			dishNameTextView.setVisibility(View.GONE);
//			
//			tvCount.setText(position+1+"/"+feedbackList.size());
//			imageView.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					
//					Intent i = new Intent(context,
//							DishCollectionActivity.class);
//
//					Bundle bundleObject = new Bundle();
//					bundleObject.putSerializable("key", (Serializable) feedbackList);
//
//					i.putExtras(bundleObject);
//					i.putExtra("name",uname);
//					i.putExtra("resname",resname);
//					context.startActivity(i);
//					
//				}
//			});
//		}
//		}
//		else
//		{

		FeedDetailDto feed = photoDetailDtos.get(position);
		imageView.setTag(feed);
		imageView.setOnClickListener(this);

		String url = "";
			
		tvCount.setText(position+1+"/"+feedbackList.size());
		BranchDishIdDto branchDishIdDto = feed.getBranchDishId();
		
		if(branchDishIdDto!=null)
		{
			if (branchDishIdDto.getDishId() != null) 
			{
				String name=branchDishIdDto.getDishId().getName();
				
				if(name!=null && name.length()>0)
				{
					dishNameTextView.setText(name);
				}
				else
				{
					dishNameTextView.setText(null);
				}
			}
			
		}

		if (feed.getDishPhoto() != null) 
		{
			url = feed.getDishPhoto().getUrl();
			if (url != null && url.length() > 0)
			{
				Picasso.with(context).load(url).placeholder(R.drawable.temp)
						.error(R.drawable.temp)

						.into(imageView);
			} 
			else
			{
				Picasso.with(context).load(R.drawable.temp)
						.placeholder(R.drawable.temp).error(R.drawable.temp)

						.into(imageView);
			}
		}

		else if (branchDishIdDto != null) 
		{
			if (branchDishIdDto.getDishId() != null) 
			{
				
				if (branchDishIdDto.getDishId().getDishPic() != null) 
				{
					url = branchDishIdDto.getDishId().getDishPic().getUrl();
					
					if (url != null && url.length() > 0) 
					{
						Picasso.with(context).load(url)
								.placeholder(R.drawable.temp)
								.error(R.drawable.temp)

								.into(imageView);

					} 
					else
					{
						Picasso.with(context).load(R.drawable.temp)
								.placeholder(R.drawable.temp)
								.error(R.drawable.temp)

								.into(imageView);
						
					}
				}
			}

		}

		if (feed.getCommentText() != null && feed.getCommentText().length() > 0) 
		{
			tvComment.setVisibility(View.VISIBLE);
			tvComment.setText(feed.getCommentText());
		} 
		else
		{
			tvComment.setVisibility(View.GONE);
		}
		
		
		if(feed.getDishTag()!=null&& feed.getDishTag().length()>0)
		{
			tvDishTag.setVisibility(View.VISIBLE);
			
			
			
			String dishTags=feed.getDishTag();
			
		
			if(dishTags!=null && dishTags.length()>0)
			{
				Utils.setColorsToDishTags(dishTags, tvDishTag);
			}
//			if(feed.getDishTag().equalsIgnoreCase("1"))
//			{
//				tvDishTag.setText("Loved it");
//			}
//			else if(feed.getDishTag().equalsIgnoreCase("2"))
//			{
//				tvDishTag.setText("Good");
//			}
//			else if(feed.getDishTag().equalsIgnoreCase("3"))
//			{
//				tvDishTag.setText("Its ok");
//			}
//			else if(feed.getDishTag().equalsIgnoreCase("4"))
//			{
//				tvDishTag.setText("Yuck");
//			}
		}
		else
		{
			tvDishTag.setVisibility(View.GONE);
		}
		
	//}
		

		view.addView(imageLayout, 0);
		return imageLayout;

	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image:
			Intent i = new Intent(context,
					DishCollectionActivity.class);

			Bundle bundleObject = new Bundle();
			bundleObject.putSerializable("key", (Serializable) feedbackList);

			i.putExtras(bundleObject);
			i.putExtra("name",uname);
			i.putExtra("resname",resname);
			
			i.putExtra("imageurl",image_url);
			context.startActivity(i);
//			if (v.getTag() == null) {
//				return;
//			}
//			FeedDetailDto dishDetailDto = (FeedDetailDto) v.getTag();
//			
//			Intent intent = new Intent(context, DishProfileActivity.class);
//			intent.putExtra(Constants.DATA, dishDetailDto);
//			context.startActivity(intent);
			break;

		default:
			break;
		}

	}

}
