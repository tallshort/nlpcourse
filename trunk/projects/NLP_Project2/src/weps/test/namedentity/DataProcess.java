package weps.test.namedentity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class DataProcess {
    HashMap<String, WordInfo> map;
    HashMap<String, WordInfo> countMap;
    int lastIndex;
    int totalNum;// 记录总共有多少元组
    // String person;
    File f; // 准备处理的document

    // 若目标文件存在，则追加，若不存在则创建新的文件。文件是否存在由person变量名决定
    public DataProcess(File f, HashMap<String, WordInfo> map, int lastIndex,
            int totalNum) {
        super();
        // this.person = person;
        this.f = f;
        this.totalNum = totalNum;
        this.map = map; // map 为当前所有维度出现的信息
        this.lastIndex = lastIndex;
        this.countMap = new HashMap<String, WordInfo>();
    }

    public String Process() // 返回当前文件处理后的内容
    {
        String result = "";
        String[] content = ReadFromFile().toString().split("[|]");
        for (String s : content) {
        	s=s.replaceAll("[.]", "").replaceAll(",", "").replaceAll(":", "").replaceAll("!", "").replaceAll(
        			"/", "").replaceAll("'", "").replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("&", "");
            if (!s.trim().equals("") && s.trim().length()>1) {
                if (countMap.containsKey(s)) {
                    countMap.get(s).SetPlus(); // 若该单词已经在本文出现，则计数++
                } else // 若不存在，向countMap添加记录
                {
                    countMap.put(s, new WordInfo(1));
                }
            }
        }
        Set<Entry<String, WordInfo>> entry = countMap.entrySet();
        Iterator<Entry<String, WordInfo>> itor = entry.iterator();
        while (itor.hasNext()) {
            Entry<String, WordInfo> en = itor.next();
            if (map.containsKey(en.getKey())) // 若在MAP（列标）里已经存在该词
            {
                result += map.get(en.getKey()).Get() + " "
                        + en.getValue().Get() + " ";// 则输出其序号和出现次数
                totalNum++;
            } else// 若列标里还不存在这个词，则将这个词放入map（列标）并返回其列标值
            {
                map.put(en.getKey(), new WordInfo(lastIndex));
                result += lastIndex + " " + en.getValue().Get() + " ";
                totalNum++;
                lastIndex++;// 计数+1

            }
        }
        return result;
    }

    public int GetIndex() {
        return lastIndex;
    }

    public int GetTotal() {
        return totalNum;
    }

    public StringBuffer ReadFromFile() {
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String s = br.readLine();
            while (s != null) {
                sb.append(s + " ");
                s = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
}
