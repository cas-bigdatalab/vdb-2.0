package vdb.metacat;

public enum Cardinality
{
	ManyToMany("m:n"), ManyToOne("m:1"), OneToMany("1:n");

	public static Cardinality forName(String name)
	{
		if (ManyToMany.equals(name))
			return ManyToMany;

		if (ManyToOne.equals(name))
			return ManyToOne;

		if (OneToMany.equals(name))
			return OneToMany;

		return null;
	}

	private String _name;

	Cardinality(String name)
	{
		_name = name;
	}

	public boolean equals(String name)
	{
		return _name.equalsIgnoreCase(name);
	}

	public String getName()
	{
		return _name;
	}

	public Cardinality inverse()
	{
		if (ManyToMany == this)
			return ManyToMany;

		if (ManyToOne == this)
			return OneToMany;

		if (OneToMany == this)
			return ManyToOne;

		return null;
	}

	public boolean isManyToMany()
	{
		return this == ManyToMany;
	}

	public boolean isManyToOne()
	{
		return this == ManyToOne;
	}

	public boolean isOneToMany()
	{
		return this == OneToMany;
	}

}