package hu.nagyi.ponggame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.nagyi.ponggame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //region VARIABLES

    private lateinit var binding: ActivityMainBinding

    //endregion

    //region METHODS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMainBinding.inflate(this.layoutInflater)
        this.setContentView(this.binding.root)

        this.binding.startBtn.setOnClickListener {
            this@MainActivity.binding.customPV.startGame()
        }

        this.binding.stopBtn.setOnClickListener {
            this@MainActivity.binding.customPV.stopGame()
        }
    }

    //endregion

}