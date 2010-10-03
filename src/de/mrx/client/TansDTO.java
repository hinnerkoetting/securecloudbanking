package de.mrx.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




/**
 * List of tans DTO for admin control
 * @author Hinni
 *
 */
public class TansDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> tanList;
	
	public TansDTO() {
		
	}
	
	public TansDTO(List<String> list) {
		tanList = new ArrayList<String>();
		
		//order is important
		int size = list.size();
		for (int i = 0; i < size; i++) {
			tanList.add(i, new String(list.get(i)));
		}
	}
	
	public List<String> getTans() {
		return tanList;
	}
}
