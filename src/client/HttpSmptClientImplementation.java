package client;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;



public class HttpSmptClientImplementation implements HttpSmptClient
{
	String clientEmail;
	String clientPassword;
	String serverEmail;
	public void readProperties() throws Exception
	{
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		prop.load(HttpSmptClientImplementation.class.getClassLoader().getResourceAsStream("resource/http.smpt.properties"));
		this.clientEmail =  prop.getProperty("clientEmail");
		this.clientPassword = prop.getProperty("clientPassword");
		this.serverEmail = prop.getProperty("serverEmail");

	}
	public String getProtocolString() throws Exception{
		// TODO Auto-generated method stub
		
		// functionName # number of parameters # type of parameter # actual parameter # parameter order #
		return "ADD#2#INT#5#1#INT#6#2";
	}
	public void sendProtocolEmail() throws Exception {
		// TODO Auto-generated method stub
		  
	    
		 Properties props = System.getProperties();
		// props.clear();
	        String host = "smtp.gmail.com";
	        props.put("mail.smtp.starttls.enable", "true");
	        props.put("mail.smtp.host", host);
	        props.put("mail.smtp.user", this.clientEmail);
	        props.put("mail.smtp.password", this.clientPassword);
	        props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.auth", "true");

	        Session session = Session.getDefaultInstance(props);
	        MimeMessage message = new MimeMessage(session);

	        try {
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
	            message.setText(getProtocolString());
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
	public void listenForReply() throws Exception{
		// TODO Auto-generated method stub
		
	}
	
	

}
