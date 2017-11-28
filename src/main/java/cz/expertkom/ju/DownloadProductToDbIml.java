package cz.expertkom.ju;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import cz.expertkom.ju.entity.PriceProductDto;


@Service
public class DownloadProductToDbIml implements DownloadProductToDb{
	
	@Autowired
	PriceProductDb ppDb;
	
	private PriceProductDto ppDto = new PriceProductDto();
	
	private String[] listStringSplit;
	
	private String stringDownloadedWebPage;
	
	boolean productFound = false;
	boolean nextIsName= false;
	boolean priceVATfound = false;
	boolean priceWithoutVatfound = false;

	public void downloadProductTodb() {
			
	final String WEB_PAGE_DOWNLOAD = "https://softcom.cz";

		try {
			/* načtení stránky do proměnné typu String */
			stringDownloadedWebPage = Unirest.get(WEB_PAGE_DOWNLOAD).asString().getBody();
			/* split podle znaku apostrofu: */
			this.listStringSplit = stringDownloadedWebPage.split("'");
			
			String predchozi ="";
			
			/* iterace pole vzniklého splitem */
			for (String sProduct: listStringSplit) {
			
				/* pokud řetez končí ".html" - jedná se o název výrobku*/ 
				if (!productFound && sProduct.endsWith(".html")&& (!(sProduct.equals(predchozi)))) {
					this.ppDto.setProductURI(sProduct);;
					System.out.println("sproduct:"+sProduct+", predchozi:"+predchozi);
					predchozi=sProduct;
					productFound=true;
					nextIsName=true;
					continue;
				}
				
				if (nextIsName) {
					/* kde začíná sekvence znaků: "</a></h2>" */
					int indexFinish=sProduct.lastIndexOf("</a></h2>");
					if (indexFinish<2) {indexFinish=2;}
					/*vezmi substring a ignoruj 1.znak do pozice kde začíná "</a></h2>" a výsledkem nastav vlastnost Name */
					ppDto.setNameProduct(sProduct.substring(1,indexFinish));
					nextIsName=false;					
					continue;
				}
				
				/* jestliže následující řetez obsahuje "cena:", pak se provede splitování podle "span" a odstraněním znaků "<u>/" vznikne cena v Kč */   
				if (productFound &&(sProduct.contains("cena:"))) {
					String[] listStringSplitPrice = sProduct.split("span");
					for (String sPrice: listStringSplitPrice) {
						if((sPrice!=null)&&sPrice.contains("Kč")) {
							if (!priceVATfound) {
								priceVATfound=true;
								this.ppDto.setPriceWithVAT(sPrice.replaceAll("[<u>/]", ""));
							} else {
								priceWithoutVatfound=true;
								this.ppDto.setPriceWithOutVAT(sPrice.replaceAll("[<u>/]", ""));;
								break;
							}
						}
					}
				}
				
				if (productFound && priceVATfound && priceWithoutVatfound) {
					/** přidat do databáze*/
					ppDb.insertPriceproduct(ppDto);
					/*  "shodit" přepínače pro další product */
					productFound = false;
					priceVATfound = false;
					priceWithoutVatfound = false;
					
				}
			} 
		
		} catch (UnirestException e) {
			System.out.println("Problém s načtenením stránky");
			this.ppDto.setNameProduct("Problém s načtenením stránky:"+e.getLocalizedMessage());
			this.ppDto.setPriceWithVAT("?????");
			this.ppDto.setPriceWithOutVAT("??????");
			e.printStackTrace();
		}
		
		/*
		int line = 1;
		
		System.out.println("************************************");
		System.out.println("Celý před splitem");
		System.out.println("************************************");
		*/
		/*System.out.println(stringDownloadedWebPage);*/

		/*
		System.out.println("************************************");
		System.out.println("Řádky po splitu");
		System.out.println("************************************");
		*/
		/*
		line=1;
		for (String s: listStringSplit) {
			System.out.println("line "+line+":"+s);
			line++;
		}
		*/
		

}
}