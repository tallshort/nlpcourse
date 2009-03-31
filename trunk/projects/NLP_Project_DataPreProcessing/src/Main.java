import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
//        String[] algorithms = { "SMO" };
//        for (String algorithm : algorithms) {
//            for (int i = -3; i <= 0; i++) {
//                for (int j = 0; j <= 3; j++) {
//                    if (Math.abs(i - j) >= 1) {
//                        classify(i, j, algorithm);
//                        compare(i, j, algorithm);
//                    }
//                }
//            }
//        }
        classify(-5, 5, "NaiveBayes");
        compare(-5, 5, "NaiveBayes");
    }

    private static void classify(int startOffset, int endOffset,
            String algorithm) {
        // TODO Auto-generated method stub
        Reader r = new Reader("Chinese_train_pos.xml"); // 此类作用是返回一个HASHMAP
        // 记录
        // 下所有词和其出现的序号
        HashMap<String, String> h = r.read();
        /* 下面两个文件分别为 已经解析过的 训练集文件 与 测试集文件，即张坚做好的那些文件 */
        File f = new File("Chinese_train_pos.xml_" + startOffset + "_"
                + endOffset + ".txt");
        File ft = new File("Chinese_test_pos.xml_" + startOffset + "_"
                + endOffset + ".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedReader br1 = new BufferedReader(new FileReader(ft));
            String s = br.readLine();
            String s1 = br1.readLine();
            int num = 0;
            while (s != null || s1 != null) // 循环每个单词 每个单词做一个分类器
            {
                List<String> train = new ArrayList<String>();
                List<String> test = new ArrayList<String>();
                while (!s.equals("<END>") && s != null) // 读入一个词的训练样本
                {
                    // System.out.println("s="+s);
                    train.add(s);
                    s = br.readLine();
                }
                // System.out.println("\n");
                while (!s1.equals("<END>") && s1 != null) // 读入一个词的测试样本
                {
                    // System.out.println("s1="+s1);
                    test.add(s1);
                    s1 = br1.readLine();
                }
                // System.out.println("正在处理的单词：" + h.get(String.valueOf(num)));
                /*
                 * 对每个词构造一个分类器，需要4个参数， 1 train ：即上面从文件读入的一个单词的训练集 LIST 2 test :
                 * 上面从文件读入的一个单词的测试集 3 如下："SMO" 即为分类器，有4种分类器可选， （1） "NaiveBayes"
                 * 贝叶斯分类器 （2） "MP" 多层前向神经网络 （注意：其训练时间会很长） （3） "SMO" 支持向量机 （4）
                 * "RBF" 径向基神经网络 4 该词的名字 ， 如"中医"等，这里用到了前面的 那个HASHMAP
                 */
                Classifer c = new Classifer(train, test, algorithm, h
                        .get(String.valueOf(num)), startOffset, endOffset);
                num++;
                s = br.readLine();
                s1 = br1.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // public Classifer(List<String> wordList,List<String> testList,String
        // model,String name)
    }

    private static void compare(int startOffset, int endOffset, String modelName) {
        File f1 = new File("ChineseLS.test.key");
        File f2 = new File("result-" + modelName + startOffset + endOffset
                + ".txt");
        try {
            int count = 0; // 记录总结果个数
            int equal = 0;
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            String s1 = br1.readLine();
            String s2 = br2.readLine();
            int cc = 0;
            while (s1 != null) {
                if (s1.equals(s2)) {
                    equal++;
                    count++;
                    // System.out.println(s1+"\t"+s2);
                    // System.out.println("处理第"+count+"个结果...相等");

                } else {
                    count++;
                    // System.out.println("处理第"+count+"个结果...不相等");
                    // System.out.println(s1+"\t"+s2);
                    // Thread.sleep(300);
                }
                // System.out.println("处理第"+count+"个结果...");
                s1 = br1.readLine();
                s2 = br2.readLine();
            }
            double x = (double) equal / count;
            System.out.println("模型：" + modelName + " 范围：" + startOffset
                    + endOffset + "正确率为：" + x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
