package provider;


public class DataProviderFactory {

	public static DataProvider getMDataProvider(int dpSymbol){
		switch(dpSymbol){
		case HomeDataProvider.H_PROVIDER_SYMB : return new HomeDataProvider();
		case YahooDataProvider.Y_PROVIDER_SYMB : return new YahooDataProvider();
		default : return new YahooDataProvider();
		}
		
	}
}
