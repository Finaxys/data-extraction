package msg;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5545589019698956395L;
	
	private String id;
	private Document body;
	private String routingKey;

	
	public Message() {
		super();
	}

	public Message(Document body) {
		super();
		this.id = new Date().getTime() + body.getProvider() + body.getDataClass().name() ;
		this.body = body;
	}
	
	public Message(String id, Document body) {
		super();
		this.id = id;
		this.body = body;
	}
	public Message(Document body, String routingKey) {
		super();
		this.body = body;
		this.routingKey = routingKey;
	}

	
	public Message(String id, Document body, String routingKey) {
		super();
		this.id = id;
		this.body = body;
		this.routingKey = routingKey;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Document getBody() {
		return body;
	}

	public void setBody(Document body) {
		this.body = body;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

}
