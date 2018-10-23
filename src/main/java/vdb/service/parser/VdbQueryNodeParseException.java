package vdb.service.parser;

public class VdbQueryNodeParseException extends Exception
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "cannot parse as a VdbQueryNode";
	}

}
