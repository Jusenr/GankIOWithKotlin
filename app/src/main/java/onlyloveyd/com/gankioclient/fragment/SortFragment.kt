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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import onlyloveyd.com.gankioclient.adapter.TabAdapter
import onlyloveyd.com.gankioclient.databinding.FragmentSortBinding
import onlyloveyd.com.gankioclient.utils.Constant

/**
 * 文 件 名: SortFragment
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：分类数据界面
 */
class SortFragment : Fragment() {

    private var _binding: FragmentSortBinding? = null
    private val binding get() = _binding!!

    private var tabAdapter: TabAdapter? = null
    private var mCurrentTag = "all"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabAdapter = TabAdapter(childFragmentManager)
        binding.vpView.adapter = tabAdapter
        binding.indicator.setViewPager(binding.vpView)
    }

    override fun onResume() {
        super.onResume()
        if (Constant.sCategryListChanged) {
            tabAdapter = null
            tabAdapter = TabAdapter(childFragmentManager)
            binding.vpView.removeAllViews()
            binding.vpView.adapter = tabAdapter
            binding.indicator.setViewPager(binding.vpView)
            for (i in Constant.sCategoryList.indices) {
                if (Constant.sCategoryList[i] == mCurrentTag) {
                    binding.vpView.setCurrentItem(i, true)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.vpView.let { mCurrentTag = Constant.sCategoryList[it.currentItem] }
        Constant.sCategryListChanged = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): SortFragment {
            val args = Bundle()
            val fragment = SortFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
