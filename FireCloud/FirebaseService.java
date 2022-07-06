package com.stkaskin.dininghallsurvey.FireCloud;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class FirebaseService {
    protected static FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static boolean TaskWait(Task task) {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (task.isComplete()) {
                return true;
            }

        }

        return false;
    }

    protected static <T> String getTableName(@NonNull Class<T> clazz) {
        Object obj = null;
        String tableName = null;
        try {
            obj = clazz.newInstance();
            tableName = obj.getClass().getMethod("TableName").invoke(obj).toString();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return tableName;
    }

    protected static <T> ArrayList<T> TaskCastTList(@NonNull Class<T> clazz, Task<QuerySnapshot> task) {
        ArrayList<T> list = new ArrayList<>();
        //dont use on
        T new_item = null;
        for (DocumentSnapshot ts : task.getResult().getDocuments()) {
            try {
                new_item = clazz.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            new_item = ts.toObject(clazz);
            String ss = ts.getId().toString();
            new_item = documentIdSet(new_item, ss);
            list.add(new_item);
        }


        return list;
    }

    protected static <T> T documentIdSet(T obj, String data) {

        try {
            obj.getClass().getMethod("setId", String.class).invoke(obj, data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return obj;


    }

}
