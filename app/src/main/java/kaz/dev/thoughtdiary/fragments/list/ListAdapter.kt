package kaz.dev.thoughtdiary.fragments.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kaz.dev.thoughtdiary.R
import kaz.dev.thoughtdiary.data.models.Priority
import kaz.dev.thoughtdiary.data.models.ToDoData
import kaz.dev.thoughtdiary.databinding.RowLayoutBinding


class ListAdapter: RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    class MyViewHolder(val binding: RowLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        private var title = itemView.findViewById<TextView>(R.id.tvTitleText)
        private var description = itemView.findViewById<TextView>(R.id.tvDescriptionText)
        private var priority = itemView.findViewById<Spinner>(R.id.spinnerPriorities)

        fun bind(data: ToDoData) {
            title.text = data.title
            description.text = data.description

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
       return  MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
     holder.binding.tvTitleText.text = dataList[position].title
     holder.binding.tvDescriptionText.text = dataList[position].description

        //при клике на объект из списка - переходитв  окно правки объекта
     holder.binding.rowBackground.setOnClickListener{
         val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
         holder.itemView.findNavController().navigate(action)
     }
        when(dataList[position].priority) {
            Priority.HIGH -> holder.binding.cvPriorityIndicator
                .setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            Priority.MEDIUM -> holder.binding.cvPriorityIndicator
                .setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
            Priority.LOW -> holder.binding.cvPriorityIndicator
                .setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}