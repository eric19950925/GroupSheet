package com.eric.groupsheet.EditSheet.EditRule

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TextRecogCamViewModel:ViewModel() {
    var image_uri = MutableLiveData<Uri>()
}