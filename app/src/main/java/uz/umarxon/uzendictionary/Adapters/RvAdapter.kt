package uz.umarxon.uzendictionary.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.databinding.MainRvItemBinding

class RvAdapter(private val list: List<Word>,var myClick: MyClick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var itemRv: MainRvItemBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(word: Word, position: Int) {

            itemRv.english.text = word.english
            itemRv.uzbek.text = word.uzbek
            itemRv.uzbek.isSelected = true
            itemRv.english.isSelected = true

            itemRv.card.setOnClickListener {
                myClick.click(word)
            }

        }
    }

    interface MyClick{
        fun click(word: Word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MainRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}