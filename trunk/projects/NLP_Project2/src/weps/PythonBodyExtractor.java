package weps;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonBodyExtractor extends AbstractBodyExtractor {

    private String pythonFilePath;
    private double density;

    /**
     * @param pythonFilePath
     *            the specified Python body extractor file path
     * @param density
     *            the density parameter for the extractor
     */
    public PythonBodyExtractor(String pythonFilePath, double density) {
        this.pythonFilePath = pythonFilePath;
        if (density < 0 || density > 1)
            this.density = 0.5;
        else
            this.density = density;
    }

    public String extractBody(String filePath) {
        Runtime rn = Runtime.getRuntime();
        Process p = null;
        String result = "";
        try {
            p = rn.exec("python " + pythonFilePath + " " + density + " "
                    + filePath);
            BufferedReader in = new BufferedReader(new InputStreamReader(p
                    .getInputStream(), "Windows-1252"));
            result = "";
            String strProc = null;
            boolean isMainInfoBegin = false;
            while ((strProc = in.readLine()) != null) {
                // strProc = new String(strProc.getBytes(), "");
                if (isMainInfoBegin == true)
                    result += strProc + "\n";
                else {
                    if (strProc.trim().contains("@@@@error@@@@")) {
                        result = "";
                        return result;
                    }
                    if (strProc.trim().contains("@@@@@@@@@@"))
                        isMainInfoBegin = true;
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

}
