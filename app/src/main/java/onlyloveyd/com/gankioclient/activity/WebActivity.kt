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
package onlyloveyd.com.gankioclient.activity

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import onlyloveyd.com.gankioclient.R
import onlyloveyd.com.gankioclient.databinding.ActivityWebBinding
import org.jetbrains.anko.browse
import org.jetbrains.anko.share

/**
 * 文 件 名: WebActivity
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：内部网页显示Activity
 */
class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    private var URL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tlWeb)
        binding.tlWeb.setNavigationIcon(R.drawable.back)
        binding.tlWeb.setTitleTextAppearance(this, R.style.ToolBarTextAppearance)

        val intent = intent
        val bundle = intent.extras
        if (bundle != null) {
            URL = bundle.getString("URL")
        }

        initWebViewSettings()

        binding.wvContent.removeJavascriptInterface("searchBoxJavaBridge_")
        binding.wvContent.removeJavascriptInterface("accessibilityTraversal")
        binding.wvContent.removeJavascriptInterface("accessibility")
        binding.wvContent.loadUrl(URL)
    }

    public override fun onPause() {
        super.onPause()
        binding.wvContent.onPause()
    }

    public override fun onResume() {
        super.onResume()
        binding.wvContent.onResume()
    }

    public override fun onDestroy() {
        super.onDestroy()
        binding.wvContent.destroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.share -> {//share url with system share windows
                URL?.let { share(it) }
            }
            R.id.openinbrowse -> {
                URL?.let { browse(it) }
            }
            R.id.copyurl -> {
                val clipboardManager = getSystemService(
                        Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.text = URL
                Snackbar.make(binding.tlWeb, "已复制到剪切板", Snackbar.LENGTH_SHORT).show()
            }
            R.id.refresh -> {
                binding.wvContent.reload()
            }
            else -> {
            }
        }
        return true
    }

    private fun initWebViewSettings() {
        val settings = binding.wvContent.settings
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.setAppCacheEnabled(true)
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.setSupportZoom(true)
        settings.savePassword = false

        binding.wvContent.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progressbar.progress = newProgress
                if (newProgress == 100) {
                    binding.progressbar.visibility = View.GONE
                } else {
                    binding.progressbar.visibility = View.VISIBLE
                }
            }


            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                setTitle(title)
            }
        }
        binding.wvContent.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                url?.let { view.loadUrl(it) }
                return true
            }
        }
    }
}