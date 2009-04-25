package weps.test.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ci.cluster.DataSetCreator;
import ci.cluster.TextDataItem;
import ci.cluster.impl.TextDataItemImpl;
import ci.textanalysis.TagMagnitude;
import ci.textanalysis.TagMagnitudeVector;
import ci.textanalysis.TextAnalyzer;
import ci.textanalysis.lucene.impl.LuceneTextAnalyzer;
import ci.textanalysis.lucene.impl.SimpleInverseDocFreqEstimator;
import ci.textanalysis.lucene.impl.TagCacheImpl;

public class BlogDataSetCreator implements DataSetCreator {

    private SimpleInverseDocFreqEstimator freqEstimator;
    private TextAnalyzer textAnalyzer;
    
    public BlogDataSetCreator() {
        this.freqEstimator = new SimpleInverseDocFreqEstimator(4);
        this.textAnalyzer = new LuceneTextAnalyzer(new TagCacheImpl(), freqEstimator);
    }

    public List<TextDataItem> createLearningData() throws Exception {
        List<TextDataItem> blogDataList = new ArrayList<TextDataItem>();
        TextDataItem blogItem = createBlogItem(
                "SAP Gets Business Intelligence.",
                "SAP",
                "SAP Gets Business Intelligence. What Do You Get?"
                );
        blogDataList.add(blogItem);
//        blogItem = createBlogItem(
//                "SAP Gets Business Intelligence.",
//                "SAP",
//                "SAP Gets Business Intelligence. What Do You Get?"
//                );
//        blogDataList.add(blogItem);
//        blogItem = createBlogItem(
//                "SAP Gets Business Intelligence.",
//                "SAP",
//                "SAP Gets Business Intelligence. What Do You Get?"
//                );
//        blogDataList.add(blogItem);
//        blogItem = createBlogItem(
//                "SAP Gets Business Intelligence.",
//                "SAP",
//                "SAP Gets Business Intelligence. What Do You Get?"
//                );
//        blogDataList.add(blogItem);
        blogItem = createBlogItem(
                "collective intelligence excites execs",
                "SAP",
                "collective intelligence excites execs zdnet.com's"
                );
        blogDataList.add(blogItem);
        blogItem = createBlogItem(
                "Corporate Social Networks",
                "Social-networking",
                "Corporate Social Networks Filed under: Collaboration");
        blogDataList.add(blogItem);
        blogItem = createBlogItem(
                "Corporate Social Networks are coming",
                "Social-networking",
                "Corporate Social Networks are becoming more and more popular.");
        blogDataList.add(blogItem);
        return blogDataList;
    }

    private TextDataItem createBlogItem(String title, String author,
            String excerpt) throws IOException {
        BlogEntry blogEntry = new BlogEntry(title, author, excerpt);
        TagMagnitudeVector tmv
            = this.textAnalyzer.createTagMagnitudeVector(blogEntry.toString());
        for (TagMagnitude tm : tmv.getTagMagnitudes()) {
            this.freqEstimator.addCount(tm.getTag());
        }
        return new TextDataItemImpl(blogEntry, tmv);
    }

}
