var exec = require('cordova/exec');

var LaserScanner = {
  scan : function (success, error) {
        exec(success, error, "LaserScanner", "scan", []);
  }
}

module.exports = LaserScanner;
