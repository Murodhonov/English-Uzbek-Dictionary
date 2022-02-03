package uz.umarxon.uzendictionary.Fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wuyr.rippleanimation.RippleAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_category.view.*
import kotlinx.android.synthetic.main.add_category.view.cancel
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.item_tab.view.*
import uz.umarxon.uzendictionary.Adapters.RvAdapter4
import uz.umarxon.uzendictionary.Adapters.VP2Adapter
import uz.umarxon.uzendictionary.DB.AppDatabase.AppDatabase
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.MainActivity
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.Utils.Data
import uz.umarxon.uzendictionary.databinding.FragmentSettingBinding
import uz.umarxon.uzendictionary.databinding.ListDialogBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
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

    lateinit var binding: FragmentSettingBinding
    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(LayoutInflater.from(context))
        root = inflater.inflate(R.layout.fragment_setting, container, false)

        Data.settingBackImage = root.back78
        Data.isSettingFragmentActive = true

        val listWord = AppDatabase.getInstance(binding.root.context).wordsDao().getAll()
        val typeList = AppDatabase.getInstance(binding.root.context).categoryDao().getAll()

        (activity as MainActivity?)?.showBottomMenu(false)
        Handler().postDelayed({
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.TRANSPARENT)
        },1000)

        root.back.setOnClickListener {
            RippleAnimation.create(root.back).setDuration(1000).start()
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.parseColor("#59595959"))
            findNavController().popBackStack()
        }

        root.addImage.setOnClickListener {
            if (root.tab_bottom_menu2.selectedTabPosition == 0){

                val dialog = AlertDialog.Builder(context,R.style.SheetDialog).create()

                val itemView = LayoutInflater.from(context).inflate(R.layout.add_category,null,false)

                itemView.save_btn.setOnClickListener {
                    val name = itemView.name_category.text.toString().trim()

                    if (name.isNotEmpty()){
                        val category = Category()

                        category.name = name

                        AppDatabase.getInstance(binding.root.context).categoryDao().addCategory(category)
                        Toast.makeText(context, "Category saved", Toast.LENGTH_SHORT).show()
                        dialog.hide()
                        onStart()
                    }else{
                        Toast.makeText(context, "Name empty", Toast.LENGTH_SHORT).show()
                    }
                    dialog?.hide()
                }
                itemView.cancel.setOnClickListener {
                    dialog?.hide()
                }

                dialog.setView(itemView)

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                dialog.show()
            }else{
                findNavController().navigate(R.id.addWordFragment)
            }
        }

        root.view_pager23.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Data.settingTabPos = position
            }
        })
        root.view_pager23.adapter = VP2Adapter(findNavController(),root.context,listWord,typeList,object : VP2Adapter.MyClick2{
            override fun click(category: Category) {
                val list = AppDatabase.getInstance(binding.root.context).wordsDao().getAll()
                val list2 = ArrayList<Word>()

                for (i in list){
                    if (i.category_id == category.id){
                        list2.add(i)
                    }
                }

                val a = BottomSheetDialog(binding.root.context)
                val i = ListDialogBinding.inflate(layoutInflater)

                i.english.text = category.name
                i.rv23.adapter = RvAdapter4(list2,object : RvAdapter4.MyClicking{
                    override fun click(user: Word) {
                        findNavController().navigate(R.id.infoFragment, bundleOf("word" to user))
                        a.hide()
                    }

                    override fun like(user: Word) {
                        AppDatabase.getInstance(binding.root.context).wordsDao().updateWord(user)
                    }
                })
                i.size.text = "${list2.size-1} ta so'z"

                a.setContentView(i.root)
                a.show()

            }

            override fun click2(word: Word) {
                findNavController().navigate(R.id.infoFragment, bundleOf("word" to word))
            }

            override fun refreshList() {
                onStart()
            }

        },root.tab_bottom_menu2.selectedTabPosition)

        val tabList = ArrayList<String>()

        tabList.add("Kategoriya")
        tabList.add("So'zlar")

        val imageList = ArrayList<Int>()

        imageList.add(R.drawable.category1)
        imageList.add(R.drawable.calendar1)

        TabLayoutMediator(root.tab_bottom_menu2, root.view_pager23) { tab, pos ->
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null, false)
            view.text1.text = tabList[pos]
            view.image_tab.setImageResource(imageList[pos])
            tab.customView = view

            when(pos){
                0->{
                    tab.customView?.image_tab?.setColorFilter(Color.parseColor("#FCB600"))
                    tab.customView?.text1?.setTextColor(Color.parseColor("#FCB600"))
                }
            }

        }.attach()

        root.tab_bottom_menu2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.image_tab?.setColorFilter(Color.parseColor("#FCB600"))
                tab.customView?.text1?.setTextColor(Color.parseColor("#FCB600"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.image_tab?.setColorFilter(Color.parseColor("#000000"))
                tab?.customView?.text1?.setTextColor(Color.parseColor("#000000"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        return root
    }

    override fun onStart() {
        super.onStart()

        val listWord = AppDatabase.getInstance(binding.root.context).wordsDao().getAll()
        val typeList = AppDatabase.getInstance(binding.root.context).categoryDao().getAll()

        (activity as MainActivity?)?.showBottomMenu(false)


        root.addImage.setOnClickListener {
            if (root.tab_bottom_menu2.selectedTabPosition == 0){

                val dialog = AlertDialog.Builder(context,R.style.SheetDialog).create()

                val itemView = LayoutInflater.from(context).inflate(R.layout.add_category,null,false)

                itemView.save_btn.setOnClickListener {
                    val name = itemView.name_category.text.toString().trim()

                    if (name.isNotEmpty()){
                        val category = Category()

                        category.name = name

                        AppDatabase.getInstance(binding.root.context).categoryDao().addCategory(category)
                        Toast.makeText(context, "Category saved", Toast.LENGTH_SHORT).show()
                        dialog.hide()
                        onStart()
                    }else{
                        Toast.makeText(context, "Name empty", Toast.LENGTH_SHORT).show()
                    }
                    dialog?.hide()
                }
                itemView.cancel.setOnClickListener {
                    dialog?.hide()
                }

                dialog.setView(itemView)

                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                dialog.show()
            }else{
                RippleAnimation.create(root.addImage).setDuration(1000).start()
                (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.parseColor("#59595959"))
                findNavController().navigate(R.id.addWordFragment)
            }
        }

        root.view_pager23.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Data.settingTabPos = position
            }
        })
        root.view_pager23.adapter = VP2Adapter(findNavController(),root.context,listWord,typeList,object : VP2Adapter.MyClick2{
            override fun click(category: Category) {
                val list = AppDatabase.getInstance(binding.root.context).wordsDao().getAll()
                val list2 = ArrayList<Word>()

                for (i in list){
                    if (i.category_id == category.id){
                        list2.add(i)
                    }
                }

                val a = BottomSheetDialog(binding.root.context)
                val i = ListDialogBinding.inflate(layoutInflater)

                i.english.text = category.name
                i.rv23.adapter = RvAdapter4(list2,object : RvAdapter4.MyClicking{
                    override fun click(user: Word) {
                        findNavController().navigate(R.id.infoFragment, bundleOf("word" to user))
                        a.hide()
                    }
                    override fun like(user: Word) {
                        AppDatabase.getInstance(binding.root.context).wordsDao().updateWord(user)
                    }
                })
                i.size.text = if (list2.size <= 0){
                    "So'z yo'q"
                }else{
                    "${list2.size} ta so'z"
                }

                a.setContentView(i.root)
                a.show()
            }

            override fun click2(word: Word) {
                findNavController().navigate(R.id.infoFragment, bundleOf("word" to word))
            }

            override fun refreshList() {
                onStart()
            }

        },root.tab_bottom_menu2.selectedTabPosition)

        val tabList = ArrayList<String>()

        tabList.add("Kategoriya")
        tabList.add("So'zlar")

        val imageList = ArrayList<Int>()

        imageList.add(R.drawable.category1)
        imageList.add(R.drawable.calendar1)

        TabLayoutMediator(root.tab_bottom_menu2, root.view_pager23) { tab, pos ->
            val view = LayoutInflater.from(context).inflate(R.layout.item_tab, null, false)
            view.text1.text = tabList[pos]
            view.image_tab.setImageResource(imageList[pos])
            tab.customView = view

            when(pos){
                0->{
                    tab.customView?.image_tab?.setColorFilter(Color.parseColor("#FCB600"))
                    tab.customView?.text1?.setTextColor(Color.parseColor("#FCB600"))
                }
            }

        }.attach()

        root.tab_bottom_menu2.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.customView?.image_tab?.setColorFilter(Color.parseColor("#FCB600"))
                tab.customView?.text1?.setTextColor(Color.parseColor("#FCB600"))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.image_tab?.setColorFilter(Color.parseColor("#000000"))
                tab?.customView?.text1?.setTextColor(Color.parseColor("#000000"))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}