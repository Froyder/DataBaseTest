package com.example.databasetest.view

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.databasetest.database.AppDatabase
import com.example.databasetest.CustomRecyclerAdapter
import com.example.databasetest.R
import com.example.databasetest.ViewModel
import com.example.databasetest.database.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //лениая инициалиация модели
    //private val viewModel by lazy {ViewModelProvider(this).get(ViewModel::class.java)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //получаем экземпляр ДБ
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "UserList"
        ).build()

        //регистрируем ресайклер
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //получаем данные из ДБ и выводим их на вьюхи
        Thread {
            val userDao = db.userDao()
            val users: List<User> = userDao.getAll()
            recyclerView.adapter = CustomRecyclerAdapter(users, this)
        }.start()

        //устанавливаем на вьюМодел слушателя/обзёрвера и метод с обработкой данных
        val viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        val observer = Observer<User> { renderData(it, db) }
        viewModel.getData().observe(this, observer)

        //выводим фрагмент AddFragment
        add.setOnClickListener(){
            supportFragmentManager.beginTransaction().replace(R.id.main_container, AddFragment())
                .addToBackStack("")
                .commit()
        } //обновляем список
        
        show.setOnClickListener(){
            this.recreate()
        }

        //удаляем все записи из списка
        delete.setOnClickListener(){
            var userList : List<User> = listOf()

            Thread {
                db.userDao().deleteAll()
                userList = db.userDao().getAll()
            }.start()

            recyclerView.adapter = CustomRecyclerAdapter(userList, this)
        }
    }

    //обрабатываем данные из слушателя
    private fun renderData(data: User, db: AppDatabase) {
        //временная переменная
        var userList : List<User> = listOf()

        //записываем нового пользователя в ДБ в новом ПРОИМЕНОВАННОМ потоке
        val dbThread = Thread() {
            db.userDao().addUser(data)
            userList = db.userDao().getAll()
        }
        dbThread.start()

        //ждем завершения работы потока с ДБ
        dbThread.join()

        //обновляем вьюхи в основном потоке
        Thread(Runnable {
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                recyclerView.adapter = CustomRecyclerAdapter(userList, this)
            })
        }).start()
    }

    //сохраняем текст из вьюхи перед пересозданием
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.run {
            putString("KEY", statusTV.text.toString())
        }
    }

    //загружаем текст во вьюху после пересоздания
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        statusTV.text = savedInstanceState.getString("KEY")
    }
}