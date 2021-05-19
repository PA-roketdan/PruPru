package org.techtown.prupru

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class CameraRequest: AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg params: String): Bitmap? {
        var bitmap: Bitmap? = null
        var imgName: String = params[0]
        try{
            var url: URL = URL("http://{server_address}/images/$imgName") // get image url
            var conn: HttpURLConnection = url.openConnection() as HttpURLConnection // connect
            conn.doInput = true
            conn.connect()

            var ins: InputStream = conn.inputStream
            bitmap = BitmapFactory.decodeStream(ins)
        } catch(e: IOException){
            println(params)
            e.printStackTrace()
        }

        return bitmap
    }
}