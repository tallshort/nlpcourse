package net.core;

import java.util.List;

import net.util.TextFile;

/**
 * 用于提取前后缀信息的数据处理器的模版实现
 */
public abstract class AbstractProcessor {  
    
    /**
     * 处理给定的数据文件
     */
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
    
    /**
     * 处理完后的善后操作
     */
    public abstract void postProcess(); 
    
    /**
     * 处理给定的数据行
     */
    public abstract void processDataLine(String[] parts);
    
    /**
     * 处理空行
     */
    public abstract void processEmptyLine();
}
