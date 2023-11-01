package com.example.todoii.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.example.todoii.R
import com.example.todoii.databinding.FragmentToDoAddBinding
import com.example.todoii.util.ToDoData
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.collection.LLRBNode

class AddTODOFragment:DialogFragment() {

    private lateinit var binding: FragmentToDoAddBinding
    private lateinit var listener: DialogInterFace
    private  var toDoData: ToDoData?=null

    fun setListener(listener:DialogInterFace){
        this.listener = listener
    }

    companion object{
        const val TAG = "addToDoPopupFragment"

        @JvmStatic
        fun newInstance(taskId:String,task:String,description: String) = AddTODOFragment().apply {
            // store the original text and id in argument From parameters (taskId,task)
            arguments = Bundle().apply {
                putString("taskId",taskId)
                putString("task",task)
                putString("description",description)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToDoAddBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // when i click to edit show the original text
        if (arguments!=null){
            // get the original text with ID
            toDoData = ToDoData(arguments?.getString("taskId").toString(),
                arguments?.getString("task").toString(),arguments?.getString("description").toString()
                     )
        }
        // get the original text to show in box edit text
        binding.editTextTask.setText(toDoData?.task)
        binding.editTextDescription.setText(toDoData?.description)
        registerEvents()

    }

    private fun registerEvents() {
        binding.buttonAddTask.setOnClickListener {
            val todoTask = binding.editTextTask.text.toString()
            val description = binding.editTextDescription.text.toString()
            if (todoTask.isNotEmpty()){
                if (toDoData ==null){
                    // send the text to listener
                    listener.onSaveTask(todoTask,description,binding.editTextTask)
                }else{
                    toDoData?.task = todoTask /* get the new value from ET (reWrite todoTask ) */
                    toDoData?.description = description /* get the new value from ET (reWrite todoTask ) */
                    listener.onUpdateTask(toDoData!!,binding.editTextTask,binding.editTextDescription)
                }

            }
            else{
                Snackbar.make(binding.buttonAddTask,"What do you think", Snackbar.LENGTH_SHORT).show()
            }
        }
        binding.closeIcon.setOnClickListener {
            dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.dialog_height)
        dialog?.window?.setLayout(width, height)
    }
    interface DialogInterFace{
        fun onSaveTask(task:String,description:String,todoEditText:TextInputEditText)
        fun onUpdateTask(todoData:ToDoData,todoEditText:TextInputEditText,descriptionEditText:TextInputEditText)
    }
}