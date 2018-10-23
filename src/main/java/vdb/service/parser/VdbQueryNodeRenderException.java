package vdb.service.parser;

public class VdbQueryNodeRenderException extends Exception
{
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage()
	{
		return "cannot parse";
	}

}
