package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.*;
import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@CommandLine.Command(
        name = "clean"
)
public class ProjectClean implements Runnable{
    @Option(names = {"-p"}, description = {"Project name"}, required = true)
    private String projectName;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<Project> projects = cloud.projects().get();
            Project project = null;
            for(Project p: projects){
                if(p.getName().equalsIgnoreCase(projectName)){
                    project = p;
                    break;
                }
            }
            if(project == null){
                throw new RuntimeException("Unable to identify project name: " + projectName);
            }
            String deviceGroup = project.getDeviceGroupsForDisplay();
            boolean toDeletetGroup = true;
            if(deviceGroup != null) {
                for (Project p : projects) {
                    if (p != project && deviceGroup.equalsIgnoreCase(p.getDeviceGroupsForDisplay())){
                       toDeletetGroup = false;
                        break;
                    }
                }
            }
            List<User> users = cloud.users().get();
            ArrayList<User> toLock = new ArrayList<>();
            for(User u: users){
                if(u.getProjects() != null && u.getProjects().length == 1 && projectName.equalsIgnoreCase(u.getProjects()[0].getName())){
                    toLock.add(u);
                }
            }
            ArrayList<String> usersName = new ArrayList<>();
            for(User u: toLock){
                usersName.add(u.getUsername());
            }
            System.out.println("User to delete: " + Arrays.toString(usersName.toArray()));
            if(toDeletetGroup && deviceGroup != null){
                System.out.println("Device group to delete: " + deviceGroup);
            } else {
                System.out.println("Device group will not be deleted as it's shared with other projects");
            }
            Scanner scanner = new Scanner(System.in);

            //  prompt for the user's name
            System.out.print("Are you sure (Yes/No)?:");
            String answer = scanner.next();
            if(!answer.equalsIgnoreCase("yes")){
                System.out.println("Operation canceled");
            } else {
                for(User u: toLock){
                    cloud.users().delete(u);
                    System.out.println("Deleted: " + u.getUsername());
                }
                if(deviceGroup != null) {
                    DeviceGroup dg = null;
                    for (DeviceGroup deviceGroup1 : cloud.deviceGroups().getAll()) {
                        if (deviceGroup.equalsIgnoreCase(deviceGroup1.getName())){
                            dg = deviceGroup1;
                        }
                    }
                    if(dg != null) {
                        cloud.deviceGroups().deleteDeviceGroup(dg);
                        System.out.println("Device group " + deviceGroup + " was deleted");
                    }
                }
                cloud.projects().delete(project);
                System.out.println("Project " + projectName + " was deleted");

            }

        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
