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
                throw new PDFRendererException("No arguments provided. Exiting process.");
            }
            if(args.length() > 2){
                mWidth = args.getInt(1);
                mHeight = args.getInt(2);
            }

            // Initialize the PDFRenderer using the requested file
            filePath = args.getString(0);
            this.initializeRenderer(filePath);

            // Open the First page of the PDF
            this.openPage(0);

            mPageNo = 0;
            mFilePath = filePath;

            // Convert the PDF bitmap into a byte array
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            byte[] bytes = this.renderBitmapAsBytes(0, bitmap);

            callbackContext.success(bytes);
            return true;
        }
        catch(Exception e){
            String msg = e.getMessage();
            if(msg == null)
                msg = "Unknown Exception has occurred";
            Log.e(LOG_TAG, msg);
            callbackContext.error(msg);

            return false;
        }
    }

    // Closes the current page and opens the next page (if available)
    private boolean executeNextPage(CallbackContext callbackContext){
        try{
            int pageCount = getPageCount();
            if(pageCount == 0)
                throw new PDFRendererException("No Pages available to display.");
            else if(mPageNo + 1 >= pageCount)
                throw new PDFRendererException("The requested page does not exist.");

            closeCurrentPage();

            // Open the next page and increment the page number variable.
            this.openPage(mPageNo + 1);
            ++mPageNo;

            // Convert the PDF bitmap into a byte array
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            byte[] bytes = this.renderBitmapAsBytes(mPageNo, bitmap);

            callbackContext.success(bytes);
            return true;
        }
        catch(Exception e){
            String msg = e.getMessage();
            if(msg == null)
                msg = "Unknown Exception has occurred";
            Log.e(LOG_TAG, msg);
            callbackContext.error(msg);

            return false;
        }
    }

    // Closes the current page and opens the previous page (if available)
    private boolean executePreviousPage(CallbackContext callbackContext){
        try {
            int pageCount = getPageCount();
            if(pageCount == 0)
                throw new PDFRendererException("No Pages available to display.");
            else if(mPageNo - 1 < 0)
                throw new PDFRendererException("The requested page does not exist.");

            closeCurrentPage();

            // Open the Previous Page and decrement the page number variable
            this.openPage(mPageNo - 1);
            --mPageNo;

            // Convert the PDF bitmap into a byte array
            Bitmap bitmap = getBitmap(mWidth, mHeight);
            byte[] bytes = this.renderBitmapAsBytes(mPageNo, bitmap);

            callbackContext.success(bytes);
            return true;
        }
        catch(Exception e){
            String msg = e.getMessage();
            if(msg == null)
                msg = "Unknown Exception has occurred";
            Log.e(LOG_TAG, msg);
            callbackContext.error(msg);

            return false;
        }
    }

    private void openPage(int index) throws IOException {
        currentPage = null;

        if(renderer == null)
            throw new PDFRendererException("An unexpected error has occurred while attempting to render your document - PDFRenderer was properly initialized.");

        int pageCount = getPageCount();
        if(pageCount < 1)
            throw new PDFRendererException("The requested document has no pages to display.");

        if(index < 0)
            throw new PDFRendererException("The requested page does not exist.");

        if(index >= pageCount)
            throw new PDFRendererException("The requested page does not exist.");

        currentPage = renderer.openPage(index);
    }

    // Converts a bitmap object to a byte array and sends the data back to the callback context
    private byte[] renderBitmapAsBytes(int index, Bitmap bitmap) throws IOException {
        if(renderer == null)
            throw new PDFRendererException("An unexpected exception has occurred at runtime - PDFRenderer was not properly initialized.");

        if(currentPage == null)
            throw new PDFRendererException("An unexpected exception has occurred at runtime - Could not render the requested page.");

        // Select the render mode and render the current page
        int renderMode = mRenderMode.equals("print") ? Page.RENDER_MODE_FOR_PRINT : Page.RENDER_MODE_FOR_DISPLAY;
        currentPage.render(bitmap, null, null, renderMode);

        return toByteArray(bitmap);
    }

    // Initializes the PDF Renderer using a file descriptor based on the file at the provided path
    private void initializeRenderer(String filePath) throws IOException {
        renderer = null;

        initializeFileDescriptor(filePath);

        renderer = new PdfRenderer(fileDescriptor);
    }

    private void closeCurrentPage() {
        if(currentPage != null)
            currentPage.close();
    }

    private void closeRenderer() {
        if(renderer == null) {
            Log.w(LOG_TAG, "Attempted to close null renderer. Skipping operation.");
            return;
        }

        renderer.close();
    }

    // Initializes the file descriptor if the file path is valid
    private void initializeFileDescriptor(String filePath) throws IOException {
        fileDescriptor = null;

        if(filePath == null || filePath.length() < 1)
            throw new FileNotFoundException("The file path provided is not a valid file path.");

        String[] pathArr = filePath.split("\\.");

        int numSections = pathArr.length;
        if(numSections < 2)
            throw new FileNotFoundException("The file path provided is not a valid file path: " + filePath);

        String ext = pathArr[numSections - 1];
        if(!ext.equals("pdf"))
            throw new FileFormatException("Invalid File Extension provided to Pdf Renderer Service: " + ext);

        fileDescriptor = getFileDescriptor(filePath);
    }

    // Copies the File at the specified path from the assets folder and initializes a ParcelFileDescriptor using that file
    private ParcelFileDescriptor getFileDescriptor(String filePath) throws IOException {
        File file = copyFileFromAssets(filePath);

        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    // Copies the specified file from the assets folder to a usable path
    private File copyFileFromAssets(String filePath) throws IOException {
        File file = null;
        InputStream input = null;
        FileOutputStream output = null;

        IOException exception = null;
        try {
            file = new File(context.getFilesDir(), filePath);

            input = context.getAssets().open("www/assets/" + filePath);
            output = context.openFileOutput(file.getName(), Context.MODE_PRIVATE);

            // Write the PDF Data to a temporary output file
            byte[] buffer = new byte[1024];
            int currentData;
            while ((currentData = input.read(buffer)) != -1) {
                output.write(buffer, 0, currentData);
            }

            output.flush();
        }
        catch(FileNotFoundException fnfe){
            exception = new FileNotFoundException("Could not find the requested file: " + filePath);
        }
        catch(IOException io){
            // Save for later - ensure filestreams are closed before throwing exception
            exception = new IOException("Unexpected IOException has occurred: " + io.getMessage());
        }
        finally{
            if(output != null)
                output.close();

            if(input != null)
                input.close();
        }

        if(exception != null)
            throw exception;

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

    private JSONObject getPageInfo() throws JSONException {
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

    class PDFRendererException extends IOException {
        PDFRendererException(String msg){ super(msg); }
    }
}