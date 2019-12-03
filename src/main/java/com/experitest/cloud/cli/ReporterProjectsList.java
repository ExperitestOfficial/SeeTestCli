package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.Project;
import com.experitest.cloud.v2.pojo.ReporterProject;
import com.experitest.cloud.v2.pojo.User;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "projects-list"
)
public class ReporterProjectsList implements Runnable{
    @CommandLine.Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;
    @CommandLine.Option(names = {"-r"}, description = {"Reporter URL"}, required = false)
    private String reporterUrl;

    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<ReporterProject> projects = cloud.reporter(reporterUrl).getProjects();

            CloudUtils.printAsTable(filter, csv, projects, "id", "name", "created", "attachmentsCurrentTotalSize", "attachmentsMaxAllowedSize");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
