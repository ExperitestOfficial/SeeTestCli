package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.DeviceGroup;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "list"
)
public class GroupsList implements Runnable{
    @CommandLine.Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;
    @CommandLine.Option(names = "-more", description = "More fields (comma separated)")
    String more = null;
    public void run(){
        String[] moreFields = new String[0];
        if(more != null && !more.isEmpty()){
            moreFields = more.split(",");
        }
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<DeviceGroup> groups = cloud.deviceGroups().getAll();
            CloudUtils.printAsTable(filter, csv, groups, moreFields,"id", "name", "type", "numberOfDevices", "devices");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
