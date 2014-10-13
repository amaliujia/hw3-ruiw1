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
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
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

import typesystems.*;;


public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

  public static EnglishAnalyzerConfigurable analyzer;
  private String stopwordsFile;
  private HashSet<String> stopwords;
  public void initialize(UimaContext aContext){
    stopwordsFile = (String)aContext.getConfigParameterValue("stopwords");
    stopwords = new HashSet<String>();
    analyzer = new EnglishAnalyzerConfigurable (org.apache.lucene.util.Version.LUCENE_40);
    try {
      File file = new File(stopwordsFile);
      Scanner scanner = new Scanner(file);
      while(scanner.hasNext()){
        stopwords.add(scanner.nextLine());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
		//System.out.println("In Engine");
		if (iter.isValid()) {
			iter.moveToNext();
			Document doc = (Document) iter.get();
			//createTermFreqVector(jcas, doc);
		}
	}
	/**
	 * 
	 * @param jcas
	 * @param doc
	 */

	private void createTermFreqVector(JCas jcas, Document doc) {

		String docText = doc.getText();
		
		//TODO: construct a vector of tokens and update the tokenList in CAS
		String[] tokens = docText.split(" ");
		ArrayList<Token> tempList = new ArrayList<Token>();//(ArrayList<Token>)JCasUtil.select(doc.getTokenList(), Token.class);
		for(int i = 0; i < tokens.length; i++){
//		  if(stopwords.contains(tokens[i])){
//		    continue;
//		  }
		  try {
        String c[] = tokenizeQuery(tokens[i]);
        tokens[i] = c[0];
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
		  
		  for(int j = 0; j < tempList.size(); j++){
		    if(tempList.get(j).getText().equals(tokens[j])){
		       int tf = tempList.get(j).getFrequency();
		       tf++;
		       tempList.get(j).setFrequency(tf);
		    }else{
		      Token token = new Token(jcas);
		      token.setText(tokens[i]);
		      token.setFrequency(1);
		      tempList.add(token);
		    }
		  }
		}
	  doc.setTokenList(utils.Utils.fromCollectionToFSList(jcas, tempList));
	}
	
  public String[] tokenizeQuery(String query) throws IOException {

    TokenStreamComponents comp = analyzer.createComponents("dummy", new StringReader(query));
    TokenStream tokenStream = comp.getTokenStream();

    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
    tokenStream.reset();

    List<String> tokens = new ArrayList<String>();
    while (tokenStream.incrementToken()) {
      String term = charTermAttribute.toString();
      tokens.add(term);
    }
    return tokens.toArray(new String[tokens.size()]);
  }

}
