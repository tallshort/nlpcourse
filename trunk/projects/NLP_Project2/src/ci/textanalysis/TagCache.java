package ci.textanalysis;

import java.io.IOException;

public interface TagCache {
    public Tag getTag(String text) throws IOException;
}
