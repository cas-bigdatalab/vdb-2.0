package cn.csdb.commons.util;

import java.util.regex.Matcher;

public interface TokenVisitor
{
	void visitToken(Matcher matcher);

	void finish(Matcher matcher);
}
