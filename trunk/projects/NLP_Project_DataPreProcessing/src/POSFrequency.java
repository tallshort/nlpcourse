import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

public class POSFrequency {

    public static void main(String[] args) {
        // doStatisticsForFlatFile("pos.txt");
        // doStatisticsForXMLFile("pos.xml");
        doStatisticsForXMLFile("Chinese_train_pos.xml");
        for (int i = -3; i <= 0; i++) {
            for (int j = 0; j <= 3; j++) {
                // At least one tokens as the features
                if (Math.abs(i - j) >= 1) {
                    doStatisticsForFlatFile("Chinese_train_pos.xml" + "_" + i
                            + "_" + j + ".txt");
                }
            }
        }
    }

    public static void doStatisticsForXMLFile(String filePath) {
        try {
            Builder builder = new Builder(false);
            Document doc = builder.build(filePath);
            Element corpus = doc.getRootElement();
            Elements lexeltElements = corpus.getChildElements("lexelt");

            Map<String, Integer> posMap = new HashMap<String, Integer>();

            for (int k = 0; k < lexeltElements.size(); k++) {
                Element lexeltElement = lexeltElements.get(k);
                Elements instanceElements = lexeltElement
                        .getChildElements("instance");
                for (int i = 0; i < instanceElements.size(); i++) {
                    Element instanceElement = instanceElements.get(i);
                    Element postaggingElement = instanceElement
                            .getFirstChildElement("postagging");
                    Elements wordElements = postaggingElement
                            .getChildElements("word");
                    for (int j = 0; j < wordElements.size(); j++) {
                        Element wordElement = wordElements.get(j);
                        Elements subwordElements = wordElement
                                .getChildElements("subword");
                        if (subwordElements.size() == 0) {
                            String pos = wordElement.getAttributeValue("pos");
                            doIncrement(posMap, pos);
                        } else {
                            for (int m = 0; m < subwordElements.size(); m++) {
                                Element subwordElement = subwordElements.get(m);
                                String pos = subwordElement
                                        .getAttributeValue("pos");
                                doIncrement(posMap, pos);
                            }
                        }
                    }
                }
            }
            doStatistics(filePath, posMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doIncrement(Map<String, Integer> posMap, String pos) {
        int count = 0;
        if (posMap.containsKey(pos)) {
            count = posMap.get(pos);
        }
        posMap.put(pos, count + 1);
    }

    public static void doStatisticsForFlatFile(String filePath) {
        Map<String, Integer> posMap = new HashMap<String, Integer>();
        ArrayList<String> lines = new TextFile(filePath);
        for (String line : lines) {
            String[] parts = line.split(",");
            for (int i = 0; i < parts.length - 1; i++) {
                int count = 0;
                String pos = parts[i];
                if (posMap.containsKey(pos)) {
                    count = posMap.get(pos);
                }
                posMap.put(pos, count + 1);
            }
        }

        doStatistics(filePath, posMap);
    }

    private static void doStatistics(String filePath,
            Map<String, Integer> posMap) {
        ArrayList<Map.Entry<String, Integer>> posFrequencyList = new ArrayList<Map.Entry<String, Integer>>(
                posMap.entrySet());
        Collections.sort(posFrequencyList,
                new Comparator<Map.Entry<String, Integer>>() {

                    public int compare(Entry<String, Integer> entry1,
                            Entry<String, Integer> entry2) {
                        return entry1.getValue().compareTo(entry2.getValue());
                    }

                });

        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<String, Integer> entry : posFrequencyList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            buffer.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        TextFile.write("pos_frequency_" + filePath.replace(".xml", ".txt"), buffer.toString());
    }

}
