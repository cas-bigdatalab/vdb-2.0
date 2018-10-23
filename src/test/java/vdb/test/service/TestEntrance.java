package vdb.test.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Web服务单元测试入口<br>
 * 各个单元测试亦可单独运行<br>
 * 说明：执行Web服务单元测试前，务必查看《WEB服务单元测试文档》
 * 
 * @author 苏贤明
 */

@RunWith(Suite.class)
@Suite.SuiteClasses( { DaiEntityQueryTest.class, DaiGetFileTest.class,
		DaiGetRecordTest.class, DaiQueryTest.class, DefaultTest.class,
		DhGetCatalogTest.class, DhGetDatabaseListTest.class,
		DhGetDatabaseSchemaTest.class, DhGetIndexesTest.class,
		DhGetIndexModificationsTest.class, SysDatabaseStatisticTest.class,
		SysGetEntityListTest.class, SysStatusTest.class, UiGetRecordTest.class,
		UiQueryTest.class, UnknownVerbTest.class })
public class TestEntrance
{

}
