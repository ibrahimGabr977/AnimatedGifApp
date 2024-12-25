package com.hope.igb.savvy.networking.firebasestorage

import android.os.Build
import android.os.Environment
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hope.igb.savvy.common.views.BaseObservable
import java.io.File

class PostDownloadUseCase(downloadUrl: String):
    BaseObservable<PostDownloadUseCase.DownloadListener>() {


    interface DownloadListener {
        fun onDownloaded(filePath: String)
        fun onFailedDownloaded(error_message: String?)
        fun onProgressChanged(progress: Int)
    }


    private val storageReference: StorageReference

    private val appFolder : File


    init {
        appFolder = baseFolder()
       storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUrl)
    }



    private fun baseFolder() : File{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Savvy")
        else
            File(Environment.getExternalStorageDirectory(), "Savvy")
    }






    fun downloadPost() {
        if(!appFolder.exists())
            appFolder.mkdir()

        val postFile = File(baseFolder(), "Savvy_${System.currentTimeMillis()}.gif")

        storageReference.getFile(postFile)
            .addOnSuccessListener {

                //downloaded succeed
                for (listener in getListeners()) listener.onDownloaded(postFile.path)
            }


            .addOnFailureListener { e: Exception ->

                //downloaded failed
                for (listener in getListeners()) listener.onFailedDownloaded(e.message)
            }


            .addOnProgressListener { snapshot: FileDownloadTask.TaskSnapshot ->

                //calculating progress percentage
                val progress =
                    100.0 * snapshot.bytesTransferred / snapshot.totalByteCount

                //listen for downloading progress
                for (listener in getListeners()) listener.onProgressChanged(progress.toInt())
            }
    }


}