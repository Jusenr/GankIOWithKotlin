package onlyloveyd.com.gankioclient.fragment

import cn.bingoogolapple.refreshlayout.BGARefreshLayout
import onlyloveyd.com.gankioclient.utils.Constant

/**
 * 文 件 名: BonusFragment
 * 创 建 人: 易冬
 * 创建日期: 2017/4/21 09:24
 * 邮   箱: onlyloveyd@gmail.com
 * 博   客: https://onlyloveyd.cn
 * 描   述：福利界面
 */
class BonusFragment : GankFragment() {

    override fun initBGAData(): Unit = binding.rlGankRefresh.beginRefreshing()

    override fun onBGARefreshLayoutBeginRefreshing(refreshLayout: BGARefreshLayout): Unit = getContent(Constant.BONUS, 1)

    override fun onBGARefreshLayoutBeginLoadingMore(refreshLayout: BGARefreshLayout): Boolean {
        getContent(Constant.BONUS, ++pagenum)
        return true
    }
}
