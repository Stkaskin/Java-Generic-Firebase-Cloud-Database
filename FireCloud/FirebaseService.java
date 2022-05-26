package com.stkaskin.restaurantmanager.FireCloud;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;


public class FirebaseService {
   /* public static <T> ArrayList<T> ReadData(T obj) {
        QuerySnapshot snapshots = ReadDataO(obj);
        ArrayList<T> list = new ArrayList<>();
        for (DocumentSnapshot snap : snapshots.getDocuments()) {
            list.add(DataService.ConvertData(obj, snap));
        }
        return list;
    }*/


    public static <T> ArrayList<T> ReadData(T obj) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String tablename = DataService.TableNameGet(obj);
        ArrayList<T> arrayList = new ArrayList<>();
        Task<QuerySnapshot> task = db.collection(tablename)
                .get();
        for (int i = 0; i < 150; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (task.isSuccessful()) {
                Log.d("TAMAMLANDI", "TAMAMLANDI");
                arrayList = DataService.ConvertData(obj, task.getResult().getDocuments());

                break;
            } else if (task.isCanceled()) {
                Log.d("Error", "Error");
                break;
            } else if (task.isComplete()) {
                task.getResult();
                Log.d("Bitti", "Bitti");
                break;
            }

        }
        return arrayList;

    }


    public static <T> String AddData(T obj) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = DataService.ConvertDataForObjectList(obj);
        String tablename = DataService.TableNameGet(obj);
// Add a new document with a generated ID
        Task<DocumentReference> referenceTask = db.collection(tablename).add(data);
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (referenceTask.isSuccessful()) {
                Log.d("Eklendi", referenceTask.getResult().getId());
                break;
            }
            else if (referenceTask.isComplete()) {
                Log.d("Tamamlandı", referenceTask.getResult().getId());
                break;
            }
        }
        db.collection(tablename)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println(documentReference.getId() + "");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        return "";
    }

}
