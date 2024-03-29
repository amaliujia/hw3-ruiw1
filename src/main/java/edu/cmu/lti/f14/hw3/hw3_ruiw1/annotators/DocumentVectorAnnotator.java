package edu.cmu.lti.f14.hw3.hw3_ruiw1.annotators;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.axis.Version;
import org.apache.axis.wsdl.symbolTable.Utils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.IntegerArray;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.uimafit.util.JCasUtil;

import edu.cmu.lti.f14.hw3.hw3_ruiw1.typesystems.Document;
import edu.cmu.lti.f14.hw3.hw3_ruiw1.typesystems.Token;
import edu.cmu.lti.f14.hw3.hw3_ruiw1.utils.StanfordLemmatizer;

/**
 * Document annotator, in charge of tokenize query and document text, and build vector space
 * @author amaliujia
 *
 */
public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

  // public static EnglishAnalyzerConfigurable analyzer;
  private String stopwordsFile;

  private HashSet<String> stopwords;

  /**
   * Initialize necessary fields.
   */
  public void initialize(UimaContext aContext) {
    //stopwordsFile = (String) aContext.getConfigParameterValue("stopwords");
    
    // analyzer = new EnglishAnalyzerConfigurable (org.apache.lucene.util.Version.LUCENE_40);
    try {
      stopwords = new HashSet<String>();
    //  File file = new File(stopwordsFile);
      //Scanner scanner = new Scanner(file);
      //while (scanner.hasNext()) {
        //stopwords.add(scanner.nextLine());
     // }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * A part of pipeline, process one document per time.
   */
  public void process(JCas jcas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
    if (iter.isValid()) {
      iter.moveToNext();
      Document doc = (Document) iter.get();
      createTermFreqVector(jcas, doc);
    }
  }

  /**
   * To create term frequency vectors.
   * @param jcas
   *          UIMA JCas
   * @param doc
   *        Input text
   */

  private void createTermFreqVector(JCas jcas, Document doc) {

    String docText = doc.getText();
    //docText = StanfordLemmatizer.stemText(docText);

    // construct a vector of tokens and update the tokenList in CAS
    // String[] tokens = docText.split(" ");
    ArrayList<String> tokens = (ArrayList<String>) tokenize0(docText);
    ArrayList<Token> tempList = new ArrayList<Token>();
    for (int i = 0; i < tokens.size(); i++) {
      // set token list
      int j;
      for (j = 0; j < tempList.size(); j++) {
        if (tempList.get(j).getText().equals(tokens.get(i))) {
          int tf = tempList.get(j).getFrequency();
          tf++;
          tempList.get(j).setFrequency(tf);
          break;
        }
      }
      if (j >= tempList.size()) {
        Token token = new Token(jcas);
        token.setText(tokens.get(i));
        token.setFrequency(1);
        tempList.add(token);
      }
    }
    doc.setTokenList(edu.cmu.lti.f14.hw3.hw3_ruiw1.utils.Utils.fromCollectionToFSList(jcas, tempList));

  }

  /**
   * A basic white-space tokenizer, it deliberately does not split on punctuation!
   * 
   * @param doc
   *          input text
   * @return a list of tokens.
   */

  List<String> tokenize0(String doc) {
    List<String> res = new ArrayList<String>();
    for (String s : doc.split("\\s+"))
      res.add(s);
    return res;
  }

  /**
   * A basic white-space tokenizer that removes uncessary punctuations.
   * 
   * @param doc
   *          input text
   * @return a list of tokens.
   */

  List<String> tokenize1(String doc) {
    List<String> res = new ArrayList<String>();
    for (String s : doc.split("\\s+")) {
 
//      if(stopwords.contains(s)){
//        continue;
//      }
//        res.add(s);
      if (Pattern.matches(".*'", s)) {
        res.add(s.substring(0, s.indexOf("'")));
      } else if (Pattern.matches(".*;", s)) {
        res.add(s.substring(0, s.indexOf(";")));
      } else if (Pattern.matches(".*\\?", s)) {
        res.add(s.substring(0, s.indexOf("?")));
      } else {
        res.add(s);
      }
    }
    return res;
  }

}
