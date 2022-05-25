package com.chesia.bangkitcapstoneproject

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.chesia.bangkitcapstoneproject.Local.UserPreferences
import com.chesia.bangkitcapstoneproject.Networking.ApiConfig
import com.chesia.bangkitcapstoneproject.Networking.LoginResponse
import com.chesia.bangkitcapstoneproject.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mPreferences: UserPreferences
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPreferences = UserPreferences(this);

        if(mPreferences.getToken() != ""){
            startActivity(Intent(this@MainActivity, HomepageActivity::class.java));
            finish()
        }
        Log.d("mToken", mPreferences.getToken());

        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = Firebase.auth

        binding.btGooglesignin.setOnClickListener {
            signIn()
            Log.d(TAG, "Button clicked")
        }


        binding.btLogin.setOnClickListener {
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
                            mPreferences.setToken(response.body()!!.data!!.token)
                            Log.d("respToken", response.body()!!.data!!.token)
                            val intent = Intent(this@MainActivity, HomepageActivity::class.java);
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@MainActivity, "Email or password wrong", Toast.LENGTH_SHORT).show()
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
        }

        setupView()
    }


    fun intentRegister(view: View) {
        val intent = Intent(this@MainActivity, RegisterActivity::class.java)
        startActivity(intent);
    }

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

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }else{
            Log.d(TAG, "firebaseAuthWithGoogle:" + result.resultCode.toString())
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    user!!.getIdToken(true).addOnSuccessListener { result ->
                        val idToken = result.token
                        val client = ApiConfig.getApiService().loginwithgoogle(user!!.email.toString(), idToken!!)
                        client.enqueue(object : Callback<LoginResponse>{
                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                setProgressBar(false)
                                if(response.isSuccessful && response.body()!!.data != null){
                                    mPreferences.setToken(response.body()!!.data!!.token)
                                    Log.d("respToken", response.body()!!.data!!.token)
                                    Log.d("GTOKEN", idToken)
                                    val intent = Intent(this@MainActivity, HomepageActivity::class.java);
                                    startActivity(intent)
                                    finish()
                                }else{
                                    Log.d("GOOGLETOKEN", idToken)
                                    Log.d("GMAIL", user!!.email.toString())
                                    Toast.makeText(this@MainActivity, "Email or password wrong", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@MainActivity, "An error occurred", Toast.LENGTH_SHORT).show()
                                Log.d("NetworkError", "ERROR: ${t.message}")
                            }

                        })
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@MainActivity, HomepageActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}