package com.xmart.projects.storline.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.RecursiveTask;

/**
 * Created by smart on 26/08/2018.
 */

public class Utils {

    public static String currencyFormat(Object value) {

        try {

            DecimalFormat df = new DecimalFormat("0.00#");
            String format = df.format(Double.valueOf(value.toString()));

            return "$ " + format;

        } catch (Exception e) {
            return "";
        }

    }



    public static String decimalFormat(Object value) {

        try {

            DecimalFormat df = new DecimalFormat("0.00#");
            String format = df.format(Double.valueOf(value.toString()));

            return format;

        } catch (Exception e) {
            return "";
        }

    }


    public static String dateTimeFormat(Object value) {

        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = (Date) value;
            String d = dateFormat.format(date);

            return d;

        }
        catch (Exception e) {
            return "";
        }

    }


    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
