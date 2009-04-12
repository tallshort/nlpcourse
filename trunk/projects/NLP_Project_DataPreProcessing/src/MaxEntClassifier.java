import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaxEntClassifier {

    private double globalRightNum;
    private double globalTotalSum;
    private ArrayList<Double> globalAccuarcyList = new ArrayList<Double>();

    private double g = 20;

    public static void main(String[] args) {
        int startOffset = -1;
        int endOffset = 2;
        int includeTokenPreOffset = -1;
        int includeTokenPostOffset = 3;
        double g = 20;
        
        MaxEntClassifier c = new MaxEntClassifier();
        c.setG(g);
        
        // Verbs
        List<String> wordList = getVerbWordList();
        c.runMaxEnt(wordList, startOffset, endOffset, includeTokenPreOffset,
                includeTokenPostOffset);
        // Nouns
        wordList = getNounWordList();
        startOffset = -1;
        endOffset = 1;
        includeTokenPreOffset = -1;
        includeTokenPostOffset = 2;
         c.runMaxEnt(wordList, startOffset, endOffset, includeTokenPreOffset,
                includeTokenPostOffset);

        c.summarize();
    }

    /**
     * 输出微平均和宏平均等统计信息
     */
    public void summarize() {
        System.out.println("==============================================");
        double sum = 0;
        for (Double accuracy : this.globalAccuarcyList) {
            sum = sum + accuracy;
        }
        System.out.println("Global macro average: " + sum
                / globalAccuarcyList.size());
        System.out.println("Global right sum: " + this.globalRightNum + " "
                + "Global total sum: " + this.globalTotalSum);
        System.out.println("Global micro average: " + this.globalRightNum
                / this.globalTotalSum);
        System.out.println("==============================================");
    }

    /**
     * 按照给定参数运行MaxEnt，并记录下统计信息
     * 
     * @param wordList
     *            待消歧词列表
     * @param startOffset
     *            相对于待消歧词的POS起始偏移量
     * @param endOffset
     *            相对于待消歧词的POS结尾偏移量
     * @param includeTokenPreOffset
     *            相对于POS起始偏移量的TOKEN起始偏移量
     * @param includeTokenPostOffset
     *            相对于POS起始偏移量的TOKEN结尾偏移量
     */
    public void runMaxEnt(List<String> wordList, int startOffset,
            int endOffset, int includeTokenPreOffset, int includeTokenPostOffset) {
        int iteration = 100;
        double rightNum = 0; // 微平均 记录特定词正确的个数
        double totalNum = 0; // 微平均 记录特定词总共的个数
        double rightSum = 0; // 微平均 记录正确的个数
        double totalSum = 0; // 微平均 记录总共的个数
        ArrayList<Double> accuarcyList = new ArrayList<Double>();

        for (String word : wordList) {
            try {
                String postfix = startOffset + "_" + endOffset + "_"
                        + includeTokenPreOffset + "_" + includeTokenPostOffset
                        + "_" + word + ".txt";
                String train = "Chinese_train_pos.xml_" + postfix;
                String test = "Chinese_test_pos.xml_" + postfix;
                String model = "model_" + postfix;
                String out = "out_" + postfix;

                double maxAccuracy = -1;
                Process p = Runtime.getRuntime().exec(
                        "maxent -i " + iteration + " -g " + g + " -m " + model
                                + " -o " + out + " " + train + " " + test);
                BufferedReader in = new BufferedReader(new InputStreamReader(p
                        .getInputStream()));

                String strProc = null;
                Pattern pattern = Pattern
                        .compile("Accuracy:(.+)% [(](\\d+)/(\\d+)[)]");
                while ((strProc = in.readLine()) != null) {
                    Matcher matcher = pattern.matcher(strProc);
                    if (matcher.find()) {
                        double accuracy = Double.parseDouble(matcher.group(1));
                        rightNum = Double.parseDouble(matcher.group(2));
                        totalNum = Double.parseDouble(matcher.group(3));
                        if (accuracy > maxAccuracy) {
                            maxAccuracy = accuracy;
                        }
                        // System.out.println(strProc);
                    }
                }
                in.close();
                rightSum += rightNum;
                this.globalRightNum += rightNum;
                totalSum += totalNum;
                this.globalTotalSum += totalNum;
                accuarcyList.add(maxAccuracy);
                this.globalAccuarcyList.add(maxAccuracy);
                System.out.println(word + ": " + maxAccuracy);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double sum = 0;
        for (Double accuracy : accuarcyList) {
            sum = sum + accuracy;
        }
        System.out.println("==============================================");
        System.out.println("G: " + g); 
        System.out.println("Feature: " + startOffset + endOffset
                + includeTokenPreOffset + includeTokenPostOffset);
        System.out.println("Macro average: " + sum / wordList.size());
        System.out.println("Right sum: " + rightSum + " " + "Total sum: "
                + totalSum);
        System.out.println("Micro average: " + rightSum / totalSum);
        System.out.println("==============================================");
    }

    public static List<String> getVerbWordList() {
        List<String> wordList = new ArrayList<String>();
        wordList.add("补");
        wordList.add("成立");
        wordList.add("吃");
        wordList.add("出");
        wordList.add("带");
        wordList.add("动");
        wordList.add("动摇");
        wordList.add("发");
        wordList.add("赶");
        wordList.add("叫");
        wordList.add("进");
        wordList.add("开通");
        wordList.add("看");
        wordList.add("平息");
        wordList.add("使");
        wordList.add("说明");
        wordList.add("挑");
        wordList.add("推翻");
        wordList.add("望");
        wordList.add("想");
        wordList.add("震惊");
        return wordList;
    }

    public static List<String> getNounWordList() {
        List<String> wordList = new ArrayList<String>();
        wordList.add("本");
        wordList.add("表面");
        wordList.add("菜");
        wordList.add("长城");
        wordList.add("单位");
        wordList.add("道");
        wordList.add("队伍");
        wordList.add("儿女");
        wordList.add("机组");
        wordList.add("镜头");
        wordList.add("面");
        wordList.add("牌子");
        wordList.add("旗帜");
        wordList.add("气息");
        wordList.add("气象");
        wordList.add("日子");
        wordList.add("天地");
        wordList.add("眼光");
        wordList.add("中医");
        return wordList;
    }

    public double getG() {
        return g;
    }

    /**
     * 设置Gaussian prior的取值
     * 
     * @param g
     */
    public void setG(double g) {
        this.g = g;
    }

}
