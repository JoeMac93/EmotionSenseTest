package com.android.emotionsensethree;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	ViewPager mViewPager;
	Constants mConst;
	
	public boolean isAccelOn, isSmsOn, isCallOn, isAppsOn;
	private Spinner sp_interval, sp_duration;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create the adapter that will return a fragment for each of the primary sections of the app.
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        
        // Specify that the Home/Up button should not be enabled since there is no hierarchical parent.
        actionBar.setHomeButtonEnabled(false);
        
        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Set up the ViewPager, attaching the adapter and setting up a listener for when the user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
        	@Override
        	public void onPageSelected(int position) {
        		// When swiping between different app sections, select the corresponding tab.
        		// We can also use ActionBar.Tab#select() to do this if we have a reference to the tab.
        		actionBar.setSelectedNavigationItem(position);
        	}
        });
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
        	// Create a tab with text corresponding to the page title defined by the adapter.
        	// Also specify this Activity object, which implements the TabListener interface, as the listener for when this tab is selected.
        	actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
        
        SharedPreferences settings = this.getSharedPreferences("config", Context.MODE_PRIVATE);;
        SharedPreferences.Editor editor = settings.edit();
        
        // Load the default parameters into SharedPreferences for the first time launch.
        int ct = settings.getInt("count", 0);
        if (ct == 0) {
        	editor.putString("start", "9"); 
        	editor.putString("end", "21");   	
        	editor.putString("interval", "15");   	
        	editor.putString("duration", "5");   	
        	editor.putString("location", "On");
        	editor.putString("upload", "On");
        	TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        	editor.putString("IMEI", tm.getDeviceId());
        	editor.putString("brand", Build.BRAND);
        	editor.putString("model", Build.MODEL); 
        	String phone_type = Build.BRAND + "_" + Build.MODEL;
        	
        	// motoX
        	if (phone_type.equals("motorola_XT1058")) {
          	editor.putString("mfcc_dist_same_semi", "13");
          	editor.putString("mfcc_dist_diff_semi", "18");
          	editor.putString("mfcc_dist_same_un", "13");
          	editor.putString("mfcc_dist_diff_un", "18");
        	}
        	// nexus 4
        	else if (phone_type.equals("google_Nexus 4")) {
          	editor.putString("mfcc_dist_same_semi", "17");
          	editor.putString("mfcc_dist_diff_semi", "22");
          	editor.putString("mfcc_dist_same_un", "17");
          	editor.putString("mfcc_dist_diff_un", "22");
        	}
        	// s2
        	else if (phone_type.equals("samsung_SAMSUNG-SGH-I727")) {
          	editor.putString("mfcc_dist_same_semi", "18");
          	editor.putString("mfcc_dist_diff_semi", "25");
          	editor.putString("mfcc_dist_same_un", "18");
          	editor.putString("mfcc_dist_diff_un", "25");
        	}
        	// s3 
        	else if (phone_type.equals("samsung_SAMSUNG-SGH-I747")) {
          	editor.putString("mfcc_dist_same_semi", "16");
          	editor.putString("mfcc_dist_diff_semi", "21");
          	editor.putString("mfcc_dist_same_un", "16");
          	editor.putString("mfcc_dist_diff_un", "21");
        	}
        	// s4
        	else if (phone_type.equals("samsung_SAMSUNG-SGH-I337")) {
          	editor.putString("mfcc_dist_same_semi", "14");
          	editor.putString("mfcc_dist_diff_semi", "24");
          	editor.putString("mfcc_dist_same_un", "14");
          	editor.putString("mfcc_dist_diff_un", "24");
        	}
        	// other devices
        	else {
          	editor.putString("mfcc_dist_same_semi", "15.6");
          	editor.putString("mfcc_dist_diff_semi", "21.6");
          	editor.putString("mfcc_dist_same_un", "15.6");
          	editor.putString("mfcc_dist_diff_un", "21.6");
        	Toast.makeText(this, "Your device is not recognized and the result might not be accurate...", Toast.LENGTH_SHORT).show();
        }
      	Log.i("EmotionSense", "First time launched");
      	}
    }
    
	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		mViewPager.setCurrentItem(arg0.getPosition());	
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				return new SensorsFragment();
			case 1:
				return new SettingsFragment();
			default:
				return new SensorsFragment();
			}
		}
		
		@Override
		public int getCount() {
			return 2;
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Sensors";
			case 1:
				return "Settings";
			default:
				return "More";
			}
		}
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}
	
	// Exit the app with back pressed.
	@Override  
	public void onBackPressed() {
		super.onBackPressed();   
		Toast.makeText(this, "Closing...", Toast.LENGTH_SHORT).show();
		this.finish();
	}
	
	public void onAccelerometerClicked(View view) {
		if (((ToggleButton) view).isChecked()) {isAccelOn = true;
		Toast.makeText(this, "Accelerometer is on.", Toast.LENGTH_SHORT).show();}
		else {isAccelOn = false;}
	}
	
	public void onSmsClicked(View view) {
		if (((ToggleButton) view).isChecked()) {isSmsOn = true;}
		else {isSmsOn = false;}
	}
	
	public void onCallClicked(View view) {
		if (((ToggleButton) view).isChecked()) {isCallOn = true;}
		else {isCallOn = false;}
	}
	
	public void onApplicationsClicked(View view) {
		if (((ToggleButton) view).isChecked()) {isAppsOn = true;}
		else {isAppsOn = false;}
	}
}
