/* **************************************************
 Copyright (c) 2012, University of Cambridge
 Neal Lathia, neal.lathia@cl.cam.ac.uk

This library was developed as part of the EPSRC Ubhave (Ubiquitous and
Social Computing for Positive Behaviour Change) Project. For more
information, please visit http://www.emotionsense.org

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.ubhave.dataformatter.json.pull;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ubhave.dataformatter.json.PullSensorJSONFormatter;
import com.ubhave.sensormanager.ESException;
import com.ubhave.sensormanager.config.SensorConfig;
import com.ubhave.sensormanager.config.sensors.pull.PullSensorConfig;
import com.ubhave.sensormanager.data.SensorData;
import com.ubhave.sensormanager.data.pullsensor.BluetoothData;
import com.ubhave.sensormanager.data.pullsensor.ESBluetoothDevice;
import com.ubhave.sensormanager.process.AbstractProcessor;
import com.ubhave.sensormanager.process.pull.BluetoothProcessor;
import com.ubhave.sensormanager.sensors.SensorUtils;

public class BluetoothFormatter extends PullSensorJSONFormatter
{	
	private final static String DEVICES = "devices";
	private final static String TIME_STAMP = "timeStamp";
	private final static String ADDRESS = "address";
	private final static String NAME = "name";
	private final static String RSSI = "rssi";

	private final static String SENSE_CYCLES = "senseCycles";

	public BluetoothFormatter(final Context context)
	{
		super(context, SensorUtils.SENSOR_TYPE_BLUETOOTH);
	}
	
	@Override
	protected void addSensorSpecificData(JSONObject json, SensorData data) throws JSONException
	{
		BluetoothData bluetoothData = (BluetoothData) data;
		ArrayList<ESBluetoothDevice> devices = bluetoothData.getBluetoothDevices();

		if (devices != null)
		{
			JSONArray neighbours = new JSONArray();
			for (ESBluetoothDevice device : devices)
			{
				JSONObject neighbour = new JSONObject();
				neighbour.put(ADDRESS, device.getBluetoothDeviceAddress());
				neighbour.put(NAME, device.getBluetoothDeviceName());
				neighbour.put(RSSI, device.getRssi());
				neighbour.put(TIME_STAMP, device.getTimestamp());
				neighbours.put(neighbour);
			}
			json.put(DEVICES, neighbours);
		}
	}

	@Override
	protected void addSensorSpecificConfig(JSONObject json, SensorConfig config) throws JSONException
	{
		json.put(SENSE_CYCLES, config.getParameter(PullSensorConfig.NUMBER_OF_SENSE_CYCLES));
	}

	@Override
	public SensorData toSensorData(String jsonString)
	{
		JSONObject jsonData = super.parseData(jsonString);
		if (jsonData != null)
		{
			long senseStartTimestamp = super.parseTimeStamp(jsonData);
			SensorConfig sensorConfig = super.getGenericConfig(jsonData);
			
			boolean setRawData = true;
			boolean setProcessedData = false;
			ArrayList<ESBluetoothDevice> btDevices = null;
			
			try
			{
				btDevices = new ArrayList<ESBluetoothDevice>();
				JSONArray neighbours = (JSONArray) jsonData.get(DEVICES);
				for (int i=0; i<neighbours.length(); i++)
				{
					JSONObject neighbour = (JSONObject) neighbours.get(i);
					long ts = (Long) neighbour.get(TIME_STAMP);
					String btAddr = (String) neighbour.get(ADDRESS);
					String btName = (String) neighbour.get(NAME);
					float btRssi = ((Double) neighbour.get(RSSI)).floatValue();
					
					btDevices.add(new ESBluetoothDevice(ts, btAddr, btName, btRssi));
				}
			}
			catch (Exception e)
			{
				setRawData = false;
			}
			
			try
			{
				BluetoothProcessor processor = (BluetoothProcessor) AbstractProcessor.getProcessor(applicationContext, sensorType, setRawData, setProcessedData);
				return processor.process(senseStartTimestamp, btDevices, sensorConfig);
			}
			catch (ESException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			return null;
		}
	}
}
