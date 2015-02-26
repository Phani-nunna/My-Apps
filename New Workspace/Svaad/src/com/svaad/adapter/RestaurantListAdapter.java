package com.svaad.adapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.svaad.R;
import com.svaad.Dto.RestaurantDetailsDto;
import com.svaad.activity.RestaurantProfilesActivity;
import com.svaad.utils.Constants;

public class RestaurantListAdapter extends ArrayAdapter<RestaurantDetailsDto>
		implements OnClickListener {

	RestaurantDetailsDto detailsDto;
	ViewHolder holder;
	int index;
	private List<RestaurantDetailsDto> restaurantDetailsDtos;
	private Context contex;

	public RestaurantListAdapter(Context context, int resource,
			List<RestaurantDetailsDto> restaurantDetailsDtos) {
		super(context, resource, restaurantDetailsDtos);
		this.restaurantDetailsDtos = restaurantDetailsDtos;
		this.contex = context;
	}

	public List<RestaurantDetailsDto> getRestaurantDetailsDtos() {
		return restaurantDetailsDtos;
	}

	public void setRestaurantDetailsDtos(
			List<RestaurantDetailsDto> restaurantDetailsDtos) {
		this.restaurantDetailsDtos = restaurantDetailsDtos;
	}

	@Override
	public int getCount() {
		return restaurantDetailsDtos.size();
	}

	@Override
	public RestaurantDetailsDto getItem(int position) {
		return restaurantDetailsDtos.get(position);
	}

	public class ViewHolder {
		private ImageView dishImage,status;
		private TextView branchNameTextView, branchAddresstextView,
				branchtimings;
	}

	private ViewHolder initHolder(View view) {
		holder = new ViewHolder();

		holder.dishImage = (ImageView) view.findViewById(R.id.dishImageButton);

		holder.branchNameTextView = (TextView) view
				.findViewById(R.id.branchNameTextView);
		holder.branchAddresstextView = (TextView) view
				.findViewById(R.id.branchAddresstextView);
		holder.branchtimings = (TextView) view.findViewById(R.id.branchtimings);
		holder.status = (ImageView) view.findViewById(R.id.status);
		return holder;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		detailsDto = getItem(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.restaurantthumbnail,
					parent, false);
			holder = initHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (detailsDto.getName() == null) {

			holder.branchNameTextView
					.setText((detailsDto.getBranchName() != null ? detailsDto
							.getBranchName() : ""));

		} else {

			holder.branchNameTextView
					.setText((detailsDto.getBranchName() != null ? detailsDto
							.getBranchName() : ""));
		}

		if (detailsDto.getLocation() != null) {
			if (detailsDto.getLocation().getName() != null
					&& detailsDto.getLocation().getName().length() > 0) {
				holder.branchAddresstextView.setText(detailsDto.getLocation()
						.getName());
			} else {
				holder.branchAddresstextView.setText(null);
			}
		} else {
			holder.branchAddresstextView.setText(null);
		}

		if (detailsDto.getTimings() != null) {
			String time = detailsDto.getTimings();
			if (detailsDto.getTimings() != null
					&& detailsDto.getTimings().length() > 0) {
				String p = time(time);
				if(p!=null && p.length()>0 && !p.equalsIgnoreCase(""))
				{
					holder.status.setVisibility(View.VISIBLE);
					String status=p.substring(0, p.length()-1);
					holder.branchtimings.setText(status+ "");
					char[] stat=p.toCharArray();
					int check=Integer.parseInt(stat[stat.length-1]+"");
					if(check==0)
					{
						holder.status.setImageResource(R.drawable.timings_green_96);
					}
					else if(check==1)
					{
						holder.status.setImageResource(R.drawable.timings_orange_96);
					}
					else
					{
						holder.status.setImageResource(R.drawable.timings_red_96);
					}
					
				}
				else
				{
					holder.branchtimings.setText(null);
					holder.status.setVisibility(View.INVISIBLE);
				}
				
				
				
			} else {
				holder.status.setVisibility(View.INVISIBLE);
				holder.branchtimings.setText(null);
			}
		} else {
			holder.status.setVisibility(View.INVISIBLE);
			holder.branchtimings.setText(null);

		}

		String url = "";

		if (detailsDto.getPhoto() == null) {

			if (detailsDto.getCoverPic() != null
					&& detailsDto.getCoverPic().getUrl() != null) {
				url = detailsDto.getCoverPic().getUrl();

				Picasso.with(contex).load(url).placeholder(R.drawable.temp)
						.error(R.drawable.temp)

						.into(holder.dishImage);

			}
			else if (detailsDto.getCoverPicSmall() != null
					&& detailsDto.getCoverPicSmall().getUrl() != null) {
				url = detailsDto.getCoverPicSmall().getUrl();

				Picasso.with(contex).load(url).placeholder(R.drawable.temp)
						.error(R.drawable.temp)

						.into(holder.dishImage);

			} 
			else {
				Picasso.with(contex).load(R.drawable.temp)
						.placeholder(R.drawable.temp).error(R.drawable.temp)
						.into(holder.dishImage);
			}
			

		}
		
		
		
		else {

			if (detailsDto.getPhoto() != null
					&& detailsDto.getPhoto().getUrl() != null) {
				url = detailsDto.getPhoto().getUrl();

				Picasso.with(contex).load(url).placeholder(R.drawable.temp)
						.error(R.drawable.temp)

						.into(holder.dishImage);
			} else {
				Picasso.with(contex).load(R.drawable.temp)
						.placeholder(R.drawable.temp).error(R.drawable.temp)
						.into(holder.dishImage);
			}
		}

		// holder.dishImageButton.setOnClickListener(this);
		holder.dishImage.setTag(detailsDto);

		holder.dishImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				RestaurantDetailsDto restaurantDetailsDto = (RestaurantDetailsDto) v
						.getTag();
				Intent i = new Intent(contex, RestaurantProfilesActivity.class);
//				Intent i = new Intent(contex, RestaurantProfilesActivity_fadedscrollview.class);
//				Intent i = new Intent(contex, NewRestaurantProfileActivity.class);
				i.putExtra(Constants.DATA, restaurantDetailsDto);
				contex.startActivity(i);
				// ((Activity) contex).finish();

			}
		});

		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dishImageButton:

			break;

		default:
			break;
		}

	}

	@SuppressLint("SimpleDateFormat")
	private String time(String b) {
		// TODO Auto-generated method stub
		String[] tokens, timings, days1;
		int[] days;
		tokens = new String[120];
		days1 = new String[120];
		timings = new String[120];
		days = new int[50];
		int count = 0;
		String s = b.substring(b.indexOf("{") + 1, b.indexOf("}"));
		StringTokenizer tymsplttc = new StringTokenizer(s, "]");
		
			for (int u = 0; tymsplttc.hasMoreTokens(); u++) 
			{
			tokens[u] = tymsplttc.nextToken();
				if (tokens[u].startsWith(",")) {
					days1[u] = tokens[u].substring(2, 4);
					timings[u] = tokens[u].substring(8, tokens[u].length() - 1);
				} else {
					days1[u] = tokens[u].substring(1, 3);
					timings[u] = tokens[u].substring(7, tokens[u].length() - 1);
				}
				count++;
	
			}

			for (int y = 0; count < 7; y++) {
				days[y] = Integer
						.parseInt(days1[y].substring(1, days1[y].length()));
	
			}

		Date dt = new Date();
		@SuppressWarnings("deprecation")
		int hours = dt.getHours();
		@SuppressWarnings("deprecation")
		int minutes = dt.getMinutes();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
		String time1 = sdf.format(dt);
		String am = time1.substring(5, time1.length() - 1);
		if (am.equals("pm")) {
			if (hours <= 12) {
				hours = hours + 12;

			}
		}
		int currenttime = hours * 60 + minutes;
		
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		day = day - 1;
		
		return getTimings(timings[day],currenttime);

	}

	public String getTimings(String timings,int x) {
		String timeret = "";
		if (timings.length() > 0 && timings != null
				&& !timings.equalsIgnoreCase("")) {
			timeret = checktime(timings,x);

		} else {
			timeret = "Holiday2";
		}

		return timeret;
	}

	private String checktime(String string,int currenttime) throws NullPointerException {
		int count = 0, count2 = 0;
		String[] tokens1, first, second;
		int[] alltime;
		tokens1 = new String[20];
		first = new String[120];
		second = new String[120];
		String ap = null, ap2 = null, ap1 = null, ap3 = null;
		
		if (string.contains("\",\"")) 
		{
			
			StringTokenizer tymsplttc = new StringTokenizer(string, "\",\"");
				for (int u = 0; tymsplttc.hasMoreTokens(); u++)
				{
					tokens1[u] = tymsplttc.nextToken();
					count++;
				}

				count2 = 2 * count;
				StringTokenizer tymsplt;
				
				for (int y = 0; count > 0; y++) 
				{
					tymsplt = new StringTokenizer(tokens1[y], "-");
					first[y] = tymsplt.nextToken();
					second[y] = tymsplt.nextToken();
					count = count - 1;
				}

			StringTokenizer hsplt, hsplt1;
			alltime=new int[count2];
			for (int p = 0, q = 0, i = 0; count2 > 0; p++, q++, i++) {

				if (first[p] != null && !first[p].equalsIgnoreCase("")
						&& first[p].length() > 0) {
					hsplt = new StringTokenizer(first[p], ":");
					ap = hsplt.nextToken();
					ap1 = hsplt.nextToken();

					if (ap != null && !ap.equalsIgnoreCase("")
							&& ap.length() > 0 && ap1 != null
							&& !ap1.equalsIgnoreCase("") && ap1.length() > 0) {
						int hour = Integer.parseInt(ap);
						int min = Integer.parseInt(ap1);
						alltime[i] = (hour * 60 + min);
					}
				}
				i++;
				if (second[q] != null && !second[q].equalsIgnoreCase("")
						&& second[q].length() > 0) {
					hsplt1 = new StringTokenizer(second[q], ":");
					ap2 = hsplt1.nextToken();
					ap3 = hsplt1.nextToken();
					if (ap2 != null && !ap2.equalsIgnoreCase("")
							&& ap2.length() > 0 && ap3 != null
							&& !ap3.equalsIgnoreCase("") && ap3.length() > 0) {
						int hour1 = Integer.parseInt(ap2);
						int min1 = Integer.parseInt(ap3);
						alltime[i] = (hour1 * 60 + min1);
					}
				}
				count2 = count2 - 1;
			}

			
			if (currenttime < alltime[0]) 
				{
						int opentime = alltime[0];
		
						int h = opentime / 60;
						int m = opentime % 60;
		
						if ((m + "").length() <= 1) {
							return "Closed, Opens at " + h + ":0" + m+""+1;
		
						} else {
							return "Closed, Opens at " + h + ":" + m+""+1;
						}
	
				} 
			else if (currenttime > alltime[alltime.length - 1]) 
				{
					return "Closed Today2";
	
				} 
			else 
			{
				for (int j = 0; j < (alltime.length)-1; j++) {
					// opens until
					if (currenttime > alltime[j] && currenttime < alltime[j + 1]) {
						
						int opentime = alltime[j + 1];
						int h = opentime / 60;
						int m = opentime % 60;

						if ((m + "").length() <= 1) {
							return "Open Now, until " + h + ":0" + m+"0";
						} else {
							return "Open Now, until " + h + ":" + m+"0";
						}
					}
					j++;
				}
				
				for (int j = 0; j < (alltime.length)-2; j++) {
					// opens at
					if (currenttime > alltime[j + 1] && currenttime < alltime[j + 2]) {
						int opentime = alltime[j + 2];
						int h = opentime / 60;
						int m = opentime % 60;

						if ((m + "").length() <= 1) {
							return "Closed, Opens at " + h + ":0" + m+"1";
						} else {
							return "Closed, Opens at " + h + ":" + m+"1";
						}
					}
					j++;

				}

			}

		} else {
			
			int alltime1 = 0, alltime2 = 0;
			count2 = count * 2;
			StringTokenizer tymsplt;
			tymsplt = new StringTokenizer(string, "-");
			String a = tymsplt.nextToken();
			String b = tymsplt.nextToken();
			StringTokenizer hsplt, hsplt1;
			if (a != null && !a.equalsIgnoreCase("") && a.length() > 0) {
				hsplt = new StringTokenizer(a, ":");
				String ap6 = hsplt.nextToken();
				String ap7 = hsplt.nextToken();
				if (ap6 != null && !ap6.equalsIgnoreCase("")
						&& ap6.length() > 0 && ap7 != null
						&& !ap7.equalsIgnoreCase("") && ap7.length() > 0) {
					int hour = Integer.parseInt(ap6);
					int min = Integer.parseInt(ap7);
					alltime1 = (hour * 60 + min);
				}
			}

			if (b != null && !b.equalsIgnoreCase("") && b.length() > 0) {
				hsplt1 = new StringTokenizer(b, ":");
				String ap8 = hsplt1.nextToken();
				String ap9 = hsplt1.nextToken();
				if (ap8 != null && !ap8.equalsIgnoreCase("")
						&& ap8.length() > 0 && ap9 != null
						&& !ap9.equalsIgnoreCase("") && ap9.length() > 0) {
					int hour1 = Integer.parseInt(ap8);
					int min1 = Integer.parseInt(ap9);
					alltime2 = (hour1 * 60 + min1);
				}
			}

			if (currenttime < alltime1) {
				int opentime = alltime1;

				int h = opentime / 60;
				int m = opentime % 60;

				if ((m + "").length() <= 1) {
					return "CLosed,Opens at " + h + ":0" + m+"1";
				} else {
					return "Closed,Opens at " + h + ":" + m+"1";
				}

			} else if (currenttime > alltime2) {
				return "Closed Today2";

			} else if (currenttime > alltime1 && currenttime < alltime2) {
				int opentime = alltime2;
				int h = opentime / 60;
				int m = opentime % 60;

				if ((m + "").length() <= 1) {
					return "Open Now, until " + h + ":0" + m+"0";
				} else {
					return "Open Now, until " + h + ":" + m+"0";
				}

			}

		}
		return "";

	}

}
