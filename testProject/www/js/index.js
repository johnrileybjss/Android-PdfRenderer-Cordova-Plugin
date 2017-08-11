var testFilePath = 'bjss-capabilities-software-development.pdf';

var app = {
    // Application Constructor
    initialize: function() {
        console.log('app.initialize function running');
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    display: function(){
        console.log('app.display function running');
        window.PdfRendererPlugin.display(testFilePath, function(data){
            console.log('Bitmap Bytes');
            console.log(data);
        });
    },

    print: function(){
        console.log('app.print function running');
        window.PdfRendererPlugin.print(testFilePath, function(data){
            console.log('Bitmap Bytes');
            console.log(data);
        });
    },

    // deviceready Event Handler
    //
    // Bind any cordova events here. Common events are:
    // 'pause', 'resume', etc.
    onDeviceReady: function() {
        console.log('app.onDeviceReady function running');
        this.display();
    }
};



app.initialize();