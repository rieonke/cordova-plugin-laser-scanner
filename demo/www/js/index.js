var app = {
    // Application Constructor
    initialize: function () {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function () {
        this.receivedEvent('deviceready');

        var startbtn = document.getElementById("start-btn");
        var clearbtn = document.getElementById("clear-btn");
        var alwaysbtn = document.getElementById("always-btn");
        var scanresult = document.getElementById("scan-result");

        startbtn.addEventListener("click", function () {
              LaserScanner.scan();
        }, false);



        clearbtn.addEventListener("click", function () {
            scanresult.innerHTML = "<p>Scan Result</p>";
        }, false);


        document.addEventListener("barcodeReceived",function(){
            var el = "<p>" + LaserScanner.result + "</p>";
            scanresult.innerHTML = scanresult.innerHTML + el;
        })


        var alwaysToggle = false;
        var t;
        alwaysbtn.addEventListener("click", function () {
            alwaysToggle = !alwaysToggle;
            if (alwaysToggle) {
                alwaysbtn.innerText = "Stop";
                t = setInterval(function () {

                    LaserScanner.scan();

                }, 1000);

            } else {
                alwaysbtn.innerText = "Always";

                clearInterval(t);
            }
        }, false);
    },

    // Update DOM on a Received Event
    receivedEvent: function (id) {
        var parentElement = document.getElementById(id);
        var listeningElement = parentElement.querySelector('.listening');
        var receivedElement = parentElement.querySelector('.received');

        listeningElement.setAttribute('style', 'display:none;');
        receivedElement.setAttribute('style', 'display:block;');

        console.log('Received Event: ' + id);
    }
};

app.initialize();