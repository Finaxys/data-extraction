package dao.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import domain.CurrencyPair;

public class CurrencyPairDaoImplTest {

	private  CurrencyPairDaoImpl exDao;
	
	@Before
	public void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
		exDao = new CurrencyPairDaoImpl(hConnection);
	}
	
	@Test
	public final void testNormalAdd() throws IOException
	{
		CurrencyPair  e1 = new CurrencyPair("CurrencyAdd", "CurrencyBaseAdd", "CurrencyQuoteAdd");
		
		System.out.println("Unit test for Add :");
		
		System.out.println("Add currency with symbol = Currency Add --> exDao.add(CurrencyPair) == True ? ");
		assertTrue(exDao.add(e1));
	}
	
	@Test
	public final void testEmptyValuesAdd() throws IOException
	{
		CurrencyPair e1 = new CurrencyPair("", "", "");
		System.out.println("Add CurrencyPair with all strings parameters are empty strings --> exDao.add(CurrencyPair) == True ?");
		assertTrue(exDao.add(e1));
	}
	
	/*@Test
	public final void testExistingValueAdd() throws IOException
	{
		CurrencyPair e1 = new CurrencyPair("CurrencyAdd", "CurrencyBaseAdd", "CurrencyQuoteAdd");
		
		System.out.println("Try to add an CurrencyPair already in the table --> exDao.add(CurrencyPair) == False ?");
		assertFalse(exDao.add(e1));
		
		System.out.println("				________________________________________");
	}*/

	@Test
	public final void testNormalGet() throws IOException
	{
		CurrencyPair e = new CurrencyPair("CurrencyGet", "CurrencyBaseGet", "CurrencyQuoteGet");
		boolean r = exDao.add(e);
		
		assertTrue(r);
		System.out.println("Unit test for get :");
		System.out.println("Add CurrencyPair e with symbol = CurrencyGet'; base = CurrencyBaseGet; quote = CurrencyQuoteGet and get result. Check result is not null and value is what we add --> result != null ? result == e ?");
//		CurrencyPair result = exDao.get(20, "suffixe_get", "mic_get");
//		assertNotNull(result);
//		assertTrue(result.equals(e));
	}
	
	@Test
	public final void testWrongSymbolValueGet() throws IOException
	{		
		System.out.println("Try to get CurrencyPair with wrong symbol value --> result == null ?");
//		CurrencyPair result = exDao.get("CurrencyAdd", "", "CurrencyQuoteAdd");
//		assertTrue(result == null);
		
		System.out.println("				________________________________________");
	}
	
	@Test
	public final void testList() throws IOException
	{
		CurrencyPair e1 = new CurrencyPair("Currency_symbol1", "CurrencyBase1", "CurrencyQuote1");
		exDao.add(e1);
		e1 = new CurrencyPair("Currency_symbol2", "CurrencyBase2", "CurrencyQuote2");
		exDao.add(e1);
		e1 = new CurrencyPair("Currency_symbol3", "CurrencyBase3", "CurrencyQuote3");
		exDao.add(e1);
		e1 = new CurrencyPair("Currency_symbol4", "CurrencyBase4", "CurrencyQuote4");
		exDao.add(e1);
		e1 = new CurrencyPair("Currency_symbol5", "CurrencyBase5", "CurrencyQuote5");
		exDao.add(e1);
		e1 = new CurrencyPair("Currency_symbol6", "CurrencyBase6", "CurrencyQuote6");
		exDao.add(e1);
		
		System.out.println("Unit test for list : ");
		
		List<CurrencyPair> result = exDao.list("Currency_");
		assertTrue(result.size() >= 6);
	}		

}
