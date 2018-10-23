package vdb.metacat.fs.io;

import java.io.File;

import org.jdom.Element;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.metacat.Identifier;
import vdb.metacat.Query;
import vdb.metacat.Relation;
import vdb.metacat.RelationKey;
import vdb.metacat.Repository;
import vdb.metacat.View;
import vdb.metacat.idgen.IdGenerator;
import cn.csdb.commons.util.FileUtils;

public class DataSetWriter
{
	private File _schemaDir;

	public DataSetWriter(File schemaDir)
	{
		_schemaDir = schemaDir;
	}

	public void delete()
	{
		FileUtils.rmdirs(_schemaDir);
	}

	public void write(DataSet dataSet) throws Exception
	{
		Element root = new Element("dataset");

		Element ve = new Element("version");
		root.addContent(0, ve.setText("1.2.1"));

		ElementWriter dw = new ElementWriter(root);

		// attributes
		dw.writeAttributes(dataSet.meta());

		// repository
		Repository repository = dataSet.getRepository();
		if (repository != null)
		{
			new ElementWriter(dw.appendChild("repository"))
					.writeAttributes(repository.meta());
		}

		// entities
		for (Entity entity : dataSet.getEntities())
		{
			Element en = dw.appendChild("entity");
			ElementWriter ew = new ElementWriter(en);
			ew.writeAttributes(entity.meta(), "titleField", "editorField",
					"timeStampField", "groupField", "orderField");

			Field titleField = entity.getTitleField();
			if (titleField != null)
			{
				ew.writeAttribute("titleField", titleField.getUri());
			}

			Field editorField = entity.getEditorField();
			if (editorField != null)
			{
				ew.writeAttribute("editorField", editorField.getUri());
			}

			Field orderField = entity.getOrderField();
			if (orderField != null)
			{
				ew.writeAttribute("orderField", orderField.getUri());
			}

			Field groupField = entity.getGroupField();
			if (groupField != null)
			{
				ew.writeAttribute("groupField", groupField.getUri());
			}

			Field timeStampField = entity.getTimeStampField();
			if (timeStampField != null)
			{
				ew.writeAttribute("timeStampField", timeStampField.getUri());
			}

			// identifier
			Identifier identifier = entity.getIdentifier();
			if (identifier != null)
			{
				ElementWriter iw = new ElementWriter(ew
						.appendChild("identifier"));
				iw.writeAttributes(identifier.meta(), "field", "idGenerator");
				// field
				Field field = identifier.getField();
				if (field != null)
				{
					iw.writeAttribute("field", field.getUri());
				}
				IdGenerator idGenerator = identifier.getIdGenerator();
				if (idGenerator != null)
				{
					iw.writeAttribute("idGenerator", idGenerator.getName());
				}
			}

			// fields
			for (Field field : entity.getFields())
			{
				ElementWriter fw = new ElementWriter(ew.appendChild("field"));
				fw.writeAttributes(field.meta(), "relationKey", "type");
				fw.writeAttribute("type", field.getTypeName());

				RelationKey relationKey = field.getRelationKey();
				if (relationKey != null)
				{
					fw.writeAttribute("relationKey", relationKey.getId());
				}
			}
		}

		// queries
		for (Query query : dataSet.getQueries())
		{
			new ElementWriter(dw.appendChild("query")).writeAttributes(query
					.meta());
		}

		// relations
		for (Relation relation : dataSet.getRelations())
		{
			ElementWriter rw = new ElementWriter(dw.appendChild("relation"));
			rw.writeAttributes(relation.meta(), "cardinality");
			rw.writeAttribute("cardinality", relation.getCardinality()
					.getName());

			// keys
			RelationKey[] keys = { relation.getKeyA(), relation.getKeyB() };
			for (RelationKey key : keys)
			{
				ElementWriter kw = new ElementWriter(rw.appendChild("key"));
				kw.writeAttributes(key.meta(), "target");
				kw.writeAttribute("target", key.getTarget().getUri());
			}
		}

		// views
		for (View view : dataSet.getViews())
		{
			new ViewWriter(root).write(view);
		}

		new XmlWriter(new File(_schemaDir, "schema.xml")).write(root);
	}
}
