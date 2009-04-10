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
    private String ignoreValue = "*";
    private boolean classLabelFirst = false;
    private boolean onefilePerItem = false;
    private boolean includeToken = false;
    private int includeTokenPreOffset = 1;
    private int includeTokenPostOffset = 4;
    private String targetWord = "";
    private boolean considerPunctuation = true;
    private boolean includeIgnoreValue = true;
    private boolean emphasizeNeighborhood = false;
    
    private Map<String, String> expectedSenseMap;
    private StringBuilder buffer;
    
    private static Set<String> excludePosSet;
    
    private Set<String> totalWordSet;
    
    private Set<String> missingWordSet;
    
    static {
        excludePosSet = new HashSet<String>();
        excludePosSet.add("x");
        excludePosSet.add("y");
        excludePosSet.add("z");
        excludePosSet.add("m");
        excludePosSet.add("f");
        
        excludePosSet.add("vi?");
        excludePosSet.add("ap");
        excludePosSet.add("ys");
        excludePosSet.add("bz");
        excludePosSet.add("d1");
        excludePosSet.add("az");
        excludePosSet.add("Ug");
        excludePosSet.add("ne");
        excludePosSet.add("d2");
        excludePosSet.add("nap");
        excludePosSet.add("nh");
        excludePosSet.add("nf");
        excludePosSet.add("o");
        excludePosSet.add("h");
        excludePosSet.add("Dg");
        excludePosSet.add("vd");
        excludePosSet.add("Tg");
        excludePosSet.add("nx");
        excludePosSet.add("Ag");
        excludePosSet.add("an");
        excludePosSet.add("k");
        excludePosSet.add("nt");
        excludePosSet.add("Vg");
        excludePosSet.add("z");
        excludePosSet.add("na");
        excludePosSet.add("nz");
        excludePosSet.add("ad");
        excludePosSet.add("s");
        excludePosSet.add("l");
    }
    
    public DataProcessing() {
        expectedSenseMap = getExpectedSenseMap();
        
        totalWordSet = this.getTotalWordSet();
        
        missingWordSet = new HashSet<String>();
    }
    
    public void process(String filePath, int startOffset, int endOffset) {
        buffer = new StringBuilder();
        try {
            Builder builder = new Builder(false);
            Document doc = builder.build(filePath);
            Element corpus = doc.getRootElement();
            Elements lexeltElements = corpus.getChildElements("lexelt");
            
            for (int k = 0; k < lexeltElements.size(); k++) {
                Element lexeltElement = lexeltElements.get(k);
                String item = lexeltElement.getAttributeValue("item");
                if (!targetWord.equals("") && !item.equals(targetWord)) {
                    continue;
                }
                // System.out.println(item);
                Elements instanceElements = lexeltElement.getChildElements("instance");
                for (int i = 0; i < instanceElements.size(); i++) {
                    Element instanceElement = instanceElements.get(i);
                    List<TokenEntry> tokenEntryList = getTokenEntryList(instanceElement);
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
                                printTokenEntrys(instanceId, instanceElement, tokenEntryList, currentIndex, startOffset, endOffset);  
                                break;
                            }
                            currentIndex++;
                            currentPrefixContext += token;
                        } else {
                            for (int m = 0; m < subwordElements.size(); m++) {
                                Element subwordElement = subwordElements.get(m);
                                String token = subwordElement.getFirstChildElement("token").getValue().trim();
                                if (token.equals(item) && currentPrefixContext.equals(prefixContext)) {
                                    printTokenEntrys(instanceId, instanceElement, tokenEntryList, currentIndex, startOffset, endOffset);   
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
                    if (includeToken) {
                        TextFile.write(filePath + "_" + startOffset + "_" + endOffset  + "_" + includeTokenPreOffset + "_" + includeTokenPostOffset + "_" + item + ".txt", buffer.toString());
                    } else {
                        TextFile.write(filePath + "_" + startOffset + "_" + endOffset + "_" + item + ".txt", buffer.toString());
                    }
                    buffer = new StringBuilder();
                } else {
                    buffer.append("<END>\n");
                }
            }
            if (!onefilePerItem) {
                if (includeToken) {
                    TextFile.write(filePath + "_" + startOffset + "_" + endOffset + "_" + includeTokenPreOffset + "_" + includeTokenPostOffset + ".txt", buffer.toString());
                } else {
                    TextFile.write(filePath + "_" + startOffset + "_" + endOffset + ".txt", buffer.toString()); 
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(missingWordSet);
    }
    
    private List<TokenEntry> getTokenEntryList(Element instanceElement) {
        Element postaggingElement = instanceElement.getFirstChildElement("postagging");
        List<TokenEntry> tokenEntryList = new ArrayList<TokenEntry>();
        
        Elements wordElements = postaggingElement.getChildElements("word");
        for (int j = 0; j < wordElements.size(); j++) {
            Element wordElement = wordElements.get(j);
            Elements subwordElements = wordElement.getChildElements("subword");
            if (subwordElements.size() == 0) {
                String token = wordElement.getFirstChildElement("token").getValue().trim();
                if (wordElement.getAttributeValue("pos").equals("w")) {
                    tokenEntryList.add(new TokenEntry("w", token));
                } else {
                    if (totalWordSet.contains(token)) {
                        tokenEntryList.add(new TokenEntry(wordElement.getAttributeValue("pos"), token));
                    } else {
                        missingWordSet.add(token);
                        tokenEntryList.add(new TokenEntry(wordElement.getAttributeValue("pos"), ignoreValue));
                    }
                }
            } else {
                for (int m = 0; m < subwordElements.size(); m++) {
                    Element subwordElement = subwordElements.get(m);
                    String token = subwordElement.getFirstChildElement("token").getValue().trim();
                    if (subwordElement.getAttributeValue("pos").equals("w")) {
                        tokenEntryList.add(new TokenEntry("w", token));
                    } else {
                        if (totalWordSet.contains(token)) {
                            tokenEntryList.add(new TokenEntry(subwordElement.getAttributeValue("pos"), token));
                        } else {
                            missingWordSet.add(token);
                            tokenEntryList.add(new TokenEntry(subwordElement.getAttributeValue("pos"), ignoreValue));
                        }
                    }
                }
            }
        }
        return tokenEntryList;
    }
    
    private void printTokenEntrys(String instanceId, Element instanceElement, List<TokenEntry> tokenEntryList, int index, int startOffset, int endOffset) {
        if (classLabelFirst) {
            printToken(instanceId, instanceElement, tokenEntryList, true, index, true);            
        }
        printTokenEntryItems(instanceId, instanceElement, tokenEntryList, true, index, startOffset, endOffset);
        if (includeToken) {
            printTokenEntryItems(instanceId, instanceElement, tokenEntryList, false, index, startOffset - includeTokenPreOffset, endOffset + includeTokenPostOffset);              
        }
        if (!classLabelFirst) {
            printToken(instanceId, instanceElement, tokenEntryList, true, index, true);            
        }
    }
    
    private void printTokenEntryItems(String instanceId, Element instanceElement, List<TokenEntry> tokenEntryList, boolean printPos, int index, int startOffset, int endOffset) {
        int punctuationIndex = -1;
        if (considerPunctuation) {
            for (int i = -1; i >= startOffset; i--) {
                if (index+i >= 0 && index+i < tokenEntryList.size()) {
                    if (tokenEntryList.get(index+i).getPos().equals("w")) {
                        punctuationIndex = index + i;
                        break; 
                    }
                }
            }
        }
        if (punctuationIndex != -1) {
            if (includeIgnoreValue) {
                for (int k = index + startOffset; k < punctuationIndex; k++) {
                    buffer.append(ignoreValue + separator);
                }
            }
            for (int k = punctuationIndex; k < index; k++) {
                printToken(instanceId, instanceElement, tokenEntryList, printPos, k, false);
            }
        } else {
            for (int k = index + startOffset; k < index; k++) {
                printToken(instanceId, instanceElement, tokenEntryList, printPos, k, false);
            }
        }
        
        int j = 0;
        for (j = 1; j <= endOffset; j++) {
            if (index+j >= 0 && index+j < tokenEntryList.size()) {
                if (!tokenEntryList.get(index+j).getPos().equals("w")
                        || !considerPunctuation) {
                    if (this.emphasizeNeighborhood && j == 1 && !printPos) {
                        printToken(instanceId, instanceElement, tokenEntryList, printPos, index + j, false);
                    }
                    printToken(instanceId, instanceElement, tokenEntryList, printPos, index + j, false);
                } else {
                    if (this.emphasizeNeighborhood && j == 1 && !printPos) {
                        printToken(instanceId, instanceElement, tokenEntryList, printPos, index + j, false);
                    }                    
                    printToken(instanceId, instanceElement, tokenEntryList, printPos, index + j, false);
                    break;
                }
            } else if (includeIgnoreValue) {
                buffer.append(ignoreValue + separator);
            }
        }
        if (includeIgnoreValue) {
            for (int k = j + 1; k <= endOffset; k++) {
                buffer.append(ignoreValue + separator);
            }
        }
    }
    private void printToken(String instanceId, Element instanceElement, List<TokenEntry> tokenEntryList, boolean printPos, int index, boolean target) {
        if (index >= 0 && index < tokenEntryList.size()) {    
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
                if (printPos) {
                    String pos = tokenEntryList.get(index).getPos();
                    buffer.append(pos + separator); 
                } else {
                    String token = tokenEntryList.get(index).getToken();
                    buffer.append(token + separator);
                }
            }
            // String token = wordElement.getFirstChildElement("token").getValue().trim();
            // System.out.print(token + separator);
        } else if (includeIgnoreValue) {
            buffer.append(ignoreValue + separator);
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

    public boolean isIncludeToken() {
        return includeToken;
    }

    public void setIncludeToken(boolean includeToken) {
        this.includeToken = includeToken;
    }

    public int getIncludeTokenPreOffset() {
        return includeTokenPreOffset;
    }

    public void setIncludeTokenPreOffset(int includeTokenPreOffset) {
        this.includeTokenPreOffset = includeTokenPreOffset;
    }

    public int getIncludeTokenPostOffset() {
        return includeTokenPostOffset;
    }

    public void setIncludeTokenPostOffset(int includeTokenPostOffset) {
        this.includeTokenPostOffset = includeTokenPostOffset;
    }
    
    public static HashSet<String> getTotalWordSet() {
        HashSet<String> wordSet = new HashSet<String>();
        try {
            Builder builder = new Builder(false);
            Document doc = builder.build("Chinese_train_pos.xml");
            Element corpus = doc.getRootElement();
            Elements lexeltElements = corpus.getChildElements("lexelt");
            
            for (int k = 0; k < lexeltElements.size(); k++) {
                Element lexeltElement = lexeltElements.get(k);
                Elements instanceElements = lexeltElement.getChildElements("instance");
                for (int i = 0; i < instanceElements.size(); i++) {
                    Element instanceElement = instanceElements.get(i);
                    Element postaggingElement = instanceElement.getFirstChildElement("postagging");                   
                    Elements wordElements = postaggingElement.getChildElements("word");
                    for (int j = 0; j < wordElements.size(); j++) {
                        Element wordElement = wordElements.get(j);
                        Elements subwordElements = wordElement.getChildElements("subword");
                        if (subwordElements.size() == 0) {
                            String token = wordElement.getFirstChildElement("token").getValue().trim();
                            if (!excludePosSet.contains(wordElement.getAttributeValue("pos"))) {
                                wordSet.add(token);
                            }      
                        } else {
                            for (int m = 0; m < subwordElements.size(); m++) {
                                Element subwordElement = subwordElements.get(m);
                                String token = subwordElement.getFirstChildElement("token").getValue().trim();
                                if (!excludePosSet.contains(subwordElement.getAttributeValue("pos"))) {
                                    wordSet.add(token);
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(wordSet);
        return wordSet;
    }

    public String getTargetWord() {
        return targetWord;
    }

    public void setTargetWord(String targetWord) {
        this.targetWord = targetWord;
    }

    public boolean isConsiderPunctuation() {
        return considerPunctuation;
    }

    public void setConsiderPunctuation(boolean considerPunctuation) {
        this.considerPunctuation = considerPunctuation;
    }

    public String getIgnoreValue() {
        return ignoreValue;
    }

    public void setIgnoreValue(String ignoreValue) {
        this.ignoreValue = ignoreValue;
    }

    public boolean isIncludeIgnoreValue() {
        return includeIgnoreValue;
    }

    public void setIncludeIgnoreValue(boolean includeIgnoreValue) {
        this.includeIgnoreValue = includeIgnoreValue;
    }

    public boolean isEmphasizeNeighborhood() {
        return emphasizeNeighborhood;
    }

    public void setEmphasizeNeighborhoodToken(boolean emphasizeNeighborhood) {
        this.emphasizeNeighborhood = emphasizeNeighborhood;
    }

}


class TokenEntry {
    
    private String pos;
    private String token;
    
    public TokenEntry(String pos, String token) {
        this.pos = pos;
        this.token = token;
    }
    
    public String getPos() {
        return pos;
    }
    public void setPos(String pos) {
        this.pos = pos;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    
}