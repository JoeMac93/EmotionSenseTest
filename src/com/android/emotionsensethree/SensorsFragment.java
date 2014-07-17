package com.android.emotionsensethree;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SensorsFragment extends Fragment {

	private ToggleButton tb_accelerometer, tb_sms, tb_call, tb_applications;
	private TextView tv_accelerometer, tv_sms, tv_call, tv_applications;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.activity_sensors_fragment, container, false);
		
		tb_accelerometer = (ToggleButton) view.findViewById(R.id.tb_accelerometer);
		tb_sms = (ToggleButton) view.findViewById(R.id.tb_sms);
		tb_call = (ToggleButton) view.findViewById(R.id.tb_call);
		tb_applications = (ToggleButton) view.findViewById(R.id.tb_applications);
	
	return view;
	}
	
	@Override
	public void onResume() {
	  super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		Log.i("SensorsFragment", "Destroying SensorsFragment");
		super.onDestroy();
	}
}