package edu.cmu.lti.f14.hw3.hw3_ruiw1.casconsumers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;

import javax.swing.text.Position;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.lti.f14.hw3.hw3_ruiw1.typesystems.Document;
import edu.cmu.lti.f14.hw3.hw3_ruiw1.typesystems.Query;
import edu.cmu.lti.f14.hw3.hw3_ruiw1.typesystems.Token;
import edu.cmu.lti.f14.hw3.hw3_ruiw1.utils.Posting;
import edu.cmu.lti.f14.hw3.hw3_ruiw1.utils.Utils;

public class RetrievalEvaluator extends CasConsumer_ImplBase {

  /** query id number **/
  public ArrayList<Integer> qIdList;

  /** query and text relevant values **/
  public ArrayList<Integer> relList;

  public ArrayList<Double> similarityList;

  public HashMap<Integer, ArrayList<String>> queryHashMap;

  public HashMap<Integer, ArrayList<Integer>> queryHashMapTF;

  public HashMap<Integer, ArrayList> rankMap;

  public ArrayList<Double> MR;

  public ArrayList<Posting> cosinePostings;

  private Writer fileWriter = null;

  private Writer errorWriter = null;

  public String output;

  public String errorOutput;

  /**
   * Initialize necessary fields.
   */
  public void initialize() throws ResourceInitializationException {

    qIdList = new ArrayList<Integer>();
    relList = new ArrayList<Integer>();
    similarityList = new ArrayList<Double>();
    queryHashMap = new HashMap<Integer, ArrayList<String>>();
    queryHashMapTF = new HashMap<Integer, ArrayList<Integer>>();
    rankMap = new HashMap<Integer, ArrayList>();
    MR = new ArrayList<Double>();
    cosinePostings = new ArrayList<Posting>();
    output = (String) getConfigParameterValue("report");
    errorOutput = (String) getConfigParameterValue("errorAnalysis");
    try {
      fileWriter = new FileWriter(new File(output));
      errorWriter = new FileWriter(errorOutput);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * construct the global word dictionary 2. keep the word frequency for each sentence
   */
  @Override
  public void processCas(CAS aCas) throws ResourceProcessException {
    // System.out.println("Evaluator");
    JCas jcas;
    try {
      jcas = aCas.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }
    FSIterator it = jcas.getAnnotationIndex(Document.type).iterator();
    if (it.hasNext()) {
      Document doc = (Document) it.next();

      // Make sure that your previous annotators have populated this in CAS
      FSList fsTokenList = doc.getTokenList();
      ArrayList<Token> tokenList = Utils.fromFSListToCollection(fsTokenList, Token.class);

      qIdList.add(doc.getQueryID());
      relList.add(doc.getRelevanceValue());

      // compute cosine similarities here
      if (doc.getRelevanceValue() == 99) {// if it is query, no need to save similarity
        int relevance = doc.getRelevanceValue();
        boolean isQuery = true;
        int id = doc.getQueryID();
        Posting aPosting = new Posting(id, isQuery, relevance, doc.getText());
        for (int j = 0; j < tokenList.size(); j++) {
          aPosting.tokenList.put(tokenList.get(j).getText(), tokenList.get(j).getFrequency());
        }
        cosinePostings.add(aPosting);
      } else {// if it is a doc, then use a sort of cosine similarity formula
        int relevance = doc.getRelevanceValue();
        boolean isQuery = false;
        int id = doc.getQueryID();
        Posting aPosting = new Posting(id, isQuery, relevance, doc.getText());
        for (int j = 0; j < tokenList.size(); j++) {
          aPosting.tokenList.put(tokenList.get(j).getText(), tokenList.get(j).getFrequency());
        }
        cosinePostings.add(aPosting);
      }
    }
  }

  /**
   * Compute Cosine Similarity and rank the retrieved sentences 2. Compute the MRR metric
   */
  public void collectionProcessComplete(ProcessTrace arg0) throws ResourceProcessException,
          IOException {

    super.collectionProcessComplete(arg0);
    HashMap<Integer, ArrayList<Posting>> merger = new HashMap<Integer, ArrayList<Posting>>();
    HashMap<Integer, Integer> idMap = new HashMap<Integer, Integer>();
    ArrayList<Integer> idUnique = new ArrayList<Integer>();
    
    for(int z = 0; z < this.qIdList.size(); z++){
      if(idMap.containsKey(qIdList.get(z))){
        continue;
      }
      idMap.put(qIdList.get(z), 1);
    }
    
    Iterator<Integer> idIterator = idMap.keySet().iterator();
    while (idIterator.hasNext()) {
      int key = idIterator.next();
      idUnique.add(key);
    }
    Collections.sort(idUnique);
    
    int index;
    for (int i = 0; i < cosinePostings.size(); i++) {
      Posting a = cosinePostings.get(i);
      if (a.isQuery) {
        a.score = 0.0;
      } else {
        int j;
        index = -1;
        for (j = 0; j < cosinePostings.size(); j++) {
          if (a.id == cosinePostings.get(j).id && j != i) {
            index = j;
            break;
          }
        }
        if (index == -1) {
          System.out.println("-________________/");
        }

        Posting query = cosinePostings.get(index);
        Posting doc = cosinePostings.get(i);

        if (merger.containsKey(doc.id)) {
          ArrayList<Posting> p = merger.get(doc.id);
          p.add(doc);
          merger.put(doc.id, p);
        } else {
          ArrayList<Posting> p = new ArrayList<Posting>();
          p.add(doc);
          merger.put(doc.id, p);
        }

        HashMap<String, Integer> queryVector = query.tokenList;
        HashMap<String, Integer> docVector = doc.tokenList;

         a.score = computeCosineSimilarity(queryVector, docVector);
      }
    }

    // compute the rank of retrieved sentences
    // the real wrok is to sort
    Iterator it = merger.keySet().iterator();
    DecimalFormat df = new DecimalFormat("0.0000");
//    while (it.hasNext()) {
//      int id = (Integer) it.next();
    for(int f = 0; f < idUnique.size(); f++){
      int id = idUnique.get(f);
      ArrayList<Posting> rankList = merger.get(id);
      Collections.sort(rankList);
      for (int j = 0; j < rankList.size(); j++) {
        if (rankList.get(j).relevance == 1) {
          double aMR = ((double) 1) / (j + 1);
          MR.add(aMR);
          fileWriter.append("cosine=" + df.format(rankList.get(j).score) + "\trank=" + (j + 1)
                  + "\tqid=" + rankList.get(j).id + "\trel=" + rankList.get(j).relevance + "\t"
                  + rankList.get(j).text + "\n");
          break;
        } else {
          errorWriter.append("cosine=" + df.format(rankList.get(j).score) + "\trank=" + (j + 1)
                  + "\tqid=" + rankList.get(j).id + "\trel=" + rankList.get(j).relevance + "\t"
                  + rankList.get(j).text + "\n");
        }
      }
    }

    // compute the metric:: mean reciprocal rank
    double metric_mrr = compute_mrr();
    fileWriter.append("MRR=" + df.format(metric_mrr));
    System.out.println(" (MRR) Mean Reciprocal Rank ::" + df.format(metric_mrr));
    fileWriter.close();
    errorWriter.close();
  }

  /**
   * 
   * @param queryVector
   *          input query vector
   * @param docVector
   *          input doc vector
   * @return cosine_similarity
   */
  private double computeCosineSimilarity(Map<String, Integer> queryVector,
          Map<String, Integer> docVector) {

    double cosine_similarity = 0.0;
    double queryNorm = 0.0;
    double docNorm = 0.0;
    // compute cosine similarity between two sentences
    Iterator<String> it = queryVector.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      if (docVector.containsKey(key)) {
        cosine_similarity += (queryVector.get(key) * docVector.get(key));
        // System.out.println(key + "  " + queryVector.get(key) + "   " + docVector.get(key));
      }
      queryNorm += (Math.pow(queryVector.get(key), 2));
    }

    it = docVector.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      docNorm += (Math.pow(docVector.get(key), 2));
    }
    // the most naive way to implement cosine similarities
    cosine_similarity = cosine_similarity / (Math.sqrt(queryNorm) * Math.sqrt(docNorm));
    return cosine_similarity;
  }

