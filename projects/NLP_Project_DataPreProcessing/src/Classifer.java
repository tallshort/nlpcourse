import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.RBFNetwork;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.NormalizedPolyKernel;
import weka.classifiers.functions.supportVector.Puk;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class Classifer {
    List<String> words; // 存放训练数据
    List<String> tests; // 存放测试数据
    List<String> classes; // 存放该词的分类结果
    String modelName; // 模型名
    String wordName; // 当前词
    int AttributeNum; // 属性的个数
    int posNum; //POS 个数
    int start;
    int end;
    HashSet<String> token;  //存放所有集合中的词

    public Classifer(List<String> wordList, List<String> testList,
            String model, String name, int startOffset, int endOffset)// 每一个词的所有训练信息
    {
        wordName = name;
        modelName = model;
        words = wordList;
        tests = testList;
        start = startOffset;
        end = endOffset;
        posNum = end - start;
        classes = new ArrayList<String>();
        token = DataProcessing.getTotalWordSet();
        try {
            executeWekaTutorial(); // 执行分类主函数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* 默认产生RBF神经网 */
    private void getClasses() {
        AttributeNum = words.get(1).split(",").length - 1;
        for (int i = 0; i < words.size(); i++) // 循环逐个加入样本
        {
            String[] temp = words.get(i).split(","); // 此数组为一个训练样本的各个属性值 如
            // {a,b,c,d,meaning}
            boolean flag = false; // 表示类别已存在
            for (int j = 0; j < classes.size(); j++) {
                if (classes.get(j).equals(temp[temp.length - 1])) {
                    flag = true;
                    break;
                }
            }
            if (!flag)
                classes.add(temp[temp.length - 1]);
        }
    }

    public static Classifier classifierFactory(String a) {
        if (a.equals("SMO")) {
            SMO smo = new SMO();
            NormalizedPolyKernel kernelPuk = new NormalizedPolyKernel();
            Puk kernelP = new Puk();
            smo.setKernel(kernelPuk);
            smo.setC(1.4);
            return smo;
        } else if (a.equals("MP")) {
            MultilayerPerceptron mp = new MultilayerPerceptron();
            mp.setHiddenLayers("a");
            mp.setLearningRate(0.3);
            mp.setMomentum(0.9);
            mp.setTrainingTime(500);
            return mp;
        } else if (a.equals("NaiveBayes")) {
            NaiveBayes nb = new NaiveBayes();
            return nb;
        } else if (a.equals("LibSVM")) {
            LibSVM ls = new LibSVM();
            ls.setSVMType(new SelectedTag(LibSVM.SVMTYPE_C_SVC,
                    LibSVM.TAGS_SVMTYPE));
            ls.setKernelType(new SelectedTag(LibSVM.KERNELTYPE_RBF,
                    LibSVM.TAGS_KERNELTYPE));
            return ls;
        } else {
            RBFNetwork rbf = new RBFNetwork();
            rbf.setNumClusters(3);
            return rbf;
        }
    }

    private void executeWekaTutorial() throws Exception {
        getClasses();
        FastVector allAttributes = createAttributes(AttributeNum, classes);
        Instances learningDataset = createLearningDataSet(allAttributes, words
                .size());
        Classifier predictiveModel = learningPredictiveModel(learningDataset);
        predictUnknownCases(learningDataset, predictiveModel);
        // Evaluation evaluation = evaluatePredictiveModel(predictiveModel,
        // learningDataset);
        // System.out.println(evaluation.toSummaryString());
    }

    private Evaluation evaluatePredictiveModel(Classifier classifier,
            Instances learningDataset) throws Exception {
        Evaluation learningSetEvaluation = new Evaluation(learningDataset);
        learningSetEvaluation.evaluateModel(classifier, learningDataset);
        return learningSetEvaluation;
    }

    private void predictUnknownCases(Instances learningDataset,
            Classifier predictiveModel) throws Exception {
        File resultFile = new File("result-" + modelName + start + end + ".txt");
        // 注意：此为存放结果的目录，目录必须已经建好，而且该文件不能已经存在，（把前面测试过的文件更名）因为文件是用APPEND方式打开的。
        OutputStream os = new FileOutputStream(resultFile, true); // APPEND
        // MODEL
        String result = ""; // 保存该单词输出结果
        // System.out.println("*** "+tests.size());
        for (int i = 0; i < tests.size(); i++) {

            String[] temp = tests.get(i).split(",");
            Instance testInstance = createInstance(learningDataset, temp);
            double res = predictiveModel.classifyInstance(testInstance);
            result += wordName + " " + wordName + "." + (i + 1) + " "
                    + classes.get((int) res) + "\n";
        }
        os.write(result.getBytes());
        os.close();

    }

    private Classifier learningPredictiveModel(Instances learningDataSet)
            throws Exception {
        Classifier classifier = classifierFactory(modelName);
        classifier.buildClassifier(learningDataSet);
        return classifier;
    }

    /* 构造属性，前n个属性（POS）由集合产生，预测属性（词意）由当前词给出 */
    private FastVector createAttributes(int n, List<String> l) {
        // Attribute ageAttribute = new Attribute("age");
        FastVector allAttributes = new FastVector(n + 1); // N 个属性 + 1个 预测属性
        FastVector Vector = new FastVector(43);
        Vector.addElement("*");
        Vector.addElement("Ag");
        Vector.addElement("a");
        Vector.addElement("ad");
        Vector.addElement("an");
        Vector.addElement("b");
        Vector.addElement("c");
        Vector.addElement("Dg");
        Vector.addElement("d");
        Vector.addElement("d1");
        Vector.addElement("e");
        Vector.addElement("f");
        Vector.addElement("g");
        Vector.addElement("h");
        Vector.addElement("i");
        Vector.addElement("j");
        Vector.addElement("k");
        Vector.addElement("l");
        Vector.addElement("m");
        Vector.addElement("Ng");
        Vector.addElement("n");
        Vector.addElement("ne");
        Vector.addElement("nf");
        Vector.addElement("nh");
        Vector.addElement("nr");
        Vector.addElement("ns");
        Vector.addElement("nt");
        Vector.addElement("nz");
        Vector.addElement("o");
        Vector.addElement("p");
        Vector.addElement("q");
        Vector.addElement("r");
        Vector.addElement("s");
        Vector.addElement("Tg");
        Vector.addElement("t");
        Vector.addElement("u");
        Vector.addElement("Vg");
        Vector.addElement("v");
        Vector.addElement("vd");
        Vector.addElement("vn");
        Vector.addElement("w");
        Vector.addElement("x");
        Vector.addElement("y");
        Vector.addElement("z");
        Vector.addElement("Ug");
        Vector.addElement("na");
        Vector.addElement("nx");
        for (int i = 0; i < posNum; i++) { //添加词性的 Attribute
            allAttributes.addElement(new Attribute(String.valueOf(i), Vector));
        }
        FastVector tokenVector=new FastVector(token.size()); //产生token 大小的FastVector
        for(String s : token)
        {
        	token.add(s);
        }
        for(int i=0;i<AttributeNum-posNum;i++)  //添加 token的 attribute
        {
        	allAttributes.addElement(new Attribute(String.valueOf(i),tokenVector));
        }
        FastVector result = new FastVector(l.size()); // 添加 预测属性
        for (int i = 0; i < l.size(); i++) {
            result.addElement(l.get(i));
            // System.out.print("类别:"+l.get(i));
        }
        // System.out.println("l.size()=" + l.size());
        allAttributes.addElement(new Attribute("result", result));
        return allAttributes;
    }

    /* instanceNum: 训练样本个数 */
    private Instances createLearningDataSet(FastVector allAttributes,
            int instanceNum) {
        // System.out.println("allAttributes.size()-1="+(allAttributes.size()-1));
        // System.out.println("instanceNum=" + instanceNum);
        Instances trainingDataSet = new Instances("Classifer", allAttributes,
                instanceNum);

        trainingDataSet.setClassIndex(allAttributes.size() - 1); //
        for (int i = 0; i < words.size(); i++) // 循环逐个加入样本
        {
            String[] temp = words.get(i).split(","); // 此数组为一个训练样本的各个属性值 如
            // {a,b,c,d,meaning}
            addInstance(trainingDataSet, temp);
        }
        return trainingDataSet;
    }

    private void addInstance(Instances trainingDataSet, String[] s) {
        Instance instance = createInstance(trainingDataSet, s);
        trainingDataSet.add(instance);
    }

    private Instance createInstance(Instances trainingDataSet, String[] s) {
        Instance instance = new Instance(s.length);
        instance.setDataset(trainingDataSet);
        for (int i = 0; i < s.length; i++) {
            try {
                instance.setValue(i, s[i]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                System.out.println(s[i]);
                throw e;
            }
        }
        return instance;
    }
}
