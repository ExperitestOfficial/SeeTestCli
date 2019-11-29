package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
		subcommands = {
				DevicesList.class
		}, description = {"Devices operations"},
		name = "devices"
)
public class Devices implements Runnable{
	@Override
	public void run() {
		new CommandLine(new CloudCli()).execute("devices", "-h");

	}
}
