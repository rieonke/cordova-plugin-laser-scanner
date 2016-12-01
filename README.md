# cordova-plugin-laser-scanner
Cordova Laser Scanner for Speedata Industrial PDA devices

# Usage

LaserScanner.scan(function(success){
    if(success != null){
       console.log("Scan result is + " + success)
    }
},function(error){
    console.log(error)
});