package com.example.konekin

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.konekin.model.UserDetail
import com.example.konekin.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        val userId = intent.getIntExtra("USER_ID", -1)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvId = findViewById<TextView>(R.id.tvId)
        val tvAge = findViewById<TextView>(R.id.tvAge)
        val tvSalary = findViewById<TextView>(R.id.tvSalary)

        val client = ApiClient.getInstance()
        val response = client.getUserDetail(userId)

        response.enqueue(object : Callback<UserDetail> {

            override fun onResponse(
                call: Call<UserDetail>,
                response: Response<UserDetail>
            ) {
                if (response.isSuccessful && response.body() != null) {

                    val user = response.body()!!.data

                    tvName.text = "Name: ${user.employeeName}"
                    tvId.text = "ID: ${user.id}"
                    tvAge.text = "Age: ${user.employeeAge}"
                    tvSalary.text = "Salary: ${user.employeeSalary}"

                } else {
                    Toast.makeText(
                        this@UserDetailActivity,
                        "HTTP ${response.code()}: ${response.errorBody()?.string()}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<UserDetail>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@UserDetailActivity,
                    "Koneksi error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}