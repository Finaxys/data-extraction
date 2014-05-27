package provider;


public interface DataProvider {

	public void getExchanges(String format) throws Exception;
	public void getStockSummaries(String format) throws Exception;
	public void getCurrencyPairs(String format) throws Exception;
	public void getIndexInfos(String format) throws Exception;
	
}
