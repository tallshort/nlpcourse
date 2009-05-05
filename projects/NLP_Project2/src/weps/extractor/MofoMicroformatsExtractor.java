package weps.extractor;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import weps.util.TextFile;

public class MofoMicroformatsExtractor extends AbstractExtractor implements
        IMicroformatsExtractor {

    private String mofoExtractorFilePath;

    public MofoMicroformatsExtractor(String mofoExtractorFilePath) {
        this.mofoExtractorFilePath = mofoExtractorFilePath;
    }

    public String extractMicroformats(String htmlFilePath) {
        Runtime runtime = Runtime.getRuntime();
        Process p = null;
        StringBuilder sb = new StringBuilder();
        try {
            p = runtime.exec("ruby " + mofoExtractorFilePath + " "
                    + htmlFilePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String strProc = null;
            while ((strProc = in.readLine()) != null) {
                System.out.println(strProc);
                sb.append(strProc + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    protected void extractContent(String filePath, String name, String rank) {
        System.out.println(filePath + " " + name + " " + rank);
        String microformats = extractMicroformats(filePath);
        TextFile.write(this.getTargetDir() + "/"
                + name + "_" + rank + ".txt", microformats);
    }
}
