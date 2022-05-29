package com.stkaskin.restaurantmanager.FireCloud;

import com.google.firebase.firestore.DocumentSnapshot;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DataService {
    public static <T> String TableNameGet(T obj) {
        String TableName = "";
        try {
            TableName = obj.getClass().getMethod("TableName").invoke(obj).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return TableName;
    }

    public static String getIdData(Object obj) {
        String dataId = "";
        try {
            dataId = obj.getClass().getMethod("getId").invoke(obj).toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return dataId;
    }

    public static <T> Map<String, Object> ConvertDataForObjectList(T obj) {
        ArrayList<Method> methods = getMethodsGet(obj);
        Field[] c = obj.getClass().getDeclaredFields();

        Map<String, Object> data_t = new HashMap<>();
        for (Field item : c) {
            for (Method mt : methods) {

                if (mt.getName().substring(3).toLowerCase(Locale.ROOT).equals(item.getName().toLowerCase(Locale.ROOT))) {
                    try {
                        data_t.put(item.getName(), mt.invoke(obj));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
        return data_t;
    }


    public static <T> T ConvertData(T obj, DocumentSnapshot data) {
        try {
            obj= (T) obj.getClass().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        Field[] fields = obj.getClass().getDeclaredFields(); //System.out.println(item.get(obj).toString()+"");

        ArrayList<Method> setmethods = setMethodsGet(obj);


        for (Field item : fields) {

            for (Method method : setmethods) {

                if (method.getName().substring(3).toLowerCase(Locale.ROOT).equals(item.getName())) {

                    obj = setFieldWithMethodInvoke(obj, method, item, data.get(item.getName()));
                }
            }
        }
        documentIdSet(obj, setmethods, fields, data.getId());
        return obj;
    }

    public static <T> ArrayList<T> ConvertData(T obj, List<DocumentSnapshot> data) {
        ArrayList<T> allTArrayList = new ArrayList<>();
        for (DocumentSnapshot snapshot : data) {

            allTArrayList.add(ConvertData(obj, snapshot));
        }
        return allTArrayList;
    }

    public static <T> T documentIdSet(T obj, ArrayList<Method> setmethods, Field[] fields, String data) {

        for (Field item : fields) {

            for (Method method : setmethods) {

                if (method.getName().substring(3).toLowerCase(Locale.ROOT).equals(item.getName())) {

                    obj = setFieldWithMethodInvoke(obj, method, item, data);
                    return obj;
                }
            }
        }
        return obj;
    }

    private static <T> T setFieldWithMethodInvoke(T obj, Method method, Field item, Object data) {
        try {
            if (data != null) {


                if (item.getType().getName().equals("int")) {
                    int a = Integer.parseInt(data.toString());
                    method.invoke(obj, a);
                } else if (item.getType().getName().contains("String"))
                    method.invoke(obj, (String) data);
                    //Boolen test edilmedi
                else if (item.getType().getName().contains("Boolean"))
                    method.invoke(obj, (boolean) data);
                else if (item.getType().getName().contains("Integer"))
                    method.invoke(obj, (Integer) data);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return obj;
    }


    private static ArrayList<Method> getMethodsGet(Object obj) {
        ArrayList<Method> setMethods = new ArrayList<>();
        for (Method item : obj.getClass().getMethods())
            if (item.getName().contains("get"))
                setMethods.add(item);
        return setMethods;
    }

    private static ArrayList<Method> setMethodsGet(Object obj) {
        ArrayList<Method> setMethods = new ArrayList<>();
        for (Method item : obj.getClass().getMethods())
            if (item.getName().contains("set"))
                setMethods.add(item);
        return setMethods;
    }
}
