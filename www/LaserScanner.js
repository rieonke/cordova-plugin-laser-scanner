var cordova = require('cordova'),
    exec = require('cordova/exec'),
    channel = require('cordova/channel');

var LaserScanner = function (){
    this.result = null;
};

LaserScanner.prototype.receive = function () {
    exec(scanner.onResult,scanner.onError,"LaserScanner","receive",[]);
};

LaserScanner.prototype.scan = function (){
    exec(scanner.onResult, scanner.onError, "LaserScanner", "scan", []);
};

LaserScanner.prototype.onResult = function(result){
    if(result == null) return;
    scanner.result = result;
    cordova.fireDocumentEvent("barcodeReceived");
    cordova.fireWindowEvent("barcodeReceived");
};

LaserScanner.prototype.onError = function(error){
    console.log("Laser Scanner error!" + error);
};


var scanner = new LaserScanner();

/**
 * ???
 */
channel.createSticky('onCordovaConnectionReady');
//channel.waitForInitialization('onCordovaConnectionReady');

channel.onCordovaReady.subscribe(function () {
    exec(scanner.onResult,scanner.onError,"LaserScanner","receive",[]);
});

module.exports = scanner;
