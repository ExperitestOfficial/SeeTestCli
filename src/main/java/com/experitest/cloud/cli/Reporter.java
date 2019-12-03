package com.experitest.cloud.cli;

import picocli.CommandLine;

@CommandLine.Command(
		subcommands = {
				ReporterProjectsList.class,
				ReporterTestsList.class,
				ReporterTest.class

		}, description = {"Reporter operations"},
		name = "reporter"
)
public class Reporter implements Runnable{
	@Override
	public void run() {
		new CommandLine(new CloudCli()).execute("reporter", "-h");

	}
}
