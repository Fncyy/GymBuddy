package hu.bme.aut.android.gymbuddy

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

abstract class BaseActivity : AppCompatActivity() {

    private val firebaseUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    protected val uid: String?
        get() = firebaseUser?.uid

    protected val userName: String?
        get() = firebaseUser?.displayName

    protected val userEmail: String?
        get() = firebaseUser?.email

    fun showProgressDialog() {
        loginProgressBar.visibility = View.VISIBLE
    }

    protected fun hideProgressDialog() {
        loginProgressBar.visibility = View.INVISIBLE
    }

    protected fun toast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}