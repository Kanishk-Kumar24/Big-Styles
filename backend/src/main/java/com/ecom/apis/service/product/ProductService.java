package com.ecom.apis.service.product;

import com.ecom.apis.entity.Products;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.model.ImageModel;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface ProductService {
    String addProducts(Products products);

    List<Products> allProducts();

    List<Products> sellerProducts(Long id);

    String removeProduct(Long id) throws NotFoundException;

    String addProductImage(ImageModel imageData) throws IOException, NotFoundException;

    List<byte[]> showImage(Long id) throws NoSuchElementException, NotFoundException;

    Products productFromId(Long productId) throws NoSuchElementException, NotFoundException;

    String addRating(Long productId, Double rating) throws NotFoundException;

    Double getProductRating(Long productId) throws NotFoundException;

    String updateProductQuantity(Long productId, int quantity) throws NotFoundException;
}
