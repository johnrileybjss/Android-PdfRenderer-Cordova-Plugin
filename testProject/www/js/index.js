var testFilePath = 'bjss-capabilities-software-development.pdf';

var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    showPDFInImage: function(data){
        if(!data){
            throw "Bitmap Data is undefined.";
            return;
        }

        var imgBlob = new Blob([data], {type: 'image/bmp'});

        var image = document.getElementById("image-display");
        if(!image){
            throw "Could not find image element to display Bitmap.";
            return;
        }

        image.src = URL.createObjectURL(imgBlob);
        document.body.appendChild(image);
    },

    updatePageCount: function(currentPage, pageCount){
        if(currentPage < 0 || pageCount < 0){
            throw "Invalid values found in page information.";
            return;
        }

        var text = document.getElementById("page-count");
        if(!text){
            throw "Could not find page count element to display page numbers.";
            return;
        }

        text.innerHTML = (currentPage + 1) + '/' + pageCount;
    },

    display: function(){
        try{
            window.PdfRendererPlugin.display(testFilePath, function(data){
                app.showPDFInImage(data);

                window.PdfRendererPlugin.getPageInfo(function(pageData){
                    app.updatePageCount(pageData.pageNumber, pageData.pageCount);
                });
            });
        }
        catch(err){
            console.log(err.message);
        }
    },

    previousPage: function(){
        try{
            window.PdfRendererPlugin.renderPreviousPage(function(data){
                app.showPDFInImage(data);

                window.PdfRendererPlugin.getPageInfo(function(pageData){
                    app.updatePageCount(pageData.pageNumber, pageData.pageCount);
                });
            });
        }
        catch(err){
            console.log(err.message);
        }
    },

    nextPage: function(){
        try{
            window.PdfRendererPlugin.renderNextPage(function(data){
                app.showPDFInImage(data);

                window.PdfRendererPlugin.getPageInfo(function(pageData){
                    app.updatePageCount(pageData.pageNumber, pageData.pageCount);
                });
            });
        }
        catch(err){
            console.log(err.message);
        }
    },

    onDeviceReady: function() {
        this.display();
    }
};

app.initialize();