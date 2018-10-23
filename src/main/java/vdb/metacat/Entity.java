package vdb.metacat;

public interface Entity extends CatalogObject
{
	void addCollectionKey(RelationKey key);

	void addField(Field field);

	void addReferenceKey(RelationKey key);

	RelationKey[] getCollectionKeys();

	DataSet getDataSet();

	String getDescription();

	String getNeedAudit();

	String getOrder();

	String getViewAfterAudit();

	public Field getEditorField();

	public Field getGroupField();

	public Field getOrderField();

	Field getField(String name);

	Field[] getFields();

	Identifier getIdentifier();

	String getName();

	RelationKey getReferenceKey(String columnName);

	RelationKey[] getReferenceKeys();

	String getTableName();

	public Field getTimeStampField();

	public String getTitle();

	public Field getTitleField();

	void removeField(Field field);

	void setDataSet(DataSet dataSet);

	void setEditorField(Field field);

	public void setGroupField(Field field);

	void setName(String name);

	void setTableName(String tableName);

	void setTimeStampField(Field field);

	void setOrderField(Field field);

	public void setTitle(String title);

	void setTitleField(Field field);

	void setIdentifier(Identifier identifier);
}
