package com.example.mindtrip

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mindtrip.databinding.ActSelectBinding
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActSelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()


    }

    fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            System.out.println(sharedText);

            val coder = Geocoder(this);
            val address: List<Address>

            try {

                address = coder.getFromLocationName(sharedText, 5);
                val location = address.get(0);
                val lat = location.getLatitude()
                val lng = location.getLongitude();

            } catch (e: IOException) {
                e.printStackTrace();
            }
        }
    }

    private fun init() {
        with(binding) {
            cafebtn.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:0,0?q=카페")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

                startActivity(mapIntent)
//                val intent = getIntent()
                val action = mapIntent.getAction()
                val type = mapIntent.getType()

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        handleSendText(mapIntent); // Handle text being sent
                    }
                }
            }
            parkbtn.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:0,0?q=공원")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(mapIntent)
                val intent = getIntent()
                val action = intent.getAction()
                val type = intent.getType()

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        handleSendText(intent); // Handle text being sent
                    }
                }
            }
            themebtn.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:0,0?q=테마파크")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(mapIntent)
                val intent = getIntent()
                val action = intent.getAction()
                val type = intent.getType()

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        handleSendText(intent); // Handle text being sent
                    }
                }
            }
            landmarkbtn.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:0,0?q=관광지")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                startActivity(mapIntent)
                val intent = getIntent()
                val action = intent.getAction()
                val type = intent.getType()

                if (Intent.ACTION_SEND.equals(action) && type != null) {
                    if ("text/plain".equals(type)) {
                        handleSendText(intent); // Handle text being sent
                    }
                }
            }
        }
    }
}
