var testFilePath = 'bjss-capabilities-software-development.pdf';

var app = {
    // Application Constructor
    initialize: function() {
        document.addEventListener('deviceready', this.onDeviceReady.bind(this), false);
    },

    setErrorMessage: function(msg){
        if(msg === undefined || msg === null){
            return;
        }

        var text = document.getElementById("error-message");
        if(!text){
            return;
        }

        text.innerHTML = msg;
    },

    showPDFInImage: function(data){
        if(!data){
            throw new Error("Bitmap Data is undefined.");
            return;
        }

        var imgBlob = new Blob([data], {type: 'image/bmp'});

        var image = document.getElementById("image-display");
        if(!image){
            throw new Error("Could not find image element to display Bitmap.");
            return;
        }

        image.src = URL.createObjectURL(imgBlob);
        document.body.appendChild(image);
    },

    updatePageCount: function(currentPage, pageCount){
        if(currentPage < 0 || pageCount < 0){
            throw new Error("Invalid values found in page information.");
            return;
        }

        var text = document.getElementById("page-count");
        if(!text){
            throw new Error("Could not find page count element to display page numbers.");
            return;
        }

        text.innerHTML = (currentPage + 1) + '/' + pageCount;
    },

    display: function(){
        try{
            window.PdfRendererPlugin.display(testFilePath, function(data){
                app.showPDFInImage(data);

                //Update the Page Count Label
                window.PdfRendererPlugin.getPageInfo(function(pageData){
                    app.updatePageCount(pageData.pageNumber, pageData.pageCount);
                    app.setErrorMessage("");
                },
                function(err){
                    console.log(err);
                    app.setErrorMessage(err);
                });
            },
            function(err){
                console.log(err);
                app.setErrorMessage(err);
            });
        }
        catch(err){
            console.log(err);
            app.setErrorMessage(err);
        }
    },

    previousPage: function(){
        try{
            window.PdfRendererPlugin.renderPreviousPage(function(data){
                app.showPDFInImage(data);

                //Update the Page Count Label
                window.PdfRendererPlugin.getPageInfo(function(pageData){
                    app.updatePageCount(pageData.pageNumber, pageData.pageCount);
                    app.setErrorMessage("");
                },
                function(err){
                    console.log(err);
                    app.setErrorMessage(err);
                });
            },
            function(err){
                console.log(err);
                app.setErrorMessage(err);
            });
        }
        catch(err){
            console.log(err);
            app.setErrorMessage(err);
        }
    },

    nextPage: function(){
        try{
            window.PdfRendererPlugin.renderNextPage(function(data){
                app.showPDFInImage(data);

                //Update the Page Count Label
                window.PdfRendererPlugin.getPageInfo(function(pageData){
                    app.updatePageCount(pageData.pageNumber, pageData.pageCount);
                    app.setErrorMessage("");
                },
                function(err){
                    console.log(err);
                    app.setErrorMessage(err);
                });
            },
            function(err){
                console.log(err);
                app.setErrorMessage(err);
            });
        }
        catch(err){
            console.log(err);
            app.setErrorMessage(err);
        }
    },

    onDeviceReady: function() {
        this.display();
    }
};

app.initialize();