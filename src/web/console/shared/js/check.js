//assertXXX: by bluejoe

function assertNotEmpty(element, title)
{
	if(element.value == "")
	{
		alert("『" + title + "』内容不能为空！");
		element.focus();
		element.select();
		return false;
	}
	return true;
}

function assertEquals(e1, t1, e2, t2)
{
	if(e1.value != e2.value)
	{
		alert("『" + t1 + "』" + "与『" + t2 + "』内容不一致！");
		e2.focus();
		return false;
	}
	return true;
}

function assertMail(element, title)
{
	if(element.value.indexOf(".") == -1 ||
		element.value.indexOf("@") == -1 ||
		element.value.length < 6)
	{
		alert("『" + title + "』内容必须为合法的邮件地址！");
		element.focus();
		return false;
	}
	return true;	
}

function assertNumeric(elementObject, elementTitle)
{
	if(isNaN(elementObject.value))
	{
		alert("『" + elementTitle + "』必须为数值类型！");
		elementObject.focus();
		elementObject.select();
		return false;
	}
	return true;	
}

function assertTextLength1(elementObject, elementTitle, len)
{
	if(elementObject.value.length < len)
	{
		alert("『" + elementTitle + "』的输入不正确，不能少于" + len + "个字符！");
		try
		{
			elementObject.focus();
		}
		catch(e)
		{
		}
		return false;
	}
	return true;
}

function assertTextLength2(elementObject, elementTitle, len)
{
	if(elementObject.value.length > len)
	{
		alert("『" + elementTitle + "』的输入不正确，不能多于" + len + "个字符！");
		try
		{
			elementObject.focus();
		}
		catch(e)
		{
		}
		return false;
	}
	return true;
}

function assertTextLength(elementObject, elementTitle, len1, len2)
{
	return assertTextLength1(elementObject, elementTitle, len1) &&
		assertTextLength2(elementObject, elementTitle, len2);
}

function assertNumericRange1(elementObject, elementTitle, len)
{
	if(elementObject.value < len)
	{
		alert("『" + elementTitle + "』的输入不正确，数值不能小于" + len + "！");
		try
		{
			elementObject.focus();
		}
		catch(e)
		{
		}
		return false;
	}
	return true;
}

function assertNumericRange2(elementObject, elementTitle, len)
{
	if(elementObject.value > len)
	{
		alert("『" + elementTitle + "』的输入不正确，数值不能大于" + len + "！");
		try
		{
			elementObject.focus();
		}
		catch(e)
		{
		}
		return false;
	}
	return true;
}

function assertNumericRange(elementObject, elementTitle, len1, len2)
{
	return assertNumericRange1(elementObject, elementTitle, len1) &&
		assertNumericRange2(elementObject, elementTitle, len2);
}

function assertTextMatches(elementObject, elementTitle, re)
{
	if (! new RegExp(re, "gi").test(elementObject.value))
	{
		alert("『" + elementTitle + "』的输入格式不正确！");
		try
		{
			elementObject.focus();
		}
		catch(e)
		{
		}
		return false;
	}
	return true;
}

function getFileTitleFromFilePath(fileName)
{
	li1 = fileName.lastIndexOf('.');
	if(li1 < 0)
		li1 = fileName.length;
	
	li2 = fileName.lastIndexOf('\\') + 1;
	li3 = fileName.lastIndexOf('/') + 1;
	
	li4 = (li2 < li3 ? li3 : li2);
	
	return fileName.substring(li4, li1);
}

function assertUploadType(e)
{
	fileName = e.value;
	fileName = fileName.toLowerCase();
	
	var invalidExtensions = new Array(".jsp", ".asp", ".aspx", ".htm", ".xml", ".xsl", ".pif");
	for (i = 0; i < invalidExtensions.length; i++)
	{
		if (fileName.lastIndexOf(invalidExtensions[i]) != -1)
		{
			alert("对不起！该类型的文件不能上传。");
			return false;
		}
	}
	
	return true;
}

function Trim(str)
{
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

String.prototype.LTrim = function()
{
	return this.replace(/(^\s*)/g, "");
}

String.prototype.RTrim = function()
{
	return this.replace(/(\s*$)/g, "");
} 