package com.example.databasetest

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.databasetest.database.AppDatabase
import com.example.databasetest.database.User
import com.example.databasetest.view.DetailsFragment
import com.example.databasetest.view.MainActivity
import com.example.databasetest.view.MyDialogFragment
import kotlinx.android.synthetic.main.activity_main.*


class CustomRecyclerAdapter(private val names: List<User>) :
                                    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    var onUserSelectedCallback : UserSelectedCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val id = names[position].id
        val name = names[position].name
        val lastName = names[position].lastName
        holder.userTV?.text = "ID $id, Name - $name, Last Name - $lastName"

        holder.userTV?.setOnClickListener {
            onUserSelectedCallback?.onUserSelected(names[position])
        }

        //слушатель долгого нажатия
        holder.userTV?.setOnLongClickListener(){
            onUserSelectedCallback?.onUserLongSelected(names[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var userTV: TextView? = null

        init {
            userTV = itemView.findViewById(R.id.userTV)
        }
    }

    interface UserSelectedCallback {
        fun onUserSelected(user: User)
        fun onUserLongSelected(user: User)
    }

}