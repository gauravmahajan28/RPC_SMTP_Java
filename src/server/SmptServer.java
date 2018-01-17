package server;

import javax.mail.Transport;

public class SmptServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RPCResponse rpcResponse = null;
		try
		{
			HttpSmptServerImplementation httpSmptServerImplementation = new HttpSmptServerImplementation();
			
			while(true)
			{
				httpSmptServerImplementation.readProperties();
				rpcResponse = httpSmptServerImplementation.listenForRPCEmail();
				httpSmptServerImplementation.sendRPCResponse(rpcResponse);
			}
	
		}
		catch(Exception e)
		{
			System.out.println("error occured :" + e.getMessage());
		}
		
	}

}
