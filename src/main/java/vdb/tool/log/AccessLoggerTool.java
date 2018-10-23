package vdb.tool.log;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletRequest;

import net.sf.json.JSONObject;
import vdb.log.util.LogQueue;
import vdb.log.vo.DataAccessLog;
import vdb.metacat.Entity;
import vdb.mydb.query.impl.JsoExpr;
import vdb.tool.VdbTool;
import vdb.tool.auth.AuthTool;

public class AccessLoggerTool extends VdbTool
{
	private ServletRequest _request;

	public void setRequest(ServletRequest request)
	{
		_request = request;
	}

	/**
	 * 记录实体查询与操作记录
	 * @param user 用户
	 * @param ip IP
	 * @param entity 查询或操作的实体
	 * @param operation 操作
	 * @param whereFilter 过滤条件
	 * @param beanId 如果是增、删、改等操作，操作的记录Id；如果是全表检索，则表示关键词
	 * @param flag 是否是前台查询(前台查询需要过滤1＝1的条件)
	 */
	public void logAccess(String user,String ip,Entity entity,String operation,JsoExpr whereFilter,
			String beanId,boolean flag){
		//如果没有传入用户，则从认证工具中取出来
		if(user==null || user.equals("")){
			try{
				user = new AuthTool().getUserName();
			}
			catch (Exception e){
				user = "";
			}
		}
		//如果IP为空，则从request中获取
		if(ip == null || ip.equals("")){
			ip = _request.getRemoteAddr();
		}
		//如果entity不为空，则存储相关日志记录
		if(entity!=null){
			try {
				String entityId = entity.getId();
				String datasetId = entity.getDataSet().getId();
				String param1="";
				String param2="";
				String param3="";
				//如果过滤条件不为空，则将查询字段、关键词等信息放入param1,param2,param3等字段
				if(whereFilter!=null){
					JSONObject json = JSONObject.fromObject(whereFilter);
					JsoExpr wf = (JsoExpr) JSONObject.toBean(json,JsoExpr.class);
					List<JsoExpr> exprs = getExprs(wf);
					if (flag)
					{
						exprs.remove(0);// 去掉第一个条件，从前台过来的查询，第一个条件默认为1=1
					}
					param1 = getQueryFields(exprs);
					param2 = getKeyWords(exprs);
					param3 = getWhereFilter(exprs);
				}
				
				if(beanId!=null){
					param2 = beanId;
					
				}
				
				log(datasetId, entityId, user, ip, operation, param1, param2, param3);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果entity为空，则不记入相关日志
	}
	
	private void getExprList(JsoExpr root, List<JsoExpr> list)
	{
		if (root != null)
		{
			if (isFieldExpr(root))
			{
				list.add(root);
			}
			else
			{
				getExprList(root.getA(), list);
				getExprList(root.getB(), list);
			}
		}
	}

	public List<JsoExpr> getExprs(JsoExpr whereFilter)
	{
		List<JsoExpr> exprs = new LinkedList<JsoExpr>();
		getExprList(whereFilter, exprs);
		return exprs;
	}

	public String getKeyWords(List<JsoExpr> list)
	{
		String keyWords = "";
		for (JsoExpr expr : list)
		{
			// 得到SDEF文档中的值
			String value = expr.getValue().toString();
			// 得到SDEF文档中VALUE节点的值
			if (value.indexOf("<value>")!=-1)
				value = value.substring(value.indexOf("<value>") + 7, value
						.indexOf("</value>"));
			try
			{
				Double.parseDouble(value);
			}
			catch (NumberFormatException e)
			{
				keyWords += value;
				keyWords += ",";
			}
		}
		return keyWords;
	}

	public String getQueryFields(List<JsoExpr> list)
	{
		String qureyFields = "";
		for (JsoExpr expr : list)
		{
			qureyFields += expr.getField();
			qureyFields += ",";
		}
		return qureyFields;
	}

	public String getWhereFilter(List<JsoExpr> list)
	{
		return list.toString();
	}

	private boolean isFieldExpr(JsoExpr expr)
	{
		if ("and".equals(expr.getOperator()) || "or".equals(expr.getOperator()))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private void log(String datasetId,String entityId,String user,String ip,String operation,
			String param1,String param2,String param3){
		
		DataAccessLog record = new DataAccessLog(user, ip, datasetId, entityId,
				operation, param1, param2, param3);
		try
		{
			LogQueue.getInstance().addLog(record);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
