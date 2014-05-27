/**
 * 
 */
package dao.impl;

import junit.framework.TestCase;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.mockito.MockitoAnnotations;

import dao.StockSummaryDao;
/**
 * @author finaxys
 *
 */
public class StockSummaryDaoImplTest extends TestCase 
{

	private StockSummaryDaoImpl testStock;
	private StockSummaryDao testStockI;
	
	/** 
	 * @throws 
	 * @Before
	 */
	public void setUp() throws ZooKeeperConnectionException
	{
		MockitoAnnotations.initMocks(this);
		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
		testStock = new StockSummaryDaoImpl(hConnection);
		
	}
	
	/**
	 * Test method for {@link dao.impl.StockSummaryDaoImpl#add(domain.StockSummary)}.
	 */
	public void testAdd()
	{
		testStock.add(stock);
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link dao.impl.StockSummaryDaoImpl#get(java.lang.Integer, java.lang.String, java.lang.String)}.
	 */
	public void testGet() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link dao.impl.StockSummaryDaoImpl#list(java.lang.String)}.
	 */
	public void testList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link dao.impl.StockSummaryDaoImpl#listAll()}.
	 */
	public void testListAll() {
		fail("Not yet implemented");
	}

}
