package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.ReporterProject;
import com.experitest.cloud.v2.pojo.ReporterTest;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "tests-list"
)
public class ReporterTestsList implements Runnable{
    @CommandLine.Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;

    @CommandLine.Option(names = {"-r"}, description = {"Reporter URL"}, required = false)
    private String reporterUrl;


    @CommandLine.Option(names = {"-p"}, description = {"Project ID"}, required = true, type = {Integer.class})
    private int projectId;

    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<ReporterTest> projects = cloud.reporter(reporterUrl).getTests(projectId);

            CloudUtils.printAsTable(filter, csv, projects, "test_id", "name", "start_time", "duration", "status");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
