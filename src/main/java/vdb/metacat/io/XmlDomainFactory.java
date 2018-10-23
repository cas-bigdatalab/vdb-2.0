package vdb.metacat.io;

import java.io.File;

import vdb.metacat.Domain;
import vdb.metacat.fs.io.DomainReader121;

public class XmlDomainFactory implements DomainFactory
{
	File _domainDir;
	
	SchemaFactory _schemaFactory;

	public Domain getDomain() throws Exception
	{
		DomainReader121 domainReader = new DomainReader121(_domainDir);
		return domainReader.getDomain();
	}

	public void setDomainDir(File domainDir)
	{
		_domainDir = domainDir;
	}

	public void setSchemaFactory(SchemaFactory schemaFactory)
	{
		_schemaFactory = schemaFactory;
	}
}
