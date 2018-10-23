package vdb.mydb.filestat.impl;

import java.util.ArrayList;
import java.util.List;

import vdb.mydb.repo.FileRepository;

public class RepositoryManager
{

	List<FileRepository> repositories = new ArrayList<FileRepository>();

	public List<FileRepository> getRepositories()
	{
		return repositories;
	}

	public void setRepositories(List<FileRepository> repositories)
	{
		this.repositories = repositories;
	}

	//FIXME: addRepository
	public void addRepository(FileRepository repository)
	{
		this.repositories.add(repository);
	}
}
