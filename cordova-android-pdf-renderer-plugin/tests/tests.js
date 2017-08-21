exports.defineAutoTests = function () {
    describe('PdfRendererPlugin.display', function(){
        it('should be defined', function(){
            expect(window.PdfRendererPlugin.display).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.display).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.display();
            }).toThrowError("Filepath argument must be of type 'string'");
        });
        it("should throw an Error if the filepath argument is not a string", function() {
            expect(function() {
                window.PdfRendererPlugin.display(42, function() {}, function() {});
            }).toThrowError("Filepath argument must be of type 'string'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.display("test", 42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.display("test", function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
        it("should return an error through the onFailure callback if the file path does not have a pdf extension", function() {
            window.PdfRendererPlugin.display("test.txt", function() {}, function(error) {
                expect(error).toEqual('Invalid File Extension provided to Pdf Renderer Service: txt');
            });
        });
        it("should return an error through the onFailure callback if the file cannot be found", function() {
            window.PdfRendererPlugin.display("test.pdf", function() {}, function(error) {
                expect(error).toEqual('Could not find the requested file: test.pdf');
            });
        });
    });

    describe('PdfRendererPlugin.renderNextPage', function(){
        it('should be defined', function(){
             expect(window.PdfRendererPlugin.renderNextPage).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.renderNextPage).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.renderNextPage();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.renderNextPage(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.renderNextPage(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
        it("should return an error through the onFailure callback if no pages are available to display", function() {
            window.PdfRendererPlugin.renderNextPage(function() {}, function(error) {
                expect(error).toEqual('No Pages available to display.');
            });
        });
    });

    describe('PdfRendererPlugin.renderPreviousPage', function(){
        it('should be defined', function(){
            expect(window.PdfRendererPlugin.renderPreviousPage).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.renderPreviousPage).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.renderPreviousPage();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.renderPreviousPage(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.renderPreviousPage(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
        it("should return an error through the onFailure callback if no pages are available to display", function() {
            window.PdfRendererPlugin.renderPreviousPage(function() {}, function(error) {
                expect(error).toEqual('No Pages available to display.');
            });
        });
    });
    describe('PdfRendererPlugin.close', function(){
        it('should be defined', function(){
            expect(window.PdfRendererPlugin.close).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.close).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.close();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.close(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.close(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
    });
    describe('PdfRendererPlugin.getPageInfo', function(){
        it('should be defined', function(){
            expect(window.PdfRendererPlugin.getPageInfo).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.getPageInfo).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.getPageInfo();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.getPageInfo(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.getPageInfo(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
        it("should return a default JSON object if no document has been loaded.", function() {
            var expectedOutput = {
                filePath: "",
                pageNumber: 0,
                pageCount: 0
            };

            window.PdfRendererPlugin.getPageInfo(function(pageInformation) {
                expect(pageInformation).toEqual(expectedOutput);
            }, function() {});
        });
    });
    describe('PdfRendererPlugin.getCurrentPageNumber', function(){
        it('should be defined', function(){
            expect(window.PdfRendererPlugin.getCurrentPageNumber).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.getCurrentPageNumber).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.getCurrentPageNumber();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.getCurrentPageNumber(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.getCurrentPageNumber(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
        it("should return a zero value if no document has been loaded.", function() {
            window.PdfRendererPlugin.getCurrentPageNumber(function(pageNo) {
                expect(pageNo).toBe(0);
            }, function() {});
        });
    });
    describe('PdfRendererPlugin.getPageCount', function(){
        it('should be defined', function(){
            expect(window.PdfRendererPlugin.getPageCount).toBeDefined();
        });
        it('should be a function', function(){
            expect(typeof window.PdfRendererPlugin.getPageCount).toEqual("function");
        });
        it("should throw an Error with no arguments", function() {
            expect(function() {
                window.PdfRendererPlugin.getPageCount();
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onSuccess callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.getPageCount(42, function() {});
            }).toThrowError("onSuccess callback must be of type 'function'");
        });
        it("should throw an Error if the onFailure callback is not a function", function() {
            expect(function() {
                window.PdfRendererPlugin.getPageCount(function() {}, 42);
            }).toThrowError("onFailure callback must be of type 'function'");
        });
        it("should return a zero value if no document has been loaded.", function() {
            window.PdfRendererPlugin.getPageCount(function(pageCount) {
                expect(pageCount).toBe(0);
            }, function() {});
        });
    });
}