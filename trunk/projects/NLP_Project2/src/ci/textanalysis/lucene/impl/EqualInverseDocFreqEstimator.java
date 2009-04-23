package ci.textanalysis.lucene.impl;

import ci.textanalysis.InverseDocFreqEstimator;
import ci.textanalysis.Tag;

public class EqualInverseDocFreqEstimator implements InverseDocFreqEstimator {

    public double estimateInverseDocFreq(Tag tag) {
        return 1;
    }

}
