package edu.cmu.lti.f14.hw3.hw3_ruiw1.collectionreaders;

import java.util.ArrayList;

import javax.management.Query;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.lti.f14.hw3.hw3_ruiw1.typesystems.Document;

//import typesystems.Query;

/**
 *  This is document reader
 * @author amaliujia
 *
 */
public class DocumentReader extends JCasAnnotator_ImplBase {

  /**
   * Line by Line document processing.
   */
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    // System.out.println("Reader");
    // reading sentence from the CAS
    String sLine = jcas.getDocumentText();

    // make sure information from text collection are extracted correctly
    ArrayList<String> docInfo = parseDataLine(sLine);

    // This is to make sure tat parsing done properly and
    // minimal data for rel,qid,text are available to proceed
    if (docInfo.size() < 3) {
      System.err.println("Not enough information in the line");
      return;
    }
    int rel = Integer.parseInt(docInfo.get(0));
    int qid = Integer.parseInt(docInfo.get(1));
    String txt = docInfo.get(2);
    Document doc = new Document(jcas);
    doc.setText(txt);
    doc.setQueryID(qid);
    // Setting relevance value
    doc.setRelevanceValue(rel);
    doc.addToIndexes();

    // Adding populated FeatureStructure to CAS
    jcas.addFsToIndexes(doc);
  }

  /**
   * Make sure parse text line correctly.
   * @param line
   *        Input text
   * @return Parse result
   */
  public static ArrayList<String> parseDataLine(String line) {
    ArrayList<String> docInfo;

    String[] rec = line.split("[\\t]");
    String sResQid = (rec[0]).replace("qid=", "");
    String sResRel = (rec[1]).replace("rel=", "");

    StringBuffer sResTxt = new StringBuffer();
    for (int i = 2; i < rec.length; i++) {
      sResTxt.append(rec[i]).append(" ");
    }

    docInfo = new ArrayList<String>();
    docInfo.add(sResRel);
    docInfo.add(sResQid);
    docInfo.add(sResTxt.toString());
    return docInfo;
  }

}