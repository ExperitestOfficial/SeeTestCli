package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.Device;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@CommandLine.Command(
        name = "list"
)
public class DevicesList implements Runnable{
    @CommandLine.Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;
    @Option(names = "-more", description = "More fields (comma separated)")
    String more = null;
    public void run(){
        String[] moreFields = new String[0];
        if(more != null && !more.isEmpty()){
            moreFields = more.split(",");
        }
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<Device> devices = cloud.devices().get(true);
            for(Device d: devices){
                if(d.getDeviceGroupNames() != null){
                    d.setGroups(Arrays.toString(d.getDeviceGroupNames()));
                }
            }
            CloudUtils.printAsTable(filter, csv, devices, moreFields,"id", "name", "os", "osversion","status", "currentuser", "currentagentname", "defaultwifissid", "groups");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
