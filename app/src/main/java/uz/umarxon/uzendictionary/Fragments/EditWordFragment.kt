package uz.umarxon.uzendictionary.Fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_edit_word.view.*
import uz.umarxon.uzendictionary.Adapters.SpinnerAdapter1
import uz.umarxon.uzendictionary.DB.AppDatabase.AppDatabase
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.databinding.FragmentEditWordBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditWordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditWordFragment : Fragment() {
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

    lateinit var root:View
    var image = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment

        root = LayoutInflater.from(context).inflate(R.layout.fragment_edit_word,null,false)

        root.image.setOnClickListener {
            getImageContent.launch("image/*")
        }

        val db = AppDatabase.getInstance(root.context)
        val typeList = ArrayList<Category>()
        val words = arguments?.getSerializable("word") as Word

        typeList.add(Category("Kategoriyani tanlang"))

        for (i in db.categoryDao().getAll()){
            typeList.add(i)
        }

        image = words.image!!

        root.type.adapter = SpinnerAdapter1(typeList)

        root.type.setSelection(getFromPosition(words,typeList))

        root.english.setText(words.english)
        root.uzbek.setText(words.uzbek)
        val liked = words.isLiked
        root.image.setImageURI(Uri.parse(image))

        root.back.setOnClickListener {
            findNavController().popBackStack()
        }

        root.save_btn.setOnClickListener {
            val eng = root.english.text.toString().trim()
            val uzb = root.uzbek.text.toString().trim()
            val type = root.type.selectedItemPosition

            if (eng.isNotEmpty() && uzb.isNotEmpty() && image.isNotEmpty() && type != 0){

                db.wordsDao().updateWord(Word(words.id,uzb,eng,typeList[type].id,liked,image,""))
                Toast.makeText(context, "O'zgartirildi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()

            }else{
                Toast.makeText(context, "Malumotlar yetarli emas", Toast.LENGTH_SHORT).show()
            }
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
         * @return A new instance of fragment EditWordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditWordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    private fun getFromPosition(words: Word, typeList: ArrayList<Category>): Int {
        var pos = 0
        for ((j,i) in typeList.withIndex()){
            if (words.category_id == i.id){
                pos = j
                break
            }
        }
        return pos
    }


    @SuppressLint("SimpleDateFormat")
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri ->
            try {
                uri ?: return@registerForActivityResult
                root.image.setImageURI(uri)

                val inputStream = activity?.contentResolver?.openInputStream(uri)
                val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = File(activity?.filesDir, "${simpleDateFormat}rasm.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)

                inputStream?.close()
                fileOutputStream.close()

                image = file.absolutePath
            } catch (e: Exception) {
                Toast.makeText(context, "Rasm tanlanmadi", Toast.LENGTH_SHORT).show()
            }
        }
}