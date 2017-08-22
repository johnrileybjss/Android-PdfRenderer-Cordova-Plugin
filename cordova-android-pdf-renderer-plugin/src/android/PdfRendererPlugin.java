package com.bjss.plugin;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
 * This class handles a pdf file called from JavaScript and converts a selected page
 * (default is first) to a byte array representing a bitmap.
 *
 * The class currently relies on the presence of the file in the standard Android assets folder
 * when loading the file.
 *
 * Changes would need to be made in order to facilitate loading a file from internal/external
 * storage on the device.
 *
 * For the sake of simplicity, the renderer only uses the 'display' render mode at the moment,
 * and is designed to render pages based on the existing page (using next page and
 * previous page methods) rather than allowing the user to choose a page by number.
 *
 * Similarly, the width and height have also been hard-coded for the sake of simplicity.
 */
public final class PdfRendererPlugin extends CordovaPlugin {

  private static final String LOG_TAG = "PdfRendererPlugin";

  private static final String ACTION_OPEN = "open";
  private static final String ACTION_CLOSE = "close";
  private static final String ACTION_NEXT_PAGE = "nextPage";
  private static final String ACTION_PREVIOUS_PAGE = "previousPage";
  private static final String ACTION_PAGE_INFO = "pageInfo";
  private static final String ACTION_PAGE_COUNT = "pageCount";
  private static final String ACTION_PAGE_NUMBER = "pageNumber";

  private ParcelFileDescriptor fileDescriptor = null;
  private PdfRenderer renderer = null;
  private Page currentPage = null;

  private int renderWidth = 400,
          renderHeight = 600,
          currentPageNumber = 0;

  private String currentRenderMode = "display";

  private Context context;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    context = cordova.getActivity().getApplicationContext();
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    //No Switch -> src 1.6
    if (action.equals(ACTION_OPEN)) { // Opens the requested file
      return executeOpen(args, callbackContext);
    } else if (action.equals(ACTION_NEXT_PAGE)) { // Displays the next page for the current file if available
      return executeNextPage(callbackContext);
    } else if (action.equals(ACTION_PREVIOUS_PAGE)) { //Displays the previous page for the current file if available
      return executePreviousPage(callbackContext);
    } else if (action.equals(ACTION_PAGE_INFO)) { // Returns a JSON object with the page information of the current page
      callbackContext.success(getPageInfo());
      return true;
    } else if (action.equals(ACTION_PAGE_COUNT)) { // Returns the number of pages in the current document
      callbackContext.success(getPageCount());
      return true;
    } else if (action.equals(ACTION_PAGE_NUMBER)) { // Returns the current page number of the current document
      callbackContext.success(getCurrentPageNo());
      return true;
    } else if (action.equals(ACTION_CLOSE)) { // Closes the current document
      closeRenderer();
      callbackContext.success();
      return true;
    }

