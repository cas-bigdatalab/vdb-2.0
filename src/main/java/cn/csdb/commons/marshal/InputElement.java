package cn.csdb.commons.marshal;

import java.util.List;

public interface InputElement
{
	String getText();

	List<InputElement> getChildren(String name);

	List<InputElement> getChildren();

	InputElement getChild(String name);

	String getName();

	void read(Marshalable object);
}