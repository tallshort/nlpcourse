package net.processor;

import java.util.HashMap;
import java.util.Map;

import net.core.AbstractProcessor;

/**
 * 机构名后缀处理器，提取频繁机构名后缀
 */
public class OrganizationSuffixProcessor extends AbstractProcessor {
    
    private Map<String, Integer> orgSuffixMap = new HashMap<String, Integer>();
    
    private String suffix1;
    private String suffix2;
    
    @Override
    public void postProcess() {
        for (Map.Entry<String, Integer> entry : orgSuffixMap.entrySet()) {
            if (entry.getValue() > 5) {
                System.out.println(entry);
            }
        }
        
    }

    @Override
    public void processDataLine(String[] parts) {
        String entityTag = parts[parts.length - 1];
        if (entityTag.equals("B-ORG")) {
            if (this.suffix1 != null && this.suffix2 != null) {
                int count = 0;
                String suffix = this.suffix2;
                if (orgSuffixMap.containsKey(suffix)) {
                    count = orgSuffixMap.get(suffix);
                }
                count++;
                orgSuffixMap.put(suffix, count);
            }
            this.suffix1 = null;
            this.suffix2 = null;
        } else if (entityTag.equals("I-ORG")) {
            this.suffix1 = this.suffix2;
            this.suffix2 = parts[0];
        }
    }

    @Override
    public void processEmptyLine() {
        
    }
    
}
