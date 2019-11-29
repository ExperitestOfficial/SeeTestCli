package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
		subcommands = {
				ProjectsList.class,
				ProjectClean.class
		}, description = {"Projects operations"},
		name = "projects"
)
public class Projects implements Runnable{
	@Override
	public void run() {
		new CommandLine(new CloudCli()).execute("projects", "-h");

	}
}
