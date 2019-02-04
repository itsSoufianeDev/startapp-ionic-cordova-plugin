var exec = require('cordova/exec');


function Startapp () {}

/**
Initialize SDK
**/
Startapp.prototype.init = (args, success, error) => {
	exec(success, error,'StartAppAds', 'init', [args]);
}


/**
Set User Consent: GDRP for EU Users
**/
Startapp.prototype.setConsent = (args, success, error) => {
	exec(success, error,'StartAppAds', 'setConsent', [args]);
}


/**
Load an Interstitial Ad
**/
Startapp.prototype.loadInterstitial = (success, error) => {
	exec(success, error,'StartAppAds', 'loadInterstitial', []);
}


/**
Load a Rewarded Video Ad
**/
Startapp.prototype.loadRewardedVideo = (success, error) => {
	exec(success, error,'StartAppAds', 'loadRewardedVideo', []);
}

/*var StartAppAds = new Startapp();
module.exports = StartAppAds;*/

Startapp.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.startapp = new Startapp();
  console.log('Startapp plugin installed');
  return window.plugins.startapp;
};

cordova.addConstructor(Startapp.install);