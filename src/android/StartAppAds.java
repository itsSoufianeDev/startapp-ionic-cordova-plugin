package cordova.plugin.startappads;

import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class StartAppAds extends CordovaPlugin {

    Context context = 
        .cordova.getActivity().getApplicationContext();
    private static final String SHARED_PREFS_GDPR_SHOWN = "gdpr_dialog_was_shown";
    private StartAppAd startAppAd = new StartAppAd(this.cordova.getActivity().getApplicationContext());
    SharedPreferences SharedPref;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("init")) {
            Log.e("MainActivity", "Init called");
            this.init(args, callbackContext);
            return true;
        }

        if (action.equals("setConsent")) {
            Log.e("MainActivity", "setConsent Called");
            Boolean consentValue = args.getBoolean(0);
            this.setConsent(consentValue, callbackContext);
            return true;
        }

        if (action.equals("loadInterstitial")) {
            Log.e("MainActivity", "loadInterstitial called");
            this.loadInterstitial(callbackContext);
            return true;
        }

        if (action.equals("loadRewardedVideo")) {
            Log.e("MainActivity", "loadRewardedVideo called");
            this.loadRewardedVideo(callbackContext);
            return true;
        }

        return false;
    }

    private void setConsent(Boolean consented, CallbackContext callback){
        if(consented != null){
            try{
                StartAppSDK.setUserConsent(this.context,
                        "pas",
                        System.currentTimeMillis(),
                        consented);

                SharedPref = this.cordova.getActivity().getApplicationContext().getSharedPreferences("", Context.MODE_PRIVATE);
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

    private void init(JSONArray args, CallbackContext callback){
        if(args != null){
            try{
                String appId = args.getJSONObject(0).getString("appId");
                boolean returnAds = args.getJSONObject(0).getBoolean("returnAds");
                // StartApp init
                StartAppSDK.init(this.context, appId, returnAds);
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

    private void loadInterstitial(CallbackContext callback){
        try{
            startAppAd.showAd();
            Log.e("MainActivity", "loadInterstitial success");
            callback.success(1);
        }catch(Exception e){
            Log.e("MainActivity", "Interstitial err: " + e);
            callback.error("Failed to load Interstitial ad: " + e);
        }
    }

    private void loadRewardedVideo(CallbackContext callback){
        final CallbackContext c = callback;
        try{
            final StartAppAd rewardedVideo = new StartAppAd(this.context);
            rewardedVideo.setVideoListener(new VideoListener(){

                @Override
                public void onVideoCompleted() {
                    Log.e("MainActivity", "Give cookie");
                    c.success("REWARD");
                }
            });

            rewardedVideo.loadAd(AdMode.REWARDED_VIDEO, new AdEventListener() {

                @Override
                public void onReceiveAd(Ad arg0) {
                    Log.e("MainActivity", "Ad received");
                    rewardedVideo.showAd();
                    c.success("LOADED");
                }

                @Override
                public void onFailedToReceiveAd(Ad arg0) {
                    Log.e("MainActivity", "Failed to load rewarded video with reason");
                    c.success("FAILED");
                }
            });
        }catch(Exception e){
            Log.e("MainActivity", "Rewarded video err: " + e);
            c.success("FAILED TO LOAD REWARDED VIDEO: " + e);
        }
    }
}
