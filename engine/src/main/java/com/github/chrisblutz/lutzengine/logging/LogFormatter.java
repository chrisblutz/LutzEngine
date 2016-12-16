package com.github.chrisblutz.lutzengine.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * @author Christopher Lutz
 */
public class LogFormatter extends Formatter {
    
    @Override
    public String format(LogRecord record) {
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(record.getMillis());
        
        String dateStr = String.format("%s/%s/%s %s:%02d:%02d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE), cal.get(Calendar.YEAR), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND));
        
        String logStr = String.format("%s [%s] - %s: %s\n", dateStr, record.getLoggerName(), record.getLevel(), record.getMessage());
        
        try {
            
            if (record.getThrown() != null) {
                
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                
                record.getThrown().printStackTrace(pw);
                
                pw.close();
                sw.close();
                
                logStr += sw.toString() + "\n";
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return logStr;
    }
}
