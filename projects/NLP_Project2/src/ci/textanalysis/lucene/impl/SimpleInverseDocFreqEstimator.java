package ci.textanalysis.lucene.impl;

import java.util.HashMap;
import java.util.Map;

import ci.textanalysis.InverseDocFreqEstimator;
import ci.textanalysis.Tag;

public class SimpleInverseDocFreqEstimator implements InverseDocFreqEstimator {

    private Map<Tag, Integer> tagFerq;
    private int totalNumDocs;
    
    public SimpleInverseDocFreqEstimator(int totalNumDocs) {
        this.totalNumDocs = totalNumDocs;
        this.tagFerq = new HashMap<Tag, Integer>();
    }
    
    public double estimateInverseDocFreq(Tag tag) {
        Integer freq = this.tagFerq.get(tag);
        if ((freq == null) || (freq.intValue() == 0)) {
            return 1;
        } else {
            return Math.log(totalNumDocs / freq.doubleValue());
        }
    }
    
    public void addCount(Tag tag) {
        Integer count = this.tagFerq.get(tag);
        if (count == null) {
            count = 1;
        } else {
            count = count + 1;
        }
        this.tagFerq.put(tag, count);
    }

}
