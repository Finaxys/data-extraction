package domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="indexInfos")
public class IndexInfos {

	private List<IndexInfo> indexInfosList;

	@XmlElementWrapper(name = "indexInfosList")
	@XmlElement(name = "indexInfo")
	public List<IndexInfo> getIndexInfosList() {
		if (indexInfosList == null)
			indexInfosList = new ArrayList<IndexInfo>();
		return indexInfosList;
	}

	public void setIndInfosList(List<IndexInfo> indexInfosList) {
		this.indexInfosList = indexInfosList;
	}
}
