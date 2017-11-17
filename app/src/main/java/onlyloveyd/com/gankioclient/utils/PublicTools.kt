/**
 * Copyright 2017 yidong
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package onlyloveyd.com.gankioclient.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson
import onlyloveyd.com.gankioclient.BuildConfig
import onlyloveyd.com.gankioclient.R
import onlyloveyd.com.gankioclient.activity.WebActivity
import onlyloveyd.com.gankioclient.data.VersionData
import onlyloveyd.com.gankioclient.http.UpdateManager
import onlyloveyd.com.gankioclient.utils.Constant.ONE_DAY
import onlyloveyd.com.gankioclient.utils.Constant.ONE_HOUR
import onlyloveyd.com.gankioclient.utils.Constant.ONE_MINUTE
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 文 件 名: PublicTools
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：工具方法类
 */
object PublicTools {
    /**
     * 获取目标时间和当前时间之间的差距

     * @param date date
     * *
     * @return String
     */
    fun getTimestampString(date: Date): String {
        val curDate = Date()
        val splitTime = curDate.time - date.time
        if (splitTime < 30 * ONE_DAY) {
            if (splitTime < ONE_MINUTE) {
                return "刚刚"
            }
            if (splitTime < ONE_HOUR) {
                return String.format("%d分钟前", splitTime / ONE_MINUTE)
            }

            if (splitTime < ONE_DAY) {
                return String.format("%d小时前", splitTime / ONE_HOUR)
            }

            return String.format("%d天前", splitTime / ONE_DAY)
        }
        val result: String
        result = "M月d日 HH:mm"
        return SimpleDateFormat(result, Locale.CHINA).format(date)
    }

    /**
     * Date（long） 转换 String

     * @param time   time
     * *
     * @param format format
     * *
     * @return String
     */
    fun date2String(time: Long, format: String): String = SimpleDateFormat(format).format(time)


    /**
     * start WebActivity
     */
    fun startWebActivity(context: Context, url: String) {
        context.startActivity<WebActivity>("URL" to url);
    }


    /**
     * hide keyboard
     */
    fun hide_keyboard_from(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    /**
     * show keyboard
     */
    fun show_keyboard_from(context: Context, view: View) {
        val inputMethodManager = context.getSystemService(
                Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 保存Bitmap为图片
     */
    @Throws(Exception::class)
    fun saveBitmap(bitmap: Bitmap, picPath: String) {
        val f = File(picPath + Constant.SUFFIX_JPEG)
        if (f.exists()) {
            f.delete()
        }
        try {
            val out = FileOutputStream(f)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
            bitmap.recycle()
        } catch (e: FileNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            bitmap.recycle()
            throw FileNotFoundException()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            bitmap.recycle()
            throw IOException()
        }

    }

//
//    /**
//     * 检查更新
//     */
//    fun checkUpdate(context: Context, auto: Boolean) {
//
//        val loadingDialog = ProgressDialog(context)
//        loadingDialog.isIndeterminate = true
//        loadingDialog.setTitle("提示")
//        loadingDialog.setMessage("正在检测新版本...")
//        loadingDialog.setCancelable(false)
//
//        FIR.checkForUpdateInFIR(Constant.FIR_API_TOKEN, object : VersionCheckCallback() {
//            override fun onSuccess(versionJson: String?) {
//                loadingDialog.hide()
//                if (BuildConfig.YLog) {
//                    Log.i("yidong", "check from fir.im success! " + "\n" + versionJson)
//                }
//                val gson = Gson()
//                val versionData = gson.fromJson(versionJson, VersionData::class.java)
//                if (BuildConfig.VERSION_NAME == versionData.versionShort) {
//                    if (!auto) {
//                        context.toast("当前已经是最新版本")
//                    }
//                } else {
//                    context.alert("更新日志：\n" + versionData.changelog, context.getString(R.string.version_update,
//                            versionData.versionShort)) {
//                        positiveButton("下载") { dialog ->
//                            val updateManager = UpdateManager(context)
//                            updateManager.setDownUrl(
//                                    Constant.GITHUB_LATEST_APK)
//                            updateManager.setApkName(versionData.name + versionData.versionShort + ".apk")
//                            updateManager.showDownloadDialog()
//                        }
//                        negativeButton("取消") { dialog -> dialog.dismiss() }
//
//                    }.show()
//                }
//
//            }
//
//            override fun onFail(exception: Exception?) {
//                if (BuildConfig.YLog) {
//                    exception!!.printStackTrace()
//                }
//                loadingDialog.hide()
//                context.toast("检查更新出现错误")
//            }
//
//            override fun onStart() {
//                if (BuildConfig.YLog) {
//                    Log.i("yidong", "onStart " + "\n")
//                }
//                if (!auto) {
//                    loadingDialog.show()
//                }
//            }
//
//            override fun onFinish() {
//                if (BuildConfig.YLog) {
//                    Log.i("yidong", "onFinish")
//                }
//            }
//        })
//    }
}
