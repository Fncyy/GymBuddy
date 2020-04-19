package hu.bme.aut.android.gymbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var gso: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient
    private var account: GoogleSignInAccount? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener { registerClick() }
        btnLogin.setOnClickListener { loginClick() }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        if (firebaseUser != null) {
            startActivity(Intent(this@LoginActivity, GymActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        account = GoogleSignIn.getLastSignedInAccount(this)
    }

    private fun validateForm() = etEmail.validateNonEmpty() && etPassword.validateNonEmpty()

    private fun registerClick() {
        if (!validateForm())
            return
        showProgressDialog()
        firebaseAuth
            .createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener { result ->
                hideProgressDialog()
                val firebaseUser = result.user
                val profileChangeRequest = UserProfileChangeRequest.Builder()
                    .setDisplayName(firebaseUser?.email?.substringBefore('@'))
                    .build()
                firebaseUser?.updateProfile(profileChangeRequest)
                toast("Registration successful")
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()
                toast(exception.message)
            }
    }

    private fun loginClick() {
        if (!validateForm())
            return
        showProgressDialog()
        firebaseAuth
            .signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnSuccessListener {
                hideProgressDialog()
                startActivity(Intent(this@LoginActivity, GymActivity::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                hideProgressDialog()
                toast(exception.localizedMessage)
            }
    }
}

fun EditText.validateNonEmpty(): Boolean {
    if (text.isEmpty()) {
        error = "Required"
        return false
    }
    return true
}
