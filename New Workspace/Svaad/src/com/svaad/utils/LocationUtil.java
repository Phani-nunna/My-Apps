package com.svaad.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationUtil   implements LocationListener {

	public static LocationUtil util;
	static Context context;
	private String provider;
	boolean gpsEnabled;
	boolean networkEnabled;
	boolean registeredLocationUpdate=false;
	public Location currentLocation;
	
	public boolean positionChanged=false;
	private LocationManager locationManager;
	public static LocationUtil getInstance(Context context)
	{
		LocationUtil.context=context;
		if(util==null)
			util=new LocationUtil();
		
		return util;
		
	}
	
	public LocationUtil()
	{
		checkNetworkStatus();
	}
	public void checkNetworkStatus() {

		try {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);

			// getting GPS status
			gpsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			// getting network status
			networkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void onLocationChanged(Location location) {
		this.currentLocation=location;
		positionChanged=true;
//		Toast.makeText(context, location.getLongitude()+" "+location.getLatitude(), 5000).show();
		System.out.println("Location-->"+location.getLongitude()+" "+location.getLatitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(context, "Provider Disabled...", 2000).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Toast.makeText(context, "Provider Enabled...", 2000).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
		
		
	}
	

	
	public boolean isRegisteredLocationUpdate() {
		return registeredLocationUpdate;
	}

	public void setRegisteredLocationUpdate(boolean registeredLocationUpdate) {
		this.registeredLocationUpdate = registeredLocationUpdate;
	}

	public boolean isGpsEnabled() {
		return gpsEnabled;
	}

	public void setGpsEnabled(boolean gpsEnabled) {
		this.gpsEnabled = gpsEnabled;
	}

	public boolean isNetworkEnabled() {
		return networkEnabled;
	}

	public void setNetworkEnabled(boolean networkEnabled) {
		this.networkEnabled = networkEnabled;
	}

	public void registerLocationListener() {

		if(registeredLocationUpdate)
			return;
		Criteria ct=new Criteria();
		
		String provider = locationManager.getBestProvider(ct, true);
		Location location =null;
		if(provider!=null)
		{
			
			// Getting Current Location
			location = locationManager.getLastKnownLocation(provider);
			
			setCurrentLocation(location);
			setProvider(provider);
			locationManager.requestLocationUpdates(provider, 10000, 10, this);
		}
		else
		{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
			setProvider(LocationManager.GPS_PROVIDER);
			Toast.makeText(context, "Provider null", 2000).show();
		}
		if(location==null)
		{
			location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			setCurrentLocation(location);
		}
		registeredLocationUpdate=true;
	}

	
	
	public boolean isPositionChanged(Location location) {
		if(currentLocation==null)
			return false;
		else if(positionChanged)
			return true;
		else if(location==null || (location.getLatitude()!=currentLocation.getLatitude() || location.getLongitude()!=currentLocation.getLongitude()))
			return true;
		else
			return false;
		
	}

	public double getLatitude()
	{
		if(currentLocation!=null)
			return currentLocation.getLatitude();
		return 0;
	}
	
	public double getLongitude()
	{
		if(currentLocation!=null)
			return currentLocation.getLongitude();
		return 0;
	}
	
	
	public void setPositionChanged(boolean positionChanged) {
		this.positionChanged = positionChanged;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public void unRegisterLocationListener() {
		registeredLocationUpdate=false;
		locationManager.removeUpdates(this);
	}
	
	
	

}
