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
	private int provider;
	private byte[] content;

	public Document(ContentType contentType, DataType dataType, DataClass dataClass, int provider, byte[] content) {
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

	public int getProvider() {
		return provider;
	}

	public void setProvider(int provider) {
		this.provider = provider;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public enum ContentType {
		XML("xml"), JSON("xml"), XLS("xls");

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
		EOD("eod", 0), INTRA("intra", 1), Ref("ref", 2);

		private final String name;
		private final byte[] bytes;

		private DataType(String name, int bytes) {
			this.name = name;
			this.bytes = Bytes.toBytes(bytes);
		}

		public String getName() {
			return name;
		}

		public byte[] getBytes() {
			return bytes;
		}
	}
}
