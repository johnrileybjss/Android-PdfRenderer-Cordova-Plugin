
var PLUGIN_NAME = "PdfRendererPlugin";

var SERVICE_OPEN = "open";
var SERVICE_CLOSE = "close";
var SERVICE_PAGE_COUNT = "pageCount";
var SERVICE_RENDER_PAGE = "renderPage";

var RENDER_MODE_DISPLAY = "display";
var RENDER_MODE_PRINT = "print";

var PdfRendererPlugin = {
    display: function(filePath, callback){
        cordova.exec(callback, function(err){
            // console.log(err);
        }, PLUGIN_NAME, SERVICE_OPEN, [filePath, RENDER_MODE_DISPLAY]);
    },

    displayWithDimensions: function(filePath, width, height, callback){
        cordova.exec(callback, function(err){
            // console.log(err);
        }, PLUGIN_NAME, SERVICE_OPEN, [filePath, RENDER_MODE_DISPLAY, width, height]);
    },

    print: function(filePath, callback){
        cordova.exec(callback, function(err){
           // console.log(err);
        }, PLUGIN_NAME, SERVICE_OPEN, [filePath, RENDER_MODE_PRINT]);
    },

    printWithDimensions: function(filePath, width, height, callback){
        cordova.exec(callback, function(err){
           // console.log(err);
        }, PLUGIN_NAME, SERVICE_OPEN, [filePath, RENDER_MODE_PRINT, width, height]);
    },

    renderPage: function(pageNo, callback){
        cordova.exec(callback, function(err){
            //console.log(err);
        }, PLUGIN_NAME, SERVICE_RENDER_PAGE, [pageNo]);
    },

    renderPageForDisplay: function(pageNo, callback){
        cordova.exec(callback, function(err){
           // console.log(err);
        }, PLUGIN_NAME, SERVICE_RENDER_PAGE, [pageNo, RENDER_MODE_DISPLAY]);
    },

    renderPageForDisplayWithDimensions: function(pageNo, width, height, callback){
        cordova.exec(callback, function(err){
           // console.log(err);
        }, PLUGIN_NAME, SERVICE_RENDER_PAGE, [pageNo, RENDER_MODE_DISPLAY, width, height]);
    },

    renderPageForPrint: function(pageNo, callback){
        cordova.exec(callback, function(err){
           // console.log(err);
        }, PLUGIN_NAME, SERVICE_RENDER_PAGE, [pageNo, RENDER_MODE_PRINT]);
    },

    renderPageForPrintWithDimensions: function(pageNo, width, height, callback){
        cordova.exec(callback, function(err){
            //console.log(err);
        }, PLUGIN_NAME, SERVICE_RENDER_PAGE, [pageNo, RENDER_MODE_PRINT, width, height]);
    },

    close: function(callback){
        cordova.exec(callback, function(err){
          //  console.log(err);
        }, PLUGIN_NAME, SERVICE_CLOSE, []);
    },

    getPageCount: function(callback){
        cordova.exec(callback, function(err){
           // console.log(err);
        }, PLUGIN_NAME, SERVICE_PAGE_COUNT, []);
    }
};

module.exports = PdfRendererPlugin;