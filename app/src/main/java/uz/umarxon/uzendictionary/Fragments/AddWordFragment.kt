package uz.umarxon.uzendictionary.Fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.coroutines.experimental.askPermission
import com.google.android.material.snackbar.Snackbar
import com.wuyr.rippleanimation.RippleAnimation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add_word.view.*
import uz.umarxon.uzendictionary.Adapters.SpinnerAdapter1
import uz.umarxon.uzendictionary.DB.AppDatabase.AppDatabase
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.MainActivity
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.Utils.Data
import uz.umarxon.uzendictionary.databinding.FragmentAddWordBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddWordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddWordFragment : Fragment() {
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

    var image = ""
    lateinit var binding: FragmentAddWordBinding
    lateinit var mr : MediaRecorder
    lateinit var root: View

    @SuppressLint("WrongConstant", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddWordBinding.inflate(LayoutInflater.from(context))
        root = inflater.inflate(R.layout.fragment_add_word, container, false)

        root.image.setOnClickListener {
            getImageContent.launch("image/*")
        }
        Data.alreadyRecorded = false

        checkAudioPermission()

        (activity as MainActivity?)?.showBottomMenu(false)

        val db = AppDatabase.getInstance(root.context)
        val typeList = ArrayList<Category>()

        val category = Category()
        category.name = "Kategoriyani tanlang"
        typeList.add(category)

        for (i in db.categoryDao().getAll()) {
            typeList.add(i)
        }

        root.type.adapter = SpinnerAdapter1(typeList)

        root.back.setOnClickListener {
            RippleAnimation.create(root.back).setDuration(1000).start()
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.parseColor("#59595959"))
            findNavController().popBackStack()
        }

        Handler().postDelayed({
            (activity as MainActivity?)?.animation_ripple?.setBackgroundColor(Color.TRANSPARENT)
        }, 1000)

        root.save_btn.setOnClickListener {
            val eng = root.english.text.toString().trim()
            val uzb = root.uzbek.text.toString().trim()
            val type = root.type.selectedItemPosition

            if (eng.isNotEmpty() && uzb.isNotEmpty() && image.isNotEmpty() && type != 0) {
                val word = Word()

                word.image = image
                word.uzbek = uzb
                word.english = eng
                word.category_id = typeList[type].id
                word.isLiked = false

                db.wordsDao().addWord(word)
                Toast.makeText(context, "Saqlandi", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()

            } else {
                Toast.makeText(context, "Malumotlar yetarli emas", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun checkAudioPermission() {
        if (ActivityCompat.checkSelfPermission(binding.root.context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as MainActivity, arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
            audioRecording()
        }else{
            audioRecording()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray, ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            audioRecording()
        }
    }

    @SuppressLint("WrongConstant")
    private fun audioRecording() {

        mr = MediaRecorder()
        binding.stop.visibility = View.GONE
        binding.play.visibility = View.GONE
        binding.recording.visibility = View.VISIBLE

        binding.recording.setOnClickListener{
            root.recording.setImageResource(R.drawable.mic_off)
            binding.recording.visibility = View.GONE
            binding.stop.visibility = View.VISIBLE
            binding.play.visibility = View.GONE
            Data.path = Environment.getExternalStorageDirectory().toString() + "/English_Uzbek_Dictionary/${SimpleDateFormat("yyyyMMdd_hhmmss").format(Date())}_audio.3gp"
            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mr.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4)
            mr.setOutputFile(Data.path)
            mr.prepare()
            mr.start()
        }

        binding.stop.setOnClickListener{
            root.recording.setImageResource(R.drawable.play)
            mr.stop()
            binding.recording.visibility = View.GONE
            binding.stop.visibility = View.GONE
            binding.play.visibility = View.VISIBLE
        }

        binding.play.setOnClickListener{
            val mp = MediaPlayer()
            mp.setDataSource(Data.path)
            mp.prepare()
            mp.start()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddWordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddWordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("SimpleDateFormat")
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri ->
            try {
                root.image.setImageURI(uri)
                val inputStream = activity?.contentResolver?.openInputStream(uri)
                val simpleDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = File(activity?.filesDir, "${simpleDateFormat}rasm.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)

                inputStream?.close()
                fileOutputStream.close()

                image = file.absolutePath
            } catch (e: NullPointerException) {
                Toast.makeText(context, "Rasm tanlanmadi", Toast.LENGTH_SHORT).show()
            }
        }
}

