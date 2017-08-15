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
import java.io.FileOutputStream;

import android.content.Context;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.graphics.pdf.PdfRenderer.Page;

import android.os.ParcelFileDescriptor;

import android.util.Log;

//TODO - Break out Exceptions/Callback Context calls --> ensure callback context is used only ONCE
/**
 * This class handles a pdf file called from JavaScript and converts a selected page (default is first) to a byte array representing a bitmap.
 */
public class PdfRendererPlugin extends CordovaPlugin {

    private static final String LOG_TAG = "PdfRendererPlugin";

    private ParcelFileDescriptor fileDescriptor = null;
    private PdfRenderer renderer = null;
    private Page currentPage = null;

    private int mWidth = 400, mHeight = 600, mPageNo = 0;
    private String mRenderMode = "display";
    private String mFilePath = "";

    private Context context;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView){
        super.initialize(cordova, webView);

        context = cordova.getActivity().getApplicationContext();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        //No Switch -> src 1.6
        if(action.equals("open")){
            return executeOpen(args, callbackContext);
        }
        else if(action.equals("nextPage")){
            return executeNextPage(callbackContext);
        }
        else if(action.equals("previousPage")){
            return executePreviousPage(callbackContext);
        }
        else if(action.equals("pageInfo")){
            callbackContext.success(this.getPageInfo());
            return true;
        }
        else if(action.equals("pageCount")){
            callbackContext.success(this.getPageCount());
            return true;
        }
        else if(action.equals("pageNumber")){
            callbackContext.success(this.getCurrentPageNo());
            return true;
        }
        else if(action.equals("close")){
            this.closeRenderer();
            callbackContext.success();
            return true;
        }

        return false;
    }

    // Initializes the Renderer and opens the file
    private boolean executeOpen(JSONArray args, CallbackContext callbackContext){
        String filePath = "";
        try{
            if(args.length() < 1){
                Log.e(LOG_TAG, "No arguments provided. Exiting process.");
                callbackContext.error("No arguments provided. Exiting process.");
                return true;
            }
            if(args.length() > 2){
                mWidth = args.getInt(1);
                mHeight = args.getInt(2);
            }

            filePath = args.getString(0);
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
            mPageNo = 0;
            mFilePath = filePath;
            this.sendBitmapAsBytes(0, bitmap, callbackContext);
        }
        return true;
    }

    // Closes the current page and opens the next page (if available)
    private boolean executeNextPage(CallbackContext callbackContext){
        int pageCount = getPageCount();
        if(pageCount == 0 || mPageNo + 1 >= pageCount)
            return false;

        closeCurrentPage();

        boolean isPageOpen = this.openPage(mPageNo + 1, callbackContext);
        if(isPageOpen){
            ++mPageNo;
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            this.sendBitmapAsBytes(mPageNo, bitmap, callbackContext);
        }

        return true;
    }

    // Closes the current page and opens the previous page (if available)
    private boolean executePreviousPage(CallbackContext callbackContext){
        int pageCount = getPageCount();
        if(pageCount == 0 || mPageNo - 1 < 0)
            return false;

        closeCurrentPage();

        boolean isPageOpen = this.openPage(mPageNo - 1, callbackContext);
        if(isPageOpen){
            --mPageNo;
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            this.sendBitmapAsBytes(mPageNo, bitmap, callbackContext);
        }

        return true;
    }

    private boolean openPage(int index, CallbackContext callbackContext){
        currentPage = null;

        if(renderer == null){
            Log.e(LOG_TAG, "Renderer was found to be null.");
            callbackContext.error("An unknown error has occurred while attempting to render your document.");
            return false;
        }

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

    // Converts a bitmap object to a byte array and sends the data back to the callback context
    private void sendBitmapAsBytes(int index, Bitmap bitmap, CallbackContext callbackContext){
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

    // Initializes the PDF Renderer using a file descriptor based on the file at the provided path
    private void initializeRenderer(String filePath, CallbackContext callbackContext){
        renderer = null;

        try {
            initializeFileDescriptor(filePath, callbackContext);

            if(fileDescriptor != null) {
                renderer = new PdfRenderer(fileDescriptor);
            }
            else{
                Log.e(LOG_TAG, "An unknown error has occurred with the File Descriptor.");
                callbackContext.error("An unknown error has occurred with the File Descriptor.");
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

    private void closeCurrentPage() {
        if(currentPage != null){
            currentPage.close();
        }
    }

    private void closeRenderer() {
        if(renderer == null) {
            Log.w(LOG_TAG, "Attempted to close null renderer. Skipping operation.");
            return;
        }

        renderer.close();
    }

    // Initializes the file descriptor if the file path is valid
    private void initializeFileDescriptor(String filePath, CallbackContext callbackContext) throws IOException, FileNotFoundException, FileFormatException {
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

        fileDescriptor = getFileDescriptor(filePath);
    }

    // Copies the File at the specified path from the assets folder and initializes a ParcelFileDescriptor using that file
    private ParcelFileDescriptor getFileDescriptor(String filePath) throws IOException, FileNotFoundException {
        File file = copyFileFromAssets(filePath);

        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    // Copies the specified file from the assets folder to a usable path
    private File copyFileFromAssets(String filePath) throws IOException {
        InputStream input = null;
        FileOutputStream output = null;

        File file = new File(context.getFilesDir(), filePath);

        input = context.getAssets().open("www/assets/" + filePath);
        output = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);

        byte[] buffer = new byte[1024];
        int currentData;
        while((currentData = input.read(buffer)) != -1){
            output.write(buffer, 0, currentData);
        }

        output.flush();

        if(output != null){
            output.close();
            output = null;
        }

        if(input != null) {
            input.close();
            input = null;
        }

        return file;
    }

    private int getPageCount() {
        if(renderer == null)
            return 0;

        return renderer.getPageCount();
    }

    private int getCurrentPageNo() {
        return mPageNo;
    }

    private JSONObject getPageInfo() throws JSONException{
        int pageNo = getCurrentPageNo();
        int pageCount = getPageCount();

        JSONObject output = new JSONObject();

        output.put("filePath", mFilePath);
        output.put("pageNumber", pageNo);
        output.put("pageCount", pageCount);

        return output;
    }

    private static byte[] toByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    private static Bitmap getBitmap(int width, int height){
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    // Exception thrown when a user attempts to open a file that is not a PDF
    class FileFormatException extends IOException {
        FileFormatException(String msg){
            super(msg);
        }
    }
}