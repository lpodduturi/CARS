package com.perspecta.hybrid.suitea;

import org.testng.annotations.Test;
import java.util.Hashtable;

import org.testng.SkipException;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.perspecta.hybrid.base.BaseTest;
import com.perspecta.hybrid.util.Constants;
import com.perspecta.hybrid.util.DataUtil;
// name of java file should be same as name in xls file
public class InvalidLogin extends BaseTest {
	// make sure that i have my prop and xls file object init
	// before test starts
	
	@Test(dataProvider="getData")
	public void InvalidLoginTest(Hashtable<String,String> data) throws Exception{
		
		test.log(Status.INFO, "Starting "+ testName);

		if(DataUtil.isSkip(testName, xls) ||data.get(Constants.RUNMODE_COL).equals(Constants.RUNMODE_NO)){
			test.log(Status.SKIP, "Runmode is set to NO");
			throw new SkipException("Runmode is set to NO");
		}		
		System.out.println("Unable to Login, Please check credentials you entered");
	    ds.executeKeywords(testName, xls, data);
	}	
}
