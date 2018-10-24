/*
 * Created on 2003-5-15
 */
package cn.csdb.commons.jsp;

import javax.servlet.http.HttpServletRequest;

import cn.csdb.commons.util.JspUtils;

/*
 * 用以实现数据记录分页显示。
 * 
 * @author bluejoe
 */
public class PageViewer
{
	private int _beginning;

	private int _end;

	private int _currentPage;

	private int _pageCount;

	private PageUrlBuilder _url;

	private int _recordCount;

	public PageViewer(HttpServletRequest request, int recordCount, int pageSize)
	{
		this(request, "page", recordCount, pageSize);
	}

	public PageViewer(HttpServletRequest request, String keyword,
			int recordCount, int pageSize)
	{
		this(new SimplePageUrlGetter(request, keyword), recordCount,
				getPageIndex(request, "page"), pageSize);
	}

	private static int getPageIndex(HttpServletRequest request, String key)
	{
		try
		{
			return Integer.parseInt(request.getParameter(key));
		}
		catch (Exception e)
		{
			return 20;
		}
	}

	public PageViewer(PageUrlBuilder url, int recordCount, int gotoPage,
			int pageSize)
	{
		_url = url;

		// 当前页索引
		_recordCount = recordCount;

		if (pageSize == -1)
		{
			_beginning = 1;
		}
		else
		{
			_pageCount = (recordCount + pageSize - 1) / pageSize;

			this._currentPage = gotoPage;
			if (gotoPage < 0)
			{
				_currentPage += _pageCount;
			}
			if (gotoPage > _pageCount)
			{
				_currentPage = _pageCount;
			}
		}

		if (_currentPage == 0)
			_currentPage = 1;

		_beginning = (_currentPage - 1) * pageSize + 1;

		if (_currentPage == _pageCount)
		{
			_end = recordCount;
		}
		else
		{
			_end = _beginning + pageSize - 1;
		}
	}

	/**
	 * 获取本页的起始记录在总记录数中的索引 第一条的索引是1，而不是0
	 */
	public int getBeginning()
	{
		return _beginning;
	}

	/**
	 * 获取本页的最后一条记录在总记录数中的索引 第一条的索引是1，而不是0
	 */
	public int getEnd()
	{
		return _end;
	}

	/**
	 * 返回当前页码
	 * 
	 * @return
	 */
	public int getCurrentPage()
	{
		return _currentPage;
	}

	/**
	 * 获取分页栏的HTML代码，getFooterBar()产生一个表格，
	 * 将getFooterBar1()和getFooterBar2()的信息包括在一起。
	 * 
	 * @return
	 */
	public String getFooterBar()
	{
		String HTML = "";

		HTML += "<table border=\"0\" width=\"100%\" cellspacing=\"0\">";
		HTML += "<tr>";
		HTML += "<td align=\"left\" valign=\"bottom\">" + getFooterBar1()
				+ "</td>";
		HTML += "<td align=\"right\" valign=\"bottom\">" + getFooterBar2()
				+ "</td>";
		HTML += "</tr>";
		HTML += "</table>";

		return HTML;
	}

	/**
	 * 返回页次信息代码
	 * 
	 * @return
	 */
	public String getFooterBar1()
	{
		return "页次: " + _currentPage + "/" + _pageCount + "页&nbsp;记录总数"
				+ _recordCount;
	}

	/**
	 * 返回页码跳转链接
	 * 
	 * @return
	 */
	public String getFooterBar2()
	{
		String HTML = getPageLinks() + "&nbsp;" + getGoToInput();
		return HTML;
	}

	public String getGoToInput()
	{
		String page = "this.nextSibling.value";
		String onClick = _url.getPageOnClick("" + page);
		return String
				.format(
						"<input type=\"button\" value=\"GO>\" onclick=\"%s\"><input size=\"2\" value=\"%d\" onmouseover=\"this.select();\">",
						(onClick == null ? "javascript:location.href=\'"
								+ getUrl("'+" + page + "+'") + "';" : onClick),
						_currentPage);
	}

