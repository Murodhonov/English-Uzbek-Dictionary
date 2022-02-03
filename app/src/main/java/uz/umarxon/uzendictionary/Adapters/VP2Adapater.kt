package uz.umarxon.uzendictionary.Adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.add_category.view.*
import kotlinx.android.synthetic.main.delete_dialog.view.*
import kotlinx.android.synthetic.main.delete_dialog.view.cancel
import uz.umarxon.uzendictionary.DB.AppDatabase.AppDatabase
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.databinding.FragmentWordsListBinding

class VP2Adapter(
    var findNavControler: NavController,
    var context: Context,
    private val listWord: List<Word>,
    private val typeList: List<Category>,
    var myClick2: MyClick2,
    var positionTab: Int,
) :
    RecyclerView.Adapter<VP2Adapter.Vh>() {
    inner class Vh(var itemRv: FragmentWordsListBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(p: Int) {

            when (p) {
                1 -> {
                    itemRv.rv2.adapter = RvAdapter2(listWord, object : RvAdapter2.MyClick {
                        override fun click(word: Word) {
                            myClick2.click2(word)
                        }

                        override fun more(word: Word, i: ImageView) {
                            @SuppressLint("DiscouragedPrivateApi")
                            val popupMenu = PopupMenu(context, i)
                            popupMenu.inflate(R.menu.delete_menu)
                            popupMenu.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.edit->{

                                        findNavControler.navigate(R.id.editWordFragment, bundleOf("word" to word))

                                        true
                                    }
                                    R.id.delete -> {
                                        val alert = AlertDialog.Builder(context).create()

                                        val itemView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null, false)

                                        itemView.title_dialog.text = "Ushbu so'zni o'chirmoqchimisiz."

                                        itemView.cancel.setOnClickListener {
                                            alert.hide()
                                        }

                                        itemView.del_btn.setOnClickListener {
                                            AppDatabase.getInstance(context).wordsDao().deleteWord(word)
                                            Toast.makeText(context, "Deleted !", Toast.LENGTH_SHORT).show()
                                            myClick2.refreshList()
                                        }

                                        alert.setView(itemView)

                                        alert.window?.setBackgroundDrawableResource(android.R.color.transparent)

                                        alert.show()
                                        true
                                    }
                                    else -> true
                                }
                            }

                            try {
                                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                                popup.isAccessible = true
                                val menu = popup.get(popupMenu)
                                menu.javaClass.getDeclaredMethod("setForceShowIcon",
                                    Boolean::class.java).invoke(menu, true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                popupMenu.show()
                            }
                        }
                    })
                }
                0 -> {
                    itemRv.rv2.adapter = RvAdapter3(typeList, object : RvAdapter3.MyClick {
                        override fun click(category: Category) {
                            myClick2.click(category)
                        }

                        @SuppressLint("DiscouragedPrivateApi", "InflateParams", "SetTextI18n")
                        override fun more(category: Category, i: ImageView, p: Int) {
                            val popupMenu = PopupMenu(context, i)
                            popupMenu.inflate(R.menu.delete_menu)
                            popupMenu.setOnMenuItemClickListener {
                                when (it.itemId) {
                                    R.id.delete -> {

                                        val alert = AlertDialog.Builder(context).create()

                                        val itemView = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null, false)

                                        var num = 0

                                        for (word in listWord) {
                                            if (word.category_id == category.id) {
                                                num++
                                            }
                                        }

                                        itemView.title_dialog.text = "Ushbu kategoriya ni o'chirmoqchimisiz\n Bu kategoriya $num ta so'zni o'z ichiga oladi agar kategoriya o'chirilsa so'zlar xam o'chib ketadi."

                                        itemView.del_btn.setOnClickListener{
                                            for (word in listWord) {
                                                if (word.category_id == category.id) {
                                                    AppDatabase.getInstance(context).wordsDao().deleteWord(word)
                                                }
                                            }
                                            AppDatabase.getInstance(context).categoryDao().deleteCategory(category)
                                            Toast.makeText(context, "Deleted!!", Toast.LENGTH_SHORT).show()
                                            myClick2.refreshList()
                                            alert.hide()
                                        }

                                        itemView.cancel.setOnClickListener {
                                            alert.hide()
                                        }

                                        alert.setView(itemView)

                                        alert.window?.setBackgroundDrawableResource(android.R.color.transparent)

                                        alert.show()

                                        true
                                    }
                                    R.id.edit -> {

                                        val dialog = AlertDialog.Builder(context, R.style.SheetDialog).create()

                                        val itemView = LayoutInflater.from(context).inflate(R.layout.add_category, null, false)

                                        itemView.name_category.setText(category.name)

                                        itemView.save_btn.setOnClickListener {

                                            val name = itemView.name_category.text.toString().trim()
                                            Toast.makeText(context, name, Toast.LENGTH_SHORT).show()

                                            if (name.isNotEmpty()) {

                                                category.name = name

                                                AppDatabase.getInstance(context).categoryDao().updateCategory(category)
                                                Toast.makeText(context,
                                                    "Category changed",
                                                    Toast.LENGTH_SHORT).show()
                                                dialog.hide()
                                                myClick2.refreshList()
                                            } else {
                                                Toast.makeText(context,
                                                    "Name empty",
                                                    Toast.LENGTH_SHORT).show()
                                            }
                                            dialog?.hide()
                                        }
                                        itemView.cancel.setOnClickListener {
                                            dialog?.hide()
                                        }

                                        dialog.setView(itemView)

                                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                                        dialog.show()

                                        true
                                    }
                                    else -> true
                                }
                            }

                            try {
                                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                                popup.isAccessible = true
                                val menu = popup.get(popupMenu)
                                menu.javaClass.getDeclaredMethod("setForceShowIcon",
                                    Boolean::class.java).invoke(menu, true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                popupMenu.show()
                            }
                        }
                     }
                  )
                }
            }
        }
    }

    interface MyClick2 {
        fun click(category: Category)
        fun click2(word:Word)
        fun refreshList()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(FragmentWordsListBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = 2
}