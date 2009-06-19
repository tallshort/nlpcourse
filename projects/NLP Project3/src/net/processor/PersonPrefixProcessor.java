package net.processor;

import java.util.HashMap;
import java.util.Map;

import net.core.AbstractProcessor;

/**
 * 人名前缀处理器，提取频繁人名前缀
 */
public class PersonPrefixProcessor extends AbstractProcessor {

    private Map<String, Integer> personPrefixMap = new HashMap<String, Integer>();

    @Override
    public void postProcess() {
        for (Map.Entry<String, Integer> entry : personPrefixMap.entrySet()) {
            if (entry.getValue() > 5) {
                System.out.println(entry);
            }
        }
    }

    @Override
    public void processDataLine(String[] parts) {
        String entityTag = parts[parts.length - 1];
        if (entityTag.equals("B-PER")) {
            int count = 0;
            String prefix = parts[0];
            if (personPrefixMap.containsKey(prefix)) {
                count = personPrefixMap.get(prefix);
            }
            count++;
            personPrefixMap.put(prefix, count);

        }
    }

    @Override
    public void processEmptyLine() {

    }

}
