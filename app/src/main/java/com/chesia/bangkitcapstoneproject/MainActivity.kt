package com.chesia.bangkitcapstoneproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
<<<<<<< Updated upstream
=======
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.LoginResponse
>>>>>>> Stashed changes
import com.chesia.bangkitcapstoneproject.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btLogin.setOnClickListener {
<<<<<<< Updated upstream
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
=======
            hideSoftKeyboard(binding.root)
            setProgressBar(true)
            if(binding.edtEmail.text.isNotEmpty() && binding.edtPassword.text.isNotEmpty()){
                val client = ApiConfig.getApiService().login(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
                client.enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        setProgressBar(false)
                        if(response.isSuccessful && response.body()!!.data != null){
                            val intent = Intent(this@MainActivity, HomepageActivity::class.java);
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@MainActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                        Log.d("NetworkError", "ERROR: ${t.message}")
                    }

                })
            }else{
                setProgressBar(false)
                Toast.makeText(this, "Tolong isi semua form!", Toast.LENGTH_SHORT).show()
            }

>>>>>>> Stashed changes
        }
//             val intent = Intent(this, HomepageActivity::class.java)
//             startActivity(intent)
//         }
    }

    fun intentRegister(view: View) {
        val intent = Intent(this@MainActivity, RegisterActivity::class.java)
        startActivity(intent);
    }
<<<<<<< Updated upstream
=======


    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        supportActionBar?.hide()

    }

    private fun hideSoftKeyboard(view: View){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setProgressBar(loading: Boolean){
        if(loading){
            binding.pbLogin.visibility = View.VISIBLE
        }else{
            binding.pbLogin.visibility = View.GONE
        }
    }
>>>>>>> Stashed changes
}