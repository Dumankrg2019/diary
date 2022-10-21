package kaz.dev.thoughtdiary.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.GridLayout
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kaz.dev.thoughtdiary.R
import kaz.dev.thoughtdiary.data.models.ToDoData
import kaz.dev.thoughtdiary.data.viewmodel.SharedViewModel
import kaz.dev.thoughtdiary.data.viewmodel.ToDoViewModel
import kaz.dev.thoughtdiary.databinding.FragmentListBinding
import kaz.dev.thoughtdiary.fragments.list.adapter.ListAdapter
import kaz.dev.thoughtdiary.utils.hideKeyboard

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

    private  var _binding:FragmentListBinding? = null
    private val binding get() = _binding!!

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }


        // set menu
        setHasOptionsMenu(true)

        //call func hide keyboard
        hideKeyboard(requireActivity())

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        swipeToDelete(binding.recyclerView)

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, {data->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        mSharedViewModel.emptyDatabase.observe(viewLifecycleOwner, {existDatabase->
            showEmptyDatabaseView(existDatabase)
        })
    }

    private fun showEmptyDatabaseView(emptyDatabase: Boolean) {
        if(emptyDatabase) {
            binding.ivNoData.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.VISIBLE
        } else {
            binding.ivNoData.visibility = View.INVISIBLE
            binding.tvNoData.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, { adapter.setData(it)})
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, { adapter.setData(it)})
        }
        return super.onOptionsItemSelected(item)
    }

    // show alert dialog when delete ALL items of database
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setPositiveButton("Yes"){_, _->
            mToDoViewModel.deleteAll()
            Toast.makeText(requireActivity(),
                "Successfully removed Everything",
                Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("No"){_,_->}
        builder.setTitle("Delete Everything?")
        builder.setMessage("Are you sure you want to remove Everything?")
        builder.create().show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
            if(query != null) {
                searchThroughDatabase(query)
            }
        return true
    }

    private fun searchThroughDatabase(query:String) {
       val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, {list->
            list?.let {
                adapter.setData(it)
            }
        })
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null) {
            searchThroughDatabase(newText)
        }
        return true
    }

    private fun swipeToDelete(recyclerView:RecyclerView) {
        val swipeToDeleteCallback = object: SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]

                //delete item
                mToDoViewModel.deleteItem(deletedItem)

                adapter.notifyItemRemoved(viewHolder.adapterPosition) //for restore deleted item
//                Toast.makeText(requireActivity(),
//                    "Successfully removed: '${deletedItem.title}'",
//                    Toast.LENGTH_SHORT).show()

                //restore deleted item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view:View, deletedItem:ToDoData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Udo") {
            mToDoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }


}