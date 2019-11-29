package com.experitest.cloud.cli;

import com.experitest.cloud.v2.AccessKeyCloudAuthentication;
import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.ProxyInformation;
import com.experitest.cloud.v2.pojo.User;
import picocli.CommandLine;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

@CommandLine.Command(
        name = "add", description = {"Add cloud url/access key"}
)
public class CloudAdd implements Runnable{
    @CommandLine.Option(names = {"-n", "-name"}, description = {"Cloud Name"}, required = true)
    private String cloudName;
    @CommandLine.Option(names = {"-u", "-url"}, description = {"Cloud URL"}, required = true)
    private String cloudUrl;
    @CommandLine.Option(names = {"-k", "-key"}, description = {"Access Key"}, required = true)
    private String accessKey;
    @CommandLine.Option(names = {"-proxy.host"}, description = {"Proxy host"}, required = false)
    private String proxyHost;

    @CommandLine.Option(names = {"-proxy.port"}, description = {"Proxy port"}, required = false, type = {Integer.class}, defaultValue = "8080")
    private int proxyPort;

    @CommandLine.Option(names = {"-proxy.user"}, description = {"Proxy user"}, required = false)
    private String proxyUser;

    @CommandLine.Option(names = {"-proxy.password"}, description = {"Proxy password"}, required = false)
    private String proxyPassword;

    public void run(){
        System.out.println("Set cloud " + cloudUrl + " with access key: " + accessKey);
        ProxyInformation proxyInformation = null;
        if(proxyHost != null){
            proxyInformation = new ProxyInformation(proxyHost, proxyPort, proxyUser, proxyPassword);
        }
        Cloud cloud = new Cloud(cloudUrl, new AccessKeyCloudAuthentication(accessKey), proxyInformation);
        try {
            User user = cloud.currentUser().get();
            System.out.println("User: " + user.getUsername() + " connected");


            File propFile = new File("cloud.properties");
            Properties properties = new Properties();
            if(propFile.exists()) {
                try (FileReader reader = new FileReader(propFile)) {
                    properties.load(reader);
                } catch (IOException ex) {

                }
            }

            properties.setProperty("cloud.name", cloudName);
            properties.setProperty(cloudName + ".cloud.url", cloudUrl);
            properties.setProperty(cloudName + ".cloud.accessKey", accessKey);
            if (proxyHost != null) {
                properties.setProperty("proxy.host", proxyHost);
                properties.setProperty("proxy.port", String.valueOf(proxyPort));
                if (proxyUser != null) {
                    properties.setProperty("proxy.user", proxyUser);
                    properties.setProperty("proxy.password", proxyPassword);
                }
            }
            try (FileWriter fw = new FileWriter("cloud.properties")) {
                properties.store(fw, null);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Configuration was saved successfully to the current folder");
        } finally {
            cloud.close();
        }
    }
}
