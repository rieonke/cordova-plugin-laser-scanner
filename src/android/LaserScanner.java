package cn.rieon.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.core.content.ContextCompat;
import android.util.Log;

public class LaserScanner extends CordovaPlugin {

    /**
     * broadcaster receive
     */
    private String SCANNER_RETURN_DATA = "com.se4500.onDecodeComplete";
    /**
     * broadcaster start scan
     */
    private String SCANNER_START_SCAN = "com.geomobile.se4500barcode";
    /**
     * broadcaster stop scan
     */
    private String SCANNER_STOP_SCAN = "com.geomobile.se4500barcode.poweroff";

    private static final String LOG_TAG = "LaserReceiver";

    private CallbackContext callbackContext = null;

    private BroadcastReceiver receiver = null;

    private String lastBarcode = null;


    /**
     * get activity
     * @return Activity
     */
    private Activity getActivity(){
        return this.cordova.getActivity();
    }

    /**
     * cordova initialize
     * @param cordova
     * @param webView
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {

        super.initialize(cordova, webView);


        IntentFilter iFilter = new IntentFilter();
        /**
         * call scanner
         */
        iFilter.addAction(SCANNER_RETURN_DATA);
        getActivity().registerReceiver(receiver, iFilter, ContextCompat.RECEIVER_EXPORTED);

        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(SCANNER_RETURN_DATA)) {
                        String data = intent.getStringExtra("se4500");
                        resultHandler(data);
                    }
                }
            };
        }

        /**
         * register receiver
         */

        getActivity().registerReceiver(receiver, iFilter, ContextCompat.RECEIVER_EXPORTED);

    }


    /**
     * execute action
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        this.callbackContext = callbackContext;

        if (action.equals("scan")) {
            startScan();
            return true;
        }
        if (action.equals("receive")){
            sendUpdate(lastBarcode,true);
            return true;
        }
        return false;
    }

    /**
     * stop scan and unregister receiver when destroy
     */
    @Override
    public void onDestroy() {

        stopScan();

        removeLaserScannerListener();

        super.onDestroy();
    }


    @Override
    public void onReset() {

        stopScan();

        removeLaserScannerListener();

        super.onReset();
    }

    /**
     * start scan
     */
    private void startScan() {
        Intent intent = new Intent();
        intent.setAction(SCANNER_START_SCAN);
        getActivity().sendBroadcast(intent, null);
    }

    private  void stopScan(){
        Intent intent = new Intent();
        intent.setAction(SCANNER_STOP_SCAN);
        getActivity().sendBroadcast(intent);
    }


    /**
     * Stop the receiver and set it to null.
     */
    private void removeLaserScannerListener() {
        if (this.receiver != null) {
            try {
                getActivity().unregisterReceiver(this.receiver);
                this.receiver = null;
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error unregistering LaserReceiver receiver: " + e.getMessage(), e);
            }
        }
    }


    private void resultHandler(String data){

            sendUpdate(data,true);
            lastBarcode = data;

    }


    private void sendUpdate(String data, boolean keepCallback) {

        if (callbackContext != null) {
            PluginResult result = new PluginResult(PluginResult.Status.OK, data);
            result.setKeepCallback(keepCallback);
            callbackContext.sendPluginResult(result);
        }

    }

}
