package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "get"
)
public class ReporterTest implements Runnable{
    @CommandLine.Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;

    @CommandLine.Option(names = {"-r"}, description = {"Reporter URL"}, required = false)
    private String reporterUrl;


    @CommandLine.Option(names = {"-id"}, description = {"Test ID"}, required = true, type = {Integer.class})
    private int testId;
    @CommandLine.Option(names = {"-p"}, description = {"Project name"}, required = true)
    private String projectName;

    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            System.out.println(cloud.reporter(reporterUrl).getText(projectName, testId));

        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
