package com.chesia.bangkitcapstoneproject.Adapter

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoItem(
    val photoBitmap: Bitmap,
    val photoDate: String,
    val photoTime: String,
) : Parcelable
