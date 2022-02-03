package uz.umarxon.uzendictionary.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.R
import uz.umarxon.uzendictionary.databinding.MainRvItem2Binding
import uz.umarxon.uzendictionary.databinding.MainRvItem3Binding
import uz.umarxon.uzendictionary.databinding.MainRvItemBinding

class RvAdapter4(private val list: List<Word>,var myClick:MyClicking) :
    RecyclerView.Adapter<RvAdapter4.Vh>() {
    inner class Vh(var itemRv: MainRvItem2Binding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(word: Word, position: Int) {

            itemRv.english.text = word.english
            itemRv.uzbek.text = word.uzbek
            itemRv.english.isSelected = true
            itemRv.uzbek.isSelected = true

            if (word.isLiked == true){
                itemRv.moreImage.setImageResource(R.drawable.fill_heart1)
            }else{
                itemRv.moreImage.setImageResource(R.drawable.black_heart)
            }

            itemRv.moreImage.setOnClickListener {
                if (word.isLiked == true){
                    itemRv.moreImage.setImageResource(R.drawable.black_heart)
                    word.isLiked = false
                    myClick.like(word)
                }else{
                    itemRv.moreImage.setImageResource(R.drawable.fill_heart1)
                    word.isLiked = true
                    myClick.like(word)
                }
            }

            itemRv.card.setBackgroundColor(Color.parseColor("#CBFFFFFF"))

            itemRv.card.setOnClickListener{
                myClick.click(word)
            }

        }
    }

    interface MyClicking{
        fun click(user:Word)
        fun like(user:Word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(MainRvItem2Binding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}