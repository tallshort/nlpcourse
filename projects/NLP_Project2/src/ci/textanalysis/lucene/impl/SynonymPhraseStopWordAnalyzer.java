package ci.textanalysis.lucene.impl;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import ci.textanalysis.PhrasesCache;
import ci.textanalysis.SynonymsCache;

public class SynonymPhraseStopWordAnalyzer extends Analyzer {
    private SynonymsCache synonymsCache;
    private PhrasesCache phrasesCache;
    
    public SynonymPhraseStopWordAnalyzer(SynonymsCache synonymsCache, PhrasesCache phrasesCache) {
        this.synonymsCache = synonymsCache;
        this.phrasesCache = phrasesCache;
    }
    
    @Override
    public TokenStream tokenStream(String fieldName, Reader reader) {
        Tokenizer tokenizer = new StandardTokenizer(reader);
        TokenFilter lowerCaseFilter = new LowerCaseFilter(tokenizer);
        TokenFilter stopFilter = new StopFilter(lowerCaseFilter, PorterStemStopWordAnalyzer.stopWords);
        return new SynonymPhraseStopWordFilter(stopFilter, this.synonymsCache, this.phrasesCache);
    }

}
