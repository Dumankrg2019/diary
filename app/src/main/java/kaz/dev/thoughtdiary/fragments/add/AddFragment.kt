package kaz.dev.thoughtdiary.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import kaz.dev.thoughtdiary.R
import kaz.dev.thoughtdiary.data.models.Priority
import kaz.dev.thoughtdiary.data.models.ToDoData
import kaz.dev.thoughtdiary.data.viewmodel.SharedViewModel
import kaz.dev.thoughtdiary.data.viewmodel.ToDoViewModel
import kaz.dev.thoughtdiary.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var binding:FragmentAddBinding
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        //set menu
        setHasOptionsMenu(true)

        binding.spinnerPriorities.onItemSelectedListener = mSharedViewModel.listener

        // Inflate the layout for this fragment
        val view = binding.root
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add) {
            insertDataToDB()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val mTitle = binding.etTitle.text.toString()
        val mPriority = binding.spinnerPriorities.selectedItem.toString()
        val mDescription = binding.etDesctiption.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if(validation) {
            // insert data to DB
            val newData = ToDoData(
                0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireActivity(), "Successfully added!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireActivity(), "Please, fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
}