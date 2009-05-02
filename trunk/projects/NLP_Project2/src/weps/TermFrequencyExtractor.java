package weps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weps.util.TextFile;

public class TermFrequencyExtractor extends AbstractExtractor {
    
    
    private String termDir = ".";
    Map<String, Integer> termFrequencyMap = new HashMap<String, Integer>();
    
    @Override
    protected void extractContent(String filePath, String name, String rank) {
        String dataFilePath
            = this.termDir + "/" + name + "_" + rank + ".txt";
        List<String> wordList = new TextFile(dataFilePath, "[|]");
        for (String word : wordList) {
            int count = 0;
            if (termFrequencyMap.containsKey(word)) {
                count = termFrequencyMap.get(word);
            }
            termFrequencyMap.put(word, count + 1);
        }
    }

    public void doStatistics() {
        ArrayList<Map.Entry<String, Integer>> termFrequencyList = new ArrayList<Map.Entry<String, Integer>>(
                termFrequencyMap.entrySet());
        Collections.sort(termFrequencyList,
                new Comparator<Map.Entry<String, Integer>>() {

                    public int compare(Map.Entry<String, Integer> entry1,
                            Map.Entry<String, Integer> entry2) {
                        return entry1.getValue().compareTo(entry2.getValue());
                    }

                });

        StringBuilder buffer = new StringBuilder();
        for (Map.Entry<String, Integer> entry : termFrequencyList) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
            buffer.append(entry.getKey() + ": " + entry.getValue() + "\n");
        }
        TextFile.write(this.getTargetDir() + "/term_frequency.txt", buffer.toString());
    }

    public String getTermDir() {
        return termDir;
    }

    public void setTermDir(String termDir) {
        this.termDir = termDir;
    }


}
