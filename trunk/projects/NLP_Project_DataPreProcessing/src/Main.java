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
        String[] algorithms = { "AdaBoostM1" };
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
        classify(-1, 2,0,0, "SMO");
        compare(-1, 2, 0,0, "SMO");
    }

    private static void classify(int startOffset, int endOffset,int includeTokenPreOffset, int includeTokenPostOffset,String algorithm) {
        // TODO Auto-generated method stub
        Reader r = new Reader("Chinese_train_pos.xml"); // 此类作用是返回一个HASHMAP
        // 记录
        // 下所有词和其出现的序号
        HashMap<String, String> h = r.read();
        /* 下面两个文件分别为 已经解析过的 训练集文件 与 测试集文件，即张坚做好的那些文件 */
        File f = new File("Chinese_train_pos.xml_" + startOffset + "_"
                + endOffset+"_"+includeTokenPreOffset+"_"+includeTokenPostOffset + ".txt");
        File ft = new File("Chinese_test_pos.xml_" + startOffset + "_"
                + endOffset + "_"+includeTokenPreOffset+"_"+includeTokenPostOffset +".txt");
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            BufferedReader br1 = new BufferedReader(new FileReader(ft));
            String s = br.readLine();
            String s1 = br1.readLine();
            int num = 0;
            while (s != null || s1 != null) // 循环每个单词 每个单词做一个分类器
            {
            	System.out.println("正在处理第 "+(num+1)+" 个单词");
                List<String> train = new ArrayList<String>();
                List<String> test = new ArrayList<String>();
                while (!s.trim().equals("<END>") && s.trim()!= null) // 读入一个词的训练样本
                {
                    // System.out.println("s="+s);
                    train.add(s);
                    s = br.readLine();
                }
                // System.out.println("\n");
                while (!s1.trim().equals("<END>") && s1.trim() != null) // 读入一个词的测试样本
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
//                for(String t : test)
//                {
//                	System.out.println(t);
//                }
                Classifer c = new Classifer(train, test, algorithm, h
                        .get(String.valueOf(num)), startOffset, endOffset,includeTokenPreOffset,includeTokenPostOffset);
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

    private static void compare(int startOffset, int endOffset, int includeTokenPreOffset, int includeTokenPostOffset, String modelName) {
        File f1 = new File("ChineseLS.test.key");
        File f2 = new File("result-" + modelName + startOffset + endOffset +"_"+ includeTokenPreOffset + includeTokenPostOffset + ".txt");
        try {
        	double right=0;//记录宏平均正确率
        	double rightNum=0; //对单个词的正确率
        	double totalNum=0; //单个词的样本总数
        	String lastWord="";//记录上个单词
            int count = 0; // 记录总结果个数
            int equal = 0;
            BufferedReader br1 = new BufferedReader(new FileReader(f1));
            BufferedReader br2 = new BufferedReader(new FileReader(f2));
            String s1 = br1.readLine();
            String s2 = br2.readLine();
            lastWord=s1.substring(0, s1.indexOf(" "));
            int cc = 0;
            while (s1 != null) {
            	if(lastWord.equals(s1.subSequence(0, s1.indexOf(" ")))) //是同一个词
            	{
            		totalNum++;
            		if (s1.equals(s2)) {
                        equal++;
                        count++;
                        rightNum++;
                    } else {
                        count++;
                    }
            	}
            	else
            	{
            		double temp=rightNum/totalNum;
            		System.out.println(lastWord+"  正确率："+temp+"("+rightNum+"/"+totalNum+")");
            		right += temp;
            		totalNum=1; //下个词的总数 清 0
            		lastWord=s1.substring(0, s1.indexOf(" ")); //记录当前词
            		rightNum=0;
	                if (s1.equals(s2)) {
	                    equal++;
	                    count++;
	                    rightNum++;
	                } else {
	                    count++;
	                }
	              
            	}
                s1 = br1.readLine();
                s2 = br2.readLine();
            }
            double x = (double) equal / count;
            System.out.println(lastWord+"  正确率："+rightNum/totalNum+"("+rightNum+"/"+totalNum+")");
            right +=rightNum/totalNum;
            System.out.println("模型：" + modelName + " 范围：" + startOffset
                    + endOffset + "\n微平均正确率为：" + x+"\n宏平均正确率为:"+right/40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
