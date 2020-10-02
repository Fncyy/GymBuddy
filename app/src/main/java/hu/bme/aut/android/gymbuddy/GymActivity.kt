package hu.bme.aut.android.gymbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import hu.bme.aut.android.gymbuddy.model.*
import kotlinx.android.synthetic.main.activity_gym.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.nav_header_gym.view.*

class GymActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {
        val workouts: MutableList<Workout> = mutableListOf()
        val exercises: MutableList<Exercise> = mutableListOf()

        val onDataChangedListener: MutableList<(String) -> Unit> = mutableListOf()
        const val WORKOUTS = "workouts"
        const val EXERCISES = "exercises"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workouts.clear()
        exercises.clear()
        setContentView(R.layout.activity_gym)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_exercises, R.id.nav_progress
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        initDataListener()

        val headerView = navView.getHeaderView(0)
        headerView.headerEmail.text = email
        headerView.headerName.text = email!!.split("@").first()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        when (item.itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.nav_home)
            }
            R.id.nav_exercises -> {
                navController.navigate(R.id.nav_exercises)
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                BaseActivity.uid = null
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initDataListener() {
        FirebaseDatabase.getInstance()
            .getReference(uid!!).child("exercises")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(snapshot: DatabaseError) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val newExercise = snapshot.getValue(DbExercise::class.java)
                    if (newExercise != null) {
                        for (i in exercises.indices) {
                            if (exercises[i].id == snapshot.key) {
                                exercises[i].name = newExercise.name
                                exercises[i].muscleGroup = newExercise.muscleGroup
                                exercises[i].weightData = newExercise.weightData
                                exercises.sortBy { it.name }
                                return
                            }
                        }
                        exercises.add(
                            Exercise(
                                newExercise.name,
                                newExercise.muscleGroup,
                                newExercise.weightData,
                                snapshot.key
                            )
                        )
                        exercises.sortBy { it.name }
                        notifyListeners(EXERCISES)
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {}
            })

        FirebaseDatabase.getInstance()
            .getReference(uid!!).child(WORKOUTS)
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(snapshot: DatabaseError) {}

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val newWorkout = snapshot.getValue(DbWorkout::class.java)
                    if (newWorkout != null) {
                        val workout = Workout(newWorkout.name, snapshot.key)
                        newWorkout.exercises.forEach {
                            var complete = false
                            for (i in exercises.indices) {
                                if (exercises[i].id == it) {
                                    workout.exercises.add(exercises[i])
                                    complete = true
                                    break
                                }
                            }
                            if (!complete) {
                                val exercise = Exercise(
                                    name = "",
                                    muscleGroup = MuscleGroup.Arms,
                                    id = it
                                )
                                exercises.add(exercise)
                                workout.exercises.add(exercise)
                            }
                        }
                        workouts.add(workout)
                        workouts.sortBy { it.name }
                        notifyListeners(WORKOUTS)
                    }

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {}

            })
    }

    private fun notifyListeners(type: String) =
        onDataChangedListener.forEach { listener -> listener(type) }
}
