package com.example.cycle_tour_helper.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cycle_tour_helper.R;
import com.example.cycle_tour_helper.models.Route;
import com.example.cycle_tour_helper.utils.FirebaseCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RouteRepository {

    private static final String TAG = "RouteRepository";
    private FirebaseCallback firebaseCallback;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Inject
    public RouteRepository(){
        init();
    }

    public RouteRepository(FirebaseCallback firebaseCallback){
        this.firebaseCallback = firebaseCallback;
        init();
    }

    private void init(){
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.storageReference = firebaseStorage.getReference();
    }


    public void getFileFromFirebase(){
        Route searchedRoute = new Route();
        searchedRoute.setRouteTitle("Penine Way");
        searchedRoute.setRouteFileName("penineway.geojson");

        StorageReference penineRef = storageReference.child("midlands/penineway.geojson");

       penineRef.getStream( new StreamDownloadTask.StreamProcessor() {
           @Override
           public void doInBackground(StreamDownloadTask.TaskSnapshot taskSnapshot,
                                      InputStream inputStream) throws IOException {
               long totalBytes = taskSnapshot.getTotalByteCount();
               Log.i(TAG,"Bytes totalBytes: " + totalBytes);

               long bytesDownloaded = 0;

               byte[] buffer = new byte[1024];
               int size;

               while ((size = inputStream.read(buffer)) != -1) {
                   bytesDownloaded += size;
                   Log.i(TAG,"Bytes downloaded: " + bytesDownloaded);
               }
               inputStream.close();
               BufferedInputStream
               // Close the stream at the end of the Task
           }
       })
                .addOnSuccessListener(new OnSuccessListener<StreamDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(StreamDownloadTask.TaskSnapshot snapshot) {
                        Log.i(TAG, "loadJSONfromStorageFile success");
                        searchedRoute.setInputString(convertStreamToString(snapshot.getStream()));
                        firebaseCallback.onSuccessFileFound(searchedRoute);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "loadJSONfromStorageFile failed");
                        firebaseCallback.onFailedFileFound(e);
                    }
                });

        /*final long ONE_MEGABYTE = 1024 * 1024;
        penineRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Log.i(TAG, "loadJSONfromStorageFile success");
                Log.i(TAG, "Bytes length = " + bytes.length);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.i(TAG, "loadJSONfromStorageFile failed");
            }
        });*/
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