  /**
   * 
   * @param queryVector
   *          input query vector
   * @param docVector
   *          input doc vector
   * @return Dice similarity
   */
  private double computeSorensonIndex(Map<String, Integer> queryVector,
          Map<String, Integer> docVector) {
    double sorensonIndex = 0.0;
    double queryLength = 0.0;
    double docLength = 0.0;

    Iterator<String> it = queryVector.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      if (docVector.containsKey(key)) {
        sorensonIndex += (queryVector.get(key) * docVector.get(key));
        // System.out.println(key + "  " + queryVector.get(key) + "   " + docVector.get(key));
      }
      queryLength += (Math.pow(queryVector.get(key), 2));
    }

    it = docVector.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      docLength += (Math.pow(docVector.get(key), 2));
    }

    sorensonIndex = 2 * sorensonIndex / (docLength + queryLength);

    return sorensonIndex;
  }

  /**
   * 
   * @param queryVector
   *          input query vector
   * @param docVector
   *          input doc vector
   * @return Jaccard similarity       
   */
  private double computeJaccardIndex(Map<String, Integer> queryVector,
          Map<String, Integer> docVector) {
    double jaccardIndex = 0.0;
    double queryLength = 0.0;
    double docLength = 0.0;

    Iterator<String> it = queryVector.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      if (docVector.containsKey(key)) {
        jaccardIndex += 1; //(queryVector.get(key) * docVector.get(key));
        // System.out.println(key + "  " + queryVector.get(key) + "   " + docVector.get(key));
      }
      queryLength += 1 ;//(Math.pow(queryVector.get(key), 2));
    }

    it = docVector.keySet().iterator();
    while (it.hasNext()) {
      String key = it.next();
      if(!docVector.containsKey(key)){
        queryLength++;
      }
      //docLength += (Math.pow(docVector.get(key), 2));
    }

    //jaccardIndex = jaccardIndex / (queryLength + docLength - jaccardIndex);
    jaccardIndex = jaccardIndex / queryLength;
    return jaccardIndex;
  }

  /**
   * Compute final MMR
   * @return mrr
   */
  private double compute_mrr() {
    double metric_mrr = 0.0;
    DecimalFormat dFormat = new DecimalFormat("");
    // compute Mean Reciprocal Rank (MRR) of the text collection
    for (int i = 0; i < MR.size(); i++) {
      metric_mrr += MR.get(i);
    }
    metric_mrr /= MR.size();
    return metric_mrr;
  }

}
