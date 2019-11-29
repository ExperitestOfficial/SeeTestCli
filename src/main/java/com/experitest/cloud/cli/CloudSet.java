package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
		subcommands = {
				CloudAdd.class,
				CloudList.class,
				CloudSwitch.class
		}, description = {"Clouds operations"},
		name = "configure"
)
public class CloudSet implements Runnable{
	@Override
	public void run() {
		new CommandLine(new CloudCli()).execute("configure", "-h");

	}
}
