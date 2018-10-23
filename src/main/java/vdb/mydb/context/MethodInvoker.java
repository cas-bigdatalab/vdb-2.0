package vdb.mydb.context;

import java.util.List;

import org.apache.commons.beanutils.MethodUtils;
import org.springframework.beans.factory.InitializingBean;

public class MethodInvoker implements InitializingBean
{
	Object _target;

	String _methodName;

	List<?> _arguments;

	public String getMethodName()
	{
		return _methodName;
	}

	public void setMethodName(String method)
	{
		_methodName = method;
	}

	public Object getTarget()
	{
		return _target;
	}

	public void setTarget(Object target)
	{
		_target = target;
	}

	public List<?> getArguments()
	{
		return _arguments;
	}

	public void setArguments(List<?> arguments)
	{
		this._arguments = arguments;
	}

	public void afterPropertiesSet() throws Exception
	{
		MethodUtils.invokeMethod(_target, _methodName, _arguments.toArray());
	}
}
