package server;

import java.util.ArrayList;

public interface HttpSmptServer {
	
	public void readProperties() throws Exception;
	
	public int add(ArrayList<String> arguments) throws Exception;
	
	public RPCResponse listenForRPCEmail() throws Exception;
	
	public void sendRPCResponse(RPCResponse rpcResponse) throws Exception;
	
	public RPCRequest parseRPCRequest(String message) throws Exception;
	
	

}
