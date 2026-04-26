package com.example.sales.orders.application.external.outboundservices;

import com.example.sales.orders.domain.services.ProductExternalService;
import com.example.sales.products.interfaces.acl.ProductsContextFacade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductExternalServiceImpl implements ProductExternalService {
  private final ProductsContextFacade productContextFacade;

  public ProductExternalServiceImpl(ProductsContextFacade productContextFacade) {
    this.productContextFacade = productContextFacade;
  }

  @Override
  public boolean existsProduct(Long productId) {
    return productContextFacade.existsProduct(productId);
  }

  @Override
  public BigDecimal getProductPrice(Long productId) {
    return productContextFacade.getProductPrice(productId);
  }

  @Override
  public boolean hasStock(Long productId, Integer quantity) {
    return productContextFacade.hasEnoughStock(productId, quantity);
  }

  @Override
  public void reduceStock(Long productId, Integer quantity) {
    productContextFacade.decreaseStock(productId, quantity);
  }

  @Override
  public void increaseStock(Long productId, Integer quantity) {
    productContextFacade.increaseStock(productId, quantity);
  }
}
