package client;


public class SmptClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			HttpSmptClientImplementation httpSmptClientImplementation = new HttpSmptClientImplementation();
			
			//read properties file containing gmail usernames and passwords
			httpSmptClientImplementation.readProperties();
			
			//create arguments into wire protocall string and send email
			httpSmptClientImplementation.sendProtocolEmail(args);
			
			//wait for reply email
			httpSmptClientImplementation.listenForReply();
			
		}
		catch(Exception e)
		{
			System.out.println("error occured");
		}

	}

}
