
var PLUGIN_NAME = "PdfRendererPlugin";

var SERVICE_OPEN = "open";
var SERVICE_CLOSE = "close";
var SERVICE_NEXT_PAGE = "nextPage";
var SERVICE_PREVIOUS_PAGE = "previousPage";

var SERVICE_PAGE_COUNT = "pageCount";
var SERVICE_PAGE_NUMBER = "pageNumber";
var SERVICE_PAGE_INFO = "pageInfo";

var RENDER_MODE_DISPLAY = "display";

var PdfRendererPlugin = {
    display: function(filePath, callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to display your PDF.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_OPEN, [filePath]);
    },

    renderPage: function(pageNo, callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to render the requested page.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_RENDER_PAGE, [pageNo]);
    },

    renderNextPage: function(callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to render the requested page.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_NEXT_PAGE, []);
    },

    renderPreviousPage: function(callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to render the requested page.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_PREVIOUS_PAGE, []);
    },

    close: function(callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to close the PDF renderer.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_CLOSE, []);
    },

    getPageInfo: function(callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to request page information.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_PAGE_INFO, []);
    },

    getCurrentPageNumber: function(callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to request the current page number.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_PAGE_NUMBER, []);
    },

    getPageCount: function(callback){
        cordova.exec(callback, function(err){
            if(!err.message){
                callback("Unexpected Exception has occurred while attempting to request the current page count.");
            }
            callback(err.message);
        }, PLUGIN_NAME, SERVICE_PAGE_COUNT, []);
    }
};

module.exports = PdfRendererPlugin;