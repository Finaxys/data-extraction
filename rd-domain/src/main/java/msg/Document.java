package msg;

import java.io.File;
import java.io.Serializable;

import org.apache.hadoop.hbase.util.Bytes;

public class Document implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4579790937340382141L;
	
	private ContentType contentType;
	private DataType dataType;
	private DataClass dataClass;
	private char provider;
	private byte[] content;

	public Document(ContentType contentType, DataType dataType, DataClass dataClass, char provider, byte[] content) {
		super();
		this.contentType = contentType;
		this.dataType = dataType;
		this.dataClass = dataClass;
		this.provider = provider;
		this.content = content;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public DataClass getDataClass() {
		return dataClass;
	}

	public void setDataClass(DataClass dataClass) {
		this.dataClass = dataClass;
	}

	public char getProvider() {
		return provider;
	}

	public void setProvider(char provider) {
		this.provider = provider;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public enum ContentType {
		XML("xml"), JSON("json"), XLS("xls");

		private final String name;

		private ContentType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public enum DataClass {
		CurrencyPairs, Exchanges, FXRates, IndexInfos, IndexQuotes, StockQuotes, Stocks
	}

	public enum DataType {
		EOD("EOD", 'e'), INTRA("INTRA", 'i'), Ref("REF", 'r');

		private final String name;
		private final byte tByte;

		private DataType(String name, char id) {
			this.name = name;
			this.tByte = (byte)id;
		}

		public String getName() {
			return name;
		}

		public byte getTByte() {
			return tByte;
		}
	}
}
