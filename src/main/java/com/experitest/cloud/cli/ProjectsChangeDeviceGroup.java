package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.Device;
import com.experitest.cloud.v2.pojo.Project;
import com.experitest.cloud.v2.pojo.Reservation;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@CommandLine.Command(
        name = "change-group"
)
public class ProjectsChangeDeviceGroup implements Runnable{

    @Option(names = "-pid", description = "Project ID/Project Name", required = true)
    String projectId = null;
    @Option(names = "-gid", description = "Device Group ID", type = {Integer.class}, required = true)
    int deviceGroupId = 0;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            int pid = CloudUtils.projectNameToId(cloud, projectId);
            if(pid == -1){
                throw new RuntimeException("Fail to find project: " + projectId);
            }
            cloud.projects().setDeviceGroup(pid, deviceGroupId);
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
