package com.datainfosys.myshops.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * All common and repeatedly used methods have been placed in this class, to access them easily.
 * Many methods are static over here. 
**/

public class AppUtils {

	public static final String LOG_TAG = AppUtils.class.getName();
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	private Context m_Context = null;
	public SharedPreferences sharedPreferences = null;
	public Editor settings_editor = null;

	public AppUtils(Context _context){
		m_Context = _context;
	}

	/**
	 * verifies if Internet is available or not
	**/
	public boolean isNetworkConnected(){
		ConnectivityManager connectivitymanager = (ConnectivityManager) m_Context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo = connectivitymanager.getActiveNetworkInfo();
		if(networkinfo == null || !networkinfo.isConnectedOrConnecting()){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * verifies if SD card is available or not
	**/
	public static boolean isSdPresent(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED)
				|| Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)
				|| Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * create directories for the application in the SD card
	**/
	public boolean makeDirectory(String directory_name){
		boolean dirCreated = false; 
		if(isSdPresent()){
			File f = new File(directory_name);
			try {
				if (f.exists()) {
					if (!f.isDirectory()) {
						f.mkdirs();
						Log.v("Directory ==> ", "Directory created");
					}
					dirCreated = true;
				} else {
					f.mkdirs();
					dirCreated = true;
					Log.v("Directory ==> ", "Directory created");
				}
			} catch (Exception e) {
				Log.e("Make directory exception ==> ", e.toString());
			}
		}
		else{
			Log.v("SD Card ==> ", "SD Card not found");
		}
		return dirCreated;
	}
	
	public int getScreenResolution(){
		
		int density = m_Context.getResources().getDisplayMetrics().densityDpi;
		return density;		
	}

	/**
	 * returns the current timestamp of the device
	 **/
	public static long currentTimeSecs(){
		long currentTimeStamp = (System.currentTimeMillis() / 1000);
		return currentTimeStamp;
	}

	public static void generateHashKey(Context context) {
		
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.dil.b2bsmart", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.v("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		}
		catch (NameNotFoundException e) {
			Log.e("NameNotFoundException:", e.getMessage());
		}
		catch (NoSuchAlgorithmException e) {
			Log.e("NoSuchAlgorithmException:", e.getMessage());
		}
	}
	
	/**
	 * Generate a value suitable for use
	 * This value will not collide with ID values generated at build time by aapt for R.id.
	 * @return a generated ID value
	**/
	public static int generateViewId() {
	    for (;;) {
	        final int result = sNextGeneratedId.get();
	        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
	        int newValue = result + 1;
	        if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
	        if (sNextGeneratedId.compareAndSet(result, newValue)) {
	            return result;
	        }
	    }
	}
	
