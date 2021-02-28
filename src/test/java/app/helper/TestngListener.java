package app.helper;

import org.testng.*;

import static app.helper.BaseTestGui.LOG;

// TestngListener this class was copied from toolsqa site.
public class TestngListener implements ITestListener, ISuiteListener, IInvokedMethodListener {

    // This belongs to ISuiteListener and will execute before the Suite start
    @Override
    public void onStart(ISuite arg0) {
  //      Reporter.log("onStart(ISuite: About to begin executing Suite " + arg0.getName(), true);
        // SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    // This belongs to ISuiteListener and will execute, once the Suite is finished
    @Override
    public void onFinish(ISuite arg0) {
        Reporter.log("onFinish(ISuite: About to end executing Suite " + arg0.getName(), true);
    }

    // This belongs to ITestListener and will execute before starting of Test set/batch
    public void onStart(ITestContext arg0) {
        Reporter.log("onStart(ITestContext : About to begin executing Test " + arg0.getName(), true);
    }

    // This belongs to ITestListener and will execute, once the Test set/batch is finished
    public void onFinish(ITestContext arg0) {
        Reporter.log("onFinish(ITestContext: Completed executing test " + arg0.getName(), true);
    }

    // This belongs to ITestListener and will execute only when the test is pass
    public void onTestSuccess(ITestResult arg0) {
        // This is calling the printTestResults method
        Reporter.log("onTestSuccess(ITestResult: Successfully Completed executing test " + arg0.getName(), true);
        printTestResults(arg0);
    }

    // This belongs to ITestListener and will execute only on the event of fail test
    public void onTestFailure(ITestResult arg0) {
        // This is calling the printTestResults method
        Reporter.log("onTestFailure(ITestResult: Completed executing test " + arg0.getName(), true);
        printTestResults(arg0);

    }

    // This belongs to ITestListener and will execute before the main test start (@Test)
    public void onTestStart(ITestResult arg0) {
        Reporter.log("onTestStart(ITestResult: The execution of the main test started!");
        System.out.println("The execution of the main test starts now");
    }

    // This belongs to ITestListener and will execute only if any of the main test(@Test) get skipped
    public void onTestSkipped(ITestResult arg0) {
        Reporter.log("onTestSkipped(ITestResult: The  main test(@Test) get skipped!");
        printTestResults(arg0);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
        Reporter.log("onTestFailedButWithinSuccessPercentage(ITestResult: The  test failed but some steps are passed.");
    }

    // This is the method which will be executed in case of test pass or fail
    // This will provide the information on the test
    private String printTestResults(ITestResult result) {
        Reporter.log("printTestResults(ITestResult: Test Method placed in: " + result.getTestClass().getName(), true);
      /*  if (result.getParameters().length != 0) {
            String params = null;
            for (Object parameter : result.getParameters()) {
                params += parameter.toString() + ",";
            }
            Reporter.log("Test Method had the following parameters : " + params, true);
        }*/
        String status = null;
        switch (result.getStatus()) {
            case ITestResult.CREATED:
                status = "CREATED";
                break;
            case ITestResult.SUCCESS:
                status = "SUCCESS";
                break;
            case ITestResult.FAILURE:
                status = "FAILURE";
                LOG.info("Maximal information about failure: " + result);
                break;
            case ITestResult.SKIP:
                status = "SKIP";
                break;
        }
        Reporter.log("Test Status: " + status, true);
        return status;
    }

    // This belongs to IInvokedMethodListener and will execute before every method including @Before @After @Test
    public void beforeInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = String.format("About to begin method: '%s', that has status:'%s'", returnMethodName(arg0.getTestMethod()), printTestResults(arg1));
        Reporter.log(textMsg, true);
    }

    // This belongs to IInvokedMethodListener and will execute after every method including @Before @After @Test
    public void afterInvocation(IInvokedMethod arg0, ITestResult arg1) {
        String textMsg = String.format("Completed method:'%s' execution with:'%s'", returnMethodName(arg0.getTestMethod()), printTestResults(arg1));
        Reporter.log(textMsg, true);
    }

    // This will return method names to the calling function
    private String returnMethodName(ITestNGMethod method) {
        return method.getRealClass().getSimpleName() + "." + method.getMethodName();
    }
}