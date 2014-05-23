package provider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;

public interface DataProvider {

	public File getExchanges();
	public void getStockSummaries(String format) throws Exception;
	
}
