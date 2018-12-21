package com.perspecta.hybrid.reports;


import java.io.File;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    
    private static ExtentReports extent;
    public static String screenshotfolderpath;
    
    public static ExtentReports getInstance(String reportpath) {
    	if (extent == null){
    		String fileName="Report.html";
    		Date d = new Date();
    		String folderName=d.toString().replace(":", "_").replace(" ", "_");
    		
    		// directory of the report folder
    		new File(reportpath+folderName+"//screenshots").mkdirs();
    		reportpath=reportpath+folderName+"//";
    		screenshotfolderpath=reportpath+"screenshots//";
    		System.out.println(reportpath+fileName);
    		createInstance(reportpath+fileName);
    	}
    	
        return extent;
    }
    
    public static ExtentReports createInstance(String fileName) {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(fileName);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.BOTTOM);
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Reports");
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setReportName("Reports - CARS Automation Testing");
        
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        
        return extent;
    }
}