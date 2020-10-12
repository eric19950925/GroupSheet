package com.eric.groupsheet.EditSheet.EditRule

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.eric.groupsheet.R
import com.eric.groupsheet.base.BaseFragment
import com.eric.groupsheet.base.observe
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextBlock
import com.google.android.gms.vision.text.TextRecognizer
import kotlinx.android.synthetic.main.fragment_text_recognizer_camera.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


class TextRecogCam :BaseFragment(){
//    var image_uri: Uri? = null
    private val viewModel by viewModel<TextRecogCamViewModel>()
    private fun checkRequiredPermissions(): Boolean {
        val deniedPermissions = mutableListOf<String>()
        for (permission in REQUIRED_PERMISSIONS) {
            if (context?.let { ContextCompat.checkSelfPermission(it, permission) } == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission)
            }
        }
        if (deniedPermissions.isEmpty().not()) {
            requestPermissions(deniedPermissions.toTypedArray(), REQUEST_PERMISSION_CODE)
        }
        return deniedPermissions.isEmpty()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_text_recognizer_camera

    override fun initData() {
    }

    override fun initObserver() {
        observe(viewModel.image_uri){
            if( it != null ){
                image_view.setImageURI(it)
            }
            else {
                image_view.setImageResource(R.drawable.icons8_camera_96)
                recogText.setText("")
            }
        }
    }

    override fun initView() {
        if(!checkRequiredPermissions()){
            Toast.makeText(context,"未開啟權限",Toast.LENGTH_LONG)
        }
        else{
            openCamera()
        }
        btn_ok.setOnClickListener {

        }
        btn_cancel.setOnClickListener {
            viewModel.image_uri.value?.let { context?.getContentResolver()?.delete(it, "", null) }
            viewModel.image_uri.value = null
            openCamera()
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        viewModel.image_uri.value = context?.getContentResolver()?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, viewModel.image_uri.value)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //called when image was captured from camera intent
        if (resultCode == Activity.RESULT_OK){
            //set image captured to image view
            image_view.setImageURI(viewModel.image_uri.value)
//            detect()
            detectOCR()
        }
    }

    private fun detectOCR() {
        val assetManager: AssetManager = context?.assets!!
        var inFile: InputStream? = null
        var fileExistFlag = false
        dstPathDir = context?.filesDir.toString() + dstPathDir
        val dstInitPathDir = DATA_PATH + "/tesseract"
        val dstPathFile = dstPathDir + DEFAULT_LANGUAGE
        var outFile: FileOutputStream? = null
//        val bitmap = (image_view.getDrawable() as BitmapDrawable).bitmap
        val bitmap =
            MediaStore.Images.Media.getBitmap(context?.getContentResolver(), viewModel.image_uri.value)

        class ProgressTask: AsyncTask<Void, Void, String>() {
            val progressDialog : Dialog = ProgressDialog(context)
            private val TIMEOUT_CONNECTION = 5000 //5sec
            private val TIMEOUT_SOCKET = 30000 //30sec
            override fun onPreExecute() {
                progressDialog.setTitle("Downloading...")
                progressDialog.show()
                inFile = assetManager.open(DEFAULT_LANGUAGE_FILE)

                val f = File(dstPathDir)

                if (!f.exists()) {
                    if (!f.mkdirs()) {
                        Log.d("TAG",DEFAULT_LANGUAGE_FILE + " can't be created.")
                    }
                    outFile = FileOutputStream(File(dstPathFile))
                } else {
                    fileExistFlag = true
                }
            }
            override fun onPostExecute(result: String?) {
                progressDialog.dismiss()
            }
            override fun doInBackground(vararg p0: Void?): String? {
//                val tessBaseAPI = TessBaseAPI()
//                if (fileExistFlag) {
//                    try {
//                        if (inFile != null) inFile!!.close()
//                        tessBaseAPI.init(dstInitPathDir, DEFAULT_LANGUAGE)
//                        Log.d("TAG","ready to set img")
//                        tessBaseAPI.setImage(bitmap)
//                        val text = tessBaseAPI.getUTF8Text()
//
//                        tessBaseAPI.end()
//                        Log.i("TEXT:",text)
//                        activity?.runOnUiThread {
//                        recogText.setText(text)}
//                        return "Executed"
//                    } catch (ex: Exception) {
//                        Log.d("TAG","error")
//                        Log.e(TAG, ex.message)
//                    }
//                }
//
//                if (inFile != null && outFile != null) {
//                    try {
//                        //copy file
//                        val buf = ByteArray(1024)
//                        var len: Int
//                        while (inFile!!.read(buf).also { len = it } != -1) {
//                            outFile!!.write(buf, 0, len)
//                        }
//                        inFile!!.close()
//                        outFile!!.close()
//                        tessBaseAPI.init(dstInitPathDir, DEFAULT_LANGUAGE)
//                    } catch (ex: Exception) {
//                        Log.e(TAG, ex.message)
//                    }
//                } else {
//                    Log.d("TAG",DEFAULT_LANGUAGE_FILE + " can't be read.")
//                }
//
//
//
////                tessBaseAPI.init(TESSBASE_PATH, DEFAULT_LANGUAGE)
////                tessBaseAPI.setImage(bitmap)
////                val text = tessBaseAPI.getUTF8Text()
////
////                tessBaseAPI.end()
////                Log.i("TEXT:",text)
//////                if(DEFAULT_LANGUAGE.equalsIgnoreCase("eng")){
//////                    outputText = outputText.replaceAll("[^a-zA-Z0-9]+", " ");
//////                }
////                recogText.setText(text)
                return "Executed"

            }
//
        }
        val progressTask = ProgressTask()
        progressTask.execute()
    }

    private fun detect() {

        //TODO 1. define TextRecognizer
        val recognizer: TextRecognizer = TextRecognizer.Builder(context).build()

        //TODO 2. Get bitmap from imageview
        val bitmap = (image_view.getDrawable() as BitmapDrawable).bitmap

        //TODO 3. get frame from bitmap
        val frame: Frame = Frame.Builder().setBitmap(bitmap).build()

        //TODO 4. get data from frame
        val sparseArray: SparseArray<TextBlock> = recognizer.detect(frame)

        //TODO 5. set data on textview
        val stringBuilder = StringBuilder()

        for (i in 0 until sparseArray.size()) {
            val tx: TextBlock = sparseArray[i]
            val str: String = tx.getValue()
            stringBuilder.append(str)
        }

        recogText.setText(stringBuilder)
    }

    companion object init{
        fun newInstance() = TextRecogCam()
        private const val REQUEST_PERMISSION_CODE: Int = 1
        private val IMAGE_CAPTURE_CODE = 1001
        private const val TAG = "IMAGETT"
        private const val DEFAULT_LANGUAGE_FILE = "eng.traineddata"
        private const val DEFAULT_LANGUAGE = "eng"
        var dstPathDir = "/tesseract"
        private val text: String? = null
        private val TESSBASE_PATH = "/tessdata"
        private val DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/tesseract"
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }


    override fun onFragmentShow() {
        if( viewModel.image_uri.value != null ){
            image_view.setImageURI(viewModel.image_uri.value)
        }
        else image_view.setImageResource(R.drawable.icons8_camera_96)
        super.onFragmentShow()
    }

}