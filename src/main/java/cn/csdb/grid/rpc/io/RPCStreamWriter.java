package cn.csdb.grid.rpc.io;

import java.io.OutputStream;

import com.caucho.hessian.io.Hessian2Output;

public class RPCStreamWriter extends Hessian2Output
{
	public RPCStreamWriter(OutputStream os)
	{
		super(os);
	}
}
