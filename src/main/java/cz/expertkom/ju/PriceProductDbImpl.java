package cz.expertkom.ju;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cz.expertkom.ju.entity.PriceProduct;
import cz.expertkom.ju.entity.PriceProductDto;
import cz.expertkom.ju.entity.PriceProducts;
import cz.expertkom.ju.PriceProductRepository;

@Service
public class PriceProductDbImpl implements PriceProductDb {
	
	@Autowired
	private PriceProductRepository priceProductRepository;

	public void insertPriceproduct(PriceProductDto priceProductDto) {
		PriceProduct pp = new PriceProduct();
		/* na-set-uj instanci z instance Dto */ 
		pp.setNameProduct(priceProductDto.getNameProduct());
		pp.setProductURI(priceProductDto.getProductURI());
		pp.setPriceWithVAT(priceProductDto.getPriceWithVAT());
		pp.setPriceWithOutVAT(priceProductDto.getPriceWithOutVAT());
		/* ulož instanci do tabulky*/
		priceProductRepository.save(pp);
	}

	public PriceProducts geAllProducts() {
		List<PriceProduct> priceProductsList = priceProductRepository.findAll(); 
		PriceProducts priceProducts = new PriceProducts();
		priceProducts.setPriceProducts(priceProductsList);
		return priceProducts;
	}

}
