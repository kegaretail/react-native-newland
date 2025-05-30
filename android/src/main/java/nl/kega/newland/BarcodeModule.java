package nl.kega.newland;

import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.os.Bundle;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class BarcodeModule extends ReactContextBaseJavaModule implements LifecycleEventListener {
	
	private final ReactApplicationContext context;
	private static final String TAG = "BarcodeModule";
	private static final String ACTION_SCAN_RESULT = "nlscan.action.SCANNER_RESULT";

	private BroadcastReceiver scanReceiver;
	private boolean isEnabled = false;
	private Boolean reading = false;

    public BarcodeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        reactContext.addLifecycleEventListener(this);

		if (android.os.Build.MANUFACTURER.equalsIgnoreCase("Newland") ) {	
			scanReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {				
					if (ACTION_SCAN_RESULT.equals(intent.getAction())) {
						String scanResult = intent.getStringExtra("SCAN_BARCODE1");
						String scanStatus = intent.getStringExtra("SCAN_STATE");
						int barcodeType = intent.getIntExtra("SCAN_BARCODE_TYPE", -1);
						
						if (scanResult != null && !scanResult.isEmpty()) {
							WritableMap params = Arguments.createMap();
							params.putString("data", scanResult);
							params.putString("type", BarcodeTypes.getBarcodeTypeName(barcodeType));
							params.putInt("typeId", barcodeType);
				
							log("Barcode scanned: " + scanResult + " (type: " + BarcodeTypes.getBarcodeTypeName(barcodeType) + " [" + barcodeType + "]) Status: " + scanStatus);
							
							dispatchEvent("BarcodeEvent", params);
						}
					}
				}
			};
		}
        
    }

	@ReactMethod
    public void read(ReadableMap config) {
		log("Starting barcode scan");
		try {
			if (scanReceiver != null) {

				IntentFilter filter = new IntentFilter("nlscan.action.SCANNER_RESULT");
				context.registerReceiver(scanReceiver, filter);

				Intent intent = new Intent("ACTION_BAR_SCANCFG");
				intent.putExtra("EXTRA_SCAN_NOTY_SND", 0);

				context.sendBroadcast(intent);

				isEnabled = true;
				reading = true;
			}
		} catch (Exception e) {
			log("Error enabling scanner: " + e.getMessage());
		}
	}

	@ReactMethod
    public void release() {
		log("Releasing barcode scanner");
		disable();
		if (scanReceiver != null) {
			try {
				context.unregisterReceiver(scanReceiver);

				reading = false;
			} catch (Exception e) {
				log("Error unregistering receiver: " + e.getMessage());
			}
		}
	}
	@ReactMethod
	public void disable() {
		log("Disabling barcode scanner");
		
		try {
			if (scanReceiver != null) {
				Intent intent = new Intent("ACTION_BAR_SCANCFG");
				intent.putExtra("EXTRA_SCAN_POWER", 0);

				context.sendBroadcast(intent);

				isEnabled = false;
			}
		} catch (Exception e) {
			log("Error disabling scanner: " + e.getMessage());
		}
    }

	@ReactMethod
    public void enable() {
		log("Enabling barcode scanner");

		try {
			if (scanReceiver != null) {
				Intent intent = new Intent("ACTION_BAR_SCANCFG");
				intent.putExtra("EXTRA_SCAN_POWER", 1);

				context.sendBroadcast(intent);
				isEnabled = true;
			}
		} catch (Exception e) {
			log("Error enabling scanner: " + e.getMessage());
		}
    }

	@ReactMethod
    public void addListener(String eventName) {
		log("addListener");
    }

    @ReactMethod
    public void removeListeners(Integer count) {
		log("removeListeners");
    }

    @Override
    public String getName() {
        return "BarcodeModule";
    }
	
	@Override
	public void onHostResume() {
		log("Host resumed");
		if (scanReceiver != null) {
			try {
				IntentFilter filter = new IntentFilter("nlscan.action.SCANNER_RESULT");
				context.registerReceiver(scanReceiver, filter);

				if (reading) {
					Intent intent = new Intent("ACTION_BAR_SCANCFG");
					intent.putExtra("EXTRA_SCAN_NOTY_SND", 0);

					context.sendBroadcast(intent);
				}
			} catch (Exception e) {
				log("Error re-registering receiver on resume: " + e.getMessage());
			}
		}
	}

	@Override
	public void onHostPause() {
		log("Host paused");
		try {
			if (scanReceiver != null) {
				context.unregisterReceiver(scanReceiver);
			}
		} catch (Exception e) {
			log("Error unregistering receiver on pause: " + e.getMessage());
		}
	}

	@Override
	public void onHostDestroy() {
		log("Host destroyed");
		release();
	}

	private static void log(String message) {
		Log.d(TAG, message);
	}

	private void dispatchEvent(String eventName, WritableMap params) {
		try {
			context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
		} catch (Exception e) {
			log("Error sending event: " + e.getMessage());
		}
	}

	private void dispatchEvent(String eventName, WritableArray params) {
		try {
			context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
		} catch (Exception e) {
			log("Error sending event: " + e.getMessage());
		}
	}
}