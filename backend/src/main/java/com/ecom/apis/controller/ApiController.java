package com.ecom.apis.controller;

import com.ecom.apis.Jwt.JwtService;
import com.ecom.apis.entity.Orders;
import com.ecom.apis.entity.Products;
import com.ecom.apis.entity.UserEntity;
import com.ecom.apis.exceptionHandling.exceptions.NotFoundException;
import com.ecom.apis.model.ForgetPass;
import com.ecom.apis.model.ImageModel;
import com.ecom.apis.model.Login;
import com.ecom.apis.model.OTP;
import com.ecom.apis.service.cart.CartService;
import com.ecom.apis.service.order.OrderService;
import com.ecom.apis.service.otp.OtpServiceInterface;
import com.ecom.apis.service.paypal.PaypalServiceImpl;
import com.ecom.apis.service.product.ProductService;
import com.ecom.apis.service.user.UserServiceInterface;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:8080/")
@Validated
@RestController
public class ApiController {
    @Autowired
    private UserServiceInterface userServiceInterface;
    @Autowired
    private OtpServiceInterface otpServiceInterface;
    @Autowired
    private ProductService productService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CartService cartService;
    @Autowired
    private PaypalServiceImpl paypalService;
    @Autowired
    private OrderService orderService;

    @Value("${server.port}")
    private String server;

    @GetMapping("/test")
    public void test() throws NotFoundException {
        System.out.println("hello");
        userServiceInterface.remove(4000l);
    }

    @RequestMapping("/")
    public String welcome() {
        return "Welcome to Big-styles";
    }

    //tested
    @PostMapping("/registration")
    public String userRegistration(@RequestBody @Valid UserEntity userEntity) {
        return userServiceInterface.addUser(userEntity);
    }

    //working fine
    @GetMapping("/sendmail/{email}")
    public String sendOtp(@PathVariable("email") String email) {
        return otpServiceInterface.sendOtp(email);
    }

    //working fine
    @PostMapping("/verify")
    public String verify(@RequestBody @Valid OTP otp) throws NotFoundException {
        return otpServiceInterface.verify(otp);
    }

    //working fine
    @PostMapping("/authenticate")
    public String login(@RequestBody @Valid Login login) throws NotFoundException {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        if (authenticate.isAuthenticated())
            if (userServiceInterface.verified(login.getEmail())) return jwtService.generateToken(login.getEmail());
            else return "Not Verified";
        else throw new NotFoundException("Invalid Credentials");
    }

    //working fine
    @PostMapping("/forgetPass")
    public String forgetPass(@RequestBody @Valid ForgetPass otp) {
        return otpServiceInterface.forgetPass(otp);
    }

    @GetMapping("/products")
    public List<Products> allProducts() {
        return productService.allProducts();
    }

    //fine
    @GetMapping("/products/{id}")
    public Products productFromId(@PathVariable("id") Long productId) throws NoSuchElementException, NotFoundException {
        return productService.productFromId(productId);
    }

    @GetMapping("/productRating/{productId}")
    public Double getProductRating(@PathVariable("productId") Long productId) throws NoSuchElementException, NotFoundException {
        return productService.getProductRating(productId);
    }

    //fine
    @PostMapping("/admin/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminAdd(@RequestBody @Valid UserEntity userEntity) {
        return userServiceInterface.adminAdd(userEntity);
    }

    //fine
    @GetMapping("/admin/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserEntity> listOfAllUser(){
        return userServiceInterface.findAllUser();
    }

    //fine
    @DeleteMapping("/admin/remove/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String removeSeller(@PathVariable("id") Long id) throws NotFoundException {
        if (id == null) return "id is null";
        return userServiceInterface.remove(id);
    }



    @GetMapping("/admin/orders")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<Orders> getAllOrders(){
        return orderService.allOrders();
    }

    //fine
    @PostMapping("/seller/addProducts")
    @PreAuthorize("hasAuthority('SELLER')")
    public String addNewProduct(@RequestBody @Valid Products products) {
        return productService.addProducts(products);
    }

    //fine
    @GetMapping("/seller/products/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    public List<Products> sellerProducts(@PathVariable("id") Long id) {
        return productService.sellerProducts(id);
    }

