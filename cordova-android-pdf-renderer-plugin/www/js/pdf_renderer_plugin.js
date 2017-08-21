
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
    display: function(filePath, onSuccess, onFailure){
        if(typeof filePath !== "string"){
            throw new Error ("Filepath argument must be of type 'string'");
        }
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_OPEN, [filePath]);
    },

    renderNextPage: function(onSuccess, onFailure){
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_NEXT_PAGE, []);
    },

    renderPreviousPage: function(onSuccess, onFailure){
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_PREVIOUS_PAGE, []);
    },

    close: function(onSuccess, onFailure){
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_CLOSE, []);
    },

    getPageInfo: function(onSuccess, onFailure){
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_PAGE_INFO, []);
    },

    getCurrentPageNumber: function(onSuccess, onFailure){
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_PAGE_NUMBER, []);
    },

    getPageCount: function(onSuccess, onFailure){
        if(typeof onSuccess !== "function"){
            throw new Error ("onSuccess callback must be of type 'function'");
        }
        if(typeof onFailure !== "function"){
            throw new Error ("onFailure callback must be of type 'function'");
        }
        cordova.exec(onSuccess, onFailure, PLUGIN_NAME, SERVICE_PAGE_COUNT, []);
    }
};

module.exports = PdfRendererPlugin;