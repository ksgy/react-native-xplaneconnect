package com.reactnativexplaneconnect;

import androidx.annotation.NonNull;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.ArrayUtils;
import com.facebook.react.module.annotations.ReactModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gov.nasa.xpc.XPlaneConnect;
import gov.nasa.xpc.discovery.XPlaneConnectDiscovery;

@ReactModule(name = XplaneconnectModule.NAME)
public class XplaneconnectModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Xplaneconnect";
  private static final String TAG = "RNXPC";

  private static boolean isConnected = false;
  private static String message = "";
  private static XPlaneConnect xpc = null;

  public XplaneconnectModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

//  openUDP
//    closeUDP
//  setCONN
//    pauseSim
//  getDREF
//    getDREFs
//  sendDREF
//    sendDREFs
//  readDATA
//    sendDATA
//  selectDATA
//    getCTRL
//  sendCTRL
//    getPOSI
//  sendPOSI
//    sendTEXT
//  sendVIEW
//    sendWYPT
//  sendVIEW

  private static String getState() {
    return  "{\"isConnected\": \""+isConnected+"\", \"message\": \""+message+"\"}";
  }
  private static <E> String getResult(E any)  {
    String json = "{" +
      "\"value\": \""+any.toString()+"\", " +
      "\"isConnected\": \""+isConnected+"\", " +
      "\"message\": \""+message+"\"" +
      "}";
    return json;
  }

  @ReactMethod
  public void connect(String host, int port, Promise promise) {
    System.out.println("X-Plane Connect example program");
    System.out.println("Setting up simulation...");

    try {
      xpc = new XPlaneConnect(host, port, 49710);
      xpc.getDREF("sim/test/test_float");
      isConnected = true;
      message = "";
      promise.resolve(getState());
    } catch (SocketException ex) {
      isConnected = false;
      message = ex.getMessage();
      promise.resolve(getState());
    } catch (IOException ex) {
      isConnected = false;
      message = ex.getMessage();
      promise.resolve(getState());
    }
  }

  @ReactMethod
  public void sendDREF(String dref, float value, Promise promise) {
    if (xpc != null) {
      try {
        xpc.sendDREF(dref, value);
        isConnected = true;
        message = "sendDREF success";
        promise.resolve(getResult(value));
      } catch (IOException ex) {
        isConnected = false;
        message = ex.getMessage();
        promise.resolve(getState());
      }
    } else {
      isConnected = false;
      message = "Not connected";
      promise.resolve(getState());
    }
  }

  @ReactMethod
  public void getDREF(String dref, Promise promise) {
    if (xpc != null) {
      try {
        float[] value = xpc.getDREF(dref);
        System.out.println(value.toString());
        isConnected = true;
        message = "getDREF success";
        promise.resolve(getResult(value[0]));
      } catch (IOException ex) {
        isConnected = false;
        message = ex.getMessage();
        promise.resolve(getState());
      }
    } else {
      isConnected = false;
      message = "Not connected";
      promise.resolve(getState());
    }
  }

  @ReactMethod
  public void getDREFs(ReadableArray drefs, Promise promise) {
    if (xpc != null) {
      String[] array = new String[drefs.size()];
      String[] arraywithindexes = new String[drefs.size()];
      for (int i = 0; i < drefs.size(); i++) {
        array[i] = drefs.getString(i).replaceAll("\\[(\\d)\\]", "");
        arraywithindexes[i] = drefs.getString(i);
      }
      System.out.println("====================================");
      System.out.println("getDREFs Array of datarefs /w indexes: " + Arrays.toString(arraywithindexes));
      System.out.println("getDREFs Array of datarefs: " + Arrays.toString(array));

      try {
        float[][] value = xpc.getDREFs(array);

        StringBuilder finalValue = new StringBuilder();
        for (int j = 0; j < value.length; j++) {
          String sep = "";
          if (j < value.length-1) {
            sep = ",";
          }
          System.out.println(" => " + array[j]);
          System.out.println("      getDREFs j, value[j] = " + j +" "+ Arrays.toString(value[j]));

          int index = 0;
          final String regex = "\\[(\\d)\\]";
          final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
          Matcher matcher = pattern.matcher(arraywithindexes[j]);

          if (matcher.find()) {
            System.out.println("      Found dataref index match in "+arraywithindexes[j]+" => " + matcher.group(1));
            int datarefIndex = Integer.parseInt(matcher.group(1));
            System.out.println("      datarefIndex: " + datarefIndex);
            index = datarefIndex;
          }

          System.out.println("      index: " + index);

          if (value[j].length > 0) {
            System.out.println(index);
            finalValue.append(value[j][index] + sep);
          }
          System.out.println("- - - - - - - - - - - ");
        }
        System.out.println(finalValue);
        isConnected = true;
        message = "getDREFs success";
        promise.resolve(getResult("[" + finalValue.toString() + "]"));
      } catch (IOException ex) {
        isConnected = false;
        message = ex.getMessage();
        promise.resolve(getState());
      }
    } else {
      isConnected = false;
      message = "Not connected";
      promise.resolve(getState());
    }
  }

}
// TODO


//    XPlaneConnectDiscovery discovery = new XPlaneConnectDiscovery();
//    discovery.onBeaconReceived(beacon -> {
//      System.out.println("Discovered XPlaneConnect plugin:");
//      System.out.println("Plugin version: " + beacon.getPluginVersion());
//      System.out.println("X-Plane version: " + beacon.getXplaneVersion());
//      System.out.println("Address: " + beacon.getXplaneAddress().getHostAddress() + ":" + beacon.getPluginPort());

//    });
//    System.out.println("Discovery start");
//    discovery.start();
