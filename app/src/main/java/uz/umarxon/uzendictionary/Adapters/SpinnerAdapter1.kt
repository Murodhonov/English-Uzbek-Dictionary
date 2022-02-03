package uz.umarxon.uzendictionary.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*
import uz.umarxon.uzendictionary.DB.Entity.Category
import uz.umarxon.uzendictionary.R

class SpinnerAdapter1(private val list: List<Category>) : BaseAdapter() {
    override fun getCount(): Int = list.size

    override fun getItem(p0: Int): Any = list[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        val itemView: View = p1 ?: LayoutInflater.from(p2?.context).inflate(R.layout.spinner_item, p2, false)

        itemView.name_spinner.text = list[p0].name

        return itemView
    }

}