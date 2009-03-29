import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;


public class DataProcessing {
    
    private String separator = ",";
    
    private Map<String, String> expectedSenseMap;
    private StringBuilder buffer;
    
    public DataProcessing() {
        expectedSenseMap = getExpectedSenseMap();
    }
    
    public void process(String filePath, int start, int end) {
        buffer = new StringBuilder();
        try {
            Builder builder = new Builder(false);
            Document doc = builder.build(filePath);
            Element corpus = doc.getRootElement();
            Elements lexeltElements = corpus.getChildElements("lexelt");
            
            // String target = "·¢";
            for (int k = 0; k < lexeltElements.size(); k++) {
                Element lexeltElement = lexeltElements.get(k);
//                if (!lexeltElement.getAttributeValue("item").equals(target)) {
//                    continue;
//                }
                String item = lexeltElement.getAttributeValue("item");
                // buffer.append("item: " + item + "\n");
                Elements instanceElements = lexeltElement.getChildElements("instance");
                for (int i = 0; i < instanceElements.size(); i++) {
                    Element instanceElement = instanceElements.get(i);
                    String instanceId = instanceElement.getAttributeValue("id");
                    // System.out.print(instanceId + separator);
                    Element postaggingElement = instanceElement.getFirstChildElement("postagging");
                    Element contextElement = instanceElement.getFirstChildElement("context");
                    
                    String prefixContext = contextElement.getChild(0).getValue().trim();
                    String currentPrefixContext = "";
                    
                    Elements wordElements = postaggingElement.getChildElements("word");
                    for (int j = 0; j < wordElements.size(); j++) {
                        Element wordElement = wordElements.get(j);
                        Elements subwordElements = wordElement.getChildElements("subword");
                        if (subwordElements.size() == 0) {
                            String token = wordElement.getFirstChildElement("token").getValue().trim();
                            if (token.equals(item) && currentPrefixContext.equals(prefixContext)) {
                                printTokens(instanceId, instanceElement, wordElements, j, start, end);  
                                break;
                            }
                            currentPrefixContext += token;
                        } else {
                            for (int m = 0; m < subwordElements.size(); m++) {
                                Element subwordElement = subwordElements.get(m);
                                String token = subwordElement.getFirstChildElement("token").getValue().trim();
                                if (token.equals(item) && currentPrefixContext.equals(prefixContext)) {
                                    printTokens(instanceId, instanceElement, subwordElements, m, start, end);   
                                    break;
                                }
                                currentPrefixContext += token;
                            }
                        }

                    }
                    buffer.append("\n");
                }
                buffer.append("<END>\n");
            }
            TextFile.write(filePath + "_" + start + "_" + end + ".txt", buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void printTokens(String instanceId, Element instanceElement, Elements wordElements, int index, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (i != 0) {
                printToken(instanceId, instanceElement, wordElements, index + i, false);                
            }  
        }
        printToken(instanceId, instanceElement, wordElements, index, true);
    }
    
    private void printToken(String instanceId, Element instanceElement, Elements wordElements, int index, boolean target) {
        if (index >= 0 && index < wordElements.size()) {
            Element wordElement = wordElements.get(index);        
            if (target) {
                Element answerElement = instanceElement.getFirstChildElement("answer");
                String senseid = answerElement.getAttributeValue("senseid");
                if (senseid.equals("")) {
                    buffer.append(expectedSenseMap.get(instanceId));
                } else {
                    buffer.append(senseid); 
                }
            } else {
                String pos = wordElement.getAttributeValue("pos");
                buffer.append(pos + separator);               
            }
            // String token = wordElement.getFirstChildElement("token").getValue().trim();
            // System.out.print(token + separator);
        } else {
            buffer.append("*" + separator);
        }
    }
    
    public Map<String, String> getExpectedSenseMap() {
        Map<String, String> expectedSenseMap = new HashMap<String, String>();
        ArrayList<String> expectedSenseList = new TextFile("ChineseLS.test.key");
        for (String line : expectedSenseList) {
            String[] parts = line.split(" ");
            expectedSenseMap.put(parts[1].replaceAll("\\.0(\\d)$", ".$1"), parts[2]);
        }
        // System.out.println(expectedSenseMap);
        return expectedSenseMap;
    }

}
