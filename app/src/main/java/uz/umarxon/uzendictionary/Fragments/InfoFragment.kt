package uz.umarxon.uzendictionary.Fragments

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.wuyr.rippleanimation.RippleAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_word.view.*
import kotlinx.android.synthetic.main.fragment_add_word.view.back
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_info.view.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import uz.umarxon.uzendictionary.DB.AppDatabase.AppDatabase
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.MainActivity
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.Utils.Data
import uz.umarxon.uzendictionary.databinding.FragmentInfoBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoFragment : Fragment() {
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

    lateinit var binding : FragmentInfoBinding
    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInfoBinding.inflate(layoutInflater)
        root = inflater.inflate(R.layout.fragment_info, container, false)

        val word = arguments?.getSerializable("word") as Word

        Data.settingBackImage = root.back45
        Data.isSettingFragmentActive = true

        root.back45.setOnClickListener {
            RippleAnimation.create(root.back45).setDuration(1000).start()
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.parseColor("#59595959"))
            findNavController().popBackStack()
        }

        root.english34.text = word.english
        root.englis_word.text = word.english
        root.uzbek_word.text = word.uzbek
        root.image_first.setImageURI(Uri.parse(word.image))

        if (word.isLiked != true){
            root.like12.setImageResource(R.drawable.hear1)
        }else{
            root.like12.setImageResource(R.drawable.fill_heart1)
        }

        root.like12.setOnClickListener {
            if (word.isLiked == true){
                word.isLiked = false
                root.like12.setImageResource(R.drawable.hear1)
            }else{
                word.isLiked = true
                root.like12.setImageResource(R.drawable.fill_heart1)
            }

            AppDatabase.getInstance(binding.root.context).wordsDao().updateWord(word)
        }


        root.image_first.setOnClickListener {
            findNavController().navigate(R.id.imageViewFragment, bundleOf("word" to word),null, FragmentNavigatorExtras( (it as ImageView) to "image1"))
        }

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}