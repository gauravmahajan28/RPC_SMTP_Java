package client;

public class SmptClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			HttpSmptClientImplementation httpSmptClientImplementation = new HttpSmptClientImplementation();
			httpSmptClientImplementation.readProperties();
			httpSmptClientImplementation.sendProtocolEmail();
		}
		catch(Exception e)
		{
			System.out.println("error occured");
		}

	}

}
