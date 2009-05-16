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
    	String dataDir="merged_data_body0.6_url_des_NE";
        int classNum = 40; // 设置分类个数
        int[] num={42,18,18,1,27,41,38,62,58,80,47,14,54,82,32,65,33,50,69,81,20,24,38,1,35,41,72,71,51,90};
        int[] numZhang={52,49,43,43,59,56,47,68,58,56,52,41,70,69,60,61,55,54,65,57,44,48,71,28,57,61,54,63,59,62};
        int[] numZhang2={49,43,33,38,55,51,44,62,49,49,41,32,60,62,44,50,46,47,53,48,42,42,62,21,50,51,42,57,52,52};
        int[] numZhang15={38,34,28,25,36,36,36,51,33,38,30,24,47,45,38,43,35,31,44,41,34,31,40,14,43,41,32,41,43,38};
        HashMap<String,String> name=new HashMap<String,String>();
        HashClass(name);
        File sourceDir = new File(dataDir); // 存放源数据的地方
        File[] all = sourceDir.listFiles();
        processDiscard(all,dataDir);   //处理丢失的文件，以对齐行标
        File source=new File(dataDir);
        File[] allFile=source.listFiles();
        String lastName = allFile[0].getName();
        lastName = lastName.substring(0, lastName.lastIndexOf("_"));// 取得当前名字
        int lastPos = 0;
        int c=0;
        for (int i = 0; i < allFile.length; i++) {
            String currName = allFile[i].getName();
            currName = currName.substring(0, currName.lastIndexOf("_"));
            if (!currName.equals(lastName)) {
                System.out.println("正在处理:" + lastName);
                processAll(allFile, lastName, lastPos, i);// 将要处理的文件返回传入
                lastPos = i;
                excuteClusty(numZhang15[c], lastName);
                lastName = currName; // 对下个人进行聚类
                c++;
            }
        }
        System.out.println("正在处理:" + lastName);
        processAll(allFile, lastName, lastPos, allFile.length);// 处理最后一个人
        excuteClusty(numZhang15[29], lastName);
    }
	private static void HashClass(HashMap<String, String> name) {
		name.put("Alvin_Cooper","50");
		name.put("Harry_Hughes","50");
		name.put("Jonathan_Brooks","50");
		name.put("Jude_Brown", "50");
		name.put("Karen_Peterson", "50");
		name.put("Marcy_Jackson", "50");
		name.put("Martha_Edwards", "50");
		name.put("Neil_Clark", "50");
		name.put("Stephan_Johnson", "50");
		name.put("Violet_Howard", "50");
		name.put("Dekang_Lin", "30");
		name.put("Chris_Brockett", "30");
		name.put("James_Curran", "30");
		name.put("Mark_Johnson", "30");
		name.put("Jerry_Hobbs", "30");
		name.put("Frank_Keller", "30");
		name.put("Leon_Barrett", "30");
		name.put("Robert_Moore", "30");
		name.put("Sharon_Goldwater", "30");
		name.put("Stephen_Clark", "30");
		name.put("Arthur_Morgan", "50");
		name.put("James_Morehead", "50");
		name.put("James_Davidson", "50");
		name.put("Patrick_Killen", "50");
		name.put("William_Dickson", "50");
		name.put("George_Foster", "50");
		name.put("James_Hamilton", "50");
		name.put("John_Nelson", "50");
		name.put("Thomas_Fraser", "50");
		name.put("Thomas_Kirk", "50");
	}
    private static void processDiscard(File[] fileList,String path)
    {
    	int no=0;
    	String temp =fileList[0].getName();
    	String lastName = fileList[0].getName().substring(0, temp.lastIndexOf("_"));
    	for(int i=0;i<fileList.length;i++)
    	{
    		String currentName=fileList[i].getName();
    		currentName = currentName.substring(0, currentName.lastIndexOf("_"));
    		String tempNo =fileList[i].getName();
        	tempNo=tempNo.substring(tempNo.lastIndexOf("_")+1,tempNo.indexOf(".")); //取序号
        	int currentNo=Integer.parseInt(tempNo);
        	if(currentName.equals(lastName) && currentNo!=no )//处理文件丢失
        	{
        		while(no<currentNo)
        		{
        			String fileNewName="";
        			if(no<10)
        			{
        				fileNewName=currentName+"_00"+no;
        			}
        			else
        			{
        				fileNewName=currentName+"_0"+no;
        			}
        			File f =new File(path+"/"+fileNewName+".txt");
        			try
        			{
        				OutputStream os=new FileOutputStream(f,false);
        				os.write("".getBytes());
        				os.close();
        			}
        			catch(Exception e)
        			{
        				e.printStackTrace();
        			}
        			no++;
        		}
        		no++;
        	}
        	else if(!currentName.equals(lastName))
        	{
        		no=1;   //对每个人重置信息
        		lastName=currentName;
        	}
        	else 
        		no++;
    	}
    }
    private static void excuteClusty(int classNum, String lastName) {
        try // 执行命令行 处理当前 名字 矩阵信息
        {
            String s = "vcluster -clmethod=agglo "
                    + // 采用层次聚类
                    "-clustfile=matrixFile\\" + lastName + "0"
                    + ".cluster "
                    //+"-agglofrom=80 "
                    //+"-rowmodel=maxtf "
                    +"-sim=cos "
                    +"-colmodel=idf " 
                    //+"-mincomponent=8 "
                    //+"cstype=best "  //聚类算法为rb rbr graph才可用
                    //+"-nnbrs=80 "
                    +"-colprune=1 "   //**选择最有影响的列数，总列数*系数
                    + "-crfun=slink "// 最后分类文件
                    //+"-showtree " + "-showfeatures "+"-labeltree "+"-showsummaries=cliques "
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
