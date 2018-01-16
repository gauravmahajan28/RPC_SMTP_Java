package client;

public interface HttpSmptClient 
{
	
	public void readProperties() throws Exception;
	
	public String getProtocolString(String args[]) throws Exception;
	
	public void sendProtocolEmail(String args[]) throws Exception;
	
	public void listenForReply() throws Exception;
}
