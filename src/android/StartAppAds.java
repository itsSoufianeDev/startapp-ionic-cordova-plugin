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

    Context context = this.cordova.getActivity().getApplicationContext();
    private static final String SHARED_PREFS_GDPR_SHOWN = "gdpr_dialog_was_shown";
    private StartAppAd startAppAd = new StartAppAd(context);
    SharedPreferences SharedPref;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("init")) {
            this.init(args, callbackContext);
            return true;
        }

        if (action.equals("setConsent")) {
            Boolean consentValue = args.getBoolean(0);
            this.setConsent(consentValue, callbackContext);
            return true;
        }

        if (action.equals("loadInterstitial")) {
            this.loadInterstitial(callbackContext);
            return true;
        }

        if (action.equals("loadRewardedVideo")) {
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

                SharedPref = context.getSharedPreferences("", Context.MODE_PRIVATE);
                SharedPref.edit().putBoolean(SHARED_PREFS_GDPR_SHOWN, consented).commit();

                callback.success(1);
            }catch(Exception e){
                callback.error("Something went wrong: " + e);
            }
        }else{
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
                callback.success(1);
            }catch(Exception e){
                callback.error("Initialization failed: " + e);
            }
        }else{
            callback.error("No arguemnts supplied");
        }
    }

    private void loadInterstitial(CallbackContext callback){
        try{
            startAppAd.showAd();
            callback.success(1);
        }catch(Exception e){
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
                    c.success("REWARD");
                }
            });

            rewardedVideo.loadAd(AdMode.REWARDED_VIDEO, new AdEventListener() {

                @Override
                public void onReceiveAd(Ad arg0) {
                    c.success("LOADED");
                    rewardedVideo.showAd();
                }

                @Override
                public void onFailedToReceiveAd(Ad arg0) {
                    c.success("FAILED");
                    Log.e("MainActivity", "Failed to load rewarded video with reason");
                }
            });
        }catch(Exception e){
            c.success("FAILED TO LOAD REWARDED VIDEO: " + e);
        }
    }
}