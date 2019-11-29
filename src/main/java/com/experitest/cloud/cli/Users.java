package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
		subcommands = {
				UsersList.class,
				DeleteUser.class,
				CreateUser.class
		}, description = {"Users operations"},
		name = "users"
)
public class Users implements Runnable{
	@Override
	public void run() {
		new CommandLine(new CloudCli()).execute("users", "-h");

	}
}
