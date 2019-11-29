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
        name = "list"
)
public class ProjectsList implements Runnable{
    @Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @Option(names = "-r", description = "Number of days to count reservations", type = {Integer.class})
    int reservations = 0;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<Project> projects = cloud.projects().get();
            for(Project p: projects){
                p.setDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date(p.getCreatedAt())));
            }
            if(reservations > 0){
                HashMap<Integer, Project> projectsMap = new HashMap<>();
                for(Project p: projects){
                    projectsMap.put((int)p.getId(), p);
                }
                List<Device> devices = cloud.devices().get();
                for(Device d: devices) {
                    List<Reservation> reservationsList = cloud.devices().getReservations(d.getId(), reservations);
                    for (Reservation r : reservationsList) {
                        Project p = projectsMap.get(r.getProjectId());
                        if (p != null) {
                            p.setReservations(p.getReservations() + 1);
                        }
                    }
                }
                CloudUtils.printAsTable(filter, csv, projects, "id", "name", "date", "amountofusers",
                        "deviceGroupsForDisplay", "reservations");
            } else {
                CloudUtils.printAsTable(filter,csv,  projects, "id", "name", "date", "amountofusers",
                        "deviceGroupsForDisplay");
            }
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
