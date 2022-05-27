package com.devprotocols.moonheart.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.kaopiz.kprogresshud.KProgressHUD
import java.io.File
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat


class BaseUtils {
    companion object{
        fun progressDialog(context: Context): KProgressHUD {
            return KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
        }

        fun getFileExtension(uri: Uri, context: Context): String{
            //Check uri format to avoid null
            val extension: String = when {
                uri.scheme.equals(ContentResolver.SCHEME_CONTENT) -> {
                    //If scheme is a content
                    val mime = MimeTypeMap.getSingleton();
                    mime.getExtensionFromMimeType(context.contentResolver.getType(uri)).toString();
                }
                else -> {
                    //If scheme is a File
                    //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
                    MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path!!)).toString());

                }
            }
            return extension
        }
        @SuppressLint("SimpleDateFormat")
        fun getTime(timeInMilli: Long): String{
            val sdf = SimpleDateFormat("hh:mm:aa")
            val date = Date(timeInMilli)
            return sdf.format(date)
        }
    }
}