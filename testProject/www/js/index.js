var testFilePath = 'bjss-capabilities-software-development.pdf';

var app = {
    // Application Constructor
    initialize: function() {
        console.log('app.initialize function running');
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    showPDFInImage: function(data){
        var imgBlob = new Blob([data], {type: 'image/bmp'});

        var image = document.getElementById("image-display");
        image.src = URL.createObjectURL(imgBlob);
        document.body.appendChild(image);
    },

    display: function(){
        console.log('app.display function running');
        window.PdfRendererPlugin.display(testFilePath, function(data){
            app.showPDFInImage(data);
        });
    },

    print: function(){
        console.log('app.print function running');
        window.PdfRendererPlugin.print(testFilePath, function(data){
            app.showPDFInImage(data);
        });
    },

    previousPage: function(){
        console.log('Previous Page Requested');
        //TODO
    },

    nextPage: function(){
        console.log('Next Page Requested');
        //TODO
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