var exec = require('cordova/exec');


var Startapp = {
	/**
	Initialize SDK
	**/
	init: function(args, success, error){
		exec(success, error,"StartAppAds", "init", [args]);
	},


	/**
	Set User Consent: GDRP for EU Users
	**/
	setConsent: function(args, success, error){
		exec(success, error,"StartAppAds", "setConsent", [args]);
	},


	/**
	Load an Interstitial Ad
	**/
	loadInterstitial: function(success, error){
		exec(success, error,"StartAppAds", "loadInterstitial", []);
	},


	/**
	Load a Rewarded Video Ad
	**/
	loadRewardedVideo: function(success, error){
		exec(success, error,"StartAppAds", "loadRewardedVideo", []);
	}

}

window.plugins.startapp = Startapp;


/*var StartAppAds = new Startapp();
module.exports = StartAppAds;*/
/*
Startapp.install = function () {
  if (!window.plugins) {
    window.plugins = {};
  }

  window.plugins.startapp = new Startapp();
  console.log('Startapp plugin installed');
  return window.plugins.startapp;
};

cordova.addConstructor(Startapp.install);*/