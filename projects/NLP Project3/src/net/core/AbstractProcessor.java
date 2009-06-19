package net.core;

import java.util.List;

import net.util.TextFile;

/**
 * 数据处理器的模版实现
 */
public abstract class AbstractProcessor {  
    
    public void process(String dataFile) {
        List<String> dataLines = new TextFile(dataFile);
        for (String line : dataLines) {
            if (!line.equals("")) {
                String[] parts = line.split(" ");
                this.processDataLine(parts);
            } else {
                this.processEmptyLine();
            }
        }
        postProcess();
    }
    
    public abstract void postProcess(); 
    
    public abstract void processDataLine(String[] parts);
    
    public abstract void processEmptyLine();
}
