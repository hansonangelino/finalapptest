package tw.edu.pu.o10930020.flipcard

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import kotlin.random.Random


class MainActivity : AppCompatActivity() {
    var db = FirebaseFirestore.getInstance()

    var user = hashMapOf(
        "first" to "Ada",
        "last" to "Lovelace",
        "born" to 1815
    )

    lateinit var text_player1 : TextView
    lateinit var text_player2 : TextView

    lateinit var iv_card1: ImageView
    lateinit var iv_card2: ImageView

    lateinit var round_text: TextView

    lateinit var tv_war: TextView

    lateinit var b_deal: Button

    lateinit var random: Random
    lateinit var record:Button
    lateinit var txv:TextView

    var player1 = 0
    var player2 = 0
    var round = 0

    var cardsArray = intArrayOf(
        R.drawable.spades2,
        R.drawable.spades3,
        R.drawable.spades4,
        R.drawable.spades5,
        R.drawable.spades6,
        R.drawable.spades7,
        R.drawable.spades8,
        R.drawable.spades9,
        R.drawable.spades10,
        R.drawable.spadesjack,
        R.drawable.spadesqueen,
        R.drawable.spadesking,
        R.drawable.spadesace,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        random = Random

        iv_card1 = findViewById(R.id.iv_card1)
        iv_card2 = findViewById(R.id.iv_card2)

        iv_card1.setImageResource(R.drawable.back)
        iv_card2.setImageResource(R.drawable.back)

        text_player1 = findViewById(R.id.tv_player1)
        text_player2 = findViewById(R.id.tv_player2)

        val player1_name = intent.getStringExtra("player1")
        val player2_name = intent.getStringExtra("player2")

        round_text = findViewById(R.id.round_text)

        tv_war = findViewById(R.id.tv_war)
        tv_war.visibility = View.INVISIBLE

        b_deal = findViewById(R.id.b_deal)
        record=findViewById(R.id.record_act_btn)
        txv=findViewById(R.id.txv)

        //Button
        b_deal.setOnClickListener{
            tv_war.visibility = View.INVISIBLE

            //Generate card
            val card1 = random.nextInt(cardsArray.size)
            val card2 = random.nextInt(cardsArray.size)

            //Set Round Count
            round++
            round_text.text = "Round Played: $round"

            if(round == 7){
                b_deal.isEnabled = false
                round_text.visibility = View.INVISIBLE
                tv_war.visibility = View.VISIBLE

                if (player1 > player2){
                    tv_war.text = "WINNER : " + player1_name
                    //Upload to database

                    user = hashMapOf(
                        "WINNER" to text_player1.text.toString(),
                    )
                    db.collection("winner")
                        //.add(user)
                        .document(text_player1.text.toString())
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Game saved",
                                Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Game failed to save :" + e.toString(),
                                Toast.LENGTH_LONG).show()
                        }

                } else if(player2 > player1){
                    tv_war.text = "WINNER : " + player2_name
                    //Upload to database

                    user = hashMapOf(
                        "WINNER" to text_player2.text.toString(),
                    )
                    db.collection("winner")
                        //.add(user)
                        .document(text_player2.text.toString())
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Game saved",
                                Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Game failed to save :" + e.toString(),
                                Toast.LENGTH_LONG).show()
                        }
                }
            }

            //Set Image
            setCardImage(card1, iv_card1)
            setCardImage(card2, iv_card2)

            //Compare war label
            if (card1 > card2){
                player1++
                text_player1.text = player1_name + " : $player1"

            } else if(card1 < card2){
                player2++
                text_player2.text = player2_name + " : $player2"
            } else {
                player1++
                player2++
                text_player1.text = player1_name + " : $player1"
                text_player2.text = player2_name + " : $player2"
                tv_war.visibility = View.VISIBLE
            }

            record.setOnClickListener({
                db.collection("winner")
                    //.whereEqualTo("名字", binding.edtName.text.toString())
                    //.orderBy("WINNNER")
                    .whereLessThan("WINNER",text_player2.text.toString())
                    .limit(3)

                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            var msg: String = ""
                            for (document in task.result!!) {
                                msg += "winner：" + document.id + "\n\n"
                            }
                            if (msg != "") {
                                txv.text = msg
                            } else {
                                txv.text = "查無資料"
                            }
                        }
                    }

            })
        }
    }

    private fun setCardImage(number: Int, image: ImageView){
        image.setImageResource(cardsArray[number])
    }
}