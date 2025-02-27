package com.ecom.apis.service.product;

import com.ecom.apis.Jwt.JwtAuthFilter;
import com.ecom.apis.entity.ImageData;
import com.ecom.apis.entity.ProductRating;
import com.ecom.apis.entity.Products;
import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.model.ImageModel;
import com.ecom.apis.repository.ImageRepository;
import com.ecom.apis.repository.ProductRepository;
import com.ecom.apis.repository.RatingRepository;
import com.ecom.apis.service.user.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ImageRepository imageRepository ;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private UserServiceImplement userServiceImplement;



    private String saveProduct(Products products) {
        if (Objects.nonNull(productRepository.save(products))){
            return "success";
        }else return  "error";}

    public Products findProductById(Long productId) throws NotFoundException {
        Products products= productRepository.findProductsByProductId(productId);
        if(products!=null) return products;
        else throw new NotFoundException("No product found with ProductId "+productId);
    }

    @Override
    public String addProducts(Products products) {
         if (!productRepository.existsByNameAndSellerId(products.getName(), products.getSellerId()))
            return saveProduct(products);
        else {
            Products byNameAndSellerId = productRepository.findByNameAndSellerId(products.getName(), products.getSellerId());
            if (byNameAndSellerId.getSale() == products.getSale() && byNameAndSellerId.getSalePrice() == products.getSalePrice()
                    && byNameAndSellerId.getPrice().equals(products.getPrice())
                    && byNameAndSellerId.getSpecification().getColor().equals(products.getSpecification().getColor())
                    && byNameAndSellerId.getSpecification().getSize().equals(products.getSpecification().getSize())
                    && byNameAndSellerId.getSpecification().getDetails().equals(products.getSpecification().getDetails())) {
                productRepository.updateQuantity(byNameAndSellerId.getQuantity() + products.getQuantity(), products.getName(), products.getSellerId());
                return "success";
            } else
                return saveProduct(products);
        }
    }

    @Override
    public List<Products> allProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Products> sellerProducts(Long id) {
        return productRepository.findAllBySellerId(id);
    }

    @Override
    public String removeProduct(Long id) {
       if(productRepository.existsById(id)){
            productRepository.deleteById(id);
            return "deleted successfully product with id "+id;
    }else return "no such product with id "+id;
    }

    @Override
    public String addProductImage(ImageModel imageModel) throws IOException, NotFoundException {

        if (imageModel != null) {
            ImageData image = ImageData.builder().imageData(ImageModel.compressImage(imageModel.getImage().getBytes()))
                    .name(imageModel.getName())
                    .products(findProductById(imageModel.getProductId()))
                    .build();
            imageRepository.save(image);
            return "success";
        }
        else return "check data";

    }

    public List<byte[]> showImage(Long productId) throws NoSuchElementException, NotFoundException {
        System.out.println(productId);
        List<ImageData> byId = imageRepository.findImages(productId);
        if (byId.size()<1) throw new NoSuchElementException("No Such related Images");
        List<byte[]> data = new ArrayList<>();
        byId.forEach(imageData -> {data.add(ImageModel.decompressImage(imageData.getImageData()));});
        return data ;
    }

    @Override
    public Products productFromId(Long productId) throws NoSuchElementException, NotFoundException {
        return findProductById(productId);
    }

    @Override
    public String addRating(Long productId, Double rating) throws NotFoundException {
        UserEntity userByEmail = userServiceImplement.getUserByEmail(jwtAuthFilter.getUser());
        if(ratingRepository.existsByUser(userByEmail)){
            ProductRating productRating=ratingRepository.findByUser(userByEmail);
            productRating.setRating(rating);
            ratingRepository.save(productRating);
            return "success";
        }
        ProductRating productRating= ProductRating.builder()
                .products(findProductById(productId))
                .rating(rating)
                .user(userByEmail)
                .build();
        ratingRepository.save(productRating);
        return "success";

    }

    @Override
    public Double getProductRating(Long productId) throws NotFoundException {
        return ratingRepository.averageRating(findProductById(productId));
    }

    @Override
    public String updateProductQuantity(Long productId, int quantity) throws NotFoundException {
        Products product = findProductById(productId);
        productRepository.updateQuantity(quantity ,product.getName(),product.getSellerId());
        return "updated";
    }


    public void productQuantityUpdate(List<Products> productsInCart) {
        for (Products product: productsInCart){
            Products productsByProductId = productRepository.findProductsByProductId(product.getProductId());
            productsByProductId.setQuantity(productsByProductId.getQuantity()-product.getQuantity());
            saveProduct(productsByProductId);
        }
    }
}
