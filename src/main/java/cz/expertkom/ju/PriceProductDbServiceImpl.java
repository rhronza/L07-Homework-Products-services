package cz.expertkom.ju;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.expertkom.ju.entity.PriceProduct;
import cz.expertkom.ju.entity.PriceProductDto;
import cz.expertkom.ju.entity.PriceProducts;

@Service
public class PriceProductDbServiceImpl implements PriceProductDb {
	
	@Autowired
	private PriceProductRepository priceProductRepository;
	
	public void insertPriceproduct(PriceProductDto priceProductDto) {
		PriceProduct pp = new PriceProduct();
		/* naSETuj instanci PriceProduct z instance třídy PriceProductDto ...Dto */ 
		pp.setNameProduct(priceProductDto.getNameProduct());
		pp.setProductURI(priceProductDto.getProductURI());
		pp.setPriceWithVAT(priceProductDto.getPriceWithVAT());
		pp.setPriceWithOutVAT(priceProductDto.getPriceWithOutVAT());
		pp.setInsertedDateTime(new Date());
		/* ulož instanci do tabulky*/
		priceProductRepository.save(pp);
	}

	public PriceProducts getAllProducts() {
		List<PriceProduct> priceProductsList = priceProductRepository.findAll(); 
		PriceProducts priceProducts = new PriceProducts();
		priceProducts.setPriceProducts(priceProductsList);
		//long pocetZzn = priceProductRepository.count();
		//System.out.println("Počet záznamů tabulky:"+pocetZzn);
		return priceProducts;
	}
	
	public PriceProducts getOneProductToList(Long id ) {
		List<PriceProduct> priceProductsList = new ArrayList<PriceProduct>();
		PriceProduct pp = new PriceProduct();
		pp = priceProductRepository.findOne(id);
		priceProductsList.add(pp);
		PriceProducts priceProducts = new PriceProducts();
		priceProducts.setPriceProducts(priceProductsList);
		System.out.println(	priceProductsList.get(0).getNameProduct()+", vloženo:"+
							priceProductsList.get(0).getInsertedDateTime()+", změněno: "+
							priceProductsList.get(0).getUpdatedDateTime());
		return priceProducts;
	}

	public void deletePriceProduct(Long id) {
		priceProductRepository.delete(id);
	}

	public void updatePriceProduct(Long id, PriceProductDto ppdto) {
		PriceProduct priceProduct = priceProductRepository.findOne(id);
		priceProduct.setNameProduct(ppdto.getNameProduct());
		priceProduct.setProductURI(ppdto.getProductURI());
		priceProduct.setPriceWithVAT(ppdto.getPriceWithVAT());
		priceProduct.setPriceWithOutVAT(ppdto.getPriceWithOutVAT());
		priceProduct.setUpdatedDateTime(Calendar.getInstance());
		priceProductRepository.save(priceProduct);
	}
	
	

}
