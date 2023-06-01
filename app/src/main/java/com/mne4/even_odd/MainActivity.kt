package com.mne4.even_odd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    private var computerFragment: Fragment? = null
    private var userFragment: Fragment? = null
    private lateinit var computerWins: TextView
    private lateinit var userWins: TextView
    private lateinit var buttonStart: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        initFrame(R.id.frameForComputer)
        initFrame(R.id.frameForUser)
    }

    private fun initFrame (id: Int) {
        supportFragmentManager.beginTransaction()
            .replace(id, GameFieldFragment.newInstance()).commit()
    }

    override fun onResume() {
        super.onResume()
        // Инициализация элементов управления
        computerWins = findViewById(R.id.textViewComputerWins)
        userWins = findViewById(R.id.textViewUserWins)
        buttonStart = findViewById(R.id.buttonStart)


        computerFragment = supportFragmentManager.findFragmentById(R.id.frameForComputer)
        computerFragment?.view?.findViewById<TextView>(R.id.textViewTittle)?.text = "Компьютер"
        val cpuNumbers = arrayOf<TextView?>(computerFragment?.view?.findViewById(R.id.textViewFirst),
            computerFragment?.view?.findViewById(R.id.textViewSecond),
            computerFragment?.view?.findViewById(R.id.textViewThird))

        var userGroup = computerFragment?.view?.findViewById<RadioGroup>(R.id.radioGroup)

        userFragment = supportFragmentManager.findFragmentById(R.id.frameForUser)
        userFragment?.view?.findViewById<TextView>(R.id.textViewTittle)?.text = "Игрок"
        val userNumbers = arrayOf<TextView?>(userFragment?.view?.findViewById(R.id.textViewFirst),
            userFragment?.view?.findViewById(R.id.textViewSecond),
            userFragment?.view?.findViewById(R.id.textViewThird))

        var cpuGroup = userFragment?.view?.findViewById<RadioGroup>(R.id.radioGroup)
        cpuGroup?.forEach {
            it.isEnabled = false
        }

        userGroup?.setOnCheckedChangeListener { radioGroup, i ->
            buttonStart.isEnabled = true
            if (arrayOf(true, false).random()) {
                cpuGroup?.check(R.id.radioButtonEven)
            }
            else {
                cpuGroup?.check(R.id.radioButtonOdd)
            }
        }

        // Нажатие на "Старт"
        buttonStart.setOnClickListener {
            var sum = 0
            for (numb in cpuNumbers) {
                numb?.text = (0..9).random().toString()
                sum += numb?.text.toString().toInt()
            }
            val cpuTotal = computerFragment?.view?.findViewById<TextView>(R.id.textViewTotal)
            cpuTotal?.text = sum.toString()

            sum = 0
            for (numb in userNumbers) {
                numb?.text = (0..9).random().toString()
                sum += numb?.text.toString().toInt()
            }
            val userTotal =  userFragment?.view?.findViewById<TextView>(R.id.textViewTotal)
            userTotal?.text = sum.toString()

            // User result logic
            if ((userGroup?.checkedRadioButtonId == R.id.radioButtonEven &&
                cpuTotal?.text.toString().toInt() % 2 == 0) ||
                (userGroup?.checkedRadioButtonId == R.id.radioButtonOdd &&
                        cpuTotal?.text.toString().toInt() % 2 == 1 )) {
                userWins.text = (userWins.text.toString().toInt() + 1).toString()
                userWins.setBackgroundColor(getColor(R.color.green))
            }
            else {
                userWins.setBackgroundColor(getColor(R.color.red))
            }

            // CPU result logic
            if ((cpuGroup?.checkedRadioButtonId == R.id.radioButtonEven &&
                userTotal?.text.toString().toInt() % 2 == 0) ||
                (cpuGroup?.checkedRadioButtonId == R.id.radioButtonOdd &&
                        userTotal?.text.toString().toInt() % 2 == 1 ) ) {
                computerWins.text = (computerWins.text.toString().toInt() + 1).toString()
                computerWins.setBackgroundColor(getColor(R.color.green))
            }
            else {
                computerWins.setBackgroundColor(getColor(R.color.red))
            }
        }
    }
}