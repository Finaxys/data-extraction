package helper;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import msg.Document;
import msg.Document.ContentType;

import org.apache.commons.io.FileUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

public class Helper {

	private static final String YQL_HOST = "query.yahooapis.com";
	private static final String YQL_PATH = "/v1/public/yql";
	private static final String YQL_QUERY_PARAM = "q";
	private static final String YQL_FORMAT_PARAM = "format";
	private static final String YQL_ENV_PARAM = "env";

	public String constructQuery(String query, String symbs) {
		return query.replaceFirst("\\?", symbs);
	}

	public URI contructYqlUri(String query, ContentType format, String env) throws URISyntaxException {

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(YQL_QUERY_PARAM, query));
		params.add(new BasicNameValuePair(YQL_FORMAT_PARAM, format.getName()));
		params.add(new BasicNameValuePair(YQL_ENV_PARAM, env));

		URIBuilder builder = new URIBuilder().setScheme("https").setHost(YQL_HOST).setPath(YQL_PATH)
				.setParameters(params);
		return builder.build();
	}

	public String getPath(String folder, String file, String ext) {
		return folder + "/" + file + "." + ext;
	}

	public File getFile(String path) throws Exception {
		File f = new File(path);
		return f;

	}

	public byte[] toBytes(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}
}
