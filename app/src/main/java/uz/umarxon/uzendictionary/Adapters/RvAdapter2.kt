package uz.umarxon.uzendictionary.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.databinding.MainRvItem2Binding
import uz.umarxon.uzendictionary.databinding.MainRvItemBinding

class RvAdapter2(private val list: List<Word>, var myClick: MyClick) :
    RecyclerView.Adapter<RvAdapter2.Vh>() {
    inner class Vh(var itemRv: MainRvItem2Binding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(word: Word, position: Int) {

            itemRv.english.text = word.english
            itemRv.uzbek.text = word.uzbek
            itemRv.uzbek.isSelected = true

            itemRv.card.setOnClickListener {
                myClick.click(word)
            }

            itemRv.moreImage.setOnClickListener {
                myClick.more(word,(it as ImageView))
            }

        }
    }

    interface MyClick{
        fun click(word: Word)
        fun more(word: Word,i:ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MainRvItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}