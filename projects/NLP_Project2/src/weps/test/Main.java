package weps.test;

import java.util.List;

import weps.AbstractExtractor;
import weps.IBodyExtractor;
import weps.PythonBodyExtractor;

public class Main {

    public static void main(String[] args) {
        testExtractor();
    }

    private static void testExtractor() {
        IBodyExtractor bodyExtractor = new PythonBodyExtractor("asset/extractor.py", 0.6);
        String body = bodyExtractor.extractBody("fixture/test1.html");
        System.out.println(body);
        AbstractExtractor extractor = (AbstractExtractor)bodyExtractor;
        extractor.setDatasetDir("weps2007/test");
        List<String> nameList = extractor.getPeopleNameList();
        System.out.println(nameList);
        System.out.println(nameList.size());
        extractor.setDatasetDir("fixture");
        extractor.setTargetDir("test_webpages_bodies");
        extractor.extractContents();
    }
}
