package uz.umarxon.uzendictionary.Fragments

import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_image_view.view.*
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.Utils.Data
import uz.umarxon.uzendictionary.databinding.FragmentImageViewBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageViewFragment : Fragment() {
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

    lateinit var binding : FragmentImageViewBinding
    lateinit var root:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImageViewBinding.inflate(layoutInflater)
        root = inflater.inflate(R.layout.fragment_image_view, container, false)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        Data.isSettingFragmentActive = false
        root.image1.setImageURI(Uri.parse((arguments?.getSerializable("word") as Word).image))

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}