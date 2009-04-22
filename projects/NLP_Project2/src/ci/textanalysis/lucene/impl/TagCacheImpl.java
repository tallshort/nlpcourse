package ci.textanalysis.lucene.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ci.textanalysis.Tag;
import ci.textanalysis.TagCache;

public class TagCacheImpl extends CacheImpl implements TagCache {

    private Map<String, Tag> tagMap;
    
    public TagCacheImpl() {
        this.tagMap = new HashMap<String, Tag>();
    }
    
    public Tag getTag(String text) throws IOException {
        Tag tag = this.tagMap.get(text);
        if (tag == null) {
            String stemmedText = this.getStemmedText(text);
            tag = new TagImpl(text, stemmedText);
            this.tagMap.put(stemmedText, tag);
        }
        return tag;
    }

}
