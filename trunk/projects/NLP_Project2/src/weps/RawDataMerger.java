package weps;

import java.util.ArrayList;
import java.util.List;

import weps.util.TextFile;

import com.google.common.base.Join;

public class RawDataMerger extends AbstractExtractor {

    private List<String> mergeDirs = new ArrayList<String>();
    
    @Override
    protected void extractContent(String filePath, String name, String rank) {
        List<String> wordList = new ArrayList<String>();
        for (String mergeDir : this.mergeDirs) {
            String dataFilePath = mergeDir + "/" + name + "_" + rank + ".txt";
            wordList.addAll(new TextFile(dataFilePath, "[| \n]"));
        }
        System.out.println("Merge Raw Data " + name + " " + rank);
        String outputPath = this.getTargetDir() + "/" + name + "_" + rank + ".txt";
        TextFile.write(outputPath, Join.join("|", wordList)); 
    }
    
    public void addMergeDir(String mergeDir) {
        this.mergeDirs.add(mergeDir);
    }

}
