package com.chesia.bangkitcapstoneproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.RegisterResponse
import com.chesia.bangkitcapstoneproject.databinding.ActivityMainBinding
import com.chesia.bangkitcapstoneproject.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("name") != null){
                binding.edtRegisterNama.setText(intent.getStringExtra("name"))
        }
        if(intent.getStringExtra("email") != null){
            binding.edtRegisterEmail.setText(intent.getStringExtra("email"))
        }
        if(intent.getStringExtra("phone") != null){
            binding.edtRegisterEmail.setText(intent.getStringExtra("phone"))
        }

        binding.btRegister.setOnClickListener{
            hideSoftKeyboard(binding.root)
            setProgressBar(true)
            if(checkForm()){
                val client = ApiConfig.getApiService().register(
                    binding.edtRegisterNama.text.toString(),
                    binding.edtRegisterEmail.text.toString(),
                    binding.edtRegisterPassword.text.toString())

                client.enqueue(object : Callback<RegisterResponse>{
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if(response.isSuccessful && response.body()!= null){
                            setProgressBar(false)
                            Toast.makeText(this@RegisterActivity, "Account successfully registered!", Toast.LENGTH_SHORT).show()
                            intentMenu(binding.root)
                        }else{
                            Toast.makeText(this@RegisterActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        setProgressBar(false)
                        Toast.makeText(this@RegisterActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                        Log.d("NETWORK", "ERROR: ${t.message}")
                    }

                })
            }
        }
    }

    fun intentMenu(view: View) {
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun checkForm(): Boolean{
        if(binding.edtRegisterNama.text.isNotEmpty()
            && binding.edtRegisterEmail.text.isNotEmpty()
            && binding.edtRegisterPassword.text.isNotEmpty()
            && binding.edtRegisterPassword2.text.isNotEmpty()
            && binding.edtRegisterTelp.text.isNotEmpty())
            {
                if(binding.edtRegisterPassword.text.toString() == binding.edtRegisterPassword2.text.toString()){
                    return true
                }else{
                    setProgressBar(false)
                    Toast.makeText(this@RegisterActivity, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                    return false
                }

            }else{
                setProgressBar(false)
                Toast.makeText(this@RegisterActivity, "Please fill all forms!", Toast.LENGTH_SHORT).show()
                return false

        }

    }

    private fun hideSoftKeyboard(view: View){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun setProgressBar(loading: Boolean){
        if(loading){
            binding.pbRegister.visibility = View.VISIBLE
        }else{
            binding.pbRegister.visibility = View.GONE
        }
    }
}