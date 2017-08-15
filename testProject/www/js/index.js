var testFilePath = 'bjss-capabilities-software-development.pdf';

var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    showPDFInImage: function(data){
        var imgBlob = new Blob([data], {type: 'image/bmp'});

        var image = document.getElementById("image-display");
        image.src = URL.createObjectURL(imgBlob);
        document.body.appendChild(image);
    },

    updatePageCount: function(currentPage, pageCount){
        var text = document.getElementById("page-count");
        text.innerHTML = (currentPage + 1) + '/' + pageCount;
    },

    display: function(){
        window.PdfRendererPlugin.display(testFilePath, function(data){
            app.showPDFInImage(data);

            window.PdfRendererPlugin.getPageInfo(function(pageData){
                console.log(pageData);
                app.updatePageCount(pageData.pageNumber, pageData.pageCount);
            });
        });
    },

    previousPage: function(){
        window.PdfRendererPlugin.renderPreviousPage(function(data){
            app.showPDFInImage(data);

            window.PdfRendererPlugin.getPageInfo(function(pageData){
                console.log(pageData);
                app.updatePageCount(pageData.pageNumber, pageData.pageCount);
            });
        });
    },

    nextPage: function(){
        window.PdfRendererPlugin.renderNextPage(function(data){
            app.showPDFInImage(data);

            window.PdfRendererPlugin.getPageInfo(function(pageData){
                console.log(pageData);
                app.updatePageCount(pageData.pageNumber, pageData.pageCount);
            });
        });
    },

    onDeviceReady: function() {
        this.display();
    }
};

app.initialize();