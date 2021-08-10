package com.example.databasetest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.databasetest.R
import com.example.databasetest.database.User
import kotlinx.android.synthetic.main.details_layout.*

class DetailsFragment(user: User) : Fragment () {

    private val userDetails = user

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //делаем фон кликабельным, перекрывая элементы из MainActivity
        details_container.isClickable = true

        details_name.text = userDetails.name
        details_lastName.text = userDetails.lastName

        back.setOnClickListener(){
            activity?.supportFragmentManager?.popBackStack()
        }

    }

}