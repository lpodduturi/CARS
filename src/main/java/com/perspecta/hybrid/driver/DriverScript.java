package com.perspecta.hybrid.driver;

//import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Properties;

import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.Status;
import com.perspecta.hybrid.keywords.AppKeywords;
import com.perspecta.hybrid.util.Constants;
import com.perspecta.hybrid.util.Xls_Reader;

public class DriverScript {
	public Properties envProp;
	public Properties prop;
	public ExtentTest test;
	AppKeywords app;
	
	public void executeKeywords(String testName, Xls_Reader xls, Hashtable<String,String> testData) throws Exception{
		int rows = xls.getRowCount(Constants.KEYWORDS_SHEET);
		System.out.println("Rows "+ rows);
		app = new AppKeywords();
		// send prop to keywords class
		app.setEnvProp(envProp);
		app.setProp(prop);
		System.out.print("Property Info - " + prop);
		// send the data
		app.setData(testData);
		app.setExtentTest(test);
		for(int rNum=2;rNum<=rows;rNum++){
		String tcid = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.TCID_COL, rNum);
			if(tcid.equals(testName)){
				String keyword = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.KEYWORD_COL, rNum);
				String objectKey = xls.getCellData(Constants.KEYWORDS_SHEET, Constants.OBJECT_COL, rNum);
				String dataKey= xls.getCellData(Constants.KEYWORDS_SHEET, Constants.DATA_COL, rNum);
				//String proceedOnFail=xls.getCellData(Constants.KEYWORDS_SHEET, Constants.PROCEED_COL, rNum);
				@SuppressWarnings("unused")
				String data = testData.get(dataKey);
				//System.out.println(tcid +" --- "+ keyword+" --- "+ prop.getProperty(objectKey)+" --- "+ data);
				//test.log(Status.INFO, tcid +" --- "+ keyword+" --- "+ prop.getProperty(objectKey)+" --- "+ data);
				app.setDataKey(dataKey);
				System.out.println("Value sent:" + dataKey);
				app.setObjectKey(objectKey);
				System.out.println("Value Entered:" + objectKey);
				//app.setProceedOnFail(proceedOnFail);
				// Reflections Api
				Method method;
				method = app.getClass().getMethod(keyword);
				System.out.println("data for:"+ method );
				method.invoke(app);
					
				
			}
		}
		app.assertAll();
		
	}


	public Properties getEnvProp() {
		return envProp;
	}


	public void setEnvProp(Properties envProp) {
		this.envProp = envProp;
	}


	public Properties getProp() {
		return prop;
	}


	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	public void setExtentTest(ExtentTest test) {
		this.test = test;
	}
	
	public void quit(){
		if(app!=null)
		app.quit();
	}
	
	
	
}
