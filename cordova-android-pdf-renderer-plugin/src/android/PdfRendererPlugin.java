package com.bjss.plugin;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.graphics.pdf.PdfRenderer.Page;

import android.os.ParcelFileDescriptor;

import android.util.Log;

/**
 * This class handles a pdf file called from JavaScript and converts a selected page (default is first) to a byte array representing a bitmap.
 */
public class PdfRendererPlugin extends CordovaPlugin {

    private static final String LOG_TAG = "PdfRendererPlugin";

    private ParcelFileDescriptor fileDescriptor = null;
    private PdfRenderer renderer = null;
    private Page currentPage = null;

    private int mWidth = 400, mHeight = 600;
    private String mRenderMode = "display";

    private Context context;
    private AssetManager assetManager;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){
        super.initialize(cordova, webView);

        assetManager = cordova.getActivity().getAssets();
        context = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        //No Switch -> src 1.6
        if(action.equals("open")){
            return executeOpen(args, callbackContext);
        }
        else if(action.equals("renderPage")){
            return executeRenderPage(args, callbackContext);
        }
        else if(action.equals("pageCount")){
            callbackContext.success(this.getPageCount());
            return true;
        }
        else if(action.equals("close")){
            this.closeRenderer();
            callbackContext.success();
            return true;
        }

        return false;
    }

    private boolean executeOpen(JSONArray args, CallbackContext callbackContext){
        String filePath = "";
        try{
            if(args.length() < 1){
                Log.e(LOG_TAG, "No arguments provided. Exiting process.");
                callbackContext.error("No arguments provided. Exiting process.");
                return true;
            }
            else if(args.length() < 2){
                Log.e(LOG_TAG, "Insufficient arguments provided. Exiting process.");
                callbackContext.error("Insufficient arguments provided. Exiting process.");
                return true;
            }
            if(args.length() > 3){
                mWidth = args.getInt(2);
                mHeight = args.getInt(3);
            }

            filePath = args.getString(0);
            mRenderMode = args.getString(1);
        }
        catch(JSONException je){
            String msg = je.getMessage();
            if(msg == null)
                msg = "Unknown JSONException has occurred";
            Log.e(LOG_TAG, msg);
        }

        this.initializeRenderer(filePath, callbackContext);

        boolean isPageOpen = this.openPage(0, callbackContext);
        if(isPageOpen){
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            this.sendBitmapAsBytes(0, bitmap, callbackContext);
        }
        return true;
    }

    private boolean executeRenderPage(JSONArray args, CallbackContext callbackContext){
        int pageNo = -1;
        try {
            if (args.length() < 1) {
                Log.e(LOG_TAG, "No arguments provided. Exiting process.");
                callbackContext.error("No arguments provided. Exiting process.");
                return true;
            }
            if (args.length() > 1) {
                mRenderMode = args.getString(1);
            }
            if (args.length() > 3) {
                mWidth = args.getInt(2);
                mHeight = args.getInt(3);
            }

            pageNo = args.getInt(0);
        }
        catch(JSONException je){
            String msg = je.getMessage();
            if(msg == null)
                msg = "Unknown JSONException has occurred";
            Log.e(LOG_TAG, msg);
        }

        if(pageNo < 0)
            return false;

        boolean isPageOpen = this.openPage(pageNo, callbackContext);
        if(isPageOpen) {
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            this.sendBitmapAsBytes(pageNo, bitmap, callbackContext);
        }
        return true;
    }

    /*
    // Requests the permission to read from external storage if not already available
    private void validatePermissions(){
        Log.d(LOG_TAG, "validatePermissions");
        if(!cordova.hasPermission(READ_EXTERNAL_STORAGE)){
            Log.i(LOG_TAG, "Requesting External Storage Read Permission...");
            cordova.requestPermission(this, CODE_READ_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE);
        }
    }
    */

    private int getPageCount() {
        if(renderer == null)
            return 0;

        return renderer.getPageCount();
    }

    private void initializeWriteFileDescriptor(String filePath, CallbackContext callbackContext) throws IOException, FileNotFoundException, FileFormatException {
        Log.d(LOG_TAG, "initializeWriteFileDescriptor");
        fileDescriptor = null;

        if(filePath == null || filePath.length() < 1)
            throw new FileNotFoundException("The file path provided is not a valid file path.");

        String[] pathArr = filePath.split("\\.");

        int numSections = pathArr.length;
        if(numSections < 2)
            throw new FileNotFoundException("The file path provided is not a valid file path: " + filePath);

        String ext = pathArr[numSections - 1];
        if(!ext.equals("pdf"))
            throw new FileFormatException("Invalid File Extension provided to Pdf Render Service: " + ext);

        fileDescriptor = getWriteFileDescriptor(filePath);
    }

    private void initializeRenderer(String filePath, CallbackContext callbackContext){
        Log.d(LOG_TAG, "initializeRenderer");
        renderer = null;

        try {
            initializeWriteFileDescriptor(filePath, callbackContext);

            if(fileDescriptor != null) {
                renderer = new PdfRenderer(fileDescriptor);
            }
        }
        catch(IOException io){
            String msg = io.getMessage();
            if(msg == null)
                msg = "An error has occurred while loading the requested file.";

            Log.e(LOG_TAG, msg);
            callbackContext.error(msg);
        }
    }

    private void closeRenderer() {
        if(renderer == null) {
            Log.w(LOG_TAG, "Attempted to close null renderer. Skipping operation.");
            return;
        }

        renderer.close();
    }

    private boolean openPage(int index, CallbackContext callbackContext){
        Log.d(LOG_TAG, "openPage");
        currentPage = null;

        int pageCount = getPageCount();
        if(pageCount < 1) {
            Log.e(LOG_TAG, "Requested document has no pages to display.");
            callbackContext.error("Requested document has no pages to display.");
            return false;
        }

        if(index >= pageCount || index < 0) {
            Log.e(LOG_TAG, String.format("No page was found at page number %d/%d", index, pageCount));
            callbackContext.error(String.format("No page was found at page number %d/%d", index, pageCount));
            return false;
        }

        currentPage = renderer.openPage(index);
        return true;
    }

    private void sendBitmapAsBytes(int index, Bitmap bitmap, CallbackContext callbackContext){
        Log.d(LOG_TAG, "sendBitmapAsBytes");
        if(renderer == null) {
            Log.e(LOG_TAG, "Renderer was not properly initialized.");
            callbackContext.error("Renderer was not properly initialized.");
            return;
        }

        if(currentPage == null) {
            Log.e(LOG_TAG, "Requested page could not be rendered.");
            callbackContext.error("Requested page could not be rendered.");
            return;
        }

        int renderMode = mRenderMode.equals("print") ? Page.RENDER_MODE_FOR_PRINT : Page.RENDER_MODE_FOR_DISPLAY;
        currentPage.render(bitmap, null, null, renderMode);

        byte[] output = toByteArray(bitmap);
        if(output == null || output.length < 1) {
            Log.e(LOG_TAG, "Bitmap Error has occurred: Invalid Output Format Detected");
            callbackContext.error("Bitmap Error has occurred: Invalid Output Format Detected");
        }
        else {
            Log.i(LOG_TAG, "Bitmap Conversion Successful");
            callbackContext.success(output);
        }
    }

    private File copyFileFromAssets(String filePath) throws IOException {
        Log.d(LOG_TAG, "copyFileFromAssets");

        InputStream input = null;
        OutputStream output = null;
        File file = new File(context.getFilesDir(), filePath);
        try {
            input = assetManager.open(filePath);
            output = context.openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            byte[] buffer = new byte[1024];
            int currentData;
            while((currentData = input.read(buffer)) != -1){
                output.write(buffer, 0, currentData);
            }

            output.flush();
        }
        catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        finally{
            if(input != null) {
                input.close();
                input = null;
            }
            if(output != null){
                output.close();
                output = null;
            }
        }
        return file;
    }

    private ParcelFileDescriptor getWriteFileDescriptor(String filePath) throws IOException, FileNotFoundException {
        Log.d(LOG_TAG, "getWriteFileDescriptor");
        File file = copyFileFromAssets(filePath);
        final int fileMode = ParcelFileDescriptor.MODE_TRUNCATE|
                             ParcelFileDescriptor.MODE_CREATE |
                             ParcelFileDescriptor.MODE_WRITE_ONLY;

        return ParcelFileDescriptor.open(file, fileMode);
    }

    private static byte[] toByteArray(Bitmap bitmap){
        Log.d(LOG_TAG, "toByteArray");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    private static Bitmap getBitmap(int width, int height){
        Log.d(LOG_TAG, "getBitmap");
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    // Exception thrown when a user attempts to open a file that is not a PDF
    class FileFormatException extends IOException {
        FileFormatException(String msg){
            super(msg);
        }
    }
}