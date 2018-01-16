package client;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import server.RPCRequest;
import server.RPCResponse;



public class HttpSmptClientImplementation implements HttpSmptClient
{
	String clientEmail;
	String clientPassword;
	String serverEmail;
	
	/**
	 * read properties file and initialise class variables
	 */
	public void readProperties() throws Exception
	{
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		prop.load(HttpSmptClientImplementation.class.getClassLoader().getResourceAsStream("resource/http.smpt.properties"));
		this.clientEmail =  prop.getProperty("clientEmail");
		this.clientPassword = prop.getProperty("clientPassword");
		this.serverEmail = prop.getProperty("serverEmail");

	}
	
	/**
	 * stub to convert command line arguments into wire protocall string
	 */
	public String getProtocolString(String args[]) throws Exception{
		// TODO Auto-generated method stub
		
		// functionName # number of parameters # type of parameter # actual parameter # parameter order #
				
		String wireProtocolString = "";
		String delimiter = "#";
		wireProtocolString = wireProtocolString.concat(args[0]);
		wireProtocolString = wireProtocolString.concat(delimiter);
		
		int numberOfArguments = Integer.parseInt(args[1]);
		wireProtocolString = wireProtocolString.concat(args[1]);
		wireProtocolString = wireProtocolString.concat(delimiter);
		
		for(int i = 0; i < numberOfArguments; i++)
		{
			wireProtocolString = wireProtocolString.concat("INT");
			wireProtocolString = wireProtocolString.concat(delimiter);
			wireProtocolString = wireProtocolString.concat(args[i+2]);
			wireProtocolString = wireProtocolString.concat(delimiter);
			wireProtocolString = wireProtocolString.concat("" + (i + 1));
			wireProtocolString = wireProtocolString.concat(delimiter);
			
		}
		//System.out.println(wireProtocolString);
		
		return wireProtocolString;
	}
	
	
	
	/**
	 * send email to RPC server with wire protocall string as email body
	 */
	public void sendProtocolEmail(String args[]) throws Exception 
	{    
		 	Properties props = System.getProperties();
		
		 // init gmail specific properties
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", this.clientEmail);
	        props.put("mail.smtp.password", this.clientPassword);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

	        try 
	        {
	            message.setFrom(new InternetAddress(this.clientEmail));
	            InternetAddress[] toAddress = new InternetAddress[1];

	            // To get the array of addresses
	            for( int i = 0; i < 1; i++ ) {
	                toAddress[i] = new InternetAddress(this.serverEmail);
	            }

	            for( int i = 0; i < 1; i++) {
	                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
	            }

	            message.setSubject("RPC_QUERY_1");
	            message.setText(getProtocolString(args));
	            Transport transport = session.getTransport("smtp");
	            transport.connect(host, this.clientEmail, this.clientPassword);
	            transport.sendMessage(message, message.getAllRecipients());
	            transport.close();
	        }
	        catch (AddressException ae) {
	            ae.printStackTrace();
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
		
	}
	
	/**
	 * wait for reply email from RPC server
	 */
	public void listenForReply() throws Exception
	{
		// TODO Auto-generated method stub
		
		System.out.println("waiting for reply email");
		
		
		try 
		{
			 
			Properties properties=new Properties();			
			
			properties.put("mail.store.protocol", "imaps");
			properties.put("mail.imaps.host", "imap.gmail.com");
			properties.put("mail.imaps.port", "993");
		      Session emailSession = Session.getInstance(properties);
		  
		     // Transport transport = emailSession.getTransport("smtp");
		     
		      Store store = emailSession.getStore("imaps");
		//    System.out.println(this.clientEmail + "-" + this.clientPassword);
		      store.connect( this.clientEmail, this.clientPassword);

		    
		      Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      RPCResponse rpcResponse = null; 	
		      int found = 0;
		      while(true)
		      {
		    	  Message[] messages = emailFolder.getMessages();
		    	//  System.out.println("messages.length---" + messages.length);
		    	  int x = messages.length;
            
		    	  for (int i = 0, n = 1; i < n; i++) 
		    	  {
		    		  Message message = messages[--x];
		    		  String subject = message.getSubject();
		    		  String toEmail =  message.getFrom()[0].toString();
		    		  String text = message.getContent().toString();
		        
		    		  if(message.getSubject().contains("Response"))
		    		  {
		    			  	System.out.println("got result as :" + text);
		    			  	found = 1;
		    		  }
		    	  } //for
		    	  if(found == 1)
		    		  break;
		    	  else
		    		  TimeUnit.SECONDS.sleep(1);
		      }   
		      
		      //close the store and folder objects
		      properties.remove("mail.store.protocol");
		      properties.remove("mail.imaps.port");
		      properties.remove("mail.imaps.host");
		      emailFolder.close(false);
		      store.close();
		}
		 catch (AddressException ae) {
	            ae.printStackTrace();
	        }
	        catch (MessagingException me) {
	            me.printStackTrace();
	        }
		
	}
		

}
