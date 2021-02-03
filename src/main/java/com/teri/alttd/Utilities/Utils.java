package com.teri.alttd.Utilities;

import com.teri.alttd.FileManagement.Log;

public class Utils {

    /**
     * Check if an object is null and if it is log it
     * @param classObject The class this function was called from
     * @param object Object to check
     * @param action Action that was taken that should have resulted in the object not being null
     * @return true if the object is null
     */
    public static boolean isNull(Object classObject, Object object, String action){
        if (object == null) {
            new Log(Log.LogType.NULL).appendLog("Unexpected null at " + classObject.getClass().getName() + " during the following action: " + action);
            return true;
        }
        return false;
    }
}
