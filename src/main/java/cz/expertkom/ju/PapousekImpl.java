package cz.expertkom.ju;

import org.springframework.stereotype.Service;

@Service
public class PapousekImpl implements Papousek{

	public String papouskovani(String inputString) {
		return "Papoušek odpovídá: "+inputString+", "+inputString;
	}
	
	
	
	

}
