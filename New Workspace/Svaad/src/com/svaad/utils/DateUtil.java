package com.svaad.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateUtil {

	public static void main(String asd[])
	{
		String date="2013-12-26T11:16:18.207Z";
		//2013-12-11T14:59:59.450Z
//yyyy-MM-dd'T'HH:mm:ss.SSS
		//Tue Apr 01 11:23:19 GMT 2014
		date="Tue Apr 01 11:23:19 GMT 2014";
		Calendar dateObj=getCalFromString("EEE MMM dd HH:mm:ss zzz yyyy",date);
		System.out.println(getStringFromCal("yyyy-MM-dd'T'HH:mm:ss.SSS", dateObj)+"Z");
		
		
	}
	
	public static Calendar getCalFromString(String format,String date)
	 {
		 if(date==null)
			 return Calendar.getInstance();
		 
		 ThreadSafeFormatter sdfFormat 			= 	new ThreadSafeFormatter(format);
		Calendar resultedCalender=Calendar.getInstance();
		
		try {
			resultedCalender.setTime(sdfFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		 return resultedCalender;
	 }
	
	public static Calendar getCalFromString(String format,String date,String timeZone)
	 {
		 if(date==null)
			 return Calendar.getInstance();
		 
		 ThreadSafeFormatter sdfFormat 			= 	new ThreadSafeFormatter(format,TimeZone.getTimeZone(timeZone));
		Calendar resultedCalender=Calendar.getInstance();
		
		try {
			resultedCalender.setTime(sdfFormat.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		 return resultedCalender;
	 }
	
	
	
	 public static String getStringFromCal(String format,Calendar cal)
	 {
		 if(cal==null)
			 return "";
		 ThreadSafeFormatter sdfFormat 			= 	new ThreadSafeFormatter(format);
		 String resultedText = sdfFormat.format(cal);
		 return resultedText;
	 }
	 
	 
	 public static String getStringFromCalWithTimezone(String format,Calendar cal)
	 {
		 if(cal==null)
			 return "";
		 ThreadSafeFormatter sdfFormat 			= 	new ThreadSafeFormatter(format,cal.getTimeZone());
		 String resultedText = sdfFormat.format(cal);
		 return resultedText;
	 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 static class ThreadSafeFormatter {

			private DateFormat df;

			public ThreadSafeFormatter(String format) {
				this.df = new SimpleDateFormat(format);
			}
			
			public ThreadSafeFormatter(String format,TimeZone timeZone) {
				this.df = new SimpleDateFormat(format);
				this.df.setTimeZone(timeZone);
			}

			public synchronized String format(Calendar date) {
				return df.format(date.getTime());
			}

			
			public synchronized String format(Date date) {
				return df.format(date);
			}
			
			public synchronized Date parse(String string) throws ParseException {
				return df.parse(string);
			}
		}
	
}
