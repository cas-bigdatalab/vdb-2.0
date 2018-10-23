package cn.csdb.grid.rpc.io;

import java.io.InputStream;

import com.caucho.hessian.io.Hessian2Input;

public class RPCStreamReader extends Hessian2Input
{
	public RPCStreamReader(InputStream is)
	{
		super(is);
	}
}
