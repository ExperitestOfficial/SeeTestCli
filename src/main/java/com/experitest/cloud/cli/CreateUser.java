package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.Role;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(
        name = "create"
)
public class CreateUser implements Runnable{
    @CommandLine.Option(names = {"-email"}, description = {"User email"}, required = true)
    private String email;

    @CommandLine.Option(names = {"-first"}, description = {"First name"}, required = false, defaultValue = "first")
    private String firstName;
    @CommandLine.Option(names = {"-last"}, description = {"Last name"}, required = false, defaultValue = "last")
    private String lastName;

    @CommandLine.Option(names = {"-role"}, description = {"User role, options: ${COMPLETION-CANDIDATES}"}, required = false, type = Role.class)
    private Role role;

    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            if(role == null){
                role = Role.USER;
            }
            String roleText = "User";
            if(role == Role.CLOUD_ADMIN){
                roleText = "Admin";
            } else if (role == Role.PROJECT_ADMIN){
                roleText = "ProjectAdmin";
            }
            cloud.users().createUser(email, firstName, lastName, email, roleText, "BASIC");
            System.out.println("Done!");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
