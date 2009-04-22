package ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ci.textanalysis.SynonymsCache;

public class SynonymsCacheImpl extends CacheImpl implements SynonymsCache {
    private Map<String, List<String>> synonyms;
    
    public SynonymsCacheImpl() throws IOException {
        this.synonyms = new HashMap<String, List<String>>();
        List<String> ciList = new ArrayList<String>();
        ciList.add("ci");
        this.synonyms.put(getStemmedText("collective intelligence"), ciList);
    }
    
    public List<String> getSynonym(String text) throws IOException {
        return this.synonyms.get(this.getStemmedText(text));
    }

}
