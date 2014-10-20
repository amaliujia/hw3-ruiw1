package utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import typesystems.Document;

/**
 * Use to save and sort final results
 * 
 * @author amaliujia
 * 
 */
public class Posting implements Comparable<Posting>{
  public int id;

  public double score;

  public boolean isQuery;

  public int relevance;

  public String text;

  public HashMap<String, Integer> tokenList;

  /**
   * Constructor
   * @param ID
   *        Query id
   * @param isQuery
   *        if this is a query
   * @param relevance
   *        relevance measure
   * @param text
   *        Document or query text
   */
  public Posting(int ID, boolean isQuery, int relevance, String text) {
    this.id = ID;
    this.isQuery = isQuery;
    this.relevance = relevance;
    this.text = text;
    tokenList = new HashMap<String, Integer>();
  }

  /**
   * Interface method, used to sort Posting list.
   * @param o
   *        Comparison target
   * @return Comparison result, 1 is this > o, -1 is this < o,
   *         and 0 is this == o
   */
  public int compareTo(Posting o) {
    if (this.score == o.score) {
      if (this.relevance == 1) {
        return 1;
      } else if (o.relevance == 1) {
        return -1;
      } else {
        return 0;
      }
    } else if (this.score < o.score)
      return 1;
    else
      return -1;
  }
}
