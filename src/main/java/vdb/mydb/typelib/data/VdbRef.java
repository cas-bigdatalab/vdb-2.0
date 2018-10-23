package vdb.mydb.typelib.data;

import java.io.Serializable;

import vdb.mydb.bean.AnyBean;
import vdb.mydb.bean.BeanRef;
import vdb.mydb.typelib.sdef.Sdef;
import vdb.mydb.typelib.sdef.SdefException;
import vdb.mydb.typelib.sdef.SimpleSdef;

public class VdbRef extends AbstractData
{
	private BeanRef _beanRef;

	public AnyBean getRefBean() throws Exception
	{
		try
		{
			return _beanRef.getBean();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public String getTitle()
	{
		String title = null;
		try
		{
			AnyBean thatBean = _beanRef.getBean();
			// title = thatBean.getTitle().getAsSdef().getTitle();
			title = thatBean.getTitle().getTitle();
		}
		catch (Exception e)
		{
		}

		return title == null ? "" : title;
	}

	public Sdef getAsSdef()
	{
		try
		{
			AnyBean thatBean = _beanRef.getBean();
			return new SimpleSdef(thatBean.getId().getValue(), thatBean
					.getTitle().getTitle());
		}
		catch (Exception e)
		{
		}

		return new SimpleSdef(null, null);
	}

	public void setAsSdef(Sdef ddl) throws SdefException
	{
		String ddlString = ddl.getValue();
		if ("null".equalsIgnoreCase(ddlString)
				|| ddlString.trim().length() == 0)
			_beanRef = null;
		else
		{
			_beanRef = new BeanRef(ddlString.substring(0, ddlString
					.indexOf("/")), ddlString
					.substring(ddlString.indexOf("/") + 1));
		}
	}

	public BeanRef getBeanRef()
	{
		return _beanRef;
	}

	public void setBeanRef(BeanRef beanRef)
	{
		_beanRef = beanRef;
	}

	public String getAsText()
	{
		return getValue();
	}

	public void setAsText(String text)
	{
		String ddlString = text;
		if ("null".equalsIgnoreCase(ddlString)
				|| ddlString.trim().length() == 0)
			_beanRef = null;
		else
		{
			_beanRef = new BeanRef(ddlString.substring(0, ddlString
					.indexOf("/")), ddlString
					.substring(ddlString.indexOf("/") + 1));
		}
	}

	/**
	 * 引用类型的值为0
	 */
	public long getBytes() {
		Serializable id = _beanRef.getId();
		if (id instanceof VdbString) {
			return id.toString().getBytes().length;
		}
		if (id instanceof VdbLong){
			return Long.SIZE;
		}
		if (id instanceof VdbDouble){
			return Double.SIZE;
		}
		if (id instanceof VdbDate){
			return Long.SIZE;
		}
		return id.toString().getBytes().length;
	}

	public boolean isEmpty() throws Exception {
		return _beanRef==null || _beanRef.getId()==null;
	}
}
