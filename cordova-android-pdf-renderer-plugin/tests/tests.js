exports.defineAutoTests = function () {
    describe('display (cordova.plugins.PdfRendererPlugin.display', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.display).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.display).toEqual("function");
        });
    });

    describe('renderPage (cordova.plugins.PdfRendererPlugin.renderPage', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.renderPage).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.renderPage).toEqual("function");
        });
    });

    describe('renderNextPage (cordova.plugins.PdfRendererPlugin.renderNextPage', function(){
        it('should be defined', function(){
             expect(cordova.plugins.PdfRendererPlugin.renderNextPage).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.renderNextPage).toEqual("function");
        });
    });

    describe('renderPreviousPage (cordova.plugins.PdfRendererPlugin.renderPreviousPage', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.renderPreviousPage).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.renderPreviousPage).toEqual("function");
        });
    });
    describe('close (cordova.plugins.PdfRendererPlugin.close', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.close).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.close).toEqual("function");
        });
    });
    describe('getPageInfo (cordova.plugins.PdfRendererPlugin.getPageInfo', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.getPageInfo).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.getPageInfo).toEqual("function");
        });
    });
    describe('getCurrentPageNumber (cordova.plugins.PdfRendererPlugin.getCurrentPageNumber', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.getCurrentPageNumber).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.getCurrentPageNumber).toEqual("function");
        });
    });
    describe('getPageCount (cordova.plugins.PdfRendererPlugin.getPageCount', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.getPageCount).toBeDefined();
        });

        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.getPageCount).toEqual("function");
        });
    });
}