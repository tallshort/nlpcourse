import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;


public class DataProcessing {
    
    private String separator = ",";
    private boolean classLabelFirst = false;
    private boolean onefilePerItem = false;
    
    private Map<String, String> expectedSenseMap;
    private StringBuilder buffer;
    
    private Set<String> ignoreWords;
    
    public DataProcessing() {
        expectedSenseMap = getExpectedSenseMap();
        ignoreWords = new HashSet<String>();
        ignoreWords.add("£¬");
        ignoreWords.add("¡£");
        ignoreWords.add("£¿");
        ignoreWords.add("¡°");
        ignoreWords.add("¡±");
        ignoreWords.add("£º");
        ignoreWords.add("£¡");
        ignoreWords.add("£©");
        ignoreWords.add("£¨");
        ignoreWords.add("£»");
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
                // System.out.println(item);
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
                if (onefilePerItem) {
                    TextFile.write(filePath + "_" + startOffset + "_" + endOffset + "_" + item + ".txt", buffer.toString());
                    buffer = new StringBuilder();
                } else {
                    buffer.append("<END>\n");
                }
            }
            if (!onefilePerItem) {
                TextFile.write(filePath + "_" + startOffset + "_" + endOffset + ".txt", buffer.toString());
            }
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
                String token = wordElement.getFirstChildElement("token").getValue().trim();
                if (ignoreWords.contains(token)) {
                    posList.add("*");
                } else {
                    posList.add(wordElement.getAttributeValue("pos"));
                }
            } else {
                for (int m = 0; m < subwordElements.size(); m++) {
                    Element subwordElement = subwordElements.get(m);
                    String token = subwordElement.getFirstChildElement("token").getValue().trim();
                    if (ignoreWords.contains(token)) {
                        posList.add("*");
                    } else {
                        posList.add(subwordElement.getAttributeValue("pos"));
                    }
                }
            }
        }
        return posList;
    }
    
    private void printTokens(String instanceId, Element instanceElement, List<String> posList, int index, int startOffset, int endOffset) {
        if (classLabelFirst) {
            printToken(instanceId, instanceElement, posList, index, true);            
        }
        int starIndex = -1;
        for (int i = -1; i >= startOffset; i--) {
            if (index+i >= 0 && index+i < posList.size()) {
                if (posList.get(index+i).equals("*")) {
                    starIndex = index + i;
                    break;
                    
                }
            }
        }
        if (starIndex != -1) {
            for (int k = index + startOffset; k <= starIndex; k++) {
                buffer.append("*" + separator);
            }
            for (int k = starIndex + 1; k < index; k++) {
                printToken(instanceId, instanceElement, posList, k, false);
            }
        } else {
            for (int k = index + startOffset; k < index; k++) {
                printToken(instanceId, instanceElement, posList, k, false);
            }
        }
        
        int j = 0;
        for (j = 1; j <= endOffset; j++) {
            if (index+j >= 0 && index+j < posList.size()) {
                if (!posList.get(index+j).equals("*")) {
                    printToken(instanceId, instanceElement, posList, index + j, false);
                } else {
                    break;
                }
            } else {
                buffer.append("*" + separator);
            }
        }
        for (int k = j; k <= endOffset; k++) {
            buffer.append("*" + separator);
        }
        if (!classLabelFirst) {
            printToken(instanceId, instanceElement, posList, index, true);
        }
    }
    private void printToken(String instanceId, Element instanceElement, List<String> posList, int index, boolean target) {
        if (index >= 0 && index < posList.size()) {    
            if (target) {
                Element answerElement = instanceElement.getFirstChildElement("answer");
                String senseid = answerElement.getAttributeValue("senseid");
                if (senseid.equals("")) {
                    if (classLabelFirst) {
                        buffer.append(expectedSenseMap.get(instanceId) + separator);
                    } else {
                        buffer.append(expectedSenseMap.get(instanceId));
                    }
                } else {
                    if (classLabelFirst) {
                        buffer.append(senseid + separator);
                    } else {
                        buffer.append(senseid);
                    }
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

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public boolean isClassLabelFirst() {
        return classLabelFirst;
    }

    public void setClassLabelFirst(boolean classLabelFirst) {
        this.classLabelFirst = classLabelFirst;
    }

    public boolean isOnefilePerItem() {
        return onefilePerItem;
    }

    public void setOnefilePerItem(boolean onefilePerItem) {
        this.onefilePerItem = onefilePerItem;
    }

}
