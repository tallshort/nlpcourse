package ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.HashSet;

import ci.textanalysis.PhrasesCache;

public class PhrasesCacheImpl extends CacheImpl implements PhrasesCache {
    
    private HashSet<String> validPhrases;
    
    public PhrasesCacheImpl() throws IOException {
        validPhrases = new HashSet<String>();
        validPhrases.add(this.getStemmedText("collective intelligence"));
    }
    
    public boolean isValidPhrase(String text) throws IOException {
        return this.validPhrases.contains(this.getStemmedText(text));
    }

}
