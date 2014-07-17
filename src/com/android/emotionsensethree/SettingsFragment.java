package com.android.emotionsensethree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsFragment extends Fragment {
	
	private Spinner sp_interval, sp_duration;
	private TextView tv_interval, tv_duration;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.activity_settings_fragment, container, false);
		
		sp_interval = (Spinner) view.findViewById(R.id.sp_interval);
		sp_duration = (Spinner) view.findViewById(R.id.sp_duration);
		
		sp_interval.setOnItemSelectedListener(new UpdateSensorInterval());
		sp_duration.setOnItemSelectedListener(new UpdateSensorDuration());
		
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
		Log.i("SettingsFragment", "Destroying SettingsFragment");
		super.onDestroy();
	}
}