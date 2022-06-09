@file:Suppress("DEPRECATION")
package com.example.mindtrip

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintStream
import java.net.HttpURLConnection
import java.net.URL


class MapActivity : AppCompatActivity(), LocationListener, GoogleMap.OnMarkerClickListener {

    var mGoogleMap: GoogleMap? = null
    var pBar : ProgressBar? = null
    var mLatitude = 0.0
    var mLongitude = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.map)
        val spinnerCari = findViewById<Spinner>(R.id.spnCari)
        pBar = findViewById(R.id.pBar)
        val fragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fragment!!.getMapAsync { googleMap ->
            mGoogleMap = googleMap
            initMap()
        }
        val myAdapter = ArrayAdapter(this@MapActivity,
            android.R.layout.simple_list_item_1,resources.getStringArray(R.array.cari_tempat))
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCari.adapter = myAdapter
        spinnerCari.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?,view:View,position:Int, l:Long){
                var xType = ""
                if(position==1) xType = "cafe" else if (position == 2) xType =
                    "park" else if (position == 3) xType = "amusement_park" else if (position == 4) xType = "tourist_attraction"

                if(position != 0){
                    val sb = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                            "location=" + mLatitude + "," + mLongitude + "&radius=5000" +  "&sensor=true" + "&types=" +
                            xType +"&key=" + "AIzaSyBl1cUMCuYATDsZkOAYL_V2M6qznle1fQU"
                    startProg()
                    PlacesTask().execute(sb)
                }

            }

            override fun onNothingSelected(adapterView: AdapterView<*>){}
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMap(){

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                115)
            return
        }
        if(mGoogleMap != null){
            startProg()
            mGoogleMap!!.isMyLocationEnabled = true
            val locationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val criteria = Criteria()
            val provider= locationManager.getBestProvider(criteria, true).toString()
            val location =
                locationManager.getLastKnownLocation(provider)
            if (location != null){
                onLocationChanged(location)
            }else stopProg()
            locationManager.requestLocationUpdates(provider,2000,10f, this)

            mGoogleMap!!.setOnMarkerClickListener(this)

        }
    }


    override fun onLocationChanged(location: Location) {
        mLatitude = location.latitude
        mLongitude = location.longitude
        val latLng = LatLng(mLatitude,mLongitude)
        mGoogleMap!!.moveCamera(
            CameraUpdateFactory.newLatLng(latLng)
        )
        mGoogleMap!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
        stopProg()
    }

    private fun stopProg(){
        pBar!!.visibility = View.GONE
    }

    private fun startProg(){
        pBar!!.visibility = View.VISIBLE
    }

    @Suppress("StaticFieldLeak")
    private inner class PlacesTask:AsyncTask<String?,Int?,String?>(){
        protected override fun doInBackground(vararg url: String?): String? {
            var data: String? = null
            try{
                data = downloadUrl(url[0].toString())
            }catch (e:Exception){
                stopProg()
                e.printStackTrace()
            }
            return data
        }

        override fun onPostExecute(result: String?) {
            ParserTask().execute(result)
        }
    }

    private fun downloadUrl(strUrl:String):String{
        var data = ""
        val iStream:InputStream
        val urlConnection: HttpURLConnection
        try{
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuilder()
            var line:String?
            while(br.readLine().also{ line = it } != null){
                sb.append(line)
            }
            data = sb.toString()
            br.close()
            iStream.close()
            urlConnection.disconnect()
        }catch (e:Exception){
            e.printStackTrace()
        }
        return data
    }

    @Suppress("StaticFieldLeak")
    private inner class ParserTask: AsyncTask<String?, Int?, List<HashMap<String,String>>?>(){
        var jObject: JSONObject?=null

        protected override fun doInBackground(vararg jsonData: String?): List<HashMap<String, String>>? {
            var places: List<HashMap<String, String>>?= null
            var parserPlace = ParserPlace()
            try{
                jObject = JSONObject(jsonData[0])
                places = parserPlace.parse(jObject!!)
            }catch (e:Exception){
                stopProg()
                e.printStackTrace()
            }
            return places

        }

        override fun onPostExecute(list: List<HashMap<String, String>>?) {
            mGoogleMap!!.clear()
            for(i in list!!.indices){
                val markerOptions = MarkerOptions()
                val hmPlace = list[i]
                val pinDrop = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                //BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_pin_drop_24)
                val lat = hmPlace["lat"]!!.toDouble()
                val lng = hmPlace["lng"]!!.toDouble()
                val name = hmPlace["place_name"]
                val address = hmPlace["vicinity"]
                val latLng = LatLng(lat,lng)
                markerOptions.icon(pinDrop)
                markerOptions.position(latLng)
                markerOptions.title(name)
                markerOptions.snippet(address)

                mGoogleMap!!.addMarker(markerOptions)
            }
            stopProg()
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val markerName = marker.title

        marker.showInfoWindow()
        mGoogleMap!!.moveCamera(
            CameraUpdateFactory.newLatLng(marker.position)
        )
        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {

            val builder: AlertDialog.Builder? = this?.let {
                AlertDialog.Builder(it)
            }

            builder!!.setMessage("여기에 가고 싶나요?")
                .setTitle(marker.title.toString())

            builder.apply {
                setPositiveButton("예") { dialog, id ->
                    val selectedId = id
                    writeFile(marker.title.toString())
                    // Log.d("TAG", marker.title.toString())
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                }
                setNegativeButton("아니요") { dialog, id ->
                    val selectedId = id

                }
            }
            val dialog: AlertDialog? = builder.create()

            dialog!!.show()
        }
        return false
    }

    private fun writeFile(place:String){
//        var name:String = ""
//        when(value){
//            1->name ="breakfast.txt"
//            2->name ="lunch.txt"
//            3->name ="dinner.txt"
//            4->name ="snack.txt"
//        }
//
        var type = ""
        val spinnerCari = findViewById<Spinner>(R.id.spnCari)
        when(spinnerCari.lastVisiblePosition){
            1-> type = "카페"
            2-> type = "공원"
            3-> type = "테마파크"
            4-> type = "관광지"
        }
        val output = PrintStream(openFileOutput("statistics.txt", MODE_APPEND))
        output.println(place)
        output.println(type)
        output.close()
    }

}