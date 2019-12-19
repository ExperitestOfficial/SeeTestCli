package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.Project;
import com.experitest.cloud.v2.pojo.User;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "list"
)
public class UsersList implements Runnable{
    @CommandLine.Option(names = {"-b"}, description = {""}, required = false)
    private boolean b;
    @CommandLine.Option(names = {"-f"}, description = {"Comma separated list"}, required = false)
    private String filter;
    @CommandLine.Option(names = {"-csv"}, description = {"Comma separated list"}, required = false)
    private boolean csv = false;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            List<User> users = cloud.users().get();
            for (User u : users) {
                if (u.getProjects() != null && u.getProjects().length > 0) {
                    u.setRole(u.getProjects()[0].getProjectRole());
                    StringBuffer buf = new StringBuffer();
                    for (Project p : u.getProjects()) {
                        buf.append(p.getName());
                        buf.append(",");
                    }
                    u.setProjectString(buf.substring(0, buf.length() - 1));
                }
            }
            if (b) {
                for(User user: users){
                    String[] userStatus = user.getData("userstatus");
                    StringBuilder buf = new StringBuilder();
                    buf.append(user.getEmail());
                    buf.append("|");
                    buf.append(user.getUsername());
                    buf.append("|");
                    buf.append(user.getRole());
                    buf.append("||||||");
                    buf.append(user.getRole());
                    buf.append("|||");
                    buf.append("user".equalsIgnoreCase(user.getRole())? "N": "Y");
                    System.out.println(buf.toString());
                }
            } else {
                CloudUtils.printAsTable(filter, csv, users, "id", "username", "email", "role", "projectstring", "lastauthentication", "userstatus");
            }
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
