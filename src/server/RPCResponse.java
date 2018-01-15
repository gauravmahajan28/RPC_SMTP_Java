package server;

public class RPCResponse {
	
	 int result;
	 String subject;
	 String toEmail;
	public RPCResponse(int result, String subject, String toEmail) {
		super();
		this.result = result;
		this.subject = subject;
		this.toEmail = toEmail;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	
	
	
	

}
