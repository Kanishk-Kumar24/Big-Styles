package com.ecom.apis.service.order;

import com.ecom.apis.Jwt.JwtAuthFilter;
import com.ecom.apis.entity.Orders;
import com.ecom.apis.entity.Products;
import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.repository.OrderRepository;
import com.ecom.apis.service.cart.CartServiceImpl;
import com.ecom.apis.service.product.ProductServiceImpl;
import com.ecom.apis.service.user.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private JwtAuthFilter jwtAuthFilter;
    @Autowired
    private UserServiceImplement userServiceImplement;
    @Autowired
    CartServiceImpl cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductServiceImpl productService;
    @Override
    public String addOrder(String createTime, String id) throws NotFoundException {
        UserEntity userEntity=userServiceImplement.getUserByEmail(jwtAuthFilter.getUser());
        List<Products> productsInCart =cartService.productsListByUser(userEntity);
        Orders order= Orders.builder().user(userEntity)
                .productsList(productsInCart)
                .price(cartService.cartAmount())
                .time(createTime)
                .transactionId(id)
                .build();
        orderRepository.save(order);
        productService.productQuantityUpdate(productsInCart);
        return "order has been placed";
    }

    @Override
    public List<Orders> allOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Orders> userOrders() throws NotFoundException {
        List<Orders> allOrderByUser = orderRepository.findAllByUser(userServiceImplement.getUserByEmail(jwtAuthFilter.getUser()));
        if (allOrderByUser==null) throw new NotFoundException( "No products for user "+jwtAuthFilter.getUser());
        return allOrderByUser;
    }
}
