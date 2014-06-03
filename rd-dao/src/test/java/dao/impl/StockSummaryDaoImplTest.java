/**
 * 
 */
package dao.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import junit.framework.TestCase;
import org.mockito.MockitoAnnotations;

/**
 * @author finaxys
 *
 */
public class StockSummaryDaoImplTest extends TestCase 
{

//	private  StockSummaryDaoImpl exDao;
//	
//	/** 
//	 * @throws 
//	 * @Before
//	 */
//	@Before
//	public void setUp() throws Exception 
//	{
//		MockitoAnnotations.initMocks(this);
//		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
//		exDao = new StockSummaryDaoImpl(hConnection);
//	}
//	
//	/**
//	 * Test method for {@link dao.impl.StockSummaryDaoImpl#add(domain.StockSummary)}.
//	 */
//	
//	public final void testNormalAdd() throws IOException
//	{
//		StockSummary e1 = new StockSummary("StockSymbol", "StockExchSymbol", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		
//		System.out.println("Unit test for Add :");
//		
//		assertTrue(exDao.add(e1));
//	}
//	
//	@Test
//	public final void testEmptyValuesAdd() throws IOException
//	{
//		StockSummary e1 = new StockSummary("", "", 0, "", new Date(), new Date(), "", "", 0);
//		System.out.println("Add StockSummary with only provider = 0, Date = AsOfDay and all strings parameters "
//				+ "are empty strings --> exDao.add(StockSummary) == True ?");
//		assertTrue(exDao.add(e1));
//	}
//	
//	/*@Test
//	public final void testExistingValueAdd() throws IOException
//	{
//		StockSummary e1 = new StockSummary("StockSymbol", "StockExchSymbol", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		boolean result = exDao.add(e1);
//		
//		System.out.println("Try to add an StockSummary already in the table --> exDao.add(StockSummary) == False ?");
//		assertFalse(result);
//		
//		System.out.println("				________________________________________");
//	}*/
//	
//	@Test
//	public final void testNormalGet() throws IOException
//	{
//		StockSummary e = new StockSummary("StockSymbol", "StockExchSymbol", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		boolean r = exDao.add(e);
//		
//		assertTrue(r);
//		System.out.println("Unit test for get :");
//		System.out.println("Add StockSummary e with  and get result. "
//				+ "Check result is not null and value is what we add --> result != null ? result == e ?");
//		StockSummary result = exDao.get(0, "StockExchSymbol", "StockSymbol");
//		assertNotNull(result);
//		assertFalse(result.equals(e));
//	}
//	
//	@Test
//	public final void testWrongProviderValueGet() throws IOException
//	{
//		System.out.println("Try to get StockSummary with wrong provider value --> result == null ?");
//		StockSummary result = exDao.get(1, "StockExchSymbol", "StockSymbol");
//		assertTrue(result == null);
//	}
//
//	@Test
//	public final void testWrongExchValueGet() throws IOException
//	{		
//		System.out.println("Try to get StockSummary with wrong Exchange value --> result == null ?");
//		StockSummary result = exDao.get(0, "StockExchSymbolE", "StockSymbol");
//		assertTrue(result == null);
//	}
//	
//	@Test
//	public final void testWrongSymbolValueGet() throws IOException
//	{		
//		System.out.println("Try to get StockSummary with wrong Symbol value --> result == null ?");
//		StockSummary result = exDao.get(0, "StockExchSymbol", "StockSymbolE");
//		assertTrue(result == null);
//		
//		System.out.println("				________________________________________");
//	}
//	
//	@Test
//	public final void testList() throws IOException
//	{
//		StockSummary e1 = new StockSummary("StockSymbol", "StockExchSymbol", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		e1 = new StockSummary("StockSymbol", "StockExchSymbol", 1, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		exDao.add(e1);
//		e1 = new StockSummary("StockSymbol", "StockExchSymbol2", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		exDao.add(e1);
//		e1 = new StockSummary("StockSymbol", "StockExchSymbol", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		exDao.add(e1);
//		e1 = new StockSummary("StockSymbol", "StockExchSymbol2", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		exDao.add(e1);
//		e1 = new StockSummary("StockSymbol", "StockExchSymbol", 0, "TestCompagny", new Date(), new Date(), "Sector Test", "Industry Test", 1);
//		exDao.add(e1);
//		
//		
//		System.out.println("Unit test for list : ");
//		String prefix = Md5Utils.md5sum(0 + Md5Utils.md5sum("StockExchSymbol").toString()).toString();
//		
//		List<StockSummary> result = exDao.list(prefix);
//		assertTrue(result.size() > 0);
//		
//		/**
//		 *  TODO : Add some list test
//		 */
//	}

}