    //fine
    @DeleteMapping("/seller/removeProduct/{id}")
    @PreAuthorize("hasAuthority('SELLER')")
    public String removeProduct(@PathVariable("id") Long id) throws NotFoundException {
        if (id == null) return "id is null";
        return productService.removeProduct(id);
    }


    //fine
    @PostMapping("/seller/addImage")
    @PreAuthorize("hasAuthority('SELLER')")
    public String addProductImage(@ModelAttribute @Valid ImageModel imageData) throws IOException, NotFoundException {
        return productService.addProductImage(imageData);
    }

    //fine
    @PutMapping("/seller/updateProduct/{productId}/{quantity}")
    @PreAuthorize("hasAuthority('SELLER')")
    public String updateProductQuantity(@PathVariable("productId") Long productId, @PathVariable("quantity") int quantity) throws  NotFoundException {
        return productService.updateProductQuantity(productId,quantity);
    }


    @GetMapping(value = "/viewImage/{id}")
    public List<ResponseEntity<byte[]>> viewImage(@PathVariable("id") Long id) throws NoSuchElementException, NotFoundException {
        List<byte[]> bytes = productService.showImage(id);
        List<ResponseEntity<byte[]>> responseEntities = new ArrayList<>();

        for (byte[] imageBytes : bytes) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            ResponseEntity<byte[]> responseEntity = ResponseEntity.ok()
                    .headers(headers)
                    .body(imageBytes);

            responseEntities.add(responseEntity);
        }
        return responseEntities;
    }



    //fine
    @GetMapping("/user/addToCart/{id}/{quantity}")
    @PreAuthorize("hasAuthority('USER')")
    public String addToCart(@PathVariable("id") String productID, @PathVariable("quantity") int quantity) throws NoSuchElementException, NotFoundException {
        return cartService.addToCart(productID, quantity);
    }

    //fine
    @PutMapping("/user/cartItem/{id}/{quantity}")
    @PreAuthorize("hasAuthority('USER')")
    public String updateCartItemQuantity(@PathVariable("id") String productID, @PathVariable("quantity") int quantity) throws  NotFoundException {
        return cartService.updateProductQuantity(productID, quantity);
    }

    //fine
    @GetMapping("/user/cartProducts")
    @PreAuthorize("hasAuthority('USER')")
    public Map<Products, Long> ListOfCartProducts() throws NotFoundException {
        return cartService.listOfProducts();
    }


    //fine
    @DeleteMapping("/user/removeCartItem/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteCartItem(@PathVariable("id") Long cartID) throws NoSuchElementException, NotFoundException {
        return cartService.deleteCartItem(cartID);
    }

    //fine
    @GetMapping("/user/rating/{productId}/{rating}")
    @PreAuthorize("hasAuthority('USER')")
    public String rateProduct(@PathVariable("productId") Long productId, @PathVariable("rating") Double rating) throws NotFoundException {
        return productService.addRating(productId, rating);
    }

    @PostMapping("/user/pay")
    @PreAuthorize("hasAuthority('USER')")
    public String payment() throws NotFoundException {
        cartService.validateCartQuantity();
        try {
            Double amount = cartService.cartAmount();
            Payment payment = paypalService.createPayment(amount, "http://localhost:" + server + "/user/pay/cancel", "http://localhost:" + server + "/user/pay/success");
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return "redirect:" + link.getHref();
                }
            }
        } catch (PayPalRESTException | NotFoundException e) {
            return e.getMessage();
        }
        return "redirect:/";
    }

    @GetMapping("/user/pay/cancel")
    @PreAuthorize("hasAuthority('USER')")
    public String cancelPay() {
        return "The initiated payment has been cancelled";
    }

    @GetMapping("/user/pay/success")
    @PreAuthorize("hasAuthority('USER')")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                return orderService.addOrder(payment.getCreateTime(), payment.getId());
            }
        } catch (PayPalRESTException | NotFoundException e) {
            return e.getMessage();
        }
        return "redirect:/";
    }

    @GetMapping("/user/orders")
    @PreAuthorize("hasAuthority('USER')")
    public List<Orders> userAllOrders() throws NotFoundException {
        return orderService.userOrders();
    }


}
