package com.ubhave.datahandler.loggertypes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.ubhave.dataformatter.DataFormatter;
import com.ubhave.datahandler.ESDataManager;
import com.ubhave.datahandler.config.DataHandlerConfig;
import com.ubhave.datahandler.config.DataStorageConfig;
import com.ubhave.datahandler.config.DataStorageConstants;
import com.ubhave.datahandler.except.DataHandlerException;
import com.ubhave.sensormanager.config.GlobalConfig;
import com.ubhave.sensormanager.data.SensorData;

public abstract class AbstractDataLogger
{
	private final static String LOG_TAG = "AbstractDataLogger";
	protected final static String TAG_SURVEY_RESPONSE = "Survey";
	protected final static String TAG_INTERACTION = "Interaction";
	protected final static String TAG_ERROR = "Error";
	
	private final static String TAG_LOGGER = "logger";
	private final static String TAG_USER_ID = "userId";
	private final static String TAG_DEVICE_ID = "deviceId";
	private final static String TAG_TIMESTAMP = "timestamp";
	private final static String TAG_LOCAL_TIME = "localTime";
	private final static String TAG_DATA_TYPE = "dataType";
	private final static String TAG_DATA_TITLE = "dataTitle";
	private final static String TAG_DATA_MESSAGE = "dataMessage";
	private final static String TAG_APP_VERSION = "applicationVersion";
	
	protected ESDataManager dataManager;
	protected final Context context;

	protected AbstractDataLogger(final Context context)
	{
		this.context = context;
		if (permissionGranted())
		{
			try
			{
				dataManager = ESDataManager.getInstance(context);
				configureDataStorage();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				dataManager = null;
			}
		}
	}
	
	protected ArrayList<String> getPermissions()
	{
		ArrayList<String> permissions = new ArrayList<String>();
		permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
		return permissions;
	}
	
	protected boolean permissionGranted()
	{
		for (String permission : getPermissions())
		{
			if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
			{
				if (GlobalConfig.shouldLog())
				{
					Log.d(LOG_TAG, "Missing permission (for data logging): "+permission);
				}
				throw new NullPointerException("Missing permission: "+permission);
			}
		}
		return true;
	}
	
	public ESDataManager getDataManager()
	{
		return dataManager;
	}

	protected void configureDataStorage()
	{
		try
		{
			dataManager.setConfig(DataHandlerConfig.PRINT_LOG_D_MESSAGES, shouldPrintLogMessages());
			dataManager.setConfig(DataStorageConfig.LOCAL_STORAGE_ROOT_DIRECTORY_NAME, getLocalStorageDirectoryName());
			dataManager.setConfig(DataStorageConfig.UNIQUE_USER_ID, getUniqueUserId());
			dataManager.setConfig(DataStorageConfig.UNIQUE_DEVICE_ID, getDeviceId());
		}
		catch (Exception e)
		{
			dataManager = null;
			e.printStackTrace();
		}
	}
	
	protected HashSet<String> getAllowedFileTypes()
	{
		HashSet<String> fileTypes = new HashSet<String>();
		fileTypes.add(DataStorageConstants.LOG_FILE_SUFFIX);
		return fileTypes;
	}

	protected abstract String getLocalStorageDirectoryName();

	protected abstract String getUniqueUserId();
	
	protected abstract boolean shouldPrintLogMessages();
	
	protected abstract String getDeviceId();

	public void log(final String tag, final String data)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					dataManager.logExtra(tag, data);
				}
				catch (DataHandlerException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void logSensorData(final SensorData data)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					dataManager.logSensorData(data);
				}
				catch (DataHandlerException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public void logSensorData(final SensorData data, final DataFormatter formatter)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					dataManager.logSensorData(data, formatter);
				}
				catch (DataHandlerException e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void logSurveyResponse(final String jsonResponse)
	{
		log(TAG_SURVEY_RESPONSE, jsonResponse);
	}
	
	@SuppressLint("SimpleDateFormat")
	private String localTime()
	{
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zZ");
		return dateFormat.format(calendar.getTime());
	}
	
	protected JSONObject format(final String dataType, final String dataTitle, final String dataMessage)
	{
		try
		{
			JSONObject json = new JSONObject();
			if (dataType != null)
			{
				json.put(TAG_DATA_TYPE, dataType);
			}
			if (dataTitle != null)
			{
				json.put(TAG_DATA_TITLE, dataTitle);
			}
			if (dataMessage != null)
			{
				json.put(TAG_DATA_MESSAGE, dataMessage);
			}
			json.put(TAG_TIMESTAMP, System.currentTimeMillis());
			json.put(TAG_LOCAL_TIME, localTime());
			
			String userId = getUniqueUserId();
			if (userId != null)
			{
				json.put(TAG_USER_ID, userId);
			}
			String deviceId = getDeviceId();
			if (deviceId != null)
			{
				json.put(TAG_DEVICE_ID, deviceId);
			}
			
			return json;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public void logError(final int appVersion, final String tag, final String error)
	{
		new Thread()
		{
			@Override
			public void run()
			{
				try
				{
					if (tag != null && error != null)
					{
						JSONObject json = format(TAG_ERROR, tag, error);
						json.put(TAG_APP_VERSION, appVersion);
						dataManager.logError(json.toString());
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void logInteraction(final String tag, final String action)
	{
		try
		{
			if (tag != null && action != null)
			{
				JSONObject json = format(TAG_INTERACTION, tag, action);
				dataManager.logExtra(TAG_INTERACTION, json.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void logExtra(final String tag, final JSONObject action)
	{
		try
		{
			if (tag != null && action != null)
			{
				if (!action.has(TAG_LOGGER)) // avoid over-writing user data
				{
					JSONObject loggingInfo = format(null, null, null);
					action.put(TAG_LOGGER, loggingInfo);
				}
				dataManager.logExtra(tag, action.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
