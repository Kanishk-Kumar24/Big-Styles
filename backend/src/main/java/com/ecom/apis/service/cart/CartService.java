package com.ecom.apis.service.cart;

import com.ecom.apis.entity.Products;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;

import java.util.Map;
import java.util.NoSuchElementException;

public interface CartService {
    String addToCart(String id, int quantity) throws NoSuchElementException, NotFoundException;

    Map<Products, Long> listOfProducts() throws NotFoundException;

    String deleteCartItem(Long cartProductID) throws NotFoundException;

    Double cartAmount() throws NotFoundException;

    void validateCartQuantity() throws NotFoundException;

    String updateProductQuantity(String productID, int quantity) throws NotFoundException;
}
