package uz.umarxon.uzendictionary.Fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wuyr.rippleanimation.RippleAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_tab.view.*
import uz.umarxon.uzendictionary.Adapters.RvAdapter
import uz.umarxon.uzendictionary.Adapters.ViewPager2Adapter
import uz.umarxon.uzendictionary.DB.AppDatabase.AppDatabase
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.MainActivity
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.Utils.Data
import uz.umarxon.uzendictionary.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentHomeBinding
    var image = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        return binding.root
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()

        Data.isHome = true
        if (Data.isApplicationIsFirstRunning){
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.TRANSPARENT)
            (activity as MainActivity?)?.showBottomMenu(true)
            Data.isApplicationIsFirstRunning = false
        }else{
            Handler().postDelayed({
                (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.TRANSPARENT)
                (activity as MainActivity?)?.showBottomMenu(true)
            }, 1000)
        }

        binding.setting.setOnClickListener {
            RippleAnimation.create(binding.setting).setDuration(1000).start()
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.parseColor("#59595959"))
            (activity as MainActivity?)?.showBottomMenu(false)
            findNavController().navigate(R.id.settingFragment)
            Data.isHome = false
        }
        val wordlist = AppDatabase.getInstance(binding.root.context).wordsDao().getAll()

        if (Data.homeSelected) {
            (activity as MainActivity?)?.first?.visibility = View.VISIBLE
            (activity as MainActivity?)?.second?.visibility = View.INVISIBLE
            val tabList = ArrayList<String>()
            rv.visibility = View.GONE

            view_pager2.visibility = View.VISIBLE
            tab_nav.visibility = View.VISIBLE


            for (i in AppDatabase.getInstance(binding.root.context).categoryDao().getAll()) {
                tabList.add(i.name!!)
            }

            if (tabList.size >= 4) {
                tab_nav.tabMode = TabLayout.MODE_SCROLLABLE
            }

            view_pager2.adapter = ViewPager2Adapter(tabList.size,
                wordlist,
                AppDatabase.getInstance(binding.root.context).categoryDao().getAll(),
                object : ViewPager2Adapter.MyClick2 {
                    override fun click(word: Word) {
                        findNavController().navigate(R.id.infoFragment, bundleOf("word" to word))
                        Data.isHome = false
                        (activity as MainActivity?)?.showBottomMenu(false)
                    }
                })

            TabLayoutMediator(tab_nav, view_pager2) { tab, pos ->
                val view = LayoutInflater.from(context).inflate(R.layout.item2, null, false)
                view.text1.text = tabList[pos]
                tab.customView = view

                if (pos == tab_nav.selectedTabPosition) {
                    view.text1.setTextColor(Color.parseColor("#FCB600"))
                }
            }.attach()

            binding.tabNav.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.customView?.text1?.setTextColor(Color.parseColor("#FCB600"))
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.customView?.text1?.setTextColor(Color.parseColor("#000000"))
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })
        } else if (Data.likeSelected) {
            Data.homeSelected = false
            Data.likeSelected = true
            (activity as MainActivity?)?.first?.visibility = View.INVISIBLE
            (activity as MainActivity?)?.second?.visibility = View.VISIBLE
            view_pager2.visibility = View.GONE
            tab_nav.visibility = View.GONE
            rv.visibility = View.VISIBLE

            val likedList = ArrayList<Word>()

            for (i in wordlist) {
                if (i.isLiked == true) {
                    likedList.add(i)
                }
            }

            rv.adapter = RvAdapter(likedList, object : RvAdapter.MyClick {
                override fun click(word: Word) {
                    findNavController().navigate(R.id.infoFragment,
                        bundleOf("word" to word))
                    Data.isHome = false
                    (activity as MainActivity?)?.showBottomMenu(false)

                }
            })
        }

        (activity as MainActivity?)?.bottom_view?.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home -> {
                        Data.homeSelected = true
                        Data.likeSelected = false
                        item.setIcon(R.drawable.home_active)
                        (activity as MainActivity?)?.first?.visibility = View.VISIBLE
                        (activity as MainActivity?)?.second?.visibility = View.INVISIBLE
                        val tabList = ArrayList<String>()
                        rv.visibility = View.GONE

                        view_pager2.visibility = View.VISIBLE
                        tab_nav.visibility = View.VISIBLE

                        for (i in AppDatabase.getInstance(binding.root.context).categoryDao()
                            .getAll()) {
                            tabList.add(i.name!!)
                        }

                        if (tabList.size >= 4) {
                            tab_nav.tabMode = TabLayout.MODE_SCROLLABLE
                        }

                        view_pager2.adapter =
                            ViewPager2Adapter(tabList.size,
                                wordlist,
                                AppDatabase.getInstance(binding.root.context).categoryDao()
                                    .getAll(),
                                object : ViewPager2Adapter.MyClick2 {
                                    override fun click(word: Word) {
                                        findNavController().navigate(R.id.infoFragment,
                                            bundleOf("word" to word))
                                        Data.isHome = false
                                        (activity as MainActivity?)?.showBottomMenu(false)
                                    }
                                })

                        TabLayoutMediator(tab_nav, view_pager2) { tab, pos ->
                            val view =
                                LayoutInflater.from(context).inflate(R.layout.item2, null, false)
                            view.text1.text = tabList[pos]
                            tab.customView = view

                            if (pos == tab_nav.selectedTabPosition) {
                                view.text1.setTextColor(Color.parseColor("#FCB600"))
                            }
                        }.attach()

                        binding.tabNav.addOnTabSelectedListener(object :
                            TabLayout.OnTabSelectedListener {
                            override fun onTabSelected(tab: TabLayout.Tab?) {
                                tab?.customView?.text1?.setTextColor(Color.parseColor("#FCB600"))
                            }

                            override fun onTabUnselected(tab: TabLayout.Tab?) {
                                tab?.customView?.text1?.setTextColor(Color.parseColor("#000000"))
                            }

                            override fun onTabReselected(tab: TabLayout.Tab?) {

                            }
                        })
                    }
                    R.id.liked -> {
                        Data.homeSelected = false
                        Data.likeSelected = true
                        item.setIcon(R.drawable.heart_active)
                        (activity as MainActivity?)?.first?.visibility = View.INVISIBLE
                        (activity as MainActivity?)?.second?.visibility = View.VISIBLE
                        view_pager2.visibility = View.GONE
                        tab_nav.visibility = View.GONE
                        rv.visibility = View.VISIBLE

                        val likedList = ArrayList<Word>()

                        for (i in wordlist) {
                            if (i.isLiked == true) {
                                likedList.add(i)
                            }
                        }

                        rv.adapter = RvAdapter(likedList, object : RvAdapter.MyClick {
                            override fun click(word: Word) {
                                findNavController().navigate(R.id.infoFragment,
                                    bundleOf("word" to word))
                                Data.isHome = false
                                (activity as MainActivity?)?.showBottomMenu(false)

                            }
                        })
                    }
                }
                return true
            }
        })
    }

}