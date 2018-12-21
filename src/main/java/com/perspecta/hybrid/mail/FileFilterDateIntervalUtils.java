package com.perspecta.hybrid.mail;


import java.io.*;
import java.text.*;
import java.util.*;

public class FileFilterDateIntervalUtils implements FilenameFilter {
    String dateStart;
    String dateEnd;
    SimpleDateFormat sdf;

    public FileFilterDateIntervalUtils(String dateStart, String dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        sdf = new SimpleDateFormat("yyyy-MM-dd");
    }

    public boolean accept(File dir, String name) {
        Date d = new Date(new File(dir, name).lastModified());
        String current = sdf.format(d);
        return ((dateStart.compareTo(current) < 0
                && (dateEnd.compareTo(current) >= 0)));
    }
    
    
}