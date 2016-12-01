# cordova-plugin-laser-scanner
Cordova Laser Scanner for Speedata Industrial PDA devices

思必拓(Speedata)手持终端激光扫描器的Cordova接口

# Usages 用途

1. Call scan 调用扫描头扫描

```javascript
LaserScanner.scan(); 
// then the device will blink and read the barcode
// 此时设备会发出激光开始扫描二维码

```

2. Receive scan result from device 从设备获取扫描结果

```javascript

// when the barcode is decoded and result is returned by device
// both a window and document `barcodeReceived` event will be fired
// register a listener and you can get the data form `LaserScanner.result`

// 当二维码被解码完毕并且返回时
// 会触发 window 和 document 的 `barcodeReceived` 事件
// 监听这个事件并从`LaserScanner.result`中获取结果数据

window.addEventListener("barcodeReceived",function(){  //also document.addEventListener.....
    var result = LaserScanner.result;
    console.log("the result of this barcode is + " result);
},false)

```

# License

MIT
