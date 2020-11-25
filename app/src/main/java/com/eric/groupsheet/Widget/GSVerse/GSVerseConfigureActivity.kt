package com.eric.groupsheet.Widget.GSVerse

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eric.groupsheet.R
import com.eric.groupsheet.base.listenClick
import kotlinx.android.synthetic.main.gsv_widget_configure.*
import kotlinx.android.synthetic.main.gsv_widget_configure.btn_create

class GSVerseConfigureActivity : AppCompatActivity() , GSVController{
    private val mGSVerseAdapter = GSVerseAdapter(this)
    lateinit var targetStyle : StyleClass
    var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    val styleList : MutableList<StyleClass> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)
        setContentView(R.layout.gsv_widget_configure)

        styleList.add(StyleClass("style1","科技前沿","冷色系\n/適合暗黑背景",R.drawable.icons8_info,false))
        styleList.add(StyleClass("style2","月桂沉眠","低彩度/沉穩/典雅",R.drawable.icons8_info,false))
        styleList.add(StyleClass("style3","活潑粉圓","撞色/搞怪",R.drawable.icons8_info,false))
        styleList.add(StyleClass("style4","鮮明極簡","百搭/無色",R.drawable.icons8_info,false))
        styleList.add(StyleClass("style5","邦托烏反","致敬銀翼殺手2049",R.drawable.icons8_info,false))
        styleList.add(StyleClass("style6","光輝燦爛","搶眼/明顯",R.drawable.icons8_info,false))
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)
        rv_gsv.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_gsv.layoutManager = mLayoutManager
        rv_gsv.adapter = mGSVerseAdapter


        mGSVerseAdapter.submitList(styleList)

        btn_create.listenClick {
            if(targetStyle == null){
                return@listenClick
            }
            val intent = intent
            val extras = intent.extras
            if (extras != null) {
                appWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
                )
            }
//            val action = intent.action
//
            //store widget in Sharepreferences with widgetId as key.
            val sov_id_file = this.getSharedPreferences("GSV_${targetStyle.styleId}_FILE", Context.MODE_PRIVATE).edit()
            sov_id_file.putInt(appWidgetId.toString(), appWidgetId)
            sov_id_file.apply()
            updateAppWidget_regular(this, appWidgetManager, appWidgetId)
            // If this activity was started with an intent without an app widget ID, finish with an error.
            if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish()
                return@listenClick
            }

            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }

    override fun storeStyle(style: StyleClass) {
        styleList.forEach {
            if(it.equals(style)){
                targetStyle = style
                it.styleSelect = true
            }
            else it.styleSelect = false
        }
        mGSVerseAdapter.notifyDataSetChanged()

    }
}