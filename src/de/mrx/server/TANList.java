package de.mrx.server;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * belongs to an internalSCBAccount. For every transaction, a TAN must be used. Server side defines the required TAN (iTAN-Procedure)
 * @author IV#11C9
 *
 */
@PersistenceCapable
public class TANList{
	
	@PrimaryKey	
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;
	

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
