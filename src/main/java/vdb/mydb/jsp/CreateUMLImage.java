package vdb.mydb.jsp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import vdb.metacat.DataSet;
import vdb.metacat.Entity;
import vdb.metacat.Field;
import vdb.mydb.VdbManager;
import vdb.mydb.types.TypeManager;
import cn.csdb.commons.util.CollectionUtils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CreateUMLImage
{
	public class EntityPosition
	{
		Entity entity;

		int height;

		int i;

		int width;

		int x;

		int y;

		public EntityPosition(int i, Entity entity, int x, int y, int height,
				int width)
		{
			this.i = i;
			this.entity = entity;
			this.x = x;
			this.y = y;
			this.height = height;
			this.width = width;
		}
	}

	int charWidth = 10;

	int ci = 0;

	int cLength = 8;

	Color[] color = { Color.red, Color.black, Color.blue, Color.green,
			Color.cyan, new Color(51, 153, 204), new Color(51, 102, 153),
			new Color(0, 102, 51) };

	int cWidth = 80;

	List<EntityPosition> epList = new ArrayList<EntityPosition>();

	Font fontContent = new Font("courier", Font.PLAIN, 14);

	Font fontTitle = new Font("courier", Font.BOLD, 16);

	Graphics2D g2;

	int height;

	boolean isC = true;

	int left = 80;

	int left2 = 400;

	int lh = 4;

	int lineHeight = 20;

	int ll;

	int lr;

	int lw = 8;

	int right = 80;

	int tHeight = 40;

	int top = 40;

	int tWidth = 200;

	int width = 700;

	public void drawLine(int i, int j, int p, int method)
	{
		EntityPosition from = epList.get(i);
		EntityPosition to = epList.get(p);

		g2.setColor(color[ci]);
		ci++;
		if (ci == cLength)
		{
			ci = 0;
		}
		// g2.setStroke(new BasicStroke(1.0f));

		if (i % 2 == p % 2)
		{
			int x1 = from.x;
			int y1 = from.y + tHeight + lineHeight + lineHeight * j - 5;
			int x2 = x1;
			int y2 = to.y + lineHeight;

			if (i % 2 == 0)
			{
				g2.drawLine(x1 - 2 * lw, y1, x1 - ll, y1);
				g2.drawLine(x1 - ll, y1, x1 - ll, y2);
				g2.drawLine(x1 - ll, y2, x2, y2);

				int[] xs = { x1, x1 - lw, x1 - 2 * lw, x1 - lw };
				int[] ys = { y1, y1 - lh, y1, y1 + lh };
				switch (method)
				{
					case 1:
						g2.fillPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 - 1.5 * lw), y1 - lh);
						break;
					case 2:
						g2.drawPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 - 1.5 * lw), y1 - lh);
						break;
					case 3:
					{
						g2.drawPolygon(xs, ys, 4);
					}
				}

				g2.drawLine(x2 - lw, y2 - lh, x2, y2);
				g2.drawLine(x2 - lw, y2 + lh, x2, y2);

				ll += 10;
			}
			else
			{
				x1 += from.width;
				x2 += to.width;
				g2.drawLine(x1 + 2 * lw, y1, x1 + lw / 3 + lr, y1);
				g2.drawLine(x1 + lw / 3 + lr, y1, x1 + lw / 3 + lr, y2);
				g2.drawLine(x1 + lw / 3 + lr, y2, x2, y2);

				int[] xs = { x1, x1 + lw, x1 + 2 * lw, x1 + lw };
				int[] ys = { y1, y1 - lh, y1, y1 + lh };
				switch (method)
				{
					case 1:
						g2.fillPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 + 0.5 * lw), y1 - lh);
						break;
					case 2:
						g2.drawPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 + 0.5 * lw), y1 - lh);
						break;
					case 3:
					{
						g2.drawPolygon(xs, ys, 4);
					}
				}

				g2.drawLine(x2 + lw, y2 - lh, x2, y2);
				g2.drawLine(x2 + lw, y2 + lh, x2, y2);

				lr += 10;
			}
		}
		else
		{
			int x1 = from.x;
			if (i % 2 == 0)
			{
				x1 += from.width + 3 * lw;
			}
			else
			{
				x1 -= 3 * lw;
			}

			int y1 = from.y + tHeight + lineHeight + lineHeight * j - 5;

			int x2 = to.x;
			if (p % 2 == 0)
			{
				x2 += to.width;
				x2 += 3 * lw;
			}
			else
			{
				x2 -= 3 * lw;
			}

			int y2 = to.y + lineHeight;

			g2.drawLine(x1, y1, x2, y2);

			if (i % 2 == 0)
			{
				g2.drawLine(x1, y1, x1 - lw, y1);
				int[] xs = { x1 - lw, x1 - 2 * lw, x1 - 3 * lw, x1 - 2 * lw };
				int[] ys = { y1, y1 - lh, y1, y1 + lh };
				switch (method)
				{
					case 1:
						g2.fillPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 - 2.5 * lw), y1 - lh);
						break;
					case 2:
						g2.drawPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 - 2.5 * lw), y1 - lh);
						break;
					case 3:
					{
						g2.drawPolygon(xs, ys, 4);
					}
				}

				g2.drawLine(x2, y2, x2 + 3 * lw, y2);
				g2.drawLine(x2 + 2 * lw, y2 - lh, x2 + 3 * lw, y2);
				g2.drawLine(x2 + 2 * lw, y2 + lh, x2 + 3 * lw, y2);
			}
			else
			{
				g2.drawLine(x1, y1, x1 + lw, y1);
				int[] xs = { x1 + lw, x1 + 2 * lw, x1 + 3 * lw, x1 + 2 * lw };
				int[] ys = { y1, y1 - lh, y1, y1 + lh };
				switch (method)
				{
					case 1:
						g2.fillPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 + 1.5 * lw), y1 - lh);
						break;
					case 2:
						g2.drawPolygon(xs, ys, 4);
						g2.drawString("+", (int) (x1 + 1.5 * lw), y1 - lh);
						break;
					case 3:
					{
						g2.drawPolygon(xs, ys, 4);
					}
				}

				g2.drawLine(x2, y2, x2 - 3 * lw, y2);
				g2.drawLine(x2 - 2 * lw, y2 - lh, x2 - 3 * lw, y2);
				g2.drawLine(x2 - 2 * lw, y2 + lh, x2 - 3 * lw, y2);
			}
		}
	}

	public void drawShadow(int x, int y, int width, int height, int size)
	{
		g2.setColor(new Color(150, 150, 150));
		g2.fillRect(x + width + 1, y + size, size, height);
		g2.fillRect(x + size, y + height + 1, width, size);
	}

	public void drawSingleEntity(int i)
	{
		EntityPosition ep = epList.get(i);

		if (!isC)
		{
			ep.width = 20 + this.getStrWidth(ep.entity.getUri(), fontTitle);
		}
		else
		{
			ep.width = tWidth;
		}

		g2.setColor(Color.black);
		g2.drawRect(ep.x, ep.y, ep.width, tHeight);
		if (isC)
		{
			drawString(ep.entity.getTitle(), ep.x, ep.y, ep.width);
		}
		else
		{
			drawString(ep.entity.getUri(), ep.x, ep.y, ep.width);
		}

		g2.setColor(Color.black);
		g2.setFont(fontContent);
		g2.drawRect(ep.x, ep.y + tHeight, ep.width, ep.height - tHeight);
		Field[] fieldList = epList.get(i).entity.getFields();
		for (int j = 0; j < fieldList.length; j++)
		{
			Field f = fieldList[j];
			String str = "";
			if (isC)
			{
				try
				{
					str = f.getTitle() + " : "
							+ this.getTypeTitle(f.getTypeName());
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
			else
			{
				str = f.getName() + " : " + f.getTypeName();
			}

			g2.drawString(str, ep.x + 20, ep.y + tHeight + 20 + lineHeight * j);
			if (f.isCollection() || f.isReference())
			{
				g2.setColor(Color.black);

				g2.drawLine(ep.x + 20, ep.y + tHeight + 20 + lineHeight * j,
						ep.x + 20 + getStrWidth(str, fontContent), ep.y
								+ tHeight + 20 + lineHeight * j);
			}
		}

		drawShadow(ep.x, ep.y, ep.width, ep.height, 10);
	}

	public void drawString(String str, int x, int y, int width)
	{
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = fontTitle.getStringBounds(str, context);
		int boHeight = (int) bounds.getHeight();
		double fX = x + (width - bounds.getWidth()) / 2;
		double fY = (tHeight + boHeight) / 2 + y;
		g2.setColor(Color.BLACK);
		g2.setFont(fontTitle);
		g2.drawString(str, (int) fX, (int) fY);
	}

	public void getDataSetUML(DataSet ds, String path, String language)
			throws Exception
	{
		ll = 20;
		lr = 20;

		List<Entity> entityList = new ArrayList<Entity>();
		CollectionUtils.copy(ds.getEntities(), entityList);

		if (language.equals("e"))
		{
			isC = false;
		}
		else
		{
			isC = true;
		}

		setSize(entityList);

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		g2 = (Graphics2D) bi.getGraphics();
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, width, height);

		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);

		for (int i = 0; i < entityList.size(); i++)
		{
			drawSingleEntity(i);
		}

		for (int i = 0; i < entityList.size(); i++)
		{
			Entity entity = entityList.get(i);

			Field[] fList = entity.getFields();
			for (int j = 0; j < fList.length; j++)
			{
				Field f = fList[j];
				if (f.isCollection() || f.isReference())
				{
					if (f.getRelationKey() == null)
						continue;

					Entity rEntity = f.getRelationKey().getTarget();
					int rPosition = 0;
					for (int p = 0; p < epList.size(); p++)
					{
						EntityPosition ep = epList.get(p);
						if (ep.entity.equals(rEntity))
						{
							rPosition = ep.i;
							break;
						}
					}

					int method = 0;
					if (f.isStrongCollection())
						method = 1;
					else if (f.isWeakCollection())
						method = 2;
					else
						method = 3;

					drawLine(i, j, rPosition, method);
				}
			}
		}

		g2.dispose();

		writeFile(path, bi);
	}

	public void getEntityUML(Entity entity, String path, String laguage)
			throws Exception
	{
		List<Field> fieldList = new ArrayList<Field>();
		CollectionUtils.copy(entity.getFields(), fieldList);

		boolean isC = true;
		if (laguage.equals("c"))
		{
			isC = true;
		}
		else
			isC = false;

		String tableName = entity.getUri();
		if (isC)
		{
			tableName = entity.getTitle();
		}

		top = 40;
		int bTop = tHeight + top;
		int bHeight = lineHeight + fieldList.size() * lineHeight;
		if (!isC)
		{
			width = 20 + charWidth * tableName.length() + 100;
		}
		else
		{
			width = tWidth + 100;
		}

		height = 40 + bTop + bHeight;

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		g2 = (Graphics2D) bi.getGraphics();
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, width, height);

		if (!isC)
		{
			tWidth = 20 + this.getStrWidth(tableName, fontTitle);
		}
		else
		{
			tWidth = 200;
		}

		g2.setColor(Color.black);
		g2.draw3DRect((width - tWidth) / 2, top, tWidth, tHeight, true);
		drawString(tableName, (width - tWidth) / 2, top, tWidth);
		g2.setFont(fontContent);
		g2.setColor(Color.black);
		g2.draw3DRect((width - tWidth) / 2, bTop, tWidth, bHeight, true);
		for (int i = 0; i < fieldList.size(); i++)
		{
			Field field = fieldList.get(i);
			String name = field.getName();
			if (isC)
				name = field.getTitle();
			String dStr = name + " : " + field.getTypeName();
			if (isC)
			{
				dStr = name + "：" + this.getTypeTitle(field.getTypeName());
			}

			int fX = 20 + (width - tWidth) / 2;
			int fY = bTop + 20 + lineHeight * i;
			g2.setColor(Color.BLACK);
			g2.drawString(dStr, fX, fY);

			if (field.isCollection() || field.isReference())
			{
				g2.setColor(Color.black);

				g2.drawLine(fX, fY, fX + getStrWidth(dStr, fontContent), fY);
			}
		}

		drawShadow((width - tWidth) / 2, top, tWidth, tHeight + bHeight, 10);

		g2.dispose();
		writeFile(path, bi);
	}

	public int getMaxEWidth(String type, List<Entity> entityList)
	{
		int yu = 0;
		if (type.equals("right"))
			yu = 1;

		int maxWidth = tWidth;
		for (int i = yu; i < entityList.size(); i += 2)
		{
			Entity e = entityList.get(i);
			int width = tWidth;
			if (!isC)
			{
				width = 20 + charWidth * e.getUri().length();
			}

			maxWidth = (maxWidth >= width) ? maxWidth : width;
		}

		return maxWidth;
	}

	public int getStrWidth(String str, Font font)
	{
		FontRenderContext context = g2.getFontRenderContext();
		Rectangle2D bounds = font.getStringBounds(str, context);
		return (int) bounds.getWidth();
	}

	public String getTypeTitle(String name)
	{
		TypeManager manager = VdbManager.getEngine().getTypeManager();
		return manager.getFieldType(name).getTitle();
	}

	public void setSize(List<Entity> entityList)
	{
		height = top;
		int eWidth = tWidth;

		if (!isC)
		{
			width = left + this.getMaxEWidth("left", entityList) + cWidth
					+ this.getMaxEWidth("right", entityList) + right;
			left2 = left + this.getMaxEWidth("left", entityList) + cWidth - 20;
		}
		else
		{
			width = left + tWidth + right + tWidth + cWidth;
			left2 = left + tWidth + cWidth;
		}

		int maxEHeight = 0;
		int i = 0;
		epList.clear();
		for (; i < entityList.size(); i++)
		{
			Entity entity = entityList.get(i);

			if (!isC)
			{
				eWidth = 20 + charWidth * entity.getUri().length();
			}
			else
			{
				eWidth = tWidth;
			}

			int x = left;
			if (i % 2 != 0)
				x = left2;
			int eHeight = tHeight + entity.getFields().length * lineHeight
					+ lineHeight;
			int y = height;
			if (i % 2 != 0)
			{
				height += 40;
				if (eHeight > maxEHeight)
					height += eHeight;
				else
					height += maxEHeight;
				maxEHeight = 0;
			}
			else
			{
				maxEHeight = eHeight;
			}

			epList.add(new EntityPosition(i, entity, x, y, eHeight, eWidth));
		}

		if (i % 2 != 0)
			height += epList.get(epList.size() - 1).height;

		height += 60;
	}

	public void writeFile(String path, BufferedImage bi) throws Exception
	{
		FileOutputStream out = new FileOutputStream(path);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
		param.setQuality(1f, true);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(bi);
		out.close();
	}

}
