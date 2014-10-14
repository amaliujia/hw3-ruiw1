package casconsumers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.management.Query;
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

import annotators.Posting;
import typesystems.Document;
import typesystems.Token;

public class RetrievalEvaluator extends CasConsumer_ImplBase {

	/** query id number **/
	public ArrayList<Integer> qIdList;

	/** query and text relevant values **/
	public ArrayList<Integer> relList;

	/** Doc Annotation **/
	public ArrayList<Document> docList;
	
	public ArrayList<Double> similarityList;
	
	public HashMap<Integer, Document> queryHashMap;
	
	public HashMap<Integer, ArrayList> rankMap;
	
	public ArrayList<Double> MR;
		
	public void initialize() throws ResourceInitializationException {

		qIdList = new ArrayList<Integer>();
		relList = new ArrayList<Integer>();
		docList = new ArrayList<Document>();
		similarityList = new ArrayList<Double>();
		queryHashMap = new HashMap<Integer, Document>();
		rankMap = new HashMap<Integer, ArrayList>();
		MR = new ArrayList<Double>();
	}

	/**
	 * construct the global word dictionary 2. keep the word
	 * frequency for each sentence
	 */
	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {
	//  System.out.println("Evaluator");
		JCas jcas;
		try {
			jcas =aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIterator it = jcas.getAnnotationIndex(Document.type).iterator();
	
		if (it.hasNext()) {
			Document doc = (Document) it.next();

			//Make sure that your previous annotators have populated this in CAS
			FSList fsTokenList = doc.getTokenList();
			//ArrayList<Token>tokenList=Utils.fromFSListToCollection(fsTokenList, Token.class);

			qIdList.add(doc.getQueryID());
			relList.add(doc.getRelevanceValue());
	    docList.add(doc);

	     //compute cosine similarities here
	    if(doc.getRelevanceValue() == 99){// if it is query, no need to save similarity
	      queryHashMap.put(doc.getQueryID(), doc);
	      similarityList.add(0.0);
	    }else{// if it is a doc, then use a sort of cosine similarity formula
	        Document queryDoc = queryHashMap.get(doc.getQueryID());
	        if(queryDoc == null){
	          try {
              throw new Exception();
            } catch (Exception e) {
              System.out.println("Query lost!!!");
              e.printStackTrace();
            }
	        }
	        HashMap<String, Integer> queryVector = new HashMap<String, Integer>();
	        HashMap<String, Integer> docVector = new HashMap<String, Integer>();
	        
	        ArrayList<Token> queryTokenList = utils.Utils.fromFSListToCollection(queryDoc.getTokenList(), Token.class);
	        ArrayList<Token> docTokenList = utils.Utils.fromFSListToCollection(doc.getTokenList(), Token.class);
	        
	        for(int i = 0; i < queryTokenList.size(); i++){
	          queryVector.put(queryTokenList.get(i).getText(), queryTokenList.get(i).getFrequency());
	        }
	        for(int i = 0; i <  docTokenList.size(); i++){
	           docVector.put(docTokenList.get(i).getText(), docTokenList.get(i).getFrequency());
	        }
	        similarityList.add(computeCosineSimilarity(queryVector, docVector));
	      }
	    }
		}

	/**
	 * Compute Cosine Similarity and rank the retrieved sentences 2.
	 * Compute the MRR metric
	 */
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {

		super.collectionProcessComplete(arg0);
		
		// compute the rank of retrieved sentences
		// the real wrok is to sort
		Iterator it = queryHashMap.keySet().iterator();
		while(it.hasNext()){
		  int id = (Integer) it.next();
		  ArrayList<Posting> rankList = new ArrayList<Posting>();
		  for(int i = 0; i < qIdList.size(); i++){
		    if((qIdList.get(i) == id) && (relList.get(i) != 99)){
		      rankList.add(new Posting(id, similarityList.get(i), docList.get(i)));
		    }
		  }
		  Collections.sort(rankList);
		  for(int j = 0; j < rankList.size(); j++){
		    if(rankList.get(j).doc.getRelevanceValue() == 1){
		      MR.add(((double)1 / (j + 1)));
		      break;
		    }
		  }
		}
		
		
		// TODO :: compute the metric:: mean reciprocal rank
		double metric_mrr = compute_mrr();
		System.out.println(" (MRR) Mean Reciprocal Rank ::" + metric_mrr);
	}

	/**
	 * 
	 * @return cosine_similarity
	 */
	private double computeCosineSimilarity(Map<String, Integer> queryVector,
			Map<String, Integer> docVector) {
		double cosine_similarity=0.0;
    double queryNorm = 0.0;
    double docNorm = 0.0;
		//compute cosine similarity between two sentences
		Iterator<String> it = queryVector.keySet().iterator();
		while (it.hasNext()) {
		  String key = it.next();
      cosine_similarity += (queryVector.get(key) * docVector.get(key));
      queryNorm += (Math.pow(queryVector.get(key), 2));
    }
		
		it = docVector.keySet().iterator();
		while(it.hasNext()){
		  String key = it.next();
		  docNorm += (Math.pow(docVector.get(key), 2));
		}
		// the most naive way to implement cosine similarities
		cosine_similarity = cosine_similarity / (Math.sqrt(queryNorm) * Math.sqrt(docNorm));

		return cosine_similarity;
	}

	/**
	 * 
	 * @return mrr
	 */
	private double compute_mrr() {
		double metric_mrr=0.0;

		// TODO :: compute Mean Reciprocal Rank (MRR) of the text collection
		
		return metric_mrr;
	}

}
