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
package onlyloveyd.com.gankioclient.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder
import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import onlyloveyd.com.gankioclient.R
import onlyloveyd.com.gankioclient.adapter.MultiRecyclerAdapter
import onlyloveyd.com.gankioclient.data.EmptyData
import onlyloveyd.com.gankioclient.databinding.FragmentGankBinding
import onlyloveyd.com.gankioclient.decorate.Visitable
import java.util.*

/**
 * 文 件 名: BaseFragment
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：Fragment基类
 */
open class BaseFragment : Fragment(), BGARefreshLayout.BGARefreshLayoutDelegate {

    private var _binding: FragmentGankBinding? = null
    internal val binding get() = _binding!!

    internal var mMultiRecyclerAdapter: MultiRecyclerAdapter? = null
    internal var mVisitableList: MutableList<Visitable> = ArrayList()

    internal var arg: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentGankBinding.inflate(inflater, container, false)

        val args = arguments
        if (args != null) {
            arg = args.getString("ARG")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBGALayout()
        initRvContent()
        initBGAData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBGALayout() {
        // 为BGARefreshLayout 设置代理
        binding.rlGankRefresh.setDelegate(this)
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能

        val refreshViewHolder = BGANormalRefreshViewHolder(context, true)
        refreshViewHolder.setLoadingMoreText(getString(R.string.load_more))
        refreshViewHolder.setLoadMoreBackgroundColorRes(R.color.white)
        refreshViewHolder.setRefreshViewBackgroundColorRes(R.color.white)
        binding.rlGankRefresh.setRefreshViewHolder(refreshViewHolder)
    }

    private fun initRvContent() {
        binding.rvContent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvContent.adapter = MultiRecyclerAdapter(null)
    }

    open fun initBGAData() {}


    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout) {

    }

    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean = false

    /**
     * 处理网络请求错误
     */
    fun onNetworkError() {
        mVisitableList.clear()
        val emptyData = EmptyData(getString(R.string.network_error))
        mVisitableList.add(0, emptyData)
        mMultiRecyclerAdapter?.data = mVisitableList
    }

    /**
     * 处理请求数据为空
     */
    fun onDataEmpty() {
        val emptyData = EmptyData(getString(R.string.empty_data))
        mVisitableList.add(0, emptyData)
    }

    /**
     * 停止刷新或者加载更多
     */
    fun endLoading() {
        binding.rlGankRefresh.let {
            if (binding.rlGankRefresh.isLoadingMore) {
                binding.rlGankRefresh.endLoadingMore()
            } else {
                binding.rlGankRefresh.endRefreshing()
            }
        }
    }
}
