import os
os.system("java -cp asset/scorer_1.1/wepsEvaluation.jar es.nlp.uned.weps.evaluation.SystemScorer weps2007/test/truth_files/official_annotation cluto_xml_result result -ALLMEASURES -AllInOne -OneInOne -Combined -average")