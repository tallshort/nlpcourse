package weps.test.namedentity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashSet;

import edu.stanford.nlp.ie.crf.CRFClassifier;

public class MLProject2Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            HashSet<String> hs = new HashSet<String>();// 为了去除重复词先将结果放入 集合
            File resultFile = new File("测试.txt"); // 这两行用于写入文件
            OutputStream os = new FileOutputStream(resultFile, false);
            // TODO Auto-generated method stub
            /* 要被处理的字符串from，直接从文件读进来即可 */
            String from = "first service mortgagefirst service mortgage, u 1st service mortgage, tony bryant, debra tillman, college park, georgia mortgage, mortgage lenders, atlanta lenders, home banc, georgia lenders, union city, tucker, alabama, new orleans, equal housing lender, hud homes, refi, art huntley, lenders, namb, fha, va, remax, investor loans, u first realty, mortgage lenders, free, FREE, atlanta mortgage loans, georgia mortgage loans, louisiana mortgage loans, alabama mortgage loans, birmingham mortgage loans, tuscaloosa mortgage loans, new orleans mortgage loans, atlanta mortgage companies, georgia mortgage companies, louisiana mortgage companies, alabama mortgage companies, birmingham mortgage companies, hud homes, hud home loans, georgia foreclosures, alabama foreclosures, louisiana foreclosures, investor loans, first time home buyer loans, relocation loans, commercial loans, 80/20 loans, fha loans, va loans, immigrant loans, down payment assistance, construction loansofficial website of First Service Mortgage offering all types of mortgages";
            /*
             * 张坚，注意下，这个对象产生需要读取一个模型，时间比较长大概10-30秒，你在处理文件的时候把这个
             * 引用传递进来，意思就是只从硬盘读取一次！很重要！！
             */
            CRFClassifier c = CRFClassifier
                    .getClassifier("asset/ner-eng-ie.crf-3-all2008-distsim.ser.gz");
            String r = c.classifyToString(from);// r为已经标号序列的 字符串

            /* 下面部分你可以COPY到你的程序里，result即为要写入的字符串，我现在是用 "|" 符号分割的每个词 */
            String result = "";// 结果字符串
            String[] res = r.split(" ");// 空格分割每个字符串
            for (String s : res) {
                int pos1 = s.indexOf("/");
                String p = s.substring(pos1 + 1);
                if (p.equals("LOCATION") || p.equals("PERSON")
                        || p.equals("ORGANIZATION"))// 如果被标记为命名实体
                {
                    hs.add(s.substring(0, pos1));
                    System.out.println(s);
                }
            }
            // 从集合hs中读出每个单词
            for (String temp : hs) {
                result += temp + "|";
            }
            result = result.substring(0, result.length() - 2);
            os.write(result.getBytes());
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(abc);
        // System.out.println(abc);

    }

}
