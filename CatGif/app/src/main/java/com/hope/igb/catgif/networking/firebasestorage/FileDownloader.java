package com.hope.igb.catgif.networking.firebasestorage;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hope.igb.catgif.comman.BaseObservable;
import java.io.File;


/**
 * This class for downloading files from firebase storage to sd card
 */
public class FileDownloader extends BaseObservable<FileDownloader.FileDownloaderListener> {



    public interface FileDownloaderListener {
        void onDownloaded();
        void onFailedDownloaded(String error_message);
        void onProgressChanged(int progress);
    }


    private final StorageReference download_ref;


    public FileDownloader(String downloadUrl) {

        this.download_ref = FirebaseStorage.getInstance().getReferenceFromUrl(downloadUrl);
    }




    public void downloadGifToFile(File dest_file){
        download_ref.getFile(dest_file)



                .addOnSuccessListener(taskSnapshot -> {

                    //downloaded succeed
                    for (FileDownloaderListener listener : getListeners())
                        listener.onDownloaded();

                })




                .addOnFailureListener(e -> {

                    //downloaded failed
                    for (FileDownloaderListener listener : getListeners())
                        listener.onFailedDownloaded(e.getMessage());

                })



                .addOnProgressListener(snapshot -> {

                    //calculating progress percentage
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                    //listen for downloading progress
                    for (FileDownloaderListener listener: getListeners())
                        listener.onProgressChanged((int) progress);

                });



    }


}
