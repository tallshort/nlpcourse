package weps.test.namedentity;

import java.io.File;
import java.io.FileOutputStream;
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
        try {
            int totalNum = 0; // 记录总共的元组个数
            File f = new File("d:/mltest/source");// 源目录
            File fMatrix = new File("d:/mltest/result/" + "Chris_Brockett.m"); // 记录矩阵文件
            OutputStream osMatrix = new FileOutputStream(fMatrix, false);
            File fColLabel = new File("d:/mltest/result/"
                    + "Chris_Brockett.clabel"); // 记录列信息
            OutputStream osCLabel = new FileOutputStream(fColLabel, false);
            File[] fileList = f.listFiles();
            String clabel = ""; // 记录所有类标
            String result = ""; // 记录结果的矩阵字符串
            HashMap<String, WordInfo> map = new HashMap<String, WordInfo>();
            int lastIndex = 0;// 用于记录map中单词的最后序号
            for (File file : fileList) {
                // String person=GetName(file.getName());
                if (file.isDirectory())
                    continue;
                DataProcess d = new DataProcess(file, map, lastIndex, totalNum);// 处理每个实例
                result += d.Process() + "\n"; // 换行表示每个 实例一行
                lastIndex = d.GetIndex(); // 更新列标值
                totalNum = d.GetTotal(); // 更新总共元组数
            }
            int rows = fileList.length;
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
