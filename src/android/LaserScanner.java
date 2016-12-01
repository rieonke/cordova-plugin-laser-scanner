package cn.rieon.cordova;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class LaserScanner extends CordovaPlugin {

    /**
     * broadcaster receive
     */
    private String SCANNER_RETURN_DATA = "com.se4500.onDecodeComplete";
    //调用扫描广播
    private String SCANNER_START_SCAN = "com.geomobile.se4500barcode";

    private String SCANNER_STOP_SCAN = "com.geomobile.se4500barcode.poweroff";

    private static final String LOG_TAG = "LaserScanner";

    private CallbackContext callbackContext;

    private BroadcastReceiver receiver;

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
        this.cordova.getActivity().registerReceiver(receiver, iFilter);

        if (this.receiver == null) {
            this.receiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    if (action.equals(SCANNER_RETURN_DATA)) {
                        String data = intent.getStringExtra("se4500");
                        /**
                         * return data to cordova callback
                         */
                        callbackContext.success(data);
                    }
                }
            };
        }

        /**
         * register receiver
         */
        this.cordova.getActivity().registerReceiver(this.receiver, iFilter);

        this.callbackContext = null;

    }


    /**
     * execute action
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        if (action.equals("scan")) {
            this.callbackContext = callbackContext;
            this.startScan();
            return true;
        }
        return false;
    }

    /**
     * stop scan and unregister receiver when destroy
     */
    public void onDestroy() {
        Intent intent = new Intent();
        intent.setAction(SCANNER_STOP_SCAN);
        webView.getContext().sendBroadcast(intent);
        if (this.receiver != null) {
            try {
                webView.getContext().unregisterReceiver(this.receiver);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error unregistering barcode receiver: " + e.getMessage(), e);
            } finally {
                receiver = null;
            }
        }
        super.onDestroy();
    }

    /**
     * start scan
     */
    private void startScan() {
        Intent intent = new Intent();
        intent.setAction(SCANNER_START_SCAN);
        this.cordova.getActivity().sendBroadcast(intent, null);
    }

}
