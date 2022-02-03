package uz.umarxon.uzendictionary.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.DB.Entity.Word
import uz.umarxon.uzendictionary.databinding.FragmentWordsListBinding

class ViewPager2Adapter(var pos:Int,private val list: List<Word>,var listTab:List<Category>,var myClick2: MyClick2) :
    RecyclerView.Adapter<ViewPager2Adapter.Vh>() {
    inner class Vh(var itemRv: FragmentWordsListBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(category: Category,position:Int) {

            val sort = ArrayList<Word>()

            for (i in list){
                if (i.category_id == category.id){
                    sort.add(i)
                }
            }

            itemRv.rv2.adapter = RvAdapter(sort,object : RvAdapter.MyClick{
                override fun click(word: Word) {
                    myClick2.click(word)
                }
            })

        }
    }
    interface MyClick2{
        fun click(word:Word)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(FragmentWordsListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(listTab[position],position)
    }

    override fun getItemCount(): Int = pos
}