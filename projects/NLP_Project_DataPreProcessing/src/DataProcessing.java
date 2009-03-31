import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public void process(String filePath, int startOffset, int endOffset) {
        buffer = new StringBuilder();
        try {
            Builder builder = new Builder(false);
            Document doc = builder.build(filePath);
            Element corpus = doc.getRootElement();
            Elements lexeltElements = corpus.getChildElements("lexelt");
            
            // String target = "·¢";
            for (int k = 0; k < lexeltElements.size(); k++) {
                Element lexeltElement = lexeltElements.get(k);
                String item = lexeltElement.getAttributeValue("item");
                // buffer.append("item: " + item + "\n");
                Elements instanceElements = lexeltElement.getChildElements("instance");
                for (int i = 0; i < instanceElements.size(); i++) {
                    Element instanceElement = instanceElements.get(i);
                    List<String> tokenList = getTokenList(instanceElement);
                    String instanceId = instanceElement.getAttributeValue("id");
                    // buffer.append(instanceId + separator);
                    Element postaggingElement = instanceElement.getFirstChildElement("postagging");
                    Element contextElement = instanceElement.getFirstChildElement("context");
                    
                    String prefixContext = contextElement.getChild(0).getValue().trim();
                    String currentPrefixContext = "";
                    int currentIndex = 0;
                    
                    Elements wordElements = postaggingElement.getChildElements("word");
                    for (int j = 0; j < wordElements.size(); j++) {
                        Element wordElement = wordElements.get(j);
                        Elements subwordElements = wordElement.getChildElements("subword");
                        if (subwordElements.size() == 0) {
                            String token = wordElement.getFirstChildElement("token").getValue().trim();
                            if (token.equals(item) && currentPrefixContext.equals(prefixContext)) {
                                printTokens(instanceId, instanceElement, tokenList, currentIndex, startOffset, endOffset);  
                                break;
                            }
                            currentIndex++;
                            currentPrefixContext += token;
                        } else {
                            for (int m = 0; m < subwordElements.size(); m++) {
                                Element subwordElement = subwordElements.get(m);
                                String token = subwordElement.getFirstChildElement("token").getValue().trim();
                                if (token.equals(item) && currentPrefixContext.equals(prefixContext)) {
                                    printTokens(instanceId, instanceElement, tokenList, currentIndex, startOffset, endOffset);   
                                    break;
                                }
                                currentIndex++;
                                currentPrefixContext += token;
                            }
                        }

                    }
                    buffer.append("\n");
                }
                buffer.append("<END>\n");
            }
            TextFile.write(filePath + "_" + startOffset + "_" + endOffset + ".txt", buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private List<String> getTokenList(Element instanceElement) {
        Element postaggingElement = instanceElement.getFirstChildElement("postagging");
        List<String> posList = new ArrayList<String>();
        
        Elements wordElements = postaggingElement.getChildElements("word");
        for (int j = 0; j < wordElements.size(); j++) {
            Element wordElement = wordElements.get(j);
            Elements subwordElements = wordElement.getChildElements("subword");
            if (subwordElements.size() == 0) {
               posList.add(wordElement.getAttributeValue("pos"));
            } else {
                for (int m = 0; m < subwordElements.size(); m++) {
                    Element subwordElement = subwordElements.get(m);
                    posList.add(subwordElement.getAttributeValue("pos"));
                }
            }
        }
        return posList;
    }
    
    private void printTokens(String instanceId, Element instanceElement, List<String> posList, int index, int startOffset, int endOffset) {
        for (int i = startOffset; i <= endOffset; i++) {
            printToken(instanceId, instanceElement, posList, index + i, false);                 
        }
        printToken(instanceId, instanceElement, posList, index, true);
    }
    
    private void printToken(String instanceId, Element instanceElement, List<String> posList, int index, boolean target) {
        if (index >= 0 && index < posList.size()) {    
            if (target) {
                Element answerElement = instanceElement.getFirstChildElement("answer");
                String senseid = answerElement.getAttributeValue("senseid");
                if (senseid.equals("")) {
                    buffer.append(expectedSenseMap.get(instanceId));
                } else {
                    buffer.append(senseid); 
                }
            } else {
                String pos = posList.get(index);
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
