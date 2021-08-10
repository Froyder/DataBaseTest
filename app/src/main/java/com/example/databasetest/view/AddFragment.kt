package com.example.databasetest.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.databasetest.R
import com.example.databasetest.ViewModel
import com.example.databasetest.database.User
import kotlinx.android.synthetic.main.activity_main.add
import kotlinx.android.synthetic.main.add_note_layout.*
import androidx.core.content.ContextCompat.getSystemService




class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_note_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //получаем viewModel из активити
        val viewModel = activity?.let { ViewModelProvider(it).get(ViewModel::class.java) }

        //добавляем нового пользователя
        add.setOnClickListener(){
            val newName = name.text.toString()
            val newLastName = surname.text.toString()
            viewModel?.addUser(User(0, newName, newLastName))

            //прячем кавиатуру после получения данных о пользователе
            val keyboard =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.hideSoftInputFromWindow(name.getWindowToken(), 0)

            activity?.supportFragmentManager?.popBackStack()
        }

    }
}