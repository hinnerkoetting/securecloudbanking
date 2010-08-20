package de.mrx.server;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class TANList implements Serializable{
	
	@PrimaryKey	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	
	private final static Logger log = Logger.getLogger(TANList.class.getName());

	@Persistent
	private List<String> tan=new ArrayList<String>();

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public List<String> getTan() {
		return tan;
	}

	public void setTan(List<String> tan) {
		this.tan = tan;
	}
	
	public void generatedTANs(){
		tan.clear();
		Random r=new Random();
		DecimalFormat format=new DecimalFormat("####");
		format.setMinimumIntegerDigits(4);
		for (int i=0;i<100;i++){
			int number=r.nextInt(10000);
			String tanText=format.format(number);			
			tan.add(tanText);
		}
		
	}
	
	
}
