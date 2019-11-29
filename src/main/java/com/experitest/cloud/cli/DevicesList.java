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
    @Option(names = "-a", description = "Advance") boolean advance = false;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<Device> devices = cloud.devices().get(true);
            for(Device d: devices){
                if(d.getDeviceGroupNames() != null){
                    d.setGroups(Arrays.toString(d.getDeviceGroupNames()));
                }
            }
            CloudUtils.printAsTable(filter, csv, devices, "id", "name", "os", "osversion","status", "currentuser", "currentagentname", "defaultwifissid", "groups");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
