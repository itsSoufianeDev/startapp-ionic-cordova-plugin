var exec = require('cordova/exec');


function Startapp () {}

Startapp.prototype.init = (args, success, error) => {
	exec(success, error, 'init', [args]);
}

Startapp.prototype.setConsent = (args, success, error) => {
	exec(success, error, 'setConsent', [args]);
}

Startapp.prototype.loadInterstitial = (success, error) => {
	exec(success, error, 'loadInterstitial', []);
}

Startapp.prototype.loadRewardedVideo = (success, error) => {
	exec(success, error, 'loadRewardedVideo', []);
}

var StartAppAds = new Startapp();
module.exports = StartAppAds;