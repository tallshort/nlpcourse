package ci.textanalysis.lucene.impl;

import ci.textanalysis.Tag;

public class TagImpl implements Tag {

    private String displayText;
    private String stemmedText;
    
    public TagImpl(String displayText, String stemmedText) {
        this.displayText = displayText;
        this.stemmedText = stemmedText;
    }
    
    public String getDisplayText() {
        return this.displayText;
    }

    public String getStemmedText() {
        return this.stemmedText;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((stemmedText == null) ? 0 : stemmedText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TagImpl other = (TagImpl) obj;
        if (stemmedText == null) {
            if (other.stemmedText != null)
                return false;
        } else if (!stemmedText.equals(other.stemmedText))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + this.displayText + ", " + this.stemmedText + "]";
    }
}
