package com.experitest.cloud.cli;

import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

@CommandLine.Command(
        name = "list", description = {"List clouds"}
)
public class CloudList implements Runnable{

    public void run(){
        File propFile = new File("cloud.properties");
        if(!propFile.exists()){
            System.err.println("cloud.properties file was not found in the current folder. You should first call set command");
            return;
        }
        Properties properties = new Properties();
        try(FileReader reader = new FileReader(propFile)){
            properties.load(reader);
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
        ArrayList<String> foundClouds = new ArrayList<>();
        for(Object key: properties.keySet()){
            String keyText = String.valueOf(key);
            if(keyText.endsWith(".cloud.url")){
                foundClouds.add(keyText.substring(0, keyText.length() - ".cloud.url".length()));
            }
        }
        System.out.println(Arrays.toString(foundClouds.toArray()));
        System.out.println("Active: " + properties.getProperty("cloud.name"));
    }
}
