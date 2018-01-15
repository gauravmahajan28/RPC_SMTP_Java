package server;

public interface HttpSmptServer {
	
	public void readProperties() throws Exception;
	
	public int add(int a, int b) throws Exception;
	
	public RPCResponse listenForRPCEmail() throws Exception;
	
	public void sendRPCResponse(RPCResponse rpcResponse) throws Exception;
	
	public RPCRequest parseRPCRequest(String message) throws Exception;
	
	

}