	private String getUrl(String page)
	{
		return _url.getPageUrl(page);
	}

	/**
	 * 获取页面总数
	 */
	public int getPageCount()
	{
		return _pageCount;
	}

	private String getPageHTML(int page, String text, String title)
	{
		String HTML = "";
		if (page < 0 || page > _pageCount)
		{
			HTML = " <font color=\"#888888\">" + text + "</font>";
		}
		else
		{
			if (page != _currentPage)
			{
				String onClick = _url.getPageOnClick("" + page);
				if (onClick == null)
				{
					String url = getUrl("" + page);

					HTML = String
							.format(
									" <a class=\"list\" style=\"font-size:9pt;line-height:1;\" href=\"%s\" title=\"%s\">%s</a>",
									url, title, text);
				}
				else
				{
					HTML = String
							.format(
									" <a class=\"list\" style=\"font-size:9pt;line-height:1;\" onclick=\"%s\" href=\"#\" title=\"%s\">%s</a>",
									onClick, title, text);

				}
			}
			else
				HTML = " <font color=\"#FF0000\">" + text+"</font>";
		}

		return HTML;
	}

	/**
	 * 返回所有页面的索引及链接
	 * 
	 * @return
	 */
	public String getPageLinks()
	{
		String html = "";

		html += getPageHTML(1, "<span  style=\"border: 1.0pt solid ;font-size:9pt;line-height:1;cursor:pointer;height:16px;width:30px;padding:2px;vertical-align:middle;\">首页</span>", "首页");
		html += getPageHTML(((_currentPage - 1) / 10 - 1) * 10 + 1,
				"<span  style=\"border: 1.0pt solid ;font-size:9pt;line-height:1;cursor:pointer;height:16px;width:43px;padding:2px;vertical-align:middle;\">前10页</span>", "前10页");

		html += "&nbsp;[";

		for (int i = ((_currentPage - 1) / 10) * 10 + 1; i <= ((_currentPage - 1) / 10) * 10 + 10; i++)
		{
			html += getPageHTML(i, "<span  style=\"border: 1.0pt solid ;font-size:9pt;line-height:1;height:16px;width:16px;padding:2px 4px;cursor:pointer;\">"+i+"</span>", "第" + i + "页");
		}

		html += "&nbsp;]";

		html += getPageHTML(((_currentPage - 1) / 10 + 1) * 10 + 1,
				"<span  style=\"border: 1.0pt solid ;font-size:9pt;line-height:1;cursor:pointer;height:16px;width:43px;padding:2px;vertical-align:middle;\">后10页</span>", "后10页");
		html += getPageHTML(_pageCount, "<span  style=\"border: 1.0pt solid ;font-size:9pt;line-height:1;padding:2px;cursor:pointer;height:16px;width:43px;padding:2px;vertical-align:middle;\">最后页</span>",
				"最后页");

		return html;
	}

	public int getRecordCount()
	{
		return _recordCount;
	}
}

class SimplePageUrlGetter implements PageUrlBuilder
{
	private String _keyword = "page";

	private HttpServletRequest _request;

	private String _url;

	public SimplePageUrlGetter(HttpServletRequest request)
	{
		_request = request;
	}

	public SimplePageUrlGetter(HttpServletRequest request, String keyword)
	{
		_request = request;
		_keyword = keyword;
	}

	public String getPageUrl(String page)
	{
		String url = getUrl();
		return url + (url.indexOf("?") >= 0 ? "&" : "?") + _keyword + "="
				+ page;
	}

	/**
	 * @return
	 */
	private String getUrl()
	{
		return _url == null ? JspUtils.getFullURI(_request, JspUtils
				.getFullQueryString(_request, _keyword)) : _url;
	}

	public String getPageOnClick(String page)
	{
		return null;
	}
}