package com.stkaskin.dininghallsurvey.FireCloud;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FirebaseServiceSubset extends FirebaseService {


    private static CollectionReference RCollection(String doc, String[] ids) {
        String[] as = doc.split("/");
        CollectionReference reference = db.collection(as[0]);
        for (int i = 1; i < as.length; i++) {
            reference = reference.document(ids[i-1]).collection(as[i]);
        }
        return reference;
    }

    public static Query QueryCreate(Class clazz, @NonNull String... collectionDocumentIds) {
        RCollection(getTableName(clazz), collectionDocumentIds);
        return db.collection(getTableName(clazz));

    }
    public static <T> ArrayList<T> GetForSubSet(Class clazz, Query query) {
        Task<QuerySnapshot> task = query.get();
        if (TaskWait(task))
            return TaskCastTList(clazz,task);
        return null;
    }
    public static <T> ArrayList<T> GetForSubSet(Class clazz,String... collectionDocumentIds) {
        Task<QuerySnapshot> task = RCollection(getTableName(clazz), collectionDocumentIds).get();
        if (TaskWait(task))
            return TaskCastTList(clazz,task);
        return null;
    }

    public static <T> T GetForSubSet(Class clazz,String id ,String... collectionDocumentIds) {
        Task<DocumentSnapshot> task = RCollection(getTableName(clazz), collectionDocumentIds).document(id).get();
        if (TaskWait(task)) {
            T obj = (T) task.getResult().toObject(clazz);
            return documentIdSet(obj, task.getResult().getId());
        }
        return null;
    }
    public static <T> String AddForSubSet(T obj,String... documentIds ) {
        Map<String, Object> data = DataService.ConvertDataForObjectList(obj);
        Task<DocumentReference> referenceTask = RCollection(getTableName(obj.getClass()),documentIds).add(obj);
        String id = "";
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (referenceTask.isSuccessful()) {
                Log.d("Eklendi", referenceTask.getResult().getId());
                id = referenceTask.getResult().getId();
                return referenceTask.getResult().getId();
            } else if (referenceTask.isComplete()) {
                Log.d("Tamamlandı", referenceTask.getResult().getId());
                id = referenceTask.getResult().getId();
                return referenceTask.getResult().getId();
            }
        }
        return id;
    }
}
