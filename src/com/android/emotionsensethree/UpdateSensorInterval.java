package com.android.emotionsensethree;

import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.sensors.SensorUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class UpdateSensorInterval implements OnItemSelectedListener {
	
	public String interval_min;
	private SharedPreferences settings;
	public boolean isAccelOn, isSmsOn, isCallOn, isAppsOn;

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		interval_min = parent.getItemAtPosition(position).toString();
		
		switch (interval_min) {
		case "10 Min":
			Toast.makeText(parent.getContext(),
					"10 Minutes selected",
					Toast.LENGTH_SHORT).show();
			break;
		case "15 Min":
			Toast.makeText(parent.getContext(),
					"15 Minutes selected",
					Toast.LENGTH_SHORT).show();
			break;
		case "20 Min":
			Toast.makeText(parent.getContext(),
					"20 Minutes selected",
					Toast.LENGTH_SHORT).show();
			break;
		case "30 Min":
			Toast.makeText(parent.getContext(),
					"30 Minutes selected",
					Toast.LENGTH_SHORT).show();
			break;
		case "60 Min":
			Toast.makeText(parent.getContext(),
					"60 Minutes selected",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			Toast.makeText(parent.getContext(),
					"Default values selected",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub		
	}
}