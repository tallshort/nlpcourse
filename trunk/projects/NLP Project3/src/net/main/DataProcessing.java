package net.main;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.core.Sentence;
import net.core.TaggingExpander;
import net.util.TextFile;

public class DataProcessing {
    
    private String dataFile = "Train_default_70.Ner";
    
    private boolean includeSegment = false;
    private boolean includePreSuffix = true;
    
    private Set<String> personPrefixSet = new HashSet<String>();
    private Set<String> orgSuffixSet = new HashSet<String>();
    private Set<String> locSuffixSet = new HashSet<String>();
    
    public DataProcessing() {
        personPrefixSet.addAll(new TextFile("personPrefix.txt"));
        orgSuffixSet.addAll(new TextFile("orgSuffix.txt"));
        locSuffixSet.addAll(new TextFile("locSuffix.txt"));
        System.out.println(personPrefixSet);
        System.out.println(orgSuffixSet);
        System.out.println(locSuffixSet);
    }

    public static void main(String[] args) throws FileNotFoundException {
        // get70TrainDataMark();
        DataProcessing dp = new DataProcessing();
        // dp.generateInternalTrainData();
        // dp.generateStandardResultFile();
        // dp.generateSegPosTaggedTrainingDataFile();
        dp.printDifferences();
        
       // String file = "Train_default.Ner";
        // new NamePrefixProcessor().process(file);
       // new OrganizationSuffixProcessor().process(file);
    }
    
    public void generateInternalTrainData() {
        String expandDataFile = "expand_" + dataFile;
        TaggingExpander expander = new TaggingExpander();
        List<Sentence> sentences = expander.expand(new TextFile(dataFile));
        StringBuilder sb = new StringBuilder();
        for (Sentence sentence : sentences) {
            // System.out.println(sentence);
            sb.append(sentence.toString()).append("\n");
        }
        TextFile.write(expandDataFile, sb.toString());
    }
    
    public void generateSegPosTaggedTrainingDataFile() {
        String expandFile = "expand_" + this.dataFile;
        List<String> dataLines = new TextFile(expandFile);
        List<String> posTags = new ArrayList<String>();
        for (String line : dataLines) {
            if (!line.equals("")) {
                String[] parts = line.split(" ");
                String segment = parts[0];
                String pos = parts[parts.length - 2];
                posTags.add("B " + pos);
                for (int i = 1; i < segment.length(); i++) {
                    posTags.add("I " + pos);
                }   
            } else {
                posTags.add("EMPTY_POS");
            }
        }
        
        StringBuilder sb = new StringBuilder();
        List<String> rawDataLines = new TextFile(dataFile);
        for (int i = 0; i < rawDataLines.size(); i++) {
            String line = rawDataLines.get(i);
            if (line.equals("")) {
                sb.append("\n");
            } else {
                String[] parts = line.split(" ");
                String character = parts[0];
                String expectedTag = parts[1];
                sb.append(character).append(" ");  
                sb.append(posTags.get(i)).append(" ");
                if (includePreSuffix) {
                    String preSuffix = "";
                    if (this.personPrefixSet.contains(character)) {
                        preSuffix += "PP";
                    }
                    if (this.orgSuffixSet.contains(character)) {
                        preSuffix += "OS";
                    }
                    if (this.locSuffixSet.contains(character)) {
                        preSuffix += "LS";
                    }
                    if (preSuffix.equals("")) {
                        sb.append("N").append(" ");
                    } else {
                        sb.append(preSuffix).append(" ");
                    }
                }
                sb.append(expectedTag).append("\n"); 
            }
        }
        TextFile.write("pos_pre_suf_" + dataFile, sb.toString());
    }
    
    public void generateStandardResultFile() {
        String resultFile = "result6.txt";
        List<String> dataLines = new TextFile(resultFile);
        List<String> entityTags = new ArrayList<String>();
        for (String line : dataLines) {
            if (!line.equals("")) {
                String[] parts = line.split(" ");
                String segment = parts[0];
                String entityTag = parts[parts.length - 1];
                if (entityTag.startsWith("B")) {
                    String entityType = entityTag.split("-")[1];
                    entityTags.add(entityTag);
                    for (int i = 1; i < segment.length(); i++) {
                        entityTags.add("I-" + entityType);
                    }
                } else {
                    for (int i = 0; i < segment.length(); i++) {
                        entityTags.add(entityTag);
                    }
                }
            } else {
                entityTags.add("EMPTY_LINE");
            }
        }
        
        StringBuilder sb = new StringBuilder();
        List<String> rawDataLines = new TextFile(dataFile);
        for (int i = 0; i < rawDataLines.size(); i++) {
            String line = rawDataLines.get(i);
            if (line.equals("")) {
                sb.append("\n");
            } else {
                String[] parts = line.split(" ");
                String character = parts[0];
                String expectedTag = parts[1];
                sb.append(character).append(" ");  
                sb.append(expectedTag).append(" "); 
                sb.append(entityTags.get(i)).append("\n");
                if (!expectedTag.equals(entityTags.get(i))) {
                    System.out.println("Line Number: " + (i+1) + " " + character + " " + expectedTag + " " + entityTags.get(i));
                }
            }
        }
        TextFile.write("standard_" + resultFile, sb.toString());
        
    }
    
    public void printDifferences() {
        List<String> rawDataLines = new TextFile("result13.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rawDataLines.size(); i++) {
            if (rawDataLines.get(i).equals("")) {
                continue;
            }
            String[] parts = rawDataLines.get(i).split(" ");
            if (!parts[parts.length - 1].equals(parts[parts.length - 2])) {
               sb.append("Line: " + (i + 1) + " " + rawDataLines.get(i)).append("\n");
               System.out.println("Line: " + (i + 1) + " " + rawDataLines.get(i)); 
            }
        }
        TextFile.write("differences13.txt", sb.toString());
    }

    public static void get70TrainDataMark() {
        List<String> lines = new TextFile("Train_default.Ner");
        int lineNum = 1;
        int sentenceNum = 1;
        for (String line : lines) {
            lineNum++;
            if (line.equals("")) {
                sentenceNum++;
                if (sentenceNum == 16227) {
                    System.out.println("Line " + lineNum + " Sentence: "
                            + sentenceNum);
                }
            }
        }
    }

}
