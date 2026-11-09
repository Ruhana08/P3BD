package com.example.konekin

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.konekin.databinding.ActivityMainBinding
import com.example.konekin.model.Users
import com.example.konekin.network.ApiClient
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(
                WindowInsetsCompat.Type.statusBars()
            )

            view.setPadding(
                view.paddingLeft,
                insets.top,
                view.paddingRight,
                view.paddingBottom
            )

            windowInsets
        }

        val client = ApiClient.getInstance()
        val response = client.getAllUsers()
        val userList = ArrayList<String>()

        response.enqueue(object : retrofit2.Callback<Users> {
            override fun onResponse(
                call: retrofit2.Call<Users>,
                response: Response<Users>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!.data

                    for (i in users) {
                        userList.add(i.employeeName)
                    }

                    val listAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        userList
                    )

                    binding.lvNama.adapter = listAdapter

                    binding.lvNama.setOnItemClickListener { _, _, position, _ ->
                        val selectedUser = users[position]

                        val intent = Intent(
                            this@MainActivity,
                            UserDetailActivity::class.java
                        )

                        intent.putExtra("USER_ID", selectedUser.id)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal mengambil data employee",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: retrofit2.Call<Users>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@MainActivity,
                    "Koneksi error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}
