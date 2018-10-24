package cn.csdb.commons.marshal;

public interface Marshalable
{
	public void read(InputElement ie);

	public void write(OutputElement oe);
}
