package weps.test;

import java.util.List;

import weps.AbstractBodyExtractor;
import weps.PythonBodyExtractor;

public class Main {

    public static void main(String[] args) {
        testExtractor();
    }

    private static void testExtractor() {
        AbstractBodyExtractor extractor = new PythonBodyExtractor("asset/extractor.py", 0.6);
        String body = extractor.extractBody("fixture/test1.html");
        System.out.println(body);
        extractor.setDatasetDir("weps2007/test");
        List<String> nameList = extractor.getPeopleNameList();
        System.out.println(nameList);
        System.out.println(nameList.size());
        extractor.setDatasetDir("fixture");
        extractor.setBodiesTargetDir("test_webpages_bodies");
        extractor.extractBodies();
    }
}