	/**
	 * hide the virtual keyboard from the current screen
	 **/
	public static void hideVirtualKeyboard(Activity _activity) {
	
		View view = _activity.getWindow().getCurrentFocus();
		InputMethodManager imm = (InputMethodManager) _activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(view != null)
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		else
			imm.hideSoftInputFromWindow((null == _activity.getCurrentFocus()) ? null : _activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
	}

	/** copies data from input to output **/
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {

			byte[] bytes = new byte[buffer_size];
			for (;;) {
				// Read byte from input stream
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				// Write byte from output stream
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
			Log.v("Exception --> ", ex.toString());
		}
	}

	/** Convert input file to byte array **/
	public byte[] readBytesFromFile(Context _context, File attachment_file) {

		byte[] bytes = null;
		InputStream is;
		try {
			is = new FileInputStream(attachment_file);
			long length = attachment_file.length();
			if (length > Integer.MAX_VALUE) {
				Log.e("IO_Exception", "Could not completely read file " + attachment_file.getName()
						+ " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
				is.close();
				return null;
			}
			bytes = new byte[(int) length];
			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
				offset += numRead;
			}
			if (offset < bytes.length) {
				Log.e("IO_Exception", "Could not completely read file " + attachment_file.getName());
			}
			is.close();
		} catch (Exception e) {
			Log.e("Exception", e.getMessage());
		}
		return bytes;
	}

	/** write input byte array to file **/
	public static void writeBytesToFile(File out_File, byte[] bytes) {
		BufferedOutputStream bos = null;
		try {
			FileOutputStream fos = new FileOutputStream(out_File);
			bos = new BufferedOutputStream(fos);
			bos.write(bytes);
		} catch (Exception e) {
			Log.e("IO_Exception", e.getMessage());
		} finally {
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} catch (Exception e) {
					Log.e("Exception", e.getMessage());
				}
			}
		}
	}

	/*public static void displayMessageDialog(FragmentManager fm,Fragment f,Pojo_DialogDetails dialogObj){
		FragmentTransaction ft = fm.beginTransaction();
		Fragment prev = fm.findFragmentByTag("status_dialog");
		if(prev != null) {
			ft.remove(prev);
			ft.commit();
		}
		ft.addToBackStack(null);
		Frag_SimpleDialog fragDialog = Frag_SimpleDialog.newInstance(dialogObj);
		if(f != null){
			fragDialog.setTargetFragment(f, StaticData.DIALOG_MESSAGE);
		}
		fragDialog.show(fm, "status_dialog");
	}*/

	/*public void showAlert(String title,String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
		builder.setMessage(message).setTitle(title)
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}*/

	public static String getAgoTime(String getdate)
	{
		String timeAgo="";
		try
		{

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date past = format.parse(getdate);
			Date now = new Date();

		//	long i= TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime());
			long minute= TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());// + " minutes ago";
			long hours= TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());// + " hours ago";
			long days= TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());//+ " days ago";

			if(minute > 60)
			{
				if(hours > 24)
				{
					if(days > 7)
					{
						int week=(int)days/7;
						timeAgo=""+week+" week ago";
						return timeAgo;
					}
					else
					{
						timeAgo=""+days+" days ago";
						return timeAgo;
					}
				}
				else
				{
					timeAgo=""+hours+" hours ago";
					return timeAgo;
				}
			}
			else
			{
				timeAgo=""+minute+" minutes ago";
				return timeAgo;
			}



		}catch (Exception e)
		{
			e.printStackTrace();
		}

		return timeAgo;

	}

	public static String getAgoTime12(String getdate)
	{
		String timeAgo="";
		try
		{

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
			Date past = format.parse(getdate);
			Date now = new Date();

		//	long i= TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime());
			long minute= TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());// + " minutes ago";
			long hours= TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());// + " hours ago";
			long days= TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());//+ " days ago";

			if(minute > 60)
			{
				if(hours > 24)
				{
					if(days > 7)
					{
						int week=(int)days/7;
						timeAgo=""+week+" week ago";
						return timeAgo;
					}
					else
					{
						timeAgo=""+days+" days ago";
						return timeAgo;
					}
				}
				else
				{
					timeAgo=""+hours+" hours ago";
					return timeAgo;
				}
			}
			else
			{
				timeAgo=""+minute+" minutes ago";
				return timeAgo;
			}



		}catch (Exception e)
		{
			e.printStackTrace();
		}

		return timeAgo;

	}

	public static String getAgeFromDateOfBirth(String dob) {

		Calendar dob_calendar = getCalendarFromDateWithCustomFormat(dob, "yyyy-MM-dd", "dd-MM-yyyy");

		GregorianCalendar gregCalendar = new GregorianCalendar();
		int y, m, d, age = 0;

		y = gregCalendar.get(Calendar.YEAR);
		m = gregCalendar.get(Calendar.MONTH);
		d = gregCalendar.get(Calendar.DAY_OF_MONTH);

		age = y - dob_calendar.get(Calendar.YEAR);
		if ((m < dob_calendar.get(Calendar.MONTH)) || ((m == dob_calendar.get(Calendar.MONTH)) && (d < dob_calendar.get(Calendar.DAY_OF_MONTH)))) {
			--age;
		}
		if(age < 0)
			throw new IllegalArgumentException("Age < 0");

		return String.valueOf(age);

	}


	public static String getAgeFromDateOfBirthFromYYYYMMDD(String dob) {

		Calendar dob_calendar = getCalendarFromDateWithCustomFormat(dob, "yyyy-MM-dd", "dd-MM-yyyy");

		GregorianCalendar gregCalendar = new GregorianCalendar();
		int y, m, d, year,month,day;
		String age="";

		y = gregCalendar.get(Calendar.YEAR);
		m = gregCalendar.get(Calendar.MONTH);
		d = gregCalendar.get(Calendar.DAY_OF_MONTH);

		year = y - dob_calendar.get(Calendar.YEAR);
		month = m - dob_calendar.get(Calendar.MONTH);
		day = d - dob_calendar.get(Calendar.DAY_OF_MONTH);
		/*if ((m < dob_calendar.get(Calendar.MONTH)) || ((m == dob_calendar.get(Calendar.MONTH)) && (d < dob_calendar.get(Calendar.DAY_OF_MONTH)))) {
			--age;
		}
		if(age < 0)
			throw new IllegalArgumentException("Age < 0");*/

		if(year == 0)
		{
			if(month == 0)
			{
				if(day != 0)
				{
					if(day > 1)
					{
						age=day+" Days(When is your Birthday?)";
					}
					else
					{
						age=day+" Day(When is your Birthday?)";
					}
				}
				else
				{
					age=" Today(When is your Birthday?)";
				}
			}
			else
			{
				age=month+" Mnth(When is your Birthday?)";
			}
		}
		else
		{
			age=year+" Yrs(When is your Birthday?)";
		}


		return String.valueOf(age);

	}

	public static String getAgeFromDateOfBirthFromYYYYMMDDOther(String dob) {

		Calendar dob_calendar = getCalendarFromDateWithCustomFormat(dob, "yyyy-MM-dd", "dd-MM-yyyy");

		GregorianCalendar gregCalendar = new GregorianCalendar();
		int y, m, d, year,month,day;
		String age="";

		y = gregCalendar.get(Calendar.YEAR);
		m = gregCalendar.get(Calendar.MONTH);
		d = gregCalendar.get(Calendar.DAY_OF_MONTH);

		year = y - dob_calendar.get(Calendar.YEAR);
		month = m - dob_calendar.get(Calendar.MONTH);
		day = d - dob_calendar.get(Calendar.DAY_OF_MONTH);
		/*if ((m < dob_calendar.get(Calendar.MONTH)) || ((m == dob_calendar.get(Calendar.MONTH)) && (d < dob_calendar.get(Calendar.DAY_OF_MONTH)))) {
			--age;
		}
		if(age < 0)
			throw new IllegalArgumentException("Age < 0");*/

		if(year == 0)
		{
			if(month == 0)
			{
				if(day != 0)
				{
					if(day > 1)
					{
						age=day+" Days";
					}
					else
					{
						age=day+" Day";
					}
				}
				else
				{
					age=" Today";
				}
			}
			else
			{
				age=month+" Month";
			}
		}
		else
		{
			age=year+" Yrs";
		}


		return String.valueOf(age);

	}

	public static String getAgeFromDateOfBirthFromYYYYMMDDOther2(String dob) {

		Calendar dob_calendar = getCalendarFromDateWithCustomFormat(dob, "yyyy-MM-dd", "dd-MM-yyyy");

		GregorianCalendar gregCalendar = new GregorianCalendar();
		int y, m, d, year,month,day;
		String age="";

		y = gregCalendar.get(Calendar.YEAR);
		m = gregCalendar.get(Calendar.MONTH);
		d = gregCalendar.get(Calendar.DAY_OF_MONTH);

		year = y - dob_calendar.get(Calendar.YEAR);
		month = m - dob_calendar.get(Calendar.MONTH);
		day = d - dob_calendar.get(Calendar.DAY_OF_MONTH);
		/*if ((m < dob_calendar.get(Calendar.MONTH)) || ((m == dob_calendar.get(Calendar.MONTH)) && (d < dob_calendar.get(Calendar.DAY_OF_MONTH)))) {
			--age;
		}
		if(age < 0)
			throw new IllegalArgumentException("Age < 0");*/

		if(year == 0)
		{
			if(month == 0)
			{
				if(day != 0)
				{
					if(day > 1)
					{
						age=day+" Days";
					}
					else
					{
						age=day+" Day";
					}
				}
				else
				{
					age=" Today";
				}
			}
			else
			{
				age=month+" Month";
			}
		}
		else if(year == 1)
		{
			age=year+" Year";
		}
		else
		{
			age=year+" Years";
		}


		return String.valueOf(age);

	}


	public static Calendar getCalendarFromDateWithCustomFormat(String date,	String currentFormat, String requiredFormat) {

		Calendar MCalendar = null;
		String output = null;
		SimpleDateFormat currentSimpleDateFormat = null, requiredSimpleDateFormat = null;

		try {
			currentSimpleDateFormat = new SimpleDateFormat(currentFormat);
			requiredSimpleDateFormat = new SimpleDateFormat(requiredFormat);
			output = requiredSimpleDateFormat.format(currentSimpleDateFormat.parse(date));
			MCalendar = Calendar.getInstance();
			MCalendar.setTime(requiredSimpleDateFormat.parse(output));
		} catch (Exception e) {
			Log.e("Exception ==> ", e.toString());
			return null;
		}

		return MCalendar;
	}

	public static String formatAMPM (int time) {

		String correctTime = "";
		if((time >= 1) &&  (time < 12)){
			correctTime = String.valueOf(time) + "am";
		}
		else if(time == 12){
			correctTime = "12pm";
		}
		else if((time > 12) && (time < 24)){
			correctTime = String.valueOf(time - 12) + "pm";
		}
		else if(time == 24 ||time==0){
			correctTime = "12am";
		}
		return correctTime;
	}

	public static String formatAMPMNew (int time) {

		String correctTime = "";
		if((time >= 1) &&  (time < 12)){
			correctTime = String.valueOf(time) + " am";
		}
		else if(time == 12){
			correctTime = "12 pm";
		}
		else if((time > 12) && (time < 24)){
			correctTime = String.valueOf(time - 12) + " pm";
		}
		else if(time == 24||time==0){
			correctTime = "12 am";
		}
		return correctTime;
	}

	public static boolean isValidTodayOrFutureDate(String date, String format) {
		SimpleDateFormat simpleDateFormat = null;
		Date SelectedDate = null, CurrentDate = null;
		try {
			simpleDateFormat = new SimpleDateFormat(format);
			SelectedDate = simpleDateFormat.parse(date);
			CurrentDate = new Date(System.currentTimeMillis());
			if((SelectedDate != null) && (CurrentDate != null)){
				return true;
			}
			else{
				return false;
			}
		} catch (Exception e) {
			Log.e("Exception ==> ", e.toString());
			return false;
		}
	}

	public static String ChangeDateFormat(String MyDate)
	{
		String mDate=MyDate;

		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dmyFormat = new SimpleDateFormat("dd MMM yyyy");

		try
		{

			Date myDate = ymdFormat.parse(mDate);

			mDate = dmyFormat.format(myDate);


		} catch (Exception e) {
			e.printStackTrace();
		}


		return mDate;
	}

	public static String ChangeDateFormatToYYYYMMDD(String MyDate)
	{
		String mDate=MyDate;

		SimpleDateFormat ymdFormat = new SimpleDateFormat("MMM dd, yyyy");
		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");

		try
		{

			Date myDate = ymdFormat.parse(mDate);

			mDate = dmyFormat.format(myDate);


		} catch (Exception e) {
			e.printStackTrace();
		}


		return mDate;
	}

	public String getDensityName() {
		float density = m_Context.getResources().getDisplayMetrics().density;
		if (density >= 4.0) {
			return "xxxhdpi";
		}
		if (density >= 3.0) {
			return "xxhdpi";
		}
		if (density >= 2.0) {
			return "xhdpi";
		}
		if (density >= 1.5) {
			return "hdpi";
		}
		if (density >= 1.0) {
			return "mdpi";
		}
		return "ldpi";
	}

	public static String parseDateMMMToYYYY(String GetDate)
	{
		String mDate=GetDate;

		SimpleDateFormat ymdFormat = new SimpleDateFormat("MMM dd, yyyy");
		SimpleDateFormat dmyFormat = new SimpleDateFormat("yyyy-MM-dd");

		try
		{

			Date myDate = ymdFormat.parse(mDate);

			mDate = dmyFormat.format(myDate);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return  mDate;
	}

	public static String parseDateYYYYMMDDToMMMDDYYYY(String GetDate)
	{
		String mDate=GetDate;

		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dmyFormat = new SimpleDateFormat("MMM dd, yyyy");

		try
		{

			Date myDate = ymdFormat.parse(mDate);

			mDate = dmyFormat.format(myDate);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return  mDate;
	}

	public static String parseDateYYYYTodd(String GetDate)
	{
		String mDate=GetDate;

		SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat dmyFormat = new SimpleDateFormat("dd-MMM-yyyy");

		try
		{

			Date myDate = ymdFormat.parse(mDate);

			mDate = dmyFormat.format(myDate);


		} catch (Exception e) {
			e.printStackTrace();
		}

		return  mDate;
	}

	public static File downloadFile(Context context, String fileUrl) throws IOException
	{

		Calendar calendar = Calendar.getInstance();
		long timeMilli = calendar.getTimeInMillis();


		URL url = new URL(fileUrl);
		URLConnection connection = url.openConnection();
		InputStream inputStream = new BufferedInputStream(url.openStream(), 10240);
		File dataDir = getDataFolder(context);
		String fileName = ""+timeMilli+".jpg";
		File outputFile = new File(dataDir, fileName);
		FileOutputStream outputStream = new FileOutputStream(outputFile);

		byte buffer[] = new byte[1024];
		int dataSize;
		int loadedSize = 0;
		while ((dataSize = inputStream.read(buffer)) != -1) {
			loadedSize += dataSize;
			outputStream.write(buffer, 0, dataSize);
		}

		/*if(outputFile.exists())
		{
			String ImagePath=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + StaticData.APP_MAINFOLDER+File.separator+fileName;
			PrefUtils.setProfilePicLocalPath(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + StaticData.APP_MAINFOLDER+File.separator+fileName,context);
		}*/

		outputStream.close();
		return outputFile;
	}

	public static File getDataFolder(Context context) {
		File dataDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dataDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Android/data/com.datainfosys.gamevame/GameVame");
			if(!dataDir.isDirectory()) {
				dataDir.mkdirs();
			}
		}

		if(!dataDir.isDirectory()) {
			dataDir = context.getFilesDir();
		}

		return dataDir;
	}

	/*public static String ChangeimageDir(String path) {
		String[] s = path.split("/");
		String pfileName = s[s.length - 1];

		File from = new File(path);


		File wallpaperDirectory = new File(StaticData.PATH);
		// have the object build the directory structure, if needed.
		if (!wallpaperDirectory.exists())
			wallpaperDirectory.mkdirs();

		File to = new File(StaticData.PATH + File.separator+ pfileName);

		try {

			FileChannel src = new FileInputStream(from).getChannel();
			FileChannel dst = new FileOutputStream(to).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return to.getAbsolutePath();
	}*/



}
