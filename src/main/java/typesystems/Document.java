

/* First created by JCasGen Sun Oct 12 15:33:24 EDT 2014 */
package typesystems;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Oct 12 15:33:24 EDT 2014
 * XML source: /Users/amaliujia/Documents/workspace/hw3-ruiw1/src/main/resources/descriptors/typesystems/VectorSpaceTypes.xml
 * @generated */
public class Document extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Document.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Document() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public Document(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Document(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Document(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: relevanceValue

  /** getter for relevanceValue - gets 
   * @generated
   * @return value of the feature 
   */
  public int getRelevanceValue() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_relevanceValue == null)
      jcasType.jcas.throwFeatMissing("relevanceValue", "typesystems.Document");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Document_Type)jcasType).casFeatCode_relevanceValue);}
    
  /** setter for relevanceValue - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setRelevanceValue(int v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_relevanceValue == null)
      jcasType.jcas.throwFeatMissing("relevanceValue", "typesystems.Document");
    jcasType.ll_cas.ll_setIntValue(addr, ((Document_Type)jcasType).casFeatCode_relevanceValue, v);}    
   
    
  //*--------------*
  //* Feature: queryID

  /** getter for queryID - gets 
   * @generated
   * @return value of the feature 
   */
  public int getQueryID() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_queryID == null)
      jcasType.jcas.throwFeatMissing("queryID", "typesystems.Document");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Document_Type)jcasType).casFeatCode_queryID);}
    
  /** setter for queryID - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setQueryID(int v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_queryID == null)
      jcasType.jcas.throwFeatMissing("queryID", "typesystems.Document");
    jcasType.ll_cas.ll_setIntValue(addr, ((Document_Type)jcasType).casFeatCode_queryID, v);}    
   
    
  //*--------------*
  //* Feature: text

  /** getter for text - gets 
   * @generated
   * @return value of the feature 
   */
  public String getText() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "typesystems.Document");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Document_Type)jcasType).casFeatCode_text);}
    
  /** setter for text - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setText(String v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_text == null)
      jcasType.jcas.throwFeatMissing("text", "typesystems.Document");
    jcasType.ll_cas.ll_setStringValue(addr, ((Document_Type)jcasType).casFeatCode_text, v);}    
   
    
  //*--------------*
  //* Feature: tokenList

  /** getter for tokenList - gets 
   * @generated
   * @return value of the feature 
   */
  public FSList getTokenList() {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "typesystems.Document");
    return (FSList)(jcasType.ll_cas.ll_getFSForRef(jcasType.ll_cas.ll_getRefValue(addr, ((Document_Type)jcasType).casFeatCode_tokenList)));}
    
  /** setter for tokenList - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setTokenList(FSList v) {
    if (Document_Type.featOkTst && ((Document_Type)jcasType).casFeat_tokenList == null)
      jcasType.jcas.throwFeatMissing("tokenList", "typesystems.Document");
    jcasType.ll_cas.ll_setRefValue(addr, ((Document_Type)jcasType).casFeatCode_tokenList, jcasType.ll_cas.ll_getFSRef(v));}    
  }

    