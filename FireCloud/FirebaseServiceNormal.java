package com.stkaskin.dininghallsurvey.FireCloud;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FirebaseServiceNormal extends FirebaseService {

    public static <T> ArrayList<T> Get(@NonNull Class<T> clazz) {
        Task<QuerySnapshot> task = db.collection(getTableName(clazz)).get();
        if (TaskWait(task))
            return TaskCastTList(clazz, task);
        return null;
    }

    public static <T> T Get(Class clazz, String id) {
        Task<DocumentSnapshot> task = db.collection(getTableName(clazz)).document(id).get();
        if (TaskWait(task)) {
            T obj = (T) task.getResult().toObject(clazz);
            return documentIdSet(obj, task.getResult().getId());
        }
        return null;
    }


    public static <T> ArrayList<T> Get(Class clazz, Query query) {
        Task<QuerySnapshot> task = query.get();
        if (TaskWait(task))
            return TaskCastTList(clazz, task);
        return null;
    }

    public static Query QueryCreate(Class clazz) {
        return db.collection(getTableName(clazz));
    }



    public static <T> String Add(T obj) {
        Map<String, Object> data = DataService.ConvertDataForObjectList(obj);
// Add a new document with a generated ID
        Task<DocumentReference> referenceTask = db.collection(getTableName(obj.getClass())).add(obj);
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


    @SuppressLint("NewApi")
    public static boolean UpdateData(Object obj) {

        String documentId = DataService.getIdData(obj);
        if (documentId.length() < 1) {
            Log.d("Update", "Failed The Update Operation Need id field ,getId method and  setId method");
            return false;
        }


        Map<String, Object> data = DataService.ConvertDataForObjectList(obj);
        Map<String, Object> dataf = new HashMap<>();
        dataf.putAll(data);
        for (Map.Entry<String, Object> entry : dataf.entrySet())
            if (entry.getValue() == null)
                data.remove(entry.getKey(), entry.getValue());
        Task<Void> referenceTask = db.collection(getTableName(obj.getClass())).document(documentId).update(data);
        return TaskWait(referenceTask);
    }

    public static boolean Delete(Object obj) {
        String documentId = DataService.getIdData(obj);
        if (documentId.length() < 1) {
            Log.d("Update", "Failed The Update Operation Need id field ,getId method and  setId method");
            return false;
        }
        Task<Void> referenceTask = db.collection(getTableName(obj.getClass())).document(documentId).delete();
        return TaskWait(referenceTask);
    }




}