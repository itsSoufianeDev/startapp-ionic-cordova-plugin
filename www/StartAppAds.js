var exec = require('cordova/exec');


function Startapp () {}

/**
Initialize SDK
**/
Startapp.prototype.init = function(args, success, error){
	exec(success, error,'StartAppAds', 'init', [args]);
}


/**
Set User Consent: GDRP for EU Users
**/
Startapp.prototype.setConsent = function(args, success, error){
	exec(success, error,'StartAppAds', 'setConsent', [args]);
}


/**
Load an Interstitial Ad
**/
Startapp.prototype.loadInterstitial = function(success, error){
	exec(success, error,'StartAppAds', 'loadInterstitial', []);
}


/**
Load a Rewarded Video Ad
**/
Startapp.prototype.loadRewardedVideo = function(success, error){
	exec(success, error,'StartAppAds', 'loadRewardedVideo', []);
}

/*var StartAppAds = new Startapp();
module.exports = StartAppAds;*/

if (!window.plugins) {
  window.plugins = {};
}
if (!window.plugins.startappads) {
  window.plugins.startappads = new Startapp();
}

if (module.exports) {
  module.exports = Startapp;
}