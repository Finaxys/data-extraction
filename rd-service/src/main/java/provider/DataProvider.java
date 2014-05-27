package provider;


public interface DataProvider {

	public void getExchanges(String format) throws Exception;
	public void getStocksQuotes(String format) throws Exception;
	public void getCurrencyPairs(String format) throws Exception;
	public void getIndexInfos(String format) throws Exception;
	public void getStocks(String format) throws Exception;
	public void getFXRates(String format) throws Exception;
	
}
