package com.gmail.erikbigler.applessentials.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.gmail.erikbigler.applessentials.Applessentials;

public class Time {

	public static Applessentials plugin;

	public Time(Applessentials plugin) {
		Time.plugin = plugin;
	}

	public static String getTime() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.ENGLISH);
		String time = sdf.format(cal.getTime());
		return time;
	}

	public static String getTimeWithSecs() {
		Calendar cal = Calendar.getInstance();
		cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH);
		String time = sdf.format(cal.getTime());
		return time;
	}

	public static long timeInMillis() {
		// you may need more advanced logic here when parsing the time if some times have am/pm and others don't.
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	public static String getTimeinMills() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.ENGLISH);
		String time = sdf.format(cal.getTime());
		return time;
	}

	public static int compareTime(String time) throws ParseException {

		String currentTime = getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.ENGLISH);
		Date date1 = sdf.parse(time);
		Date date2 = sdf.parse(currentTime);

		Long differnceInMills = date2.getTime() - date1.getTime();

		long timeInMinutes = differnceInMills/60000;
		int totalMinutes = (int) timeInMinutes;

		return totalMinutes;
	}

	public static int compareTimeSecs(String time) throws ParseException {

		String currentTime = getTimeWithSecs();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm:ss a", Locale.ENGLISH);
		Date date1 = sdf.parse(time);
		Date date2 = sdf.parse(currentTime);

		Long differnceInMills = date2.getTime() - date1.getTime();

		long timeInSecs = differnceInMills/1000;
		int totalSecs = (int) timeInSecs;

		return totalSecs;
	}

	public static long compareTimeMills(String time) throws ParseException {

		String currentTime = getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.ENGLISH);
		Date date1 = sdf.parse(time);
		Date date2 = sdf.parse(currentTime);

		Long differenceInMills = date2.getTime() - date1.getTime();

		return differenceInMills;
	}

	public static int returnMins(String deathTime) {
		try {
			int totalMins = compareTime(deathTime);
			return totalMins;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String timeString(long milliseconds) {

		String timeStr = "";
		int totalMins = (int) (milliseconds/60000);
		int totalHours = totalMins/60;
		int totalDays = totalHours/24;
		int remainingMins = totalMins % 60;
		int remainingHours = totalHours % 24;

		if(totalDays > 0) {
			timeStr += Integer.toString(totalDays) + " ";
			if(totalDays > 1) {
				timeStr += "days";
			} else {
				timeStr += "day";
			}
		}
		if(totalHours > 0) {
			int hours = totalHours;
			if(totalDays > 0) {
				hours = remainingHours;
				if(remainingHours > 0) {
					if(remainingMins > 0) {
						timeStr += ", ";
					} else {
						timeStr += " and ";
					}
					timeStr += Integer.toString(hours) + " ";
					if(hours > 1) {
						timeStr += "hours";
					} else {
						timeStr += "hour";
					}
				}
			} else {
				timeStr += Integer.toString(hours) + " ";
				if(hours > 1) {
					timeStr += "hours";
				} else {
					timeStr += "hour";
				}
			}
		}
		if(totalMins > 0) {
			if(totalDays > 0) {
				if(remainingMins > 0) {
					if(remainingHours > 0) {
						timeStr += ", and ";
					} else {
						timeStr += " and ";
					}
				}
			} else {
				if(totalHours > 0) {
					if(remainingMins > 0) {
						timeStr += " and ";
					}
				}
			}
			int mins = totalMins;
			if(totalDays > 0 || totalHours > 0) {
				mins = remainingMins;
			}
			if(mins > 0) {
				timeStr += Integer.toString(mins) + " ";
				if(mins > 1) {
					timeStr += "minutes";
				} else {
					timeStr += "minute";
				}
			}
		}
		if(totalMins < 1) {
			timeStr = "Less than a minute";
		}

		return timeStr;
	}

	public static String dateFromMills(Long milliseconds) {
		Date dateFromMills = new Date(milliseconds);
		DateFormat df = new SimpleDateFormat("MMM d, yyyy");
		String date = df.format(dateFromMills);
		return date;
	}

	public static String dateAndTimeFromMills(Long milliseconds) {
		Date dateFromMills = new Date(milliseconds);
		DateFormat df = new SimpleDateFormat("M/d/yy h:mm a");
		String date = df.format(dateFromMills);
		return date;
	}
}
