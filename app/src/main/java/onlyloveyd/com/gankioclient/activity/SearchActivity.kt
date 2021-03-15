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

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import onlyloveyd.com.gankioclient.R
import onlyloveyd.com.gankioclient.adapter.MultiRecyclerAdapter
import onlyloveyd.com.gankioclient.data.SearchData
import onlyloveyd.com.gankioclient.databinding.ActivitySearchBinding
import onlyloveyd.com.gankioclient.decorate.Visitable
import onlyloveyd.com.gankioclient.http.HttpMethods
import onlyloveyd.com.gankioclient.utils.PublicTools
import java.util.*

/**
 * 文 件 名: SearchActivity
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：搜索Activity
 */
class SearchActivity : AppCompatActivity(), BGARefreshLayout.BGARefreshLayoutDelegate {

    internal var mMultiRecyclerAdapter: MultiRecyclerAdapter? = null
    internal var mVisitableList: MutableList<Visitable> = ArrayList()

    private lateinit var binding: ActivitySearchBinding
    private var pageindex = 1
    private var keyword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.tlSearch)
        binding.tlSearch.setNavigationIcon(R.drawable.back)

        initBGALayout()
        initRvContent()

        val adapter = ArrayAdapter.createFromResource(this, R.array.dummy_items, R.layout.spinner_item_text)
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_list)

        binding.spCategory.adapter = adapter

        binding.tvSearch.setOnClickListener {
            PublicTools.hide_keyboard_from(this, binding.etSearch)
            refreshData()
        }
        binding.etSearch.addTextChangedListener(SearchTextWatcher())
    }

    inner class SearchTextWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            keyword = s!!.toString()
            if (s.isEmpty()) {
                binding.tvSearch.setTextColor(resources.getColor(R.color.colorPrimary))
                binding.tvSearch.isClickable = false
            } else {
                binding.tvSearch.setTextColor(resources.getColor(R.color.white))
                binding.tvSearch.isClickable = true
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }
    }

    private fun initBGALayout() {
        // 为BGARefreshLayout 设置代理
        binding.rlSearchContent.setDelegate(this)
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        val refreshViewHolder = BGANormalRefreshViewHolder(this, true)
        refreshViewHolder.setLoadingMoreText("加载更多")
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.white)
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white)
        binding.rlSearchContent.setRefreshViewHolder(refreshViewHolder)
    }

    private fun initRvContent() {
        val llm = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mMultiRecyclerAdapter = MultiRecyclerAdapter(null)
        binding.rvContent.layoutManager = llm
        binding.rvContent.adapter = mMultiRecyclerAdapter
    }

    private fun queryGanks(keyword: String, category: String, pageindex: Int) {
        val subscriber = object : Observer<SearchData> {
            override fun onComplete() {
                if (binding.rlSearchContent.isLoadingMore) {
                    binding.rlSearchContent.endLoadingMore()
                } else {
                    binding.rlSearchContent.endRefreshing()
                }
            }

            override fun onError(e: Throwable) {
                Snackbar.make(binding.rvContent, "网络请求错误", Snackbar.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(searchData: SearchData) {
                if (!binding.rlSearchContent.isLoadingMore) {
                    mVisitableList.clear()
                }
                mVisitableList.addAll(searchData.results)
                mMultiRecyclerAdapter?.data = mVisitableList
            }
        }
        HttpMethods.instance.searchData(subscriber, keyword, category, pageindex)
    }

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {
        refreshData()
    }

    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
        if (keyword != null && keyword!!.isNotEmpty()) {
            val category = binding.spCategory.selectedItem as String
            queryGanks(keyword as String, category, ++pageindex)
        }
        return true
    }

    private fun refreshData() {
        pageindex = 1
        binding.rlSearchContent.beginRefreshing()
        if (keyword != null && keyword!!.isNotEmpty()) {
            val category = binding.spCategory.selectedItem as String
            queryGanks(keyword as String, category, pageindex)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> {
            }
        }
        return true
    }
}
