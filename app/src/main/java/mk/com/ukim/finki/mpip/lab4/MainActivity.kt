package mk.com.ukim.finki.mpip.lab4

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import mk.com.ukim.finki.mpip.lab4.util.FactoryInjector
import mk.com.ukim.finki.mpip.lab4.viewmodel.AuthViewModel


class MainActivity : AppCompatActivity() {
    private val authViewModel: AuthViewModel by viewModels {
        FactoryInjector.getAuthViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_logout -> {
                authViewModel.signOutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}