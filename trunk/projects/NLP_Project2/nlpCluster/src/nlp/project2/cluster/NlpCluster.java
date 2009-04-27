package nlp.project2.cluster;


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
	
	public Clusterer getClusterer(Instances instances, String clusterType) {
		Clusterer clusterer = null;
		try {
			if (clusterType.equals("em")) {
				EM em = new EM();

				em.setNumClusters(-1);
				em.setMaxIterations(100);

				clusterer = em;
			} else if (clusterType.equals("k")) {
				SimpleKMeans km = new SimpleKMeans();
				km.setNumClusters(3);//kmeans分为几类，在这里设置
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
	
	public ClusterEvaluation evaluateCluster(Clusterer clusterer,
			Instances instances){
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(clusterer);
		try {
			eval.evaluateClusterer(instances);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String evalString = eval.clusterResultsToString();
		System.out.println(evalString);
		int numClusters = eval.getNumClusters();
		//double[] assignments = eval.getClusterAssignments();
		System.out.println("NumClusters=" + numClusters);
		return eval;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NlpCluster nlp=new NlpCluster();
		//要进行分类的数据
		Instances ins=nlp.loadInstance("D:\\Program Files\\Weka-3-6\\data\\cpu.arff");
		Clusterer clus=nlp.getClusterer(ins,"k");
		nlp.evaluateCluster(clus, ins);

	}

}
