package com.example.todoii.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todoii.R
import com.example.todoii.adapter.ToDoAdapter
import com.example.todoii.databinding.FragmentHomeBinding
import com.example.todoii.util.ToDoData
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment: Fragment(),AddTODOFragment.DialogInterFace,
    ToDoAdapter.ToDoAdapterClicksInterface {

    private lateinit var authentication: FirebaseAuth
    private lateinit var dataBaseReference: DatabaseReference

    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private  var popUpFragment: AddTODOFragment?=null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mList: MutableList<ToDoData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()

    }
    private fun getDataFromFirebase() {
        dataBaseReference.addValueEventListener(object :ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (taskSnapshot in snapshot.children){
                    val title = taskSnapshot.child("title").value.toString()
                    val description  = taskSnapshot.child("description").value.toString()
                    val time = taskSnapshot.child("time").value.toString()
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it,title,description,time)
                    }
                    if (todoTask!=null){
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
                Snackbar.make(binding.addNotes,error.message, Snackbar.LENGTH_SHORT).show()
            }
        })

    }

    private fun registerEvents() {
        binding.addNotes.setOnClickListener {
            if (popUpFragment!=null){
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            }
            popUpFragment = AddTODOFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(
                childFragmentManager,
                AddTODOFragment.TAG
            )
        }
        binding.exit.setOnClickListener {
            performLogout()

        }

    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()
        navController.navigate(R.id.action_homeFragment_to_signInFragment)
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        authentication = FirebaseAuth.getInstance()
        dataBaseReference = FirebaseDatabase.getInstance().reference.child("Users")
            .child(authentication.currentUser?.uid.toString())

        mList = mutableListOf()
        adapter = ToDoAdapter(mList)
        adapter.setListener(this)
        binding.recycleView.adapter = adapter
    }
    override fun onSaveTask(task: String,description:String, todoEditText: TextInputEditText) {
        // first text
        val taskMap = mapOf(
            "title" to task,
            "description" to description,
            "time" to getCurrentTime()
        )

        dataBaseReference.push().setValue(taskMap).addOnCompleteListener{
            if (it.isSuccessful){
                Snackbar.make(binding.addNotes,"Saved successfully", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(binding.addNotes,it.exception?.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
            todoEditText.text = null
            popUpFragment!!.dismiss()
        }
    }

    private fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return timeFormat.format(currentTime)}

    override fun onUpdateTask(todoData: ToDoData, todoEditText: TextInputEditText,description: TextInputEditText) {
        // get the same id with new value to update
        val map = HashMap<String,Any>()
        map["title"] = todoData.task
        map["description"] = todoData.description
        dataBaseReference.child(todoData.taskId).updateChildren(map).addOnCompleteListener{
            if (it.isSuccessful){
                Snackbar.make(binding.addNotes,"Updated Task", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(binding.addNotes,it.exception?.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
            todoEditText.text = null
            description.text = null
            popUpFragment!!.dismiss()
        }

    }
    override fun onDeleteTask(task: ToDoData) {
        dataBaseReference.child(task.taskId).removeValue().addOnCompleteListener{
            if (it.isSuccessful){
                Snackbar.make(binding.addNotes,"Completed Successfully", Snackbar.LENGTH_SHORT).show()
            }else{
                Snackbar.make(binding.addNotes,it.exception?.message.toString(), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
    override fun onEditTask(task: ToDoData) {
        if (popUpFragment!=null){
            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
        }
        // task to update it will store it to fragment with id
        popUpFragment = AddTODOFragment.newInstance(task.taskId,task.task,task.description)
        popUpFragment!!.setListener(this)
        popUpFragment!!.show(childFragmentManager,AddTODOFragment.TAG)
    }
}