package com.ecom.apis.service.cart;

import com.ecom.apis.Jwt.JwtAuthFilter;
import com.ecom.apis.entity.Cart;
import com.ecom.apis.entity.Products;
import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.repository.CartRepository;
import com.ecom.apis.service.product.ProductServiceImpl;
import com.ecom.apis.service.user.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService{
@Autowired CartRepository cartRepository;
@Autowired ProductServiceImpl productService;
@Autowired UserServiceImplement userServiceImplement;
@Autowired JwtAuthFilter jwtAuthFilter;


    @Override
    public String addToCart(String productId, int productQuantity) throws  NotFoundException {
        Products product=productService.findProductById(Long.parseLong(productId));
        UserEntity user = userServiceImplement.getUserByEmail(jwtAuthFilter.getUser());
            Cart cart =cartRepository.findWithProductUser(product,user);
            if(productQuantity<=product.getQuantity()){
            if(cart==null)
                cart = Cart.builder().user(user).products(product).quantity(productQuantity).build();
            else{
                if (cart.getQuantity() + productQuantity<=product.getQuantity())
                cart.setQuantity(cart.getQuantity() + productQuantity);
                else return "Can't add to stock as the product is low in stocks";
            }
            }
            else return "Can't add to stock as the product is low in stocks";
            cartRepository.save(cart);
            return "product added to cart";

    }

    @Override
    public Map<Products, Long> listOfProducts() throws NotFoundException {
        Map<Products,Long> productsMap=new HashMap<>();
        for (Cart cart : cartRepository.findAllByUser(userServiceImplement.getUserByEmail(jwtAuthFilter.getUser()))) {
            Products prod = cart.getProducts();
            prod.setQuantity(cart.getQuantity());
            productsMap.put(prod, cart.getCartId());
        }

        return productsMap;
    }

    @Override
    public String deleteCartItem(Long cartID) throws NotFoundException {
        if(cartRepository.existsById(cartID)){
            cartRepository.deleteById(cartID);
            return "Cart item removed with cartID "+cartID ;
        }else {
            throw new NotFoundException("No cart product available with id "+cartID);
        }
    }

    @Override
    public Double cartAmount() throws NotFoundException {
        Double amount=0d;
        List<Cart> cartList= cartRepository.findAllByUser(userServiceImplement.getUserByEmail(jwtAuthFilter.getUser()));
        if (cartList.size()==0) throw new NotFoundException("No products in cart related to user "+jwtAuthFilter.getUser());
        else for (Cart cart : cartList) amount += cart.getProducts().getPrice();
        return amount;
    }

    @Override
    public void validateCartQuantity() throws NotFoundException {
        UserEntity userEntity = userServiceImplement.getUserByEmail(jwtAuthFilter.getUser());
        if (cartRepository.existsByUser(userEntity)) {
            for (Cart cart:cartRepository.findAllByUser(userEntity)){
                if(cart.getQuantity()>=cart.getProducts().getQuantity()) throw new NotFoundException("Product in the cart "+ cart.getProducts().getName() +
                        "is low in stocks. Either reduce its quantity or remove from cart");}
        }else
            throw new NotFoundException("No products found related to "+userEntity.getUserEmail());
    }


    @Override
    public String updateProductQuantity(String productID, int quantity) throws NotFoundException {
        UserEntity userEntity = userServiceImplement.getUserByEmail(jwtAuthFilter.getUser());
        Products productById = productService.findProductById(Long.parseLong(productID));
        Cart withProductUser = cartRepository.findWithProductUser(productById, userEntity);
        if(withProductUser== null) return "No item in cart related to product";
        if(quantity<=productById.getQuantity()){
        cartRepository.updateProductQuantity(quantity,withProductUser.getCartId());
        return "updated";}
        else return "The product is low in stocks";
    }


    public  List<Products> productsListByUser(UserEntity userEntity) throws NotFoundException {
        List<Products> productsList = new ArrayList<>();
        if (cartRepository.existsByUser(userEntity)) {
            for (Cart cart:cartRepository.findAllByUser(userEntity)){
                productsList.add(cart.getProducts());
            }
            return productsList;
        }else
            throw new NotFoundException("No products found related to "+userEntity.getUserEmail());

    }
}
