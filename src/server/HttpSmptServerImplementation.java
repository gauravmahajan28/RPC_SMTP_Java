package server;

import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.Flags.Flag;


public class HttpSmptServerImplementation implements HttpSmptServer {
	
	String serverEmail;
	String serverPassword;
	
	/**
	 * read properties file containing gmail usernames and password and initialize class variables
	 */
	public void readProperties() throws Exception
	{
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		prop.load(HttpSmptServerImplementation.class.getClassLoader().getResourceAsStream("resource/http.smpt.properties"));
		this.serverEmail = prop.getProperty("serverEmail");
		this.serverPassword = prop.getProperty("serverPassword");

	}
	
	@Override
	/**
	 * function add arguments 
	 */
	public int add(ArrayList<String> arguments) throws Exception {
		// TODO Auto-generated method stub
		int result = 0;
		try
		{
			for(int i = 0; i < arguments.size(); i++)
			{
				result += Integer.parseInt(arguments.get(i));
			}
			return result;
		}
		catch(Exception e)
		{
			return -1;
		}
		
	}
	
	@Override
	/**
	 * listen for RPC email
	 */
	public RPCResponse listenForRPCEmail() throws Exception {
		// TODO Auto-generated method stub
		try {

		 
			Properties properties=new Properties();
			
			properties.put("mail.store.protocol", "imaps");
			
			properties.put("mail.imaps.host", "imap.gmail.com");
			
			properties.put("mail.imaps.port", "993");
		     Session emailSession = Session.getInstance(properties);
		  
		     // Transport transport = emailSession.getTransport("smtp");
		     
		      Store store = emailSession.getStore("imaps");

		      store.connect( this.serverEmail, this.serverPassword);

		    
		      Folder emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_WRITE);
		      RPCResponse rpcResponse = null; 	
		      int found = 0;
		      while(true)
		      {
		    	   store = emailSession.getStore("imaps");

			      store.connect( this.serverEmail, this.serverPassword);
			      emailFolder = store.getFolder("INBOX");
			      emailFolder.open(Folder.READ_WRITE);

		    	  Message messages[] = emailFolder.search(new FlagTerm(new Flags(Flag.SEEN), false));	
		    	  //  Message[] messages = emailFolder.getMessages();
		    	  System.out.println("messages.length---" + messages.length);
		    	  int x = messages.length;
                  if(x == 0)
                  {
                	  TimeUnit.SECONDS.sleep(1);
                	  emailFolder.close(false);
        		      store.close();
                	  continue;
                  }
		    	  for (int i = 0, n = 1; i < n; i++) 
		    	  {
		    		  Message message = messages[--x];
		    		  String subject = message.getSubject();
		    		  String toEmail =  message.getFrom()[0].toString();
		    		  String text = message.getContent().toString();
		        
		    		  if(message.getSubject().contains("RPC_QUERY"))
		    		  {
		    			  emailFolder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
		    			  RPCRequest rpcRequest = parseRPCRequest(text);
		    			  if(rpcRequest.getFunctionName().equals("ADD"))
		    			  {
		    				  System.out.println("Email Found");
		    				  int result = add(rpcRequest.getArguments());
		    				  System.out.println("result :" + result);
		    				  rpcResponse = new RPCResponse(result, subject + " : Response", toEmail);
		    				  found = 1;
		    				  break;
		    			  }
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
		      return rpcResponse;
		      

		      } 
		catch (MessagingException e) {
		         e.printStackTrace();
		         return null;
		      } catch (Exception e) {
		         e.printStackTrace();
		         return null;
		      }
		
	}
	@Override
	public void sendRPCResponse(RPCResponse rpcResponse) throws Exception {
		// TODO Auto-generated method stub
	//	System.out.println(serverEmail + "-" + serverPassword);
		Properties props = System.getProperties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.user", this.serverEmail);
		props.put("mail.smtp.password", this.serverPassword);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.port", "587"); // 587 is the port number of yahoo mail
		props.put("mail.smtp.auth", "true");

        //Session session = Session.getDefaultInstance(props);
		Session session = Session.getInstance(props);
		MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(this.serverEmail));
            InternetAddress[] toAddress = new InternetAddress[1];

            // To get the array of addresses
            for( int i = 0; i < 1; i++ ) {
                toAddress[i] = new InternetAddress(rpcResponse.getToEmail());
            }

            for( int i = 0; i < 1; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(rpcResponse.getSubject());
            if(rpcResponse.getResult() == -1)
            	message.setText("Answer : " + "Error in Arguments");
            else 
            	message.setText("Answer : " + rpcResponse.getResult());
            Transport transport = session.getTransport("smtp");
            props.put("mail.smtp.port", "587");
            transport.connect("smtp.gmail.com", this.serverEmail, this.serverPassword);
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
	@Override
	/**
	 * convert wire protocall string intp RPCRequest POJO
	 */
	public RPCRequest parseRPCRequest(String message) throws Exception {
		// TODO Auto-generated method stub
		//ADD#2#INT#5#1#INT#5#2
		
		StringTokenizer stringTokenizer = new StringTokenizer(message, "#");
		
		RPCRequest rpcRequest = new RPCRequest();
		String functionName;
		functionName = stringTokenizer.nextToken();
		
		int numberOfArguments = Integer.parseInt(stringTokenizer.nextToken());
		
		ArrayList<String> arguments = new ArrayList<>();
		
		for(int count = 1; count <= numberOfArguments; count++)
		{
			String type = stringTokenizer.nextToken();
			String val = stringTokenizer.nextToken();
			String order = stringTokenizer.nextToken();
			
			arguments.add(val);
		}
		
		rpcRequest.setArguments(arguments);
		rpcRequest.setFunctionName(functionName);
		
		return rpcRequest;
	}
	
	

}
