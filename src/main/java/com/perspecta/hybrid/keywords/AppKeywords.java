package com.perspecta.hybrid.keywords;

import java.util.concurrent.TimeUnit;


//import org.openqa.selenium.By;

import com.aventstack.extentreports.Status;

public class AppKeywords extends GenericKeywords{
	
	public void defaultlogin(){
			String username=envProp.getProperty("adminusername");
			String password=envProp.getProperty("adminpassword");
			System.out.println("Default username "+username );
			System.out.println("Default password "+password );
	}
	
	public void validatelogin(){
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		test.log(Status.INFO,"Validating Login");
		boolean validatelogin = isElementPresent("loginhomepage_xpath");
	    if(validatelogin == true) {	
			System.out.println(dataKey);
			TakesSceenShot();		
		}else {
			System.out.println("Loginfailed");
			TakesSceenShot();
			reportFailure("Loginfailed");
		}
	}		
}