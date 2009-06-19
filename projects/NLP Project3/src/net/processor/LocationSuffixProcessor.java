package net.processor;

import java.util.HashMap;
import java.util.Map;

import net.core.AbstractProcessor;

/**
 * 地名后缀处理器，提取频繁地名后缀
 */
public class LocationSuffixProcessor extends AbstractProcessor {
    
    private Map<String, Integer> locSuffixMap = new HashMap<String, Integer>();
    
    private String suffix1;
    private String suffix2;
    
    @Override
    public void postProcess() {
        for (Map.Entry<String, Integer> entry : locSuffixMap.entrySet()) {
            if (entry.getValue() > 5) {
                System.out.println(entry);
            }
        }
        
    }

    @Override
    public void processDataLine(String[] parts) {
        String entityTag = parts[parts.length - 1];
        if (entityTag.equals("B-LOC")) {
            if (this.suffix1 != null && this.suffix2 != null) {
                int count = 0;
                String suffix = this.suffix2;
                if (locSuffixMap.containsKey(suffix)) {
                    count = locSuffixMap.get(suffix);
                }
                count++;
                locSuffixMap.put(suffix, count);
            }
            this.suffix1 = null;
            this.suffix2 = null;
        } else if (entityTag.equals("I-LOC")) {
            this.suffix1 = this.suffix2;
            this.suffix2 = parts[0];
        }
    }

    @Override
    public void processEmptyLine() {
        // TODO Auto-generated method stub
        
    }
    
}
