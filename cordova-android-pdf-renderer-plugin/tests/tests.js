exports.defineAutoTests = function () {
    describe('display (cordova.plugins.PdfRendererPlugin.display', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.display).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.display).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.display();
            }).toThrowError("Filepath argument must be of type 'string'");
        });
        it("should throw an Error if the filepath argument is not a string", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.display(42, function() {}, function() {});
            }).toThrowError("Filepath argument must be of type 'string'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.display("test", 42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.display("test", function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });

    describe('renderPage (cordova.plugins.PdfRendererPlugin.renderPage', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.renderPage).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.renderPage).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPage();
            }).toThrowError("Page Number argument must be of type 'number'");
        });
        it("should throw an Error if the page number argument is not a number", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPage("test", function() {}, function() {});
            }).toThrowError("Page Number argument must be of type 'number'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPage(42, 42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPage(42, function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });

    describe('renderNextPage (cordova.plugins.PdfRendererPlugin.renderNextPage', function(){
        it('should be defined', function(){
             expect(cordova.plugins.PdfRendererPlugin.renderNextPage).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.renderNextPage).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderNextPage();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderNextPage(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderNextPage(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });

    describe('renderPreviousPage (cordova.plugins.PdfRendererPlugin.renderPreviousPage', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.renderPreviousPage).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.renderPreviousPage).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPreviousPage();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPreviousPage(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.renderPreviousPage(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });
    describe('close (cordova.plugins.PdfRendererPlugin.close', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.close).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.close).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.close();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.close(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.close(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });
    describe('getPageInfo (cordova.plugins.PdfRendererPlugin.getPageInfo', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.getPageInfo).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.getPageInfo).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getPageInfo();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getPageInfo(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getPageInfo(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });
    describe('getCurrentPageNumber (cordova.plugins.PdfRendererPlugin.getCurrentPageNumber', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.getCurrentPageNumber).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.getCurrentPageNumber).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getCurrentPageNumber();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getCurrentPageNumber(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getCurrentPageNumber(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });
    describe('getPageCount (cordova.plugins.PdfRendererPlugin.getPageCount', function(){
        it('should be defined', function(){
            expect(cordova.plugins.PdfRendererPlugin.getPageCount).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof cordova.plugins.PdfRendererPlugin.getPageCount).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getPageCount();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getPageCount(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                cordova.plugins.PdfRendererPlugin.getPageCount(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });
}