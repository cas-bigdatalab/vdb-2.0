package cn.csdb.commons.marshal;

public interface OutputElement
{
	void setText(String value);

	OutputElement addChild(String name);

	OutputElement write(Marshalable object);
}