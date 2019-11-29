package com.experitest.cloud.cli;

import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@CommandLine.Command(
        name = "active", description = {"Set active cloud"}
)
public class CloudSwitch implements Runnable{

    @CommandLine.Option(names = {"-name"}, description = {"Set active cloud"}, required = true)
    private String name;

    public void run(){
        new CloudList().run();
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
        if(!properties.containsKey(name + ".cloud.url")){
            throw new RuntimeException("Cannot find the provided cloud name in the cloud.properties");
        }
        properties.setProperty("cloud.name", name);
        try (FileWriter fw = new FileWriter("cloud.properties")) {
            properties.store(fw, null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        new CloudList().run();
    }
}
