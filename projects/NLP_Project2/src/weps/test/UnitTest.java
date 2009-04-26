package weps.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.junit.Test;

import weps.AbstractExtractor;
import weps.INamedEntityRecognizer;
import weps.MofoMicroformatsExtractor;
import weps.StandfordNamedEntityRecognizer;
import weps.test.mock.BlogCluster;
import weps.test.mock.BlogDataSetCreator;
import weps.test.mock.BlogHierCluster;
import ci.cluster.Clusterer;
import ci.cluster.TextCluster;
import ci.cluster.TextDataItem;
import ci.cluster.hiercluster.HierCluster;
import ci.cluster.impl.TextHierarchialClusterer;
import ci.cluster.impl.TextKMeansClusterer;
import ci.textanalysis.InverseDocFreqEstimator;
import ci.textanalysis.Tag;
import ci.textanalysis.TagCache;
import ci.textanalysis.TagMagnitude;
import ci.textanalysis.TagMagnitudeVector;
import ci.textanalysis.TextAnalyzer;
import ci.textanalysis.lucene.impl.EqualInverseDocFreqEstimator;
import ci.textanalysis.lucene.impl.LuceneTextAnalyzer;
import ci.textanalysis.lucene.impl.PhrasesCacheImpl;
import ci.textanalysis.lucene.impl.PorterStemStopWordAnalyzer;
import ci.textanalysis.lucene.impl.SynonymPhraseStopWordAnalyzer;
import ci.textanalysis.lucene.impl.SynonymsCacheImpl;
import ci.textanalysis.lucene.impl.TagCacheImpl;
import ci.textanalysis.termvector.impl.TagMagnitudeImpl;
import ci.textanalysis.termvector.impl.TagMagnitudeVectorImpl;

public class UnitTest {

    @Test
    public void testBodyExtractor() {
        AbstractExtractor extractor = new AbstractExtractor() {
            @Override
            protected void extractContent(String absolutePath, String name,
                    String rank) {
            }
        };
        extractor.setDatasetDir("weps2007/test");
        List<String> nameList = extractor.getPeopleNameList();
        assertEquals(30, nameList.size());
        extractor.setDatasetDir("weps2007/training");
        nameList = extractor.getPeopleNameList();
        assertEquals(49, nameList.size());
    }

    @Test
    public void testPorterStemmingAnalyzer() throws IOException {
        Analyzer analyzer = new PorterStemStopWordAnalyzer();
        String text = "Collective Intelligence and Web2.0";
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("collect");
        expectedList.add("intellig");
        expectedList.add("web2.0");

        List<String> actualList = new ArrayList<String>();
        Reader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream(null, reader);
        Token token = ts.next();
        while (token != null) {
            actualList.add(token.termText());
            token = ts.next();
        }
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testSynonyPhrasesStopWordAnalyzer() throws IOException {
        Analyzer analyzer = new SynonymPhraseStopWordAnalyzer(
                new SynonymsCacheImpl(), new PhrasesCacheImpl());
        String text = "Collective Intelligence and Web2.0";
        List<String> expectedList = new ArrayList<String>();
        expectedList.add("collective");
        expectedList.add("intelligence");
        expectedList.add("ci");
        expectedList.add("collective intelligence");
        expectedList.add("web2.0");

        List<String> actualList = new ArrayList<String>();
        Reader reader = new StringReader(text);
        TokenStream ts = analyzer.tokenStream(null, reader);
        Token token = ts.next();
        while (token != null) {
            actualList.add(token.termText());
            token = ts.next();
        }
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testTagMagnitude() throws Exception {
        TagCache tagCache = new TagCacheImpl();
        List<TagMagnitude> tmList = new ArrayList<TagMagnitude>();
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("aaa"), 1));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("bbb"), 2));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("ccc"), 1.5));
        tmList.add(new TagMagnitudeImpl(tagCache.getTag("aaa"), 1));
        TagMagnitudeVector vector = new TagMagnitudeVectorImpl(tmList);
        assertEquals("[bbb, bbb, 0.6963106238227914]"
                + "[ccc, ccc, 0.5222329678670935]"
                + "[aaa, aaa, 0.49236596391733095]" + "\nSumSqd = 1.0", vector
                .toString());

    }

    @Test
    public void testTextAnalyzer() throws IOException {
        String title = "Collective Intelligence and Web2.0";
        String body = "Web2.0 is all about connecting users to users, " +
        		"inviting users to participate and applying their " +
        		"collective intelligence to improve the application. " +
        		"Collective intelligence enhances the user experiense";
        TagCacheImpl t = new TagCacheImpl();
        InverseDocFreqEstimator idfEstimator = new EqualInverseDocFreqEstimator();
        TextAnalyzer analyzer = new LuceneTextAnalyzer(t, idfEstimator);
        List<Tag> tags = analyzer.analyzeText(title);
        assertEquals(5, tags.size());
        tags = analyzer.analyzeText(body);
        // System.out.println(tags);
        assertEquals(21, tags.size());
        TagMagnitudeVector tmvBody = analyzer.createTagMagnitudeVector(body);
        assertTrue(tmvBody.toString().contains("[users, user, 0.6246950475544243]"));
        PorterStemStopWordAnalyzer.stopWords = new String[] {
                "and", "of", "the", "to", "is", "their", "can", "all"
        };
        tmvBody = analyzer.createTagMagnitudeVector(body);
        TagMagnitudeVector tmvTitle = analyzer.createTagMagnitudeVector(title);
        TagMagnitudeVector tmvCombined = tmvTitle.add(tmvBody);
        // System.out.println(tmvCombined);
        assertTrue(tmvCombined.toString().contains("[users, user, 0.4364357804719848]"));
    }
    
    @Test
    public void testKMeansCluster() throws Exception {
        List<TextDataItem> blogData = new BlogDataSetCreator().createLearningData();
        Clusterer clusterer = new TextKMeansClusterer(blogData, 2) {
            @Override
            protected TextCluster createTextCluster(int clusterId) {
                return new BlogCluster(clusterId);
            }
        };
        // for (int i = 0; i < 10; i++) {
            List<TextCluster> clusters = clusterer.cluster();
            System.out.println(clusterer);
            // assertEquals(2, clusters.get(0).getDataItems().size());
        // }       
    }
    
    @Test
    public void testHierarchialCluster() throws Exception {
        List<TextDataItem> blogData = new BlogDataSetCreator().createLearningData();
        Clusterer clusterer = new TextHierarchialClusterer(blogData) {
            @Override
            protected HierCluster createHierCluster(int clusterId,
                    HierCluster c1, HierCluster c2, double similarity,
                    TextDataItem textDataItem) {
                return new BlogHierCluster(clusterId, c1, c2, similarity, textDataItem);
            }        
        };
        List<TextCluster> clusters = clusterer.cluster();
        System.out.println("");
        System.out.println(clusterer);    
    }
    
    @Test
    public void testNamedEntityRecognizer() {
        INamedEntityRecognizer recognizer = new StandfordNamedEntityRecognizer();
        String text = "";
        List<String> namedEntities = recognizer.recognizeNamedEntities(text);
    }
    
    @Test
    public void testMicroformatsExtractor() {
        AbstractExtractor extractor
        = new MofoMicroformatsExtractor("asset/microformats_extractor.rb");
        extractor.setDatasetDir("weps2007/test");
        extractor.setTargetDir("test_webpages_microformats");
        extractor.extractContents();
    }
}
