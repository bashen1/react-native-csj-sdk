package com.maochunjie.csjsdk

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import com.bytedance.sdk.openadsdk.AdSlot
import java.lang.ref.WeakReference


object SplashAd {
  private var mSplashDialog: Dialog? = null
  private var mActivity: WeakReference<Activity>? = null

  /**
   * 打开启动屏
   */
  fun show(activity: Activity?, resourceId: Int, splashView: View) {
    if (activity == null) return
    mActivity = WeakReference<Activity>(activity)
    activity.runOnUiThread(Runnable {
      if (!activity.isFinishing) {
        mSplashDialog = Dialog(activity, resourceId)
        mSplashDialog!!.setContentView(splashView)
        mSplashDialog!!.setCancelable(false)
        if (!mSplashDialog!!.isShowing) {
          mSplashDialog!!.show()
        }
      }
    })
  }

  fun hide(activity: Activity?) {
    var tempActivity = activity
    if (activity == null) {
      if (mActivity == null) {
        return
      }
      tempActivity = mActivity!!.get()
    }
    if (tempActivity == null) return
    val activityRes: Activity = tempActivity
    activityRes.runOnUiThread {
      if (mSplashDialog != null && mSplashDialog!!.isShowing) {
        var isDestroyed = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          isDestroyed = activityRes.isDestroyed
        }
        if (!activityRes.isFinishing && !isDestroyed) {
          mSplashDialog!!.dismiss()
        }
        mSplashDialog = null
      }
    }
  }

  fun buildSplashAdslot(codeId: String?, widthPx: Int, heightPx: Int): AdSlot {
    return AdSlot.Builder()
      .setCodeId(codeId) //广告位ID
      .setImageAcceptedSize(widthPx, heightPx) //设置广告宽高 单位px
      .build()
  }

  fun dp2Pix(reactContext: Context, dp: Int): Int {
    return try {
      val density: Float =
        reactContext.applicationContext.resources.displayMetrics.density
      (dp * density + 0.5f).toInt()
    } catch (e: Exception) {
      dp.toInt()
    }
  }
}
