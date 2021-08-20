package com.example.databasetest.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.databasetest.database.AppDatabase
import com.example.databasetest.CustomRecyclerAdapter
import com.example.databasetest.Helper
import com.example.databasetest.R
import com.example.databasetest.ViewModel
import com.example.databasetest.database.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //лениая инициалиация модели
    //private val viewModel by lazy {ViewModelProvider(this).get(ViewModel::class.java)}

    private var userList : List<User> = listOf()

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
        val dbThread = Thread {
            val userDao = db.userDao()
            userList = userDao.getAll()
        }
        dbThread.start()
        dbThread.join()

        //регистрируем адаптер и привязываем его к списку польователей
        val recyclerAdapter = CustomRecyclerAdapter(userList)
        recyclerView.adapter = recyclerAdapter

        //реализуем листенеры через интерфейс ресайклерАдаптера
        recyclerAdapter.onUserSelectedCallback =
            object : CustomRecyclerAdapter.UserSelectedCallback {
                override fun onUserSelected(user: User) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, DetailsFragment(user))
                        .addToBackStack("")
                        .commit()
                }

                override fun onUserLongSelected(user: User) {
                    val dbThread = Thread {
                        db.userDao().delete(user)
                        userList = db.userDao().getAll()
                    }
                    dbThread.start()
                    dbThread.join()
                    val recyclerAdapter = CustomRecyclerAdapter(userList)
                    recyclerView.adapter = recyclerAdapter
                }
            }

        //устанавливаем на вьюМодел слушателя/обзёрвера и метод с обработкой данных
        val viewModel = ViewModelProvider(this).get(ViewModel::class.java)
        val observer = Observer<User> { renderData(it, db) }
        viewModel.getData().observe(this, observer)

        setButtons(db)
    }

        //обрабатываем данные из слушателя
        private fun renderData(data: User, db: AppDatabase) {
            //временная переменная
            var userList: List<User> = listOf()

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
                    recyclerView.adapter = CustomRecyclerAdapter(userList)
                })
            }).start()
        }

    private fun setButtons(db: AppDatabase) {
        //выводим фрагмент AddFragment
        add.setOnClickListener(){
            supportFragmentManager.beginTransaction().replace(R.id.main_container, AddFragment())
                .addToBackStack("")
                .commit()
        }

        //обновляем список c примененным фильтром
        show.setOnClickListener(){
            var userList : List<User> = listOf()
            var noEmptyNames : List<User> = ArrayList()

            val dbThread = Thread {
                userList = db.userDao().getAll()
                //заполняем новый спискок с фильтром (имена "неПустые")
                noEmptyNames = userList.filter { it.name.isNotEmpty() }
                //noEmptyNames = userList.filter { it.name.contains("Ass") }

            }
            dbThread.start()
            dbThread.join()

            //устанавливаем список только с заполненными именами
            recyclerView.adapter = CustomRecyclerAdapter(noEmptyNames)

            //выводим сообщение из функции объекта Helper
            Toast.makeText(this, Helper.testFilter(), Toast.LENGTH_SHORT).show()
        }

        //удаляем все записи из списка
        delete.setOnClickListener(){
            var userList : List<User> = listOf()

            Thread {
                db.userDao().deleteAll()
                userList = db.userDao().getAll()
            }.start()

            recyclerView.adapter = CustomRecyclerAdapter(userList)

            //выводим сообщение из функции объекта Helper
            Toast.makeText(this, Helper.testDelete(), Toast.LENGTH_SHORT).show()
        }

        statusTV.setOnClickListener(){
            statusTV.text = ""
        }
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
