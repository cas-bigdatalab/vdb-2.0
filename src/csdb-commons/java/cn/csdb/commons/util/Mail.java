/*
 * Created on 2003-5-16
 * 
 */
package cn.csdb.commons.util;

import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * @author bluejoe
 * 
 * 该类用于实现邮件的发送。
 * 
 */

public class Mail
{
	// 定义收件人、发送人、主题等
	private String description = "Content-Disposition";

	private String charset = "gb2312";

	private MimeMessage mm;

	private MimeBodyPart bodyPart = new MimeBodyPart();

	private Date sendDate;

	private List files = new Vector();

	/**
	 * @param smtpServer
	 *            SMTP服务器地址
	 */
	public Mail(String smtpServer)
	{
		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpServer);

		Session session = Session.getDefaultInstance(props);
		mm = new MimeMessage(session);
	}

	/**
	 * @param smtpServer
	 * @param account
	 *            SMTP认证帐号
	 * @param password
	 *            SMTP认证密码
	 */
	public Mail(String smtpServer, String account, String password)
	{
		Properties props = java.lang.System.getProperties();
		props.put("mail.smtp.host", smtpServer);
		props.put("mail.smtp.auth", "true");

		Authenticator sa = new SmtpAuthenticator(account, password);
		Session session = Session.getInstance(props, sa);
		mm = new MimeMessage(session);
	}

	/**
	 * 设置发件人
	 * 
	 * @param from
	 * @throws Exception
	 */
	public void setFrom(String from) throws Exception
	{
		mm.setFrom(new InternetAddress(from));
	}

	/**
	 * 设置收件人
	 * 
	 * @param to
	 * @throws Exception
	 */
	public void setRecipient(String to) throws Exception
	{
		InternetAddress[] address = { new InternetAddress(to) };
		mm.setRecipients(Message.RecipientType.TO, address);
	}

	/**
	 * 追加收件人
	 * 
	 * @param to
	 * @throws Exception
	 */
	public void addRecipient(String to) throws Exception
	{
		InternetAddress address = new InternetAddress(to);
		mm.addRecipient(Message.RecipientType.TO, address);
	}

	/**
	 * 设置发送时间
	 * 
	 * @param sendDate
	 * @throws Exception
	 */
	public void setSendDate(Date sendDate) throws Exception
	{
		this.sendDate = sendDate;
	}

	/**
	 * 设置主题
	 * 
	 * @param subject
	 * @throws Exception
	 */
	public void setSubject(String subject) throws Exception
	{
		mm.setSubject(subject);
	}

	/**
	 * 追加文件
	 * 
	 * @param filename
	 */
	public void addFile(String filename)
	{
		files.add(filename);
	}

	/**
	 * 设置正文内容
	 * 
	 * @param body
	 * @throws MessagingException
	 */
	public void setBody(String body) throws MessagingException
	{
		bodyPart.setText(body);
	}

	public void setBodyContent(String body, String contentType)
			throws MessagingException
	{
		bodyPart.setContent(body, contentType);
	}

	public void setBody(String body, String charset) throws MessagingException
	{
		bodyPart.setText(body, charset);
		this.charset = charset;
	}

	/**
	 * 发送邮件，需要先设置好邮件属性
	 * 
	 * @throws MessagingException
	 */
	public void send() throws MessagingException
	{
		if (sendDate == null)
			sendDate = new Date();

		// 把message part加入新创建的Multipart
		Multipart mp = new MimeMultipart();

		// 邮件内容的第一部分
		mp.addBodyPart(bodyPart);

		// 邮件内容的第二部分
		for (int i = 0; i < files.size(); i++)
		{
			MimeBodyPart mbp = new MimeBodyPart();
			String filename = (String) files.get(i);

			FileDataSource fds = new FileDataSource(filename);
			mbp.setDataHandler(new DataHandler(fds));
			mbp.setFileName(fds.getName());
			mbp.setDescription(description, charset);

			mp.addBodyPart(mbp);
		}

		// 把MultiPart加入邮件
		mm.setContent(mp);
		mm.setSentDate(sendDate);
		Transport.send(mm);
	}

	/**
	 * 发送简单邮件
	 * 
	 * @param to
	 * @param from
	 * @param subject
	 * @param body
	 * @throws Exception
	 */
	public void send(String to, String from, String subject, String body)
			throws Exception
	{
		setRecipient(to);
		setFrom(from);
		setSubject(subject);
		setBody(body);

		send();
	}

	public void setCharset(String charset)
	{
		this.charset = charset;
	}
}

class SmtpAuthenticator extends Authenticator
{
	private String account;

	private String password;

	/**
	 * @param account
	 * @param password
	 */
	public SmtpAuthenticator(String account, String password)
	{
		this.account = account;
		this.password = password;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.mail.Authenticator#getPasswordAuthentication()
	 */
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication(account, password);
	}

}
