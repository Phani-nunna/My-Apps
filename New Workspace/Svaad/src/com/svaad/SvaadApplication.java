package com.svaad;

import java.util.List;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.svaad.Dto.LocationDto;
import com.svaad.responseDto.LogInResponseDto;
import com.svaad.utils.Constants.Config;

public class SvaadApplication extends Application {

	private static SvaadApplication svaadApplication;

	private int menuItemPosition;

	private LogInResponseDto loginUserInfo;
	
	private List<LocationDto> locationDtos;

	@SuppressWarnings("unused")
	@Override
	public void onCreate() {
		try {
			if (Config.DEVELOPER_MODE
					&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
				StrictMode
						.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
								.detectAll().penaltyDialog().build());
				StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
						.detectAll().penaltyDeath().build());
			}
			super.onCreate();
			svaadApplication = this;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static SvaadApplication getInstance() {
		return svaadApplication;
	}

	public int getMenuItemPosition() {
		return menuItemPosition;
	}

	public void setMenuItemPosition(int menuItemPosition) {
		this.menuItemPosition = menuItemPosition;
	}

	public LogInResponseDto getLoginUserInfo() {
		return loginUserInfo;
	}

	public void setLoginUserInfo(LogInResponseDto loginUserInfo) {
		this.loginUserInfo = loginUserInfo;
	}

	/**
	 * @return the locationDtos
	 */
	public List<LocationDto> getLocationDtos() {
		return locationDtos;
	}

	/**
	 * @param locationDtos the locationDtos to set
	 */
	public void setLocationDtos(List<LocationDto> locationDtos) {
		this.locationDtos = locationDtos;
	}

}
