package com.bdh.automation;

import org.apache.commons.lang3.BooleanUtils;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;

/**
 * Example test methods showing how to use Allure.
 * 
 * @author Jimmy Zeisweiss
 *
 */
@Features("Jimmy's feature")
public class ExampleTest {

	private CoreWebDiver driver;

	/**
	 * Start the driver and clean old Allure reports.
	 */
	@BeforeSuite
	public void beforeSuite() {
		if (BooleanUtils.toBoolean(System.getProperty("SKIP_SELENIUM_TESTS", "false"))) {
			throw new SkipException("Skip selenium tests.");
		}
		driver = new CoreWebDiver();
		AllureCommand.REMOVE_OLD_REPORT.run();
	}

	/**
	 * Reset the step count.
	 */
	@BeforeMethod
	public void beforeMethod() {
		driver.restStepCount();
	}

	/**
	 * Shutdown the driver, generate the report, and open the report.
	 */
	@AfterSuite
	public void afterSuite() {
		driver.quit();
		AllureCommand.GENERATE.run();
		AllureCommand.OPEN.run();
	}

	@Stories("Jimmy's Individual Examples")
	@Test(description = "Demonstrate how to use steps.")
	public void testStepExample() {
		driver.addStep("Conquer the world")
		      .addStep("Have fun")
		      .addStep("Go Crazy");
	}

	@Stories("Jimmy's Individual Examples")
	@Test(description = "Demonstrate how to take screenshots.")
	public void testScreenshotExample() {
		driver.recordScreenshot("During Test example.");
	}

	@Stories("Jimmy's Individual Examples")
	@Test(description = "Demonstrate how to attach a data file.")
	public void testTextAttachmentExample() {
		driver.recordDataFile("During test data file", "{ Lots of Data }");
	}

	@Stories("Jimmy's Full Examples")
	@Test(description = "Combine all examples.")
	public void testFullExample() {
		driver.addStep("Produce amazing things")
		      .recordScreenshot("During Test example.")
		      .recordDataFile("During test data file", "{ More Data }");
	}

}
