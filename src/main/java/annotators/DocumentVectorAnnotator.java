package annotators;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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

import typesystems.*;

;

public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

  // public static EnglishAnalyzerConfigurable analyzer;
  private String stopwordsFile;

  private HashSet<String> stopwords;

  public void initialize(UimaContext aContext) {
    stopwordsFile = (String) aContext.getConfigParameterValue("stopwords");
    stopwords = new HashSet<String>();
    // analyzer = new EnglishAnalyzerConfigurable (org.apache.lucene.util.Version.LUCENE_40);
    try {
      File file = new File(stopwordsFile);
      Scanner scanner = new Scanner(file);
      while (scanner.hasNext()) {
        stopwords.add(scanner.nextLine());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void process(JCas jcas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
    if (iter.isValid()) {
      iter.moveToNext();
      Document doc = (Document) iter.get();
      createTermFreqVector(jcas, doc);
    }
  }

  /**
   * 
   * @param jcas
   * @param doc
   */

  private void createTermFreqVector(JCas jcas, Document doc) {

    String docText = doc.getText();

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
    doc.setTokenList(utils.Utils.fromCollectionToFSList(jcas, tempList));

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
   * Use to remove stopwords and do stemming
   * 
   * @param query
   *          the word need to deal with.
   * @return The cleaner word
   * @throws IOException
   */
  // public String[] tokenizeQuery(String query) throws IOException {
  //
  // TokenStreamComponents comp = analyzer.createComponents("dummy", new StringReader(query));
  // TokenStream tokenStream = comp.getTokenStream();
  //
  // CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
  // tokenStream.reset();
  //
  // List<String> tokens = new ArrayList<String>();
  // while (tokenStream.incrementToken()) {
  // String term = charTermAttribute.toString();
  // tokens.add(term);
  // }
  // return tokens.toArray(new String[tokens.size()]);
  // }

}
