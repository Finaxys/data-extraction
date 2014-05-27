package dao;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.mockito.MockitoAnnotations;

import dao.impl.ExchangeDaoImpl;
import domain.Exchange;
import junit.framework.TestCase;

public class ExchangeDaoTest extends TestCase 
{
	private  ExchangeDaoImpl e;
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		MockitoAnnotations.initMocks(this);
		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
		e = new ExchangeDaoImpl(hConnection);
	}

	public final void testAdd()
	{
		Exchange e = new Exchange("AMS", "AS", 15, "Amsterdam Stock Exchange", )
		fail("Not yet implemented"); // TODO
	}

	public final void testGet() {
		fail("Not yet implemented"); // TODO
	}

	public final void testList() {
		fail("Not yet implemented"); // TODO
	}

}
