package weps.cluster.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import weps.util.TextFile;
import ci.cluster.DataSetCreator;
import ci.cluster.TextDataItem;
import ci.cluster.impl.TextDataItemImpl;
import ci.textanalysis.TagMagnitude;
import ci.textanalysis.TagMagnitudeVector;
import ci.textanalysis.TextAnalyzer;
import ci.textanalysis.lucene.impl.LuceneTextAnalyzer;
import ci.textanalysis.lucene.impl.SimpleInverseDocFreqEstimator;
import ci.textanalysis.lucene.impl.TagCacheImpl;

public class WebPeopleDataSetCreator implements DataSetCreator {

    private SimpleInverseDocFreqEstimator freqEstimator;
    private TextAnalyzer textAnalyzer;
    
    private String dataSetDir = "merged_data";
    
    public WebPeopleDataSetCreator() {
        this.freqEstimator = new SimpleInverseDocFreqEstimator(100);
        this.textAnalyzer = new LuceneTextAnalyzer(new TagCacheImpl(), freqEstimator);
    }

    public List<TextDataItem> createLearningData() throws Exception {
        List<TextDataItem> webPeopleDataList = new ArrayList<TextDataItem>();
        for (String fileName : new File(this.dataSetDir).list()) {
            String name = fileName.substring(0, fileName.lastIndexOf("_"));
            String rank = fileName.substring(fileName.lastIndexOf("_"));
            String data = TextFile.read(this.dataSetDir + "/" + fileName).replace("|", "");
            webPeopleDataList.add(this.createWebPeopleItem(name, rank, data));
        }
        return webPeopleDataList;
    }
    
    private TextDataItem createWebPeopleItem(String name, String rank,
            String data) throws IOException {
        WebPeopleDocEntry docEntry = new WebPeopleDocEntry(name, rank);
        TagMagnitudeVector tmv
            = this.textAnalyzer.createTagMagnitudeVector(data);
        for (TagMagnitude tm : tmv.getTagMagnitudes()) {
            this.freqEstimator.addCount(tm.getTag());
        }
        return new TextDataItemImpl(docEntry, tmv);
    }

    public String getDataSetDir() {
        return dataSetDir;
    }

    public void setDataSetDir(String dataSetDir) {
        this.dataSetDir = dataSetDir;
    }
}