    return false;
  }

  // Initializes the Renderer and opens the file
  private final boolean executeOpen(final JSONArray args, final CallbackContext callbackContext) {
    try {
      if (args.length() < 1) {
        throw new PDFRendererException("No arguments provided. Exiting process.");
      }
      if (args.length() > 2) {
        renderWidth = args.getInt(1);
        renderHeight = args.getInt(2);
      }

      // Initialize the PDFRenderer using the requested file
      String filePath = args.getString(0);
      initializeRenderer(filePath);

      // Open the First page of the PDF
      openPage(0);

      // Convert the PDF bitmap into a byte array
      byte[] bitmapAsBytes = renderBitmapAsBytes();

      callbackContext.success(bitmapAsBytes);
      return true;
    } catch (JSONException jsonException) {
      String errorMessage = jsonException.getMessage();
      if (errorMessage == null) {
        errorMessage = "Unknown JSONException has occurred";
      }

      callbackContext.error(errorMessage);
      return false;
    } catch (IOException ioException) {
      String errorMessage = ioException.getMessage();
      if (errorMessage == null) {
        errorMessage = "Unknown IOException has occurred";
      }

      callbackContext.error(errorMessage);
      return false;
    }
  }

  // Closes the current page and opens the next page (if available)
  private final boolean executeNextPage(final CallbackContext callbackContext) {
    try {
      int pageCount = getPageCount();
      if (pageCount == 0) {
        throw new PDFRendererException("No Pages available to display.");
      } else if (currentPageNumber + 1 >= pageCount) {
        throw new PDFRendererException("The requested page does not exist.");
      }

      closeCurrentPage();

      // Open the next page and increment the page number variable.
      openPage(currentPageNumber + 1);

      // Convert the PDF bitmap into a byte array
      byte[] bitmapAsBytes = renderBitmapAsBytes();

      callbackContext.success(bitmapAsBytes);
      return true;
    } catch (IOException ioException) {
      String errorMessage = ioException.getMessage();
      if (errorMessage == null) {
        errorMessage = "Unknown Exception has occurred";
      }

      callbackContext.error(errorMessage);
      return false;
    }
  }

  // Closes the current page and opens the previous page (if available)
  private final boolean executePreviousPage(final CallbackContext callbackContext) {
    try {
      int pageCount = getPageCount();
      if (pageCount == 0) {
        throw new PDFRendererException("No Pages available to display.");
      } else if (currentPageNumber - 1 < 0) {
        throw new PDFRendererException("The requested page does not exist.");
      }

      closeCurrentPage();

      // Open the Previous Page and decrement the page number variable
      openPage(currentPageNumber - 1);

      // Convert the PDF bitmap into a byte array
      byte[] bitmapAsBytes = renderBitmapAsBytes();

      callbackContext.success(bitmapAsBytes);
      return true;
    } catch (IOException ioException) {
      String errorMessage = ioException.getMessage();
      if (errorMessage == null) {
        errorMessage = "Unknown Exception has occurred";
      }

      callbackContext.error(errorMessage);
      return false;
    }
  }

  private final int getPageCount() {
    if (renderer == null) {
      return 0;
    }

    return renderer.getPageCount();
  }

  private final int getCurrentPageNo() {
    return currentPageNumber;
  }

  private final JSONObject getPageInfo() throws JSONException {
    int pageNumber = getCurrentPageNo();
    int pageCount = getPageCount();

    JSONObject output = new JSONObject();

    output.put("pageNumber", pageNumber);
    output.put("pageCount", pageCount);

    return output;
  }

  // Initializes the PDF Renderer using a file descriptor based on the file at the provided path
  private final void initializeRenderer(final String filePath) throws IOException {
    // Set the renderer to null in case an error occurs while initializing the renderer
    renderer = null;

    initializeFileDescriptor(filePath);

    renderer = new PdfRenderer(fileDescriptor);
  }

  // Initializes the file descriptor if the file path is valid
  private final void initializeFileDescriptor(final String filePath) throws IOException {
    // Set the file descriptor to null in case an error occurs while opening the file descriptor
    fileDescriptor = null;

    if (filePath == null || filePath.length() < 1) {
      throw new FileNotFoundException("The file path provided is not a valid file path.");
    }

    String[] pathSegments = filePath.split("\\.");

    int numberOfPathSegments = pathSegments.length;
    if (numberOfPathSegments < 2) {
      throw new FileNotFoundException("The file path provided is not a valid file path: " + filePath);
    }

    String fileExtension = pathSegments[numberOfPathSegments - 1];
    if (!fileExtension.equals("pdf")) {
      throw new FileFormatException("Invalid File Extension provided to Pdf Renderer Service: " + fileExtension);
    }

    fileDescriptor = openFileDescriptor(filePath);
  }

  private final void openPage(final int index) throws IOException {
    // Set the page to null in case an error occurs while opening the page
    currentPage = null;

    if (renderer == null) {
      throw new PDFRendererException("An unexpected error has occurred while attempting to render your document - PDFRenderer was properly initialized.");
    }

    int pageCount = getPageCount();
    if (pageCount < 1) {
      throw new PDFRendererException("The requested document has no pages to display.");
    }

    if (index < 0) {
      throw new PDFRendererException("The requested page does not exist.");
    }

    if (index >= pageCount) {
      throw new PDFRendererException("The requested page does not exist.");
    }

    currentPage = renderer.openPage(index);

    currentPageNumber = index;
  }

  // Copies the File at the specified path from the assets folder and opens a ParcelFileDescriptor using that file
  private final ParcelFileDescriptor openFileDescriptor(final String filePath) throws IOException {
    File assetFile = loadFileFromAssets(filePath);

    return ParcelFileDescriptor.open(assetFile, ParcelFileDescriptor.MODE_READ_ONLY);
  }

  // Loads the specified file from the assets folder to a usable path
  private final File loadFileFromAssets(final String filePath) throws IOException {
    File outputFile = null;

    InputStream inputStream = null;
    FileOutputStream outputStream = null;

    BufferedReader fileReader = null;

    IOException exception = null;
    try {
      outputFile = new File(context.getFilesDir(), filePath);

      // Loads the Asset File into an Input Stream
      inputStream = context.getAssets().open("www/assets/" + filePath);

      // Initializes a Buffered Reader for reading the data of the input stream
      fileReader = new BufferedReader(new InputStreamReader(inputStream));

      // Creates an output stream for the temporary copy of the asset file
      outputStream = context.openFileOutput(outputFile.getName(), Context.MODE_PRIVATE);

      // Write the PDF Data to a temporary output file
      int currentData;

      // Sizes the buffer based on the number of readable bytes until a block will occur
      byte[] buffer = new byte[inputStream.available()];
      while ((currentData = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, currentData);
      }

      outputStream.flush();
    } catch (FileNotFoundException fileNotFoundException) {
      exception = new FileNotFoundException("Could not find the requested file: " + filePath);
    } catch (IOException ioException) {
      // Save for later - ensure filestreams are closed before throwing exception
      exception = new IOException("Unexpected IOException has occurred: " + ioException.getMessage());
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }

      if (inputStream != null) {
        inputStream.close();
      }
    }

    if (exception != null) {
      throw exception;
    }

    return outputFile;
  }

  // Render the bitmap and output the byte array representing it.
  private final byte[] renderBitmapAsBytes() throws IOException {
    if (renderer == null) {
      throw new PDFRendererException("An unexpected exception has occurred at runtime - PDFRenderer was not properly initialized.");
    }

    if (currentPage == null) {
      throw new PDFRendererException("An unexpected exception has occurred at runtime - Could not render the requested page.");
    }

    Bitmap bitmap = getBitmap(renderWidth, renderHeight);

    // Select the render mode (currently only 'display' is used) and render the current page
    int renderMode = currentRenderMode.equals("print") ? Page.RENDER_MODE_FOR_PRINT : Page.RENDER_MODE_FOR_DISPLAY;
    currentPage.render(bitmap, null, null, renderMode);

    return convertBitmapToByteArray(bitmap);
  }

  private final void closeCurrentPage() {
    if (currentPage != null) {
      currentPage.close();
    }
  }

  private final void closeRenderer() {
    if (renderer == null) {
      Log.w(LOG_TAG, "Attempted to close null renderer. Skipping operation.");
      return;
    }

    renderer.close();
  }

  // Provides a Standard Method of Converting Bitmaps to an array of Bytes
  private static byte[] convertBitmapToByteArray(Bitmap bitmap) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

    return outputStream.toByteArray();
  }

  // Provides Standard Method of Bitmap Creation
  private static Bitmap getBitmap(final int width, final int height) {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
  }

  // Exception thrown when a user attempts to open a file that is not a PDF
  private final class FileFormatException extends IOException {
    FileFormatException(final String msg) {
      super(msg);
    }
  }

  // Exception thrown concerning errors with the PDFRenderer class
  private final class PDFRendererException extends IOException {
    PDFRendererException(final String msg) {
      super(msg);
    }
  }
}