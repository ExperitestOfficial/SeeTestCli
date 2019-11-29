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
    @CommandLine.Option(names = {"-id"}, description = {"Device ID/Name"}, required = true)
    private String id;
    @CommandLine.Option(names = {"-r"}, description = {"Remove from group name"}, required = false)
    private String from;
    @CommandLine.Option(names = {"-a"}, description = {"Add to group name"}, required = false)
    private String to;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            int deviceId = -1;
            try{
                deviceId = Integer.parseInt(id);
            } catch (Exception ignore){
                List<Device> devices = cloud.devices().get();
                for(Device d: devices){
                    if(id.equalsIgnoreCase(d.getName())){
                        deviceId = d.getId();
                        break;
                    }
                }
            }
            if(deviceId == -1){
                throw new RuntimeException("Cannot find device");
            }
            List<DeviceGroup> groups = cloud.deviceGroups().getAll();
            if(from != null){
                DeviceGroup group = null;
                for(DeviceGroup g: groups){
                    if(from.equalsIgnoreCase(g.getName())){
                        group = g;
                        break;
                    }
                }
                if(group == null){
                    throw new RuntimeException("Group " + from + " wasn't found");
                }
                Device d = new Device();
                d.setId(deviceId);
                cloud.deviceGroups().removeDeviceFromGroup(d, group);
            }
            if(to != null){
                DeviceGroup group = null;
                for(DeviceGroup g: groups){
                    if(to.equalsIgnoreCase(g.getName())){
                        group = g;
                        break;
                    }
                }
                if(group == null){
                    throw new RuntimeException("Group " + to + " wasn't found");
                }
                Device d = new Device();
                d.setId(deviceId);
                cloud.deviceGroups().assignDeviceToGroup(d, group);
            }
            System.out.println("Done!");

        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
