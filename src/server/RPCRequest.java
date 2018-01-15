package server;

import java.util.ArrayList;
import java.util.List;

public class RPCRequest {
	String functionName;
	ArrayList<String> arguments;
	
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public ArrayList<String> getArguments() {
		return arguments;
	}
	public void setArguments(ArrayList<String> arguments) {
		this.arguments = arguments;
	}
	
	
	

}
