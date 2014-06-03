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

import domain.IndexInfo;

public class IndexInfoDaoImplTest {

private  IndexInfoDaoImpl exDao;
	
//	@Before
//	public void setUp() throws Exception 
//	{
//		MockitoAnnotations.initMocks(this);
//		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
//		exDao = new IndexInfoDaoImpl(hConnection);
//	}
//	
//	@Test
//	public final void testNormalAdd() throws IOException
//	{
//		IndexInfo  e1 = new IndexInfo("SymbolIndexAdd", "NameIndexAdd", "ExchSymbolAdd", 0);
//		
//		System.out.println("Unit test for Add :");
//		
//		System.out.println("Add currency with symbol = SymbolIndexAdd, name = NameIndexAdd, provider = 0,"
//				+ "exchange_symbol = ExchSymbol --> exDao.add(IndexInfo) == True ? ");
//		assertTrue(exDao.add(e1));
//	}
//	
//	@Test
//	public final void testEmptyValuesAdd() throws IOException
//	{
//		IndexInfo e1 = new IndexInfo("", "", "", 0);
//		System.out.println("Add IndexInfo with all strings parameters are empty strings --> exDao.add(IndexInfo) == True ?");
//		assertTrue(exDao.add(e1));
//	}
//	
//	/*@Test
//	public final void testExistingValueAdd() throws IOException
//	{
//		IndexInfo e1 = new IndexInfo("CurrencyAdd", "CurrencyBaseAdd", "CurrencyQuoteAdd");
//		
//		System.out.println("Try to add an IndexInfo already in the table --> exDao.add(IndexInfo) == False ?");
//		assertFalse(exDao.add(e1));
//		
//		System.out.println("				________________________________________");
//	}*/
//
//	@Test
//	public final void testNormalGet() throws IOException
//	{
//		IndexInfo e = new IndexInfo("SymbolIndexGet", "NameIndexGet", "ExchSymbolGet", 0);
//		boolean r = exDao.add(e);
//		
//		assertTrue(r);
//		System.out.println("Unit test for get :");
//		System.out.println("Add IndexInfo e ith symbol = SymbolIndexAdd, name = NameIndexAdd, provider = 0,"
//				+ "exchange_symbol = ExchSymbol --> exDao.add(IndexInfo) == True ? "
//				+ "Check result is not null and value is what we add --> result != null ? result == e ?");
////		IndexInfo result = exDao.get(20, "suffixe_get", "mic_get");
////		assertNotNull(result);
////		assertTrue(result.equals(e));
//	}
//	
//	@Test
//	public final void testWrongProviderValueGet() throws IOException
//	{		
//		System.out.println("Try to get IndexInfo with wrong symbol value --> result == null ?");
//		IndexInfo result = exDao.get(1, "ExchSymbolGet", "SymbolIndexGet");
//		assertTrue(result == null);
//		
//		System.out.println("				________________________________________");
//	}
//	
//	@Test
//	public final void testWrongSymbolValueGet() throws IOException
//	{		
//		System.out.println("Try to get IndexInfo with wrong symbol value --> result == null ?");
//		IndexInfo result = exDao.get(0, "ExchSymbolGet", "");
//		assertTrue(result == null);
//		
//		System.out.println("				________________________________________");
//	}
//	
//	@Test
//	public final void testWrongSymbolIndexGet() throws IOException
//	{		
//		System.out.println("Try to get IndexInfo with wrong symbol value --> result == null ?");
//		IndexInfo result = exDao.get(0, "", "SymbolIndexGet");
//		assertTrue(result == null);
//		
//		System.out.println("				________________________________________");
//	}
//	
//	@Test
//	public final void testList() throws IOException
//	{
//		IndexInfo e1 = new IndexInfo("SymbolList", "NameSymbolList", "ExchSymbolList", 1);
//		exDao.add(e1);
//		e1 = new IndexInfo("SymbolList1", "NameSymbolList", "ExchSymbolList", 0);
//		exDao.add(e1);
//		e1 = new IndexInfo("SymbolList", "NameSymbolList", "ExchSymbolList", 0);
//		exDao.add(e1);
//		e1 = new IndexInfo("SymbolList1", "NameSymbolList2", "ExchSymbolList", 1);
//		exDao.add(e1);
//		e1 = new IndexInfo("SymbolList", "NameSymbolList2", "ExchSymbolList", 0);
//		exDao.add(e1);
//		e1 = new IndexInfo("SymbolList", "NameSymbolList", "ExchSymbolList", 0);
//		exDao.add(e1);
//		
//		System.out.println("Unit test for list : ");
//		
//		List<IndexInfo> result = exDao.list("");
//		assertTrue(result.size() >= 6);
//	}
}
