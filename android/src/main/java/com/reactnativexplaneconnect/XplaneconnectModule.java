package com.reactnativexplaneconnect;

import androidx.annotation.NonNull;

import android.util.Log;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.io.IOException;
import java.net.SocketException;
import java.util.Arrays;

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
    return  "{isConnected: \""+isConnected+"\", message: \""+message+"\"}";
  }
  private static <E> String getResult(E any) {
    return  "{" +
      "value: \""+any.toString()+"\", " +
      "isConnected: \""+isConnected+"\", " +
      "message: \""+message+"\"" +
      "}";
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
      message = "Unable to set up the connection. (Error message was '" + ex.getMessage() + "'.)";
      promise.resolve(getState());
    } catch (IOException ex) {
      isConnected = false;
      message = "Something went wrong... ¯\\_(ツ)_/¯ (Error message was '" + ex.getMessage() + "'.)";
      promise.resolve(getState());
    }
  }

  @ReactMethod
  public void sendDREF(String dref, float value, Promise promise) {
    if (xpc != null) {
      try {
        xpc.sendDREF(dref, value);
        promise.resolve(getResult(value));
      } catch (IOException ex) {
        message = "Something went wrong in sendDREF ... ¯\\_(ツ)_/¯ (Error message was '" + ex.getMessage() + "'.)";
        promise.resolve(getState());
      }
    } else {
      message = "Not connected ¯\\_(ツ)_/¯";
      promise.resolve(getState());
    }
  }

  @ReactMethod
  public void getDREF(String dref, Promise promise) {
    if (xpc != null) {
      try {
        float[] value = xpc.getDREF(dref);
        System.out.println(value.toString());
        promise.resolve(getResult(value[0]));
      } catch (IOException ex) {
        message = "Something went wrong in sendDREF ... ¯\\_(ツ)_/¯ (Error message was '" + ex.getMessage() + "'.)";
        promise.resolve(getState());
      }
    } else {
      message = "Not connected ¯\\_(ツ)_/¯";
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
