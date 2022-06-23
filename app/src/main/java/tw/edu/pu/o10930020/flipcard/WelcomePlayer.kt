package tw.edu.pu.o10930020.flipcard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class WelcomePlayer : AppCompatActivity() {

    private lateinit var btnSendData : Button
    private lateinit var editPlayer1 : EditText
    private lateinit var editPlayer2 : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_player)

        btnSendData = findViewById(R.id.btn_sendData)
        editPlayer1 = findViewById(R.id.et_player1)
        editPlayer2 = findViewById(R.id.et_player2)

        btnSendData.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java)
                .putExtra("player1", editPlayer1.text.toString())
                .putExtra("player2", editPlayer2.text.toString()))
        }
    }
}