package net.core;

import java.util.List;

import net.util.TextFile;

/**
 * ������ȡǰ��׺��Ϣ�����ݴ�������ģ��ʵ��
 */
public abstract class AbstractProcessor {  
    
    /**
     * ��������������ļ�
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
     * ���������ƺ����
     */
    public abstract void postProcess(); 
    
    /**
     * ���������������
     */
    public abstract void processDataLine(String[] parts);
    
    /**
     * �������
     */
    public abstract void processEmptyLine();
}
