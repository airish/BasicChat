package com.airish.basicchat.server;

import java.util.ArrayList;
import java.util.List;

public class UniqueIdentifier {
	private static List<Integer> ids = new ArrayList<Integer>();
	private static final int RANGE = 10000;
	private static int index = 0;
	
	static {
		for(int i = 0; i < RANGE; i++){
			ids.add(i);
		}
	}
	
	private UniqueIdentifier(){
		
	}

	// Return the unique identifier
	public static int getIdentifier(){
		if(index > ids.size() - 1) {
			index = 0;
		}
		
		// Return index, then increment the index
		return ids.get(index++);
	}
	
	
}
