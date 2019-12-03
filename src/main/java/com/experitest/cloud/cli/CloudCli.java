package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
        subcommands = {
                CloudSet.class,
                Devices.class,
                Users.class,
                Projects.class,
                Groups.class,
                Reporter.class
        }, description = {"SeeTest cloud command line tool"}, version = {"cc 1.0"}, name = "cc", mixinStandardHelpOptions = true
)
public class CloudCli implements Runnable{
    public static void main(String... args) {
        int exitCode = new CommandLine(new CloudCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        new CommandLine(new CloudCli()).printVersionHelp(System.out);
        new CommandLine(new CloudCli()).execute("-h");
    }
}
