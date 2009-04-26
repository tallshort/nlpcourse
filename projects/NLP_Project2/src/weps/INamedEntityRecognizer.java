package weps;

import java.util.List;

public interface INamedEntityRecognizer {

    public List<String> recognizeNamedEntities(String text);
    
}
