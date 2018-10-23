package vdb.mydb.typelib.sdef;

/**
 * a Sdef object represents a SDEF text
 * 
 * @author bluejoe
 * 
 */
public interface Sdef extends SdefNode
{
	/**
	 * retrieves all text in xml format, e.g. "<value>male</value><title>��</title>"
	 * 
	 * @return
	 */
	String getXml();

	/**
	 * retrieves value of this object, often be the value stored in database,
	 * e.g. "male"
	 * 
	 * @return
	 */
	String getValue();

	/**
	 * retrieves title of this object, often be caption of a label, which will
	 * be displayed on user interface, e.g. "��"
	 * 
	 * @return
	 */
	String getTitle();

	void setXml(String xml) throws Exception;
}