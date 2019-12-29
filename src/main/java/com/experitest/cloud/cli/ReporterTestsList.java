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


    @CommandLine.Option(names = {"-p"}, description = {"Project ID/Name"}, required = true)
    private String projectId;

    @CommandLine.Option(names = "-more", description = "More fields (comma separated)")
    String more = null;
    public void run(){
        String[] moreFields = new String[0];
        if(more != null && !more.isEmpty()){
            moreFields = more.split(",");
        }
        try (Cloud cloud = CloudUtils.getCloud()) {
            int pid = CloudUtils.projectNameToId(cloud, projectId);
            if(pid == -1){
                throw new RuntimeException("Fail to find project: " + projectId);
            }
            List<ReporterTest> projects = cloud.reporter(reporterUrl).getTests(pid);
            CloudUtils.printAsTable(filter, csv, projects, moreFields, "test_id", "name", "start_time", "duration", "status");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
