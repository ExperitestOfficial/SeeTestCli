package com.experitest.cloud.cli;

import com.experitest.cloud.v2.Cloud;
import com.experitest.cloud.v2.pojo.User;
import picocli.CommandLine;

import java.io.IOException;
import java.util.List;

@CommandLine.Command(
        name = "delete"
)
public class DeleteUser implements Runnable{
    @CommandLine.Option(names = {"-id"}, description = {"User ID/user name"}, required = true)
    String id;
    public void run(){
        try (Cloud cloud = CloudUtils.getCloud()) {
            int userId = -1;
            try {
                userId = Integer.parseInt(id);
            } catch (Exception ignore){
                List<User> users = cloud.users().get();
                for(User u: users){
                    if(id.equalsIgnoreCase(u.getUsername())){
                        userId = u.getId();
                        break;
                    }
                }
            }
            if(userId == -1){
                throw new RuntimeException("Cannot find user");
            }
            if(cloud.currentUser().get().getId() == userId){
                System.err.println("You cannot delete yourself!");
            }
            User u = new User();
            u.setId(userId);
            cloud.users().delete(u);
            System.out.println("Done!");
        } catch (IOException ex){
            ex.printStackTrace();
            throw new RuntimeException("Fail to init cloud connection");
        }
    }
}
