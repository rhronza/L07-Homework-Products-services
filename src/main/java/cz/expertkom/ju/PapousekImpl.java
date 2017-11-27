package cz.expertkom.ju;

import org.springframework.stereotype.Service;

@Service
public class PapousekImpl implements Papousek{

	public String papouskovani(String inputString, String inputString2) {
		return "Papoušek odpovídá: "+inputString+", "+inputString2;
	}
	
	
	
	

}
