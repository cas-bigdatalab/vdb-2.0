package vdb.metacat.fs.page;

import vdb.metacat.Entity;

/**
 * 页面类型接口
 * 
 * @author gzb
 * 
 */
public interface Page
{

	public Entity getEntity();// 获得实体

	public String getName();// 获得页面的名字(唯一标识)

	public String getType();// 获得页面类型

	public boolean isDefault();// 获得页面的是否为默认页面
}
