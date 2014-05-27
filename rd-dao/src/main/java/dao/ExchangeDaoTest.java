package dao;

import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.mockito.MockitoAnnotations;

import dao.impl.ExchangeDaoImpl;
import domain.Exchange;
import junit.framework.TestCase;

public class ExchangeDaoTest extends TestCase 
{
	private  ExchangeDaoImpl exDao;
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		MockitoAnnotations.initMocks(this);
		HConnection hConnection = HConnectionManager.createConnection(HBaseConfiguration.create());
		exDao = new ExchangeDaoImpl(hConnection);
	}

	public final void testAdd() throws IOException
	{
		Exchange e1 = new Exchange("mic_add", "symbol_add", "suffixe_add", 0, "Test Exchange add", "Interne Type add", "Europe", "France", "USD/EUR", 1, 10, false);
		
		System.out.println("Unit test for Add :");
		
		System.out.println("Add exchange with mic = 'mic_add'; suffix = 'suffixe_add'; provider = 0 --> exDao.add(exchange) == True ? ");
		assertTrue(exDao.add(e1));
		
		e1 = new Exchange("", "", "", 0, "", "", "", "", "", 0, 0, false);
		System.out.println("Add exchange with only provider = 0, closeTime = 0 and openTime = 0 and all strings parameters are empty strings --> exDao.add(exchange) == True ?");
		assertTrue(exDao.add(e1));
		
		e1 = new Exchange("mic_add", "symbol_add", "suffixe_add", 0, "Test Exchange add", "Interne Type add", "Europe", "France", "USD/EUR", 1, 10, false);
		System.out.println("Try to add an exchange already in the table --> exDao.add(exchange) == False ?");
		assertFalse(exDao.add(e1));
		
		System.out.println("				________________________________________");
	}

	public final void testGet() throws IOException
	{
		Exchange e = new Exchange("mic_get", "symbol_get", "suffixe_get", 20, "Test Exchange get", "Interne Type get", "Europe", "France", "USD/EUR", 5, 6, true);
		exDao.add(e);
		
		System.out.println("Unit test for get :");
		System.out.println("Add exchange e with mic = 'mic_add'; suffix = 'suffixe_add'; provider = 0 and get result. Check result is not null and value is what we add --> result != null ? result == e ?");
		Exchange result = exDao.get(20, "suffixe_get", "mic_get");
		assertTrue(result != null);
		assertTrue(result.equals(e));
		
		System.out.println("Try to get exchange with wrong provider value --> result == null ?");
		result = exDao.get(0, "suffixe_get", "mic_get");
		assertTrue(result == null);
		
		System.out.println("Try to get exchange with wrong mic value --> result == null ?");
		result = exDao.get(20, "suffixe_get", "micet");
		assertTrue(result == null);
		
		System.out.println("Try to get exchange with wrong suffix value --> result == null ?");
		result = exDao.get(0, "suffet", "mic_get");
		assertTrue(result == null);
		
		System.out.println("				________________________________________");
	}

	public final void testList()
	{
		Exchange e1 = new Exchange("mic_list1", "symbol_get", "suffixe_get", 20, "Test Exchange list", "Interne Type list", "Europe", "France", "USD/EUR", 5, 6, true);
		exDao.add(e1);
		e1 = new Exchange("mic_list1", "symbol_get", "suffixe_get", 17, "Test Exchange list", "Interne Type list", "Europe", "France", "USD/EUR", 3, 68, true);
		exDao.add(e1);
		e1 = new Exchange("mic_list1", "symbol_get", "suffixe_get", 18, "Test Exchange list", "Interne Type list", "Europe", "France", "USD/EUR", 1, 0, true);
		exDao.add(e1);
		e1 = new Exchange("mic_list1", "symbol_get", "suffixe_get", 18, "Test Exchange list", "Interne Type list", "Europe", "France", "USD/EUR", 55, 6, true);
		exDao.add(e1);
		e1 = new Exchange("mic_get", "symbol_get", "suffixe_get", 20, "Test Exchange list", "Interne Type list", "Europe", "France", "USD/EUR", 9, 10, true);
		exDao.add(e1);
		e1 = new Exchange("mic_list1", "symbol_get", "suffixe_get", 20, "Test Exchange list", "Interne Type list", "Europe", "France", "USD/EUR", 3, -5, true);
		exDao.add(e1);
		
		System.out.println("Unit test for list : ");
		
		//List<Exchange> result = exDao.list(prefix)
	}

}
