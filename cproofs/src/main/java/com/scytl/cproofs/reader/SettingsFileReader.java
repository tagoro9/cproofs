package com.scytl.cproofs.reader;

import android.content.Context;

import com.google.gson.Gson;
import com.scytl.cproofs.crypto.ElGamal.ElGamalExtendedParameterSpec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by victor on 6/21/14.
 */
public class SettingsFileReader implements SettingsReader {

    public static String SETTINGS_FILE = "parameters.json";

    @Override
    public ElGamalExtendedParameterSpec read(String fileName, Context context) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            Gson gson = new Gson();
            return gson.fromJson(sb.toString(), ElGamalExtendedParameterSpec.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean write(ElGamalExtendedParameterSpec parameters, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(SETTINGS_FILE, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = gson.toJson(parameters, ElGamalExtendedParameterSpec.class);
            fos.write(json.getBytes());
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
