package ci.textanalysis.termvector.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ci.textanalysis.Tag;
import ci.textanalysis.TagMagnitude;
import ci.textanalysis.TagMagnitudeVector;

public class TagMagnitudeVectorImpl implements TagMagnitudeVector {

    private Map<Tag, TagMagnitude> tagMagnitudeMap;
    
    public TagMagnitudeVectorImpl(List<TagMagnitude> tagMagnitudes) {
        this.normalize(tagMagnitudes);
    }
    
    private void normalize(List<TagMagnitude> tagMagnitudes) {
        tagMagnitudeMap = new HashMap<Tag, TagMagnitude>();
        if ((tagMagnitudes == null) || (tagMagnitudes.size() == 0)) {
            return;
        }
        double sumSqd = 0;
        for (TagMagnitude tm : tagMagnitudes) {
            sumSqd += tm.getMagnitudeSqd();
        }
        if (sumSqd == 0) {
            sumSqd = 1.0 / tagMagnitudes.size();
        }
        double normFactor = Math.sqrt(sumSqd);
        for (TagMagnitude tm : tagMagnitudes) {
            TagMagnitude otherTm = this.tagMagnitudeMap.get(tm.getTag());
            double magnitude = tm.getMagnitude();
            if (otherTm != null) {
                magnitude = mergeMagnitudes(magnitude, 
                        otherTm.getMagnitude() * normFactor);
            }
            TagMagnitude normalizedTm = new TagMagnitudeImpl(tm.getTag(), 
                    (magnitude / normFactor));
            this.tagMagnitudeMap.put(tm.getTag(), normalizedTm);
        }
    }

    private double mergeMagnitudes(double a, double b) {
        return Math.sqrt(a * a + b * b);
    }

    public TagMagnitudeVector add(TagMagnitudeVector o) {
        Map<Tag, TagMagnitude> otherMap = o.getTagMagnitudeMap();
        Set<Tag> uniqueTags = new HashSet<Tag>();
        uniqueTags.addAll(this.tagMagnitudeMap.keySet());
        uniqueTags.addAll(otherMap.keySet());
        List<TagMagnitude> tagMagnitudeList
            = new ArrayList<TagMagnitude>(uniqueTags.size());
        for (Tag tag : uniqueTags) {
            TagMagnitude tm = mergeTagMagnitudes(this.tagMagnitudeMap.get(tag), otherMap.get(tag));
            tagMagnitudeList.add(tm);
        }
        return new TagMagnitudeVectorImpl(tagMagnitudeList);
    }

    private TagMagnitude mergeTagMagnitudes(TagMagnitude a,
            TagMagnitude b) {
        if (a == null) {
            if (b == null) {
                return null;
            }
            return b;
        } else if (b == null) {
            return a;
        } else {
            double magnitude = mergeMagnitudes(a.getMagnitude(), b.getMagnitude());
            return new TagMagnitudeImpl(a.getTag(), magnitude);
        }
    }

    public TagMagnitudeVector add(Collection<TagMagnitudeVector> tmList) {
        Map<Tag, Double> uniqueTags = new HashMap<Tag, Double>();
        for (TagMagnitude tagMagnitude : this.tagMagnitudeMap.values()) {
            uniqueTags.put(tagMagnitude.getTag(), tagMagnitude.getMagnitudeSqd());
        }
        for (TagMagnitudeVector tmv : tmList) {
            Map<Tag, TagMagnitude> tagMap = tmv.getTagMagnitudeMap();
            for (TagMagnitude tm : tagMap.values()) {
                Double sumSqd = uniqueTags.get(tm.getTag());
                if (sumSqd == null) {
                    uniqueTags.put(tm.getTag(), tm.getMagnitudeSqd());
                } else {
                    sumSqd = new Double(sumSqd.doubleValue() + tm.getMagnitudeSqd());
                    uniqueTags.put(tm.getTag(), sumSqd);
                }
            }
        }
        List<TagMagnitude> newList = new ArrayList<TagMagnitude>();
        for (Tag tag : uniqueTags.keySet()) {
            newList.add(new TagMagnitudeImpl(tag, 
                    Math.sqrt(uniqueTags.get(tag))));
        }
        return new TagMagnitudeVectorImpl(newList);
    } 

    public double dotProduct(TagMagnitudeVector o) {
        Map<Tag, TagMagnitude> otherMap = o.getTagMagnitudeMap();
        double dotProduct = 0;
        for (Tag tag : this.tagMagnitudeMap.keySet()) {
            TagMagnitude otherTm = otherMap.get(tag);
            if (otherTm != null) {
                TagMagnitude tm = this.tagMagnitudeMap.get(tag);
                dotProduct += tm.getMagnitude() * otherTm.getMagnitude();
            }
        }
        return dotProduct;
    }

    public Map<Tag, TagMagnitude> getTagMagnitudeMap() {
        return this.tagMagnitudeMap;
    }

    public List<TagMagnitude> getTagMagnitudes() {
        List<TagMagnitude> sortedTagMagnitudes = new ArrayList<TagMagnitude>();
        sortedTagMagnitudes.addAll(this.tagMagnitudeMap.values());
        Collections.sort(sortedTagMagnitudes);
        return sortedTagMagnitudes;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<TagMagnitude> sortedList = this.getTagMagnitudes();
        double sumSqd = 0;
        for (TagMagnitude tm : sortedList) {
            sb.append(tm);
            sumSqd += tm.getMagnitudeSqd();
        }
        sb.append("\nSumSqd = " + sumSqd);
        return sb.toString();
    }

}
