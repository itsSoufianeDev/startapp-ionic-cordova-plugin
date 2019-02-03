var exec = require('cordova/exec');


function Startapp () {}

/**
Initialize SDK
**/
Startapp.prototype.init = (args, success, error) => {
	exec(success, error, 'init', [args]);
}


/**
Set User Consent: GDRP for EU Users
**/
Startapp.prototype.setConsent = (args, success, error) => {
	exec(success, error, 'setConsent', [args]);
}


/**
Load an Interstitial Ad
**/
Startapp.prototype.loadInterstitial = (success, error) => {
	exec(success, error, 'loadInterstitial', []);
}


/**
Load a Rewarded Video Ad
**/
Startapp.prototype.loadRewardedVideo = (success, error) => {
	exec(success, error, 'loadRewardedVideo', []);
}

var StartAppAds = new Startapp();
module.exports = StartAppAds;