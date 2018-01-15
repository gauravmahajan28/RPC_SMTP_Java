package client;

public interface HttpSmptClient 
{
	
	public void readProperties() throws Exception;
	
	public String getProtocolString() throws Exception;
	
	public void sendProtocolEmail() throws Exception;
	
	public void listenForReply() throws Exception;
}
