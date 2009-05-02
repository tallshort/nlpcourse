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
    int totalNum;// ��¼�ܹ��ж���Ԫ��
    // String person;
    File f; // ׼�������document

    // ��Ŀ���ļ����ڣ���׷�ӣ����������򴴽��µ��ļ����ļ��Ƿ������person����������
    public DataProcess(File f, HashMap<String, WordInfo> map, int lastIndex,
            int totalNum) {
        super();
        // this.person = person;
        this.f = f;
        this.totalNum = totalNum;
        this.map = map; // map Ϊ��ǰ����ά�ȳ��ֵ���Ϣ
        this.lastIndex = lastIndex;
        this.countMap = new HashMap<String, WordInfo>();
    }

    public String Process() // ���ص�ǰ�ļ�����������
    {
        String result = "";
        String[] content = ReadFromFile().toString().split("[|]");
        for (String s : content) {
        	s=s.replaceAll("[.]", "").replaceAll(",", "").replaceAll(":", "").replaceAll("!", "").replaceAll(
        			"/", "").replaceAll("'", "").replaceAll("[(]", "").replaceAll("[)]", "").replaceAll("&", "");
            if (!s.trim().equals("") && s.trim().length()>1) {
                if (countMap.containsKey(s)) {
                    countMap.get(s).SetPlus(); // ���õ����Ѿ��ڱ��ĳ��֣������++
                } else // �������ڣ���countMap��Ӽ�¼
                {
                    countMap.put(s, new WordInfo(1));
                }
            }
        }
        Set<Entry<String, WordInfo>> entry = countMap.entrySet();
        Iterator<Entry<String, WordInfo>> itor = entry.iterator();
        while (itor.hasNext()) {
            Entry<String, WordInfo> en = itor.next();
            if (map.containsKey(en.getKey())) // ����MAP���б꣩���Ѿ����ڸô�
            {
                result += map.get(en.getKey()).Get() + " "
                        + en.getValue().Get() + " ";// ���������źͳ��ִ���
                totalNum++;
            } else// ���б��ﻹ����������ʣ�������ʷ���map���б꣩���������б�ֵ
            {
                map.put(en.getKey(), new WordInfo(lastIndex));
                result += lastIndex + " " + en.getValue().Get() + " ";
                totalNum++;
                lastIndex++;// ����+1

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
