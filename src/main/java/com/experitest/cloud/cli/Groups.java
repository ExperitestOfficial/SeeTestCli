package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
		subcommands = {
				GroupsList.class,
				GroupsMove.class
		}, description = {"Devices group"},
		name = "groups"
)
public class Groups implements Runnable{
	@Override
	public void run() {
		new CommandLine(new CloudCli()).execute("groups", "-h");

	}
}
