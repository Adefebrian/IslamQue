package com.example.finalproject.activities

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject.R
import com.example.finalproject.adapter.DataAdapter
import com.example.finalproject.model.ModelData
import kotlinx.android.synthetic.main.activity_hukum.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.nio.charset.StandardCharsets

class HukumActivity : AppCompatActivity() {

    var dataAdapter: DataAdapter? = null
    var modelData: MutableList<ModelData> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hukum)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }


        dataAdapter = DataAdapter(modelData)

        recycler_view_hukum.layoutManager = LinearLayoutManager(this)
        //rvDM.setHasFixedSize(true)
        recycler_view_hukum.adapter = dataAdapter

        getDM()
    }

    private fun getDM() {
        try {
            val stream = assets.open("hukum.json")
            val size = stream.available()
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()
            val strResponse = String(buffer, StandardCharsets.UTF_8)
            try {
                val jsonArray = JSONArray(strResponse)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val dataModel = ModelData()
                    dataModel.id = jsonObject.getString("id")
                    dataModel.name = jsonObject.getString("name")
                    dataModel.arab = jsonObject.getString("arab")
                    dataModel.task = jsonObject.getString("task")
                    modelData.add(dataModel)
                }
                dataAdapter?.notifyDataSetChanged()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } catch (ignored: IOException) {
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val window = activity.window
            val layoutParams = window.attributes
            if (on) {
                layoutParams.flags = layoutParams.flags or bits
            } else {
                layoutParams.flags = layoutParams.flags and bits.inv()
            }
            window.attributes = layoutParams
        }
    }

}
