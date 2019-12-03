package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.Device;
import com.experitest.cloud.v2.pojo.DeviceGroup;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "move"
)
public class GroupsMove implements Runnable{
    @CommandLine.Option(names = {"-id"}, description = {"Device ID/Name list (comma separated)"}, required = true)
    private String ids;
    @CommandLine.Option(names = {"-r"}, description = {"Remove from group name"}, required = false)
    private String from;
    @CommandLine.Option(names = {"-a"}, description = {"Add to group name"}, required = false)
    private String to;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<DeviceGroup> groups = cloud.deviceGroups().getAll();
            List<Device> devices = cloud.devices().get();
            String[] values = ids.split(",");
            for(String value: values) {
                int deviceId = -1;
                try {
                    deviceId = Integer.parseInt(value);
                } catch (Exception ignore) {
                    for (Device d : devices) {
                        if (value.equalsIgnoreCase(d.getName())) {
                            deviceId = d.getId();
                            break;
                        }
                    }
                }
                if (deviceId == -1) {
                    throw new RuntimeException("Cannot find device");
                }
                if (from != null) {
                    DeviceGroup group = null;
                    for (DeviceGroup g : groups) {
                        if (from.equalsIgnoreCase(g.getName())) {
                            group = g;
                            break;
                        }
                    }
                    if (group == null) {
                        throw new RuntimeException("Group " + from + " wasn't found");
                    }
                    Device d = new Device();
                    d.setId(deviceId);
                    cloud.deviceGroups().removeDeviceFromGroup(d, group);
                }
                if (to != null) {
                    DeviceGroup group = null;
                    for (DeviceGroup g : groups) {
                        if (to.equalsIgnoreCase(g.getName())) {
                            group = g;
                            break;
                        }
                    }
                    if (group == null) {
                        throw new RuntimeException("Group " + to + " wasn't found");
                    }
                    Device d = new Device();
                    d.setId(deviceId);
                    cloud.deviceGroups().assignDeviceToGroup(d, group);
                }
            }
            System.out.println("Done!");

        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
