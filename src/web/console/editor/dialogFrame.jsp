<%@page contentType="text/html; charset=UTF-8" %>
<HTML>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=request.getParameter("title")%></title>
</head>
<frameset rows="0,*" frameborder="0" border="0" framespacing="0">
	<frame src="">
	<frame name=mainFrame src="<%=request.getParameter("url")%>">
</frameset>
</HTML>