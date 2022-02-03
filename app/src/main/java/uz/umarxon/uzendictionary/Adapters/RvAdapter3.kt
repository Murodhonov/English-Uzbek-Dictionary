package uz.umarxon.uzendictionary.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.databinding.MainRvItem2Binding
import uz.umarxon.uzendictionary.databinding.MainRvItem3Binding
import uz.umarxon.uzendictionary.databinding.MainRvItemBinding

class RvAdapter3(private val list: List<Category>, var myClick: MyClick) :
    RecyclerView.Adapter<RvAdapter3.Vh>() {
    inner class Vh(var itemRv: MainRvItem3Binding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(word: Category, position: Int) {

            itemRv.english.text = word.name
            itemRv.english.isSelected = true

            itemRv.card.setOnClickListener {
                myClick.click(word)
            }

            itemRv.moreImage.setOnClickListener {
                myClick.more(word,(it as ImageView),position)
            }

        }
    }

    interface MyClick{
        fun click(category: Category)
        fun more(category: Category,i:ImageView,p:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MainRvItem3Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}