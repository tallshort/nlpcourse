package weps.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import weps.AbstractBodyExtractor;

public class UnitTest {
    
    @Test
    public void testBodyExtractor() {
        AbstractBodyExtractor extractor = new AbstractBodyExtractor() {
            public String extractBody(String filePath) {
               return null;
            }            
        };
        extractor.setDatasetDir("weps2007/test");
        List<String> nameList = extractor.getPeopleNameList();
        assertEquals(30, nameList.size());
        extractor.setDatasetDir("weps2007/training");
        nameList = extractor.getPeopleNameList();
        assertEquals(49, nameList.size());
    }
}
