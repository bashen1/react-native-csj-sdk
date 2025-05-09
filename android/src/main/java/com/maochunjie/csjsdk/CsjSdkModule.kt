package com.maochunjie.csjsdk

import com.bytedance.sdk.openadsdk.CSJAdError
import com.bytedance.sdk.openadsdk.CSJSplashAd
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter


class CsjSdkModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "CsjSdk"
  }

  @ReactMethod
  fun init(params: ReadableMap, promise: Promise) {
    val map = Arguments.createMap()
    val appId: String? = params.getString("appId")

    if (appId != "") {
      TTAdSdk.init(reactApplicationContext, TTAdConfig.Builder().appId(appId) //APP ID
        .useMediation(true) //开启聚合功能
        .build()
      )
      TTAdSdk.start(object : TTAdSdk.Callback {
        override fun success() {
          map.putString("message", "")
          map.putString("code", "1")
          promise.resolve(map)
        }

        override fun fail(code: Int, msg: String) {
          map.putString("message", msg)
          map.putString("code", "-1")
          promise.resolve(map)
        }
      })
    } else {
      map.putString("message", "appId为空")
      map.putString("code", "0")
      promise.resolve(map)
    }
  }

  @ReactMethod
  fun loadSplashAd(params: ReadableMap, promise: Promise) {
    val codeId: String? = params.getString("codeId")
    val adNativeLoader = TTAdSdk.getAdManager().createAdNative(currentActivity)
    if (codeId != "") {
      val widthPx: Int = params.getInt("width")
      val heightPx: Int = params.getInt("height")
      val timeout: Int = params.getInt("timeout")

      // 请求开屏广告
      adNativeLoader.loadSplashAd(
        SplashAd.buildSplashAdslot(
          codeId,
          SplashAd.dp2Pix(reactApplicationContext, widthPx),
          SplashAd.dp2Pix(reactApplicationContext, heightPx)
        ), object : TTAdNative.CSJSplashAdListener {
          override fun onSplashRenderSuccess(csjSplashAd: CSJSplashAd) {
            /** 渲染成功后，展示广告  */
            showSplashAd(csjSplashAd)
          }

          override fun onSplashLoadSuccess(p0: CSJSplashAd?) {
            // 加载成功
            fireSplashAdEvent("onAdLoadSuccess", "", "")
          }

          override fun onSplashLoadFail(csjAdError: CSJAdError) {
            // 加载失败
            fireSplashAdEvent("onAdLoadFail", csjAdError.msg, csjAdError.code.toString())
          }

          override fun onSplashRenderFail(csjSplashAd: CSJSplashAd, csjAdError: CSJAdError) {
            fireSplashAdEvent("onRenderFail", csjAdError.msg, csjAdError.code.toString())
          }
        }, timeout
      )
    }
  }


  // 展示开屏广告
  fun showSplashAd(csjSplashAd: CSJSplashAd) {
    csjSplashAd.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
      override fun onSplashAdShow(csjSplashAd: CSJSplashAd) {
        fireSplashAdEvent("onAdShow", "", "")
      }

      override fun onSplashAdClick(csjSplashAd: CSJSplashAd) {
        SplashAd.hide(currentActivity)
        fireSplashAdEvent("onAdClick", "", "")
      }

      override fun onSplashAdClose(csjSplashAd: CSJSplashAd, i: Int) {
        // 广告关闭后，销毁广告页面
        SplashAd.hide(currentActivity)
        fireSplashAdEvent("onAdClose", "", "")
      }
    })
    val splashView = csjSplashAd.splashView
    SplashAd.show(currentActivity, R.id.splash_container, splashView)
  }

  fun fireSplashAdEvent(eventName: String, message: String, code: String) {
    val map = Arguments.createMap()
    map.putString("message", message)
    if (code != "") {
      map.putString("code", code)
    }
    fireEvent("SplashAd-$eventName", map)
  }

  private fun fireEvent(eventName: String, params: WritableMap) {
    reactApplicationContext.getJSModule(RCTDeviceEventEmitter::class.java)
      .emit(eventName, params)
  }
}
