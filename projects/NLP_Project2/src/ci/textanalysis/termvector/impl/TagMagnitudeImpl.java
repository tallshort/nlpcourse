package ci.textanalysis.termvector.impl;

import ci.textanalysis.Tag;
import ci.textanalysis.TagMagnitude;

public class TagMagnitudeImpl implements TagMagnitude {

    private Tag tag;
    private double magnitude;

    public TagMagnitudeImpl(Tag tag, double magnitude) {
        this.tag = tag;
        this.magnitude = magnitude;
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public double getMagnitudeSqd() {
        return this.magnitude * this.magnitude;
    }

    public Tag getTag() {
        return this.tag;
    }

    public String getDisplayText() {
        return this.tag.getDisplayText();
    }

    public String getStemmedText() {
        return this.tag.getStemmedText();
    }

    @Override
    public String toString() {  
        return "[" + this.getDisplayText() + ", " + this.getStemmedText()
                + ", " + this.getMagnitude() + "]";
    }

    public int compareTo(TagMagnitude o) {
        double diff = this.magnitude - o.getMagnitude();
        if (diff > 0) {
            return -1;
        } else if (diff < 0) {
            return 1;
        } else {
            return 0;
        }
    }

}
