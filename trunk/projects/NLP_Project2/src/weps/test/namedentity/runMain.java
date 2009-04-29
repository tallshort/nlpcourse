package weps.test.namedentity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class runMain {

    /**
     * @param args
     */
    /*
     * 处理流程描述： 1.读取每个文件内容，为每维词组创建WordInfo
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        /* 先将所有文件写入.m 和 .clabel文件 然后进行exe调用执行CLUTO产生所有分类文件 */

        int classNum = 30; // 设置分类个数
        File sourceDir = new File("clutoRawData"); // 存放源数据的地方
        File[] allFile = sourceDir.listFiles();
        String lastName = allFile[0].getName();
        lastName = lastName.substring(0, lastName.lastIndexOf("_"));// 取得当前名字
        int lastPos = 0;
        for (int i = 0; i < allFile.length; i++) {
            String currName = allFile[i].getName();
            currName = currName.substring(0, currName.lastIndexOf("_"));
            if (!currName.equals(lastName)) {
                System.out.println("正在处理:" + lastName);
                processAll(allFile, lastName, lastPos, i);// 将要处理的文件返回传入
                lastPos = i;
                excuteClusty(classNum, lastName);
                lastName = currName; // 对下个人进行聚类
            }
        }
        System.out.println("正在处理:" + lastName);
        processAll(allFile, lastName, lastPos, allFile.length);// 处理最后一个人
        excuteClusty(classNum, lastName);
    }

    private static void excuteClusty(int classNum, String lastName) {
        try // 执行命令行 处理当前 名字 矩阵信息
        {
            String s = "vcluster -clmethod=rb "
                    + // 采用层次聚类
                    "-clustfile=matrixFile\\" + lastName + classNum
                    + ".cluster "
                    + // 最后分类文件
                    "-showtree " + "-showfeatures "
                    + "-clabelfile=matrixFile\\" + lastName + ".clabel " + // 添加列标文件
                    "matrixFile\\" + lastName + ".m " + classNum;
            Process p = Runtime.getRuntime().exec(s); // 要处理的矩阵文件
            BufferedReader in = new BufferedReader(new InputStreamReader(p
                    .getInputStream()));
            String result = "";
            String temp = in.readLine();
            while (temp != null) {
                result += temp;
                temp = in.readLine();
            }
            File f = new File("detail/" + lastName + ".detail"); // 将具体的聚类信息写入detail目录下
            OutputStream osDetail = new FileOutputStream(f, false);
            osDetail.write(result.getBytes());
            osDetail.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processAll(File[] files, String Name, int from, int end) {
        try {
            int totalNum = 0; // 记录总共的元组个数
            File fMatrix = new File("matrixFile\\" + Name + ".m"); // 记录矩阵文件
            OutputStream osMatrix = new FileOutputStream(fMatrix, false);
            File fColLabel = new File("matrixFile\\" + Name + ".clabel"); // 记录列信息
            OutputStream osCLabel = new FileOutputStream(fColLabel, false);
            String clabel = ""; // 记录所有类标
            String result = ""; // 记录结果的矩阵字符串
            HashMap<String, WordInfo> map = new HashMap<String, WordInfo>();
            int lastIndex = 1;// 用于记录map中单词的最后序号
            for (int i = from; i < end; i++) {
                // String person=GetName(file.getName());
                DataProcess d = new DataProcess(files[i], map, lastIndex,
                        totalNum);// 处理每个实例
                result += d.Process() + "\n"; // 换行表示每个 实例一行
                lastIndex = d.GetIndex(); // 更新列标值
                totalNum = d.GetTotal(); // 更新总共元组数
            }
            int rows = end - from;
            int cols = map.size();
            result = rows + " " + cols + " " + totalNum + "\n" + result;
            clabel = GetLabel(map);
            osMatrix.write(result.getBytes());
            osCLabel.write(clabel.getBytes());
            osMatrix.close(); // 写入文件
            osCLabel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetLabel(HashMap<String, WordInfo> map)
    // 需要将map中所有内容取出，并按value对象中的position变量排序将key部分输出
    {
        String label = "";
        for (int i = 0; i < map.size(); i++) {
            Set<Entry<String, WordInfo>> set = map.entrySet();
            Iterator<Entry<String, WordInfo>> it = set.iterator();
            while (it.hasNext()) {
                Entry<String, WordInfo> entry = it.next();
                if (entry.getValue().Get() == i) {
                    label += entry.getKey() + "\n";
                    break;
                }
            }
        }
        return label;
    }

    public static String GetName(String name) {
        int pos1 = name.lastIndexOf("_");// 截取前面部分为当前人名
        return name.substring(0, pos1);
    }

}
