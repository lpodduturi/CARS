package com.perspecta.hybrid.keywords;

import java.io.File;
import java.io.IOException;
//import java.net.MalformedURLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.perspecta.hybrid.reports.ExtentManager;

public class GenericKeywords {
	public Properties envProp;
	public Properties prop;
	public String objectKey;
	public String dataKey;
	//public String proceedOnFail;
	public Hashtable<String,String> data;
	public WebDriver driver;
	public ExtentTest test;
	public SoftAssert softAssert = new SoftAssert();
	
	
	/*********************Setter functions***************************/
	//public String getProceedOnFail() {
		//return proceedOnFail;
	//}

	//public void setProceedOnFail(String proceedOnFail) {
		//this.proceedOnFail = proceedOnFail;
	//}


	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}
	
	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}


	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public void setDataKey(String dataKey) {
		this.dataKey = dataKey;
	}

	public void setData(Hashtable<String, String> data) {
		this.data = data;
	}
    /*****************************************/
	
	public void openbrowser(){
		String browser=data.get(dataKey);
		test.log(Status.INFO,"Opening Browser "+browser );
		//if(prop.getProperty("gridRun").equals("Y")) {
			// run on grid
			
			//DesiredCapabilities cap=null;
			//if(browser.equals("Mozilla")) {
			//	cap = DesiredCapabilities.firefox();
			//	cap.setJavascriptEnabled(true);
			//	cap.setPlatform(Platform.WINDOWS);
			//}else if(browser.equals("Chrome")) {
			//  cap = DesiredCapabilities.chrome();
			//	cap.setJavascriptEnabled(true);
			//	cap.setPlatform(Platform.WINDOWS);
		//	}
			
			//try {
				//driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			//} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			//}
			
			// run on normal browser
		//	}else {
		    if(browser.equals("Mozilla")){
			// options
			System.setProperty(FirefoxDriver.SystemProperty.BROWSER_LOGFILE, "null");
			// invoke Firefox profile
			System.setProperty("webdriver.gecko.driver",prop.getProperty("firefoxpath"));
			driver = new FirefoxDriver();
		    }else if(browser.equals("Chrome")){
			// invoke Chrome profile
			System.setProperty("webdriver.chrome.driver",prop.getProperty("chromepath"));
			ChromeOptions ChromeOptions = new ChromeOptions();
			ChromeOptions.addArguments("--headless", "window-size=1024,768", "--no-sandbox");
			driver = new ChromeDriver(ChromeOptions);
			driver = new ChromeDriver();
		    }else if(browser.equals("IE")){
			// invoke IE profile
			System.setProperty("webdriver.ie.driver",prop.getProperty("iepath"));
			driver = new InternetExplorerDriver();
		    }else if(browser.equals("Edge")){
			// invoke Edge profile
			System.setProperty("webdriver.edge.driver",prop.getProperty("edgepath"));
			driver = new EdgeDriver();
		}
		
		// max and set implicit wait
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
	//}
	
	public void navigate(){
		test.log(Status.INFO,"Navigating to website "+envProp.getProperty(objectKey));
		driver.get(envProp.getProperty(objectKey));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Navigating to website Success");
	}

	public void click(){
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		test.log(Status.INFO,"Clicking "+prop.getProperty(objectKey));
		getObject(objectKey).click();
		System.out.println("Clicked");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	}

	public void type(){
		test.log(Status.INFO,"Typing in "+prop.getProperty(objectKey)+" . Data - "+data.get(dataKey));
		getObject(objectKey).sendKeys(data.get(dataKey));
		System.out.println("Entered Value:"+ dataKey);
	}
	
	public void clickntype() {
		getObject(objectKey).click();
		test.log(Status.INFO,"Typing in "+prop.getProperty(objectKey)+" . Data - "+data.get(dataKey));
		getObject(objectKey).sendKeys(data.get(dataKey));
		System.out.println("Entered Value:"+ dataKey);
	}

	public void select(){
		test.log(Status.INFO,"Selecting from "+prop.getProperty(objectKey)+" . Data - "+data.get(dataKey));
		if(!isElementInList())
			reportFailure("Option not found in list "+ data.get(dataKey));
		    new Select(getObject(objectKey)).selectByVisibleText(data.get(dataKey));
		    System.out.println("Selected Value:"+ dataKey);
	}
	
	public void clear(){
		test.log(Status.INFO,"Clearing "+prop.getProperty(objectKey));
		getObject(objectKey).clear();
	}
	
	public void waitForPageToLoad(){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		int i=0;
		while(i!=10){
		String state = (String)js.executeScript("return document.readyState;");
		System.out.println(state);

		if(state.equals("complete"))
			break;
		else
			wait(2);

		i++;
		}
		// check for jquery status
		i=0;
		while(i!=10){
	
			Long d= (Long) js.executeScript("return jQuery.active;");
			System.out.println(d);
			if(d.longValue() == 0 )
			 	break;
			else
				 wait(2);
			 i++;
				
		}
	}
	
	public void acceptAlert(){
		test.log(Status.INFO, "Switching to alert");
		
		try{
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			test.log(Status.INFO, "Alert accepted successfully");
		}catch(Exception e){
			if(objectKey.equals("Y")){
				reportFailure("Alert not found when mandatory");
			}
		}
	}
	
	public void wait(int timeSec){
		try {
			Thread.sleep(timeSec*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void validateelementnotinlist(){
		if(isElementInList())
			reportFailure("Could not delete the option");
	}
	
	public void validatetitle(){
		test.log(Status.INFO,"Validating title - "+prop.getProperty(objectKey) );
		String expectedTitle = prop.getProperty(objectKey);
		String actualTitle=driver.getTitle();
		if(!expectedTitle.equals(actualTitle)){
			System.out.println("Title Validation Sucess: " + actualTitle );
			TakesSceenShot();
		}else {
			// report failure
			reportFailure("Titles Did Not Match. Got title as "+ actualTitle);
		}
	}
	
	public void validateelementpresent(){
		if(!isElementPresent(objectKey)){
			System.out.println("Element Present: " + prop.getProperty(objectKey));
			TakesSceenShot();
		} else {
			// report failure
			reportFailure("Element Not Found: "+ prop.getProperty(objectKey));
		}
	}
	
	public void waitTillSelectionToBe(String objectkey , String expected) {
		int i=0;
		String actual="";
		while(i!=10){
			WebElement e = getObject(objectkey);
			Select s = new Select(e);
		    actual = s.getFirstSelectedOption().getText();
			if(actual.equals(expected))
				return;
			else
				wait(1);			
				i++;	
		}
		reportFailure("Values Dont match. Got value as "+actual);
	}
	public void quit(){
		if(driver!=null)
			driver.quit();
	}
	
	/*********************Utitlity Functions************************/
	// central function to extract Objects
	public WebElement getObject(String objectKey){
		WebElement e=null;
		try{
		if(objectKey.endsWith("_xpath"))
			e = driver.findElement(By.xpath(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_id"))
			e = driver.findElement(By.id(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_css"))
			e = driver.findElement(By.cssSelector(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_name"))
			e = driver.findElement(By.name(prop.getProperty(objectKey)));

		WebDriverWait wait = new WebDriverWait(driver,20);
		// visibility of Object
		wait.until(ExpectedConditions.visibilityOf(e));
		// state of the object-  clickable
		wait.until(ExpectedConditions.elementToBeClickable(e));
		
		}catch(Exception ex){
			// failure -  report that failure
			reportFailure("Object Not Found "+ objectKey);
		}
		return e;
	}
	// true - present
	// false - not present
	public boolean isElementPresent(String objectKey){
		List<WebElement> list=null;
		
		if(objectKey.endsWith("_xpath"))
			list = driver.findElements(By.xpath(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_id"))
			list = driver.findElements(By.id(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_css"))
			list = driver.findElements(By.cssSelector(prop.getProperty(objectKey)));
		else if(objectKey.endsWith("_name"))
			list = driver.findElements(By.name(prop.getProperty(objectKey)));

		if(list.size()==0)
			return false;
		else
			return true;
	}
	
	public boolean isElementInList(){
		// validate whether value is present in dropdown
				List<WebElement> options = new Select(getObject(objectKey)).getOptions();
				for(int i=0;i<options.size();i++){
					if(options.get(i).getText().equals(data.get(dataKey)))
						return true;
			}
				return false;
			}
	
	/*******Reporting function********/
	public void reportFailure(String failureMsg){
		// fail the test
		test.log(Status.FAIL, failureMsg);
		// take the screenshot, embed screenshot in reports
		TakesSceenShot();
	}
	
	public void TakesSceenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotfile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// take screenshot
		File srcfile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			// get the dynamic folder name
			FileUtils.copyFile(srcfile, new File(ExtentManager.screenshotfolderpath+screenshotfile));
			//put screenshot file in reports
			test.log(Status.INFO,"Screenshot-> "+ test.addScreenCaptureFromPath(ExtentManager.screenshotfolderpath+screenshotfile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void assertAll(){
		softAssert.assertAll();
	}
}
