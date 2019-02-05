package cordova.plugin.startappads;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;


import com.startapp.android.publish.ads.nativead.NativeAdDetails;
import com.startapp.android.publish.ads.nativead.NativeAdPreferences;
import com.startapp.android.publish.ads.nativead.StartAppNativeAd;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppAd.AdMode;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.VideoListener;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.startapp.android.publish.adsCommon.adListeners.AdEventListener;


/**
 * This class echoes a string called from JavaScript.
 */
public class StartAppAds extends CordovaPlugin {

    
    private static final String SHARED_PREFS_GDPR_SHOWN = "gdpr_dialog_was_shown";
    SharedPreferences SharedPref;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Context context = this.cordova.getActivity().getApplicationContext();
        StartAppAd startAppAd = new StartAppAd(context);

        if (action.equals("init")) {
            Log.e("MainActivity", "Init called");
            this.init(context, args, callbackContext);
            return true;
        }

        if (action.equals("setConsent")) {
            Log.e("MainActivity", "setConsent Called");
            Boolean consentValue = args.getBoolean(0);
            this.setConsent(context, consentValue, callbackContext);
            return true;
        }

        if (action.equals("loadInterstitial")) {
            Log.e("MainActivity", "loadInterstitial called");
            this.loadInterstitial(context, startAppAd, callbackContext);
            return true;
        }

        if (action.equals("loadRewardedVideo")) {
            Log.e("MainActivity", "loadRewardedVideo called");
            this.loadRewardedVideo(context, startAppAd, callbackContext);
            return true;
        }

        return false;
    }

    private void init(Context cthis, JSONArray args, CallbackContext callback){
        if(args != null){
            try{
                String appId = args.getJSONObject(0).getString("appId");
                boolean returnAds = args.getJSONObject(0).getBoolean("returnAds");
                // StartApp init
                StartAppSDK.init(cthis, appId, returnAds);
                Log.e("MainActivity", "Init succeeded");
                callback.success(1);
            }catch(Exception e){
                Log.e("MainActivity", "Init failed err: " + e);
                callback.error("Initialization failed: " + e);
            }
        }else{
            Log.e("MainActivity", "Init invalid inputs: ");
            callback.error("No arguemnts supplied");
        }
    }

    private void setConsent(Context cthis, Boolean consented, CallbackContext callback){
        if(consented != null){
            try{
                StartAppSDK.setUserConsent((Context) cthis,
                        "pas",
                        System.currentTimeMillis(),
                        consented);

                //getPreferences(Context.MODE_PRIVATE).edit().putBoolean(SHARED_PREFS_GDPR_SHOWN, consented).commit();
                SharedPref = cthis.getSharedPreferences("", Context.MODE_PRIVATE);
                SharedPref.edit().putBoolean(SHARED_PREFS_GDPR_SHOWN, consented).commit();
                callback.success(1);
            }catch(Exception e){
                Log.e("MainActivity", "SetConsent err: " + e);
                callback.error("Something went wrong: " + e);
            }
        }else{
            Log.e("MainActivity", "SetConsent invalid value");
            callback.error("Invalid consent value");
        }
    }


    private void loadInterstitial(Context cthis, StartAppAd sta, CallbackContext callback){
        try{
            sta.showAd();
            Log.e("MainActivity", "loadInterstitial success");
            callback.success(1);
        }catch(Exception e){
            Log.e("MainActivity", "Interstitial err: " + e);
            callback.error("Failed to load Interstitial ad: " + e);
        }
    }

    private void loadRewardedVideo(Context cthis, StartAppAd sta, CallbackContext callback){
        final CallbackContext c = callback;
        try{
            final StartAppAd rewardedVideo = sta;
            rewardedVideo.setVideoListener(new VideoListener(){

                @Override
                public void onVideoCompleted() {
                    Log.e("MainActivity", "Give cookie");
                    //c.success();
                    super.appView.loadUrl("javascript:cordova.fireDocumentEvent('REWARD',{})");
                }
            });

            rewardedVideo.loadAd(AdMode.REWARDED_VIDEO, new AdEventListener() {

                @Override
                public void onReceiveAd(Ad arg0) {
                    Log.e("MainActivity", "Ad received");
                    rewardedVideo.showAd();
                    //this.forwardEventToJS("LOADED");
                    super.appView.loadUrl("javascript:cordova.fireDocumentEvent('LOADED',{})");
                    //c.success();
                }

                @Override
                public void onFailedToReceiveAd(Ad arg0) {
                    Log.e("MainActivity", "Failed to load rewarded video with reason");
                    //this.forwardEventToJS("FAILED");
                    super.appView.loadUrl("javascript:cordova.fireDocumentEvent('FAILED',{})");
                    //c.success("FAILED");
                }
            });
        }catch(Exception e){
            Log.e("MainActivity", "Rewarded video err: " + e);
            c.error("FAILED TO LOAD REWARDED VIDEO: " + e);
        }
    }
}
