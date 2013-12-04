package com.proxy;

import java.util.ArrayList;
/*This class represents the list of blocked website that cannot be visited. 
 * The admin, via the ManagementConsole, can add and remove IP addresses on the fly.
 * Wikipedia is set as a default "bad ip" by the colsole in its class.*/
public class BlockedList {

	ArrayList<String> blocked = new ArrayList<String>(); 
	
	public BlockedList(){
		//blank constructor
		
	}
	//constructor
	public BlockedList(String one){
		this.blocked.add(one);
		
	}
	
	public void addBadUrl(String add){
		this.blocked.add(add);
		this.blocked.add("theregister.co.uk");
	}
	
	public void removeUrl(String remove){
		this.blocked.remove(remove);
		
	}
	
	public boolean isBad(String url){
		return blocked.contains(url);
	}
	//string representation of the blocked list
	//only used for printing purposes. 
	public String toString(){
		String s = "";
		for(int i = 0; i< this.blocked.size(); i++){
			s += this.blocked.get(i);
			s += "\n"; 
		}
		return s;
	}
}
