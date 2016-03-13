package com.bdh.automation;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Commands for creating, generating, and cleaning Allure reports.
 * 
 * @author Jimmy Zeisweiss.
 *
 */
public enum AllureCommand {

	/**
	 * Generate an Allure report using existing Allure result data files.
	 */
	GENERATE("generate"),

	/**
	 * Open an existing Allure report.
	 */
	OPEN("report open"),

	/**
	 * Remove an existing Allure report.
	 */
	REMOVE_OLD_REPORT("report clean ");

	private final String command;
	private static final String CONSOLE_HORIZONTAL_LINE = "----------------------------------------------------------------------------------";
	private final String ALLURE_HOME = SystemUtils.USER_DIR + "/allure-tools/1.4.22";
	private final String TOOL_PATH = ALLURE_HOME + "/bin/allure" + (SystemUtils.IS_OS_WINDOWS ? ".bat" : "");
	private static final String REPORT_DATA_PATH = "target/allure-results";

	AllureCommand(String command) {
		this.command = CommandLine.parse(TOOL_PATH).getExecutable() + " " + command + " ";
	}

	/**
	 * Execute a command line command.
	 * 
	 * @param command
	 *            to run.
	 * @throws IOException
	 */
	public void executeCommand(final String commandLine) {
		System.out.println(CONSOLE_HORIZONTAL_LINE);
		System.out.println("Running Command: $ " + commandLine);
		ExecutorService service = Executors.newSingleThreadExecutor();
		Future<String> future = service.submit(new Callable<String>() {
			@Override
			public String call() {
				try {
					Process process = Runtime.getRuntime().exec(commandLine);
					InputStreamReader stream = new InputStreamReader(process.getInputStream());
					BufferedReader output = new BufferedReader(stream);
					while (process.isAlive()) {
						String line = output.readLine();
						if (line != null) {
							System.out.println("OUTPUT: " + line);
						}
					}
				} catch (IOException | SecurityException e) {
					e.printStackTrace();
				}
				return "Success";
			}
		});
		try {
			future.get(5, TimeUnit.SECONDS);
		} catch (TimeoutException | InterruptedException | ExecutionException e) {
			System.out.println("Timeout reached");
		}
		service.shutdownNow();
		System.out.println(CONSOLE_HORIZONTAL_LINE);
	}

	/**
	 * Run the current command.
	 */
	public void run() {
		switch (this) {
			case GENERATE:
				executeCommand(command + REPORT_DATA_PATH);
				break;
			case OPEN:
				executeCommand(command);
				break;
			case REMOVE_OLD_REPORT:
				executeCommand(command);
				FileUtils.deleteQuietly(new File(REPORT_DATA_PATH));
				try {
					FileUtils.forceMkdir(new File(REPORT_DATA_PATH));
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
		}
	}

}
