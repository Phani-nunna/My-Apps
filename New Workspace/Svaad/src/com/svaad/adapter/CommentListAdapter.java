package com.svaad.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.Dto.CommentsDetailDto;
import com.svaad.responseDto.LogInResponseDto;

public class CommentListAdapter extends ArrayAdapter<CommentsDetailDto> {

	private Context context;
	private List<CommentsDetailDto> commentListDetailDtos;

	public CommentListAdapter(Context context, int resource,
			List<CommentsDetailDto> commentListDetailDtos) {
		super(context, resource, commentListDetailDtos);
		this.context = context;
		this.commentListDetailDtos = commentListDetailDtos;
	}

	public List<CommentsDetailDto> getCommentListDetailDtos() {
		return commentListDetailDtos;
	}

	public void setCommentListDetailDtos(
			List<CommentsDetailDto> commentListDetailDtos) {
		this.commentListDetailDtos = commentListDetailDtos;
	}

	@Override
	public int getCount() {
		return commentListDetailDtos.size();
	}

	@Override
	public CommentsDetailDto getItem(int position) {
		return commentListDetailDtos.get(position);
	}

	// private ViewHolder initHolder(View convertView) {
	// ViewHolder holder = new ViewHolder();
	//
	// holder.nameTV = (TextView) convertView.findViewById(R.id.fromTextView);
	// holder.commentTV = (TextView) convertView
	// .findViewById(R.id.commentTextView);
	// holder.profileImageView = (ImageView) convertView
	// .findViewById(R.id.profileImageView);
	// return holder;
	// }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.comment_layout, parent,
					false);
			// holder = initHolder(convertView);

			holder = new ViewHolder();

			holder.nameTV = (TextView) convertView
					.findViewById(R.id.usernameTextView);
			holder.commentTV = (TextView) convertView
					.findViewById(R.id.commentTextView);
			holder.profileImageView = (ImageView) convertView
					.findViewById(R.id.profileImageView);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		CommentsDetailDto commentListDetailDto = getItem(position);

		if (commentListDetailDto != null) {
			LogInResponseDto userId = commentListDetailDto.getUserId();
			String commentText = commentListDetailDto.getCommentText();
			// if (userId != null) {
			//
			// pic = userId.getDisplayPicUrl();
			// uname = userId.getUname();
			//
			// holder.nameTV.setText((commentListDetailDto.getUserId()
			// .getUname() != null ? commentListDetailDto.getUserId()
			// .getUname() : ""));
			// } else {
			// holder.profileImageView
			// .setImageResource(R.drawable.default_profile);
			// holder.nameTV.setText(null);
			// }
			//
			// holder.commentTV
			// .setText((commentListDetailDto.getCommentText() != null ?
			// commentListDetailDto
			// .getCommentText() : ""));

			if (userId != null) {

				String pic = userId.getDisplayPicUrl();
				String uname = userId.getUname();

				if (uname != null && uname.length() > 0) {
					holder.nameTV.setText(uname.toString().trim());
				} else {
					holder.nameTV.setText(null);
				}

				if (pic != null && pic.length() > 0) {
					Picasso.with(context).load(pic)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(holder.profileImageView);
				} else {
					Picasso.with(context).load(R.drawable.default_profile)
							.placeholder(R.drawable.temp)
							.error(R.drawable.temp)

							.into(holder.profileImageView);

				}

			} else

			{
				holder.nameTV.setText(null);
				Picasso.with(context).load(R.drawable.default_profile)
						.placeholder(R.drawable.temp).error(R.drawable.temp)

						.into(holder.profileImageView);

			}

			if (commentText != null && commentText.length() > 0) {
				holder.commentTV.setText(commentText.toString().trim());
			} else {
				holder.commentTV.setText(null);
			}

		}

		return convertView;
	}

	private static class ViewHolder {
		private TextView nameTV;
		private TextView commentTV;
		private ImageView profileImageView;
	}

}
