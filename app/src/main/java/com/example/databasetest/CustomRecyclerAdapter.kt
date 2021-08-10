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
import com.example.databasetest.database.User
import com.example.databasetest.view.DetailsFragment
import kotlinx.android.synthetic.main.activity_main.*


class CustomRecyclerAdapter(private val names: List<User>, context : Activity) :
                                    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    //регистрируем фрагмент менеджер из context (необязательный параметр, добавлен только ради ФМ)
    val fm: FragmentManager = (context as FragmentActivity).supportFragmentManager
    val tV : TextView = (context as FragmentActivity).statusTV

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.userTV?.text = names[position].toString()

        //временный(?) листенер
        holder.userTV?.setOnClickListener {

            //выводим Тоаст из контекста вьюхи
            Toast.makeText(holder.itemView.context, names[position].toString(), Toast.LENGTH_SHORT).show()

            //открываем фрагмент с подробностями, передаем данные о выбранном пользователе (names[position])
            fm.beginTransaction().replace(R.id.main_container, DetailsFragment(names[position]))
                .addToBackStack("")
                .commit()

            //дублируем позицию в логе
            Log.d("RecyclerView", "onClick：" + position)
        }

        //слушатель долгого нажатия
        holder.userTV?.setOnLongClickListener(){
            tV.text = names[position].toString()
            holder.userTV?.setTextColor(Color.BLUE)
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

}