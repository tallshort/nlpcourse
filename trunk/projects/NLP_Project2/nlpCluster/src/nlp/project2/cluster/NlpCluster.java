package nlp.project2.cluster;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
/**
 * Weka 的聚类，和分类器几乎完全一样。
 * 可以用EM和KMeans的聚类
 * @author Dong Han
 *
 */
public class NlpCluster {

	
	private void getFileList(String strPath,List<String> fList)
	{
		File dir = new File(strPath);
		File[] files = dir.listFiles();

		if (files == null)
			return;
		for (int i = 0; i < files.length; i++) {
			String path = files[i].getAbsolutePath();
			if (files[i].isDirectory()) {
				getFileList(path, fList);
			} else {
				String type=path.substring(path.lastIndexOf(".")+1);
				if(type.equals("arff"))
				{
					fList.add(path);
				}
				
			}
		} 
		
		
	}
	
	
	public Instances loadInstance(String filepath){
		Instances data = null;
		try {
			DataSource source;
			source = new DataSource(filepath);
			data=source.getDataSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public Clusterer getClusterer(Instances instances, String clusterType,int numClusters) {
		Clusterer clusterer = null;
		try {
			if (clusterType.equals("em")) {
				EM em = new EM();

				em.setNumClusters(-1);
				em.setMaxIterations(100);

				clusterer = em;
			} else if (clusterType.equals("k")) {
				SimpleKMeans km = new SimpleKMeans();
				km.setNumClusters(numClusters);//kmeans分为几类，在这里设置
				clusterer = km;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			clusterer.buildClusterer(instances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return clusterer;
	}
	
	public void saveToFile(String orgFilePath,int num,double []result){
		String str=orgFilePath.substring(0, orgFilePath.lastIndexOf("."));
		str+="_"+String.format("%1$03d",num)+".cluster";
		FileWriter fw;
		try {
			fw = new FileWriter(str);
			 BufferedWriter bw = new BufferedWriter(fw);  
			 int length=result.length;
			 int i=0;
			 for(i=0;i<length-1;++i)
			 {
				 bw.write(""+((int)result[i]));
				 bw.newLine();				 
			 }
			 bw.write(""+((int)result[i]));
			 bw.flush();
			 fw.flush();
			 bw.close();
			 fw.close();	
			 System.out.println(str+"文件保存完毕");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClusterEvaluation evaluateCluster(Clusterer clusterer,
			Instances instances,String orgFilePath){
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(clusterer);
		try {
			eval.evaluateClusterer(instances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String evalString = eval.clusterResultsToString();
		//System.out.println(evalString);
		double[] assignments=eval.getClusterAssignments();
		//System.out.println("分类数:"+eval.getNumClusters());
		//System.out.println("结果:");
		saveToFile(orgFilePath,eval.getNumClusters(),assignments);
		return eval;
	}
	
	//输入并返回目录字符串
	private String getDir()
	{
		System.out.println("请输入要聚类的文件:");
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
		try {
			return r.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private HashMap<String,Integer> getClusterNumMap()
	{
		HashMap<String,Integer> map=new HashMap<String,Integer>();
		map.put("Alvin_Cooper",Integer.valueOf(52));
		map.put("Arthur_Morgan",Integer.valueOf(49));
		map.put("Chris_Brockett",Integer.valueOf(43));
		map.put("Dekang_Lin",Integer.valueOf(43));
		map.put("Frank_Keller",Integer.valueOf(59));
		map.put("George_Foster",Integer.valueOf(56));
		map.put("Harry_Hughes",Integer.valueOf(47));
		map.put("James_Curran",Integer.valueOf(68));
		map.put("James_Davidson",Integer.valueOf(58));
		map.put("James_Hamilton",Integer.valueOf(56));
		map.put("James_Morehead",Integer.valueOf(52));
		map.put("Jerry_Hobbs",Integer.valueOf(41));
		map.put("John_Nelson",Integer.valueOf(70));
		map.put("Jonathan_Brooks",Integer.valueOf(69));
		map.put("Jude_Brown",Integer.valueOf(60));
		map.put("Karen_Peterson",Integer.valueOf(61));
		map.put("Leon_Barrett",Integer.valueOf(55));
		map.put("Marcy_Jackson",Integer.valueOf(54));
		map.put("Mark_Johnson",Integer.valueOf(65));
		map.put("Martha_Edwards",Integer.valueOf(57));
		map.put("Neil_Clark",Integer.valueOf(44));
		map.put("Patrick_Killen",Integer.valueOf(48));
		map.put("Robert_Moore",Integer.valueOf(71));
		map.put("Sharon_Goldwater",Integer.valueOf(28));
		map.put("Stephan_Johnson",Integer.valueOf(57));
		map.put("Stephen_Clark",Integer.valueOf(61));
		map.put("Thomas_Fraser",Integer.valueOf(54));
		map.put("Thomas_Kirk",Integer.valueOf(63));
		map.put("Violet_Howard",Integer.valueOf(59));
		map.put("William_Dickson",Integer.valueOf(62));
		return map;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NlpCluster nlp=new NlpCluster();
		//要进行分类的数据
		String orgFilePath=nlp.getDir();
		List<String> list=new Vector<String>();
		nlp.getFileList(orgFilePath,list);
		HashMap<String,Integer> map=nlp.getClusterNumMap();
		for(String filePath:list)
		{
			String filename=filePath.substring(filePath.lastIndexOf("\\")+1, filePath.lastIndexOf("."));
			Instances ins=nlp.loadInstance(filePath);
			Clusterer clus=nlp.getClusterer(ins,"k",map.get(filename).intValue());
			nlp.evaluateCluster(clus, ins,filePath);
		}
		System.out.println("全部完成");
	}
}
