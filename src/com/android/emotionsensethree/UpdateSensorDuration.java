package com.android.emotionsensethree;

import com.ubhave.sensormanager.ESSensorManager;
import com.ubhave.sensormanager.ESSensorManagerInterface;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.sensors.SensorUtils;
import com.ubhave.sensormanager.tester.ApplicationContext;
import com.ubhave.sensormanager.tester.pull.ExampleSensorConfigUpdater;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

public class UpdateSensorDuration implements OnItemSelectedListener {
	
	static String duration_min;
	private SharedPreferences settings;
	public boolean isAccelOn = true, isSmsOn, isCallOn, isAppsOn;
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		duration_min = parent.getItemAtPosition(position).toString();
		
		switch (duration_min) {
		case "1 Min":
			Toast.makeText(parent.getContext(), "1 Minute selected",
					Toast.LENGTH_SHORT).show();
			if (isAccelOn) {
				
				ExampleSensorConfigUpdater accelUpdate = new ExampleSensorConfigUpdater(
						SensorUtils.SENSOR_TYPE_ACCELEROMETER);
				accelUpdate.setSensorSampleWindow(60000);
			}
			if (isSmsOn) {
				ExampleSensorConfigUpdater smsUpdate = new ExampleSensorConfigUpdater(SensorUtils.SENSOR_TYPE_SMS_CONTENT_READER);
				smsUpdate.setSensorSampleWindow(60000);
			}
			if (isCallOn) {
				ExampleSensorConfigUpdater callUpdate = new ExampleSensorConfigUpdater(SensorUtils.SENSOR_TYPE_CALL_CONTENT_READER);
				callUpdate.setSensorSampleWindow(60000);
			}
			if (isAppsOn) {
				ExampleSensorConfigUpdater appsUpdate = new ExampleSensorConfigUpdater(SensorUtils.SENSOR_TYPE_APPLICATION);
				appsUpdate.setSensorSampleWindow(60000);
			}
			break;
		case "2 Min":
			break;
		case "3 Min":
			break;
		case "5 Min":
			break;
		case "10 Min":
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub		
	}
}