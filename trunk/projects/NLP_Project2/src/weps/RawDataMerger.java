package weps;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import weps.util.TextFile;
import ci.textanalysis.lucene.impl.PhrasesCacheImpl;
import ci.textanalysis.lucene.impl.SynonymPhraseStopWordAnalyzer;
import ci.textanalysis.lucene.impl.SynonymsCacheImpl;

import com.google.common.base.Join;

public class RawDataMerger extends AbstractExtractor {

    private List<String> mergeDirs = new ArrayList<String>();
    private Analyzer analyzer;
    
    public RawDataMerger() throws IOException {
        this.analyzer
            = new SynonymPhraseStopWordAnalyzer(new SynonymsCacheImpl(), 
                    new PhrasesCacheImpl());
    }
    
    @Override
    protected void extractContent(String filePath, String name, String rank) {
        List<String> wordList = new ArrayList<String>();
        for (String mergeDir : this.mergeDirs) {
            String dataFilePath = mergeDir + "/" + name + "_" + rank + ".txt";
            wordList.addAll(new TextFile(dataFilePath, "[| \n]"));
        }
        System.out.println("Merge Raw Data " + name + " " + rank);
        String outputPath = this.getTargetDir() + "/" + name + "_" + rank + ".txt";
        
        wordList = removeStopWords(wordList);
        TextFile.write(outputPath, Join.join("|", wordList)); 
    }
    
    private List<String> removeStopWords(List<String> wordList) {
        List<String> cleanedWordList = new ArrayList<String>();
        Reader reader = new StringReader(Join.join(" ", wordList));
        TokenStream tokenStream = analyzer.tokenStream(null, reader);
        try {
            Token token = tokenStream.next();
            while (token != null) {
                cleanedWordList.add(token.termText());
                token = tokenStream.next();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cleanedWordList;
    }

    public void addMergeDir(String mergeDir) {
        this.mergeDirs.add(mergeDir);
    }

}
