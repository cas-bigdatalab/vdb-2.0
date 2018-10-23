package vdb.mydb.typelib.sdef;

import java.util.List;

public interface SdefNode
{
	String getNodeValue();

	void setNodeValue(String nodeValue);

	String getChildValue(String nodeName);

	List<SdefNode> getChildren(String nodeName);

	SdefNode getChild(String nodeName);

	SdefNode addChild(String nodeName);

	void addChild(String nodeName, String nodeValue);

	SdefNode selectSingleNode(String path);

	List<SdefNode> selectNodes(String path);
}
