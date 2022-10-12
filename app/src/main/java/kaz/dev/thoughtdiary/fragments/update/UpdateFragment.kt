package kaz.dev.thoughtdiary.fragments.update

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kaz.dev.thoughtdiary.R
import kaz.dev.thoughtdiary.data.models.Priority
import kaz.dev.thoughtdiary.data.models.ToDoData
import kaz.dev.thoughtdiary.data.viewmodel.SharedViewModel
import kaz.dev.thoughtdiary.data.viewmodel.ToDoViewModel
import kaz.dev.thoughtdiary.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        //Set menu
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etCurrentTitle.setText(args.currentItem.title)
        binding.etCurrentDesctiption.setText(args.currentItem.description)
        binding.spinnerCurrentPriorities.setSelection(mSharedViewModel.parsePriorityToInt(args.currentItem.priority))
        binding.spinnerCurrentPriorities.onItemSelectedListener = mSharedViewModel.listener

    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_save) {
            updateItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val title = binding.etCurrentTitle.text.toString()
        val description = binding.etCurrentDesctiption.text.toString()
        val getPriority = binding.spinnerCurrentPriorities.selectedItem.toString()

        val validation = mSharedViewModel.verifyDataFromUser(title, description)
        if(validation) {
            //update current item
            val updateItem = ToDoData(
                args.currentItem.id,
                title,
                mSharedViewModel.parsePriority(getPriority),
                description
            )
            mToDoViewModel.updateDate(updateItem)
            Toast.makeText(requireActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show()

            //navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireActivity(), "Please, fill out all fields!", Toast.LENGTH_SHORT).show()
        }
    }
}