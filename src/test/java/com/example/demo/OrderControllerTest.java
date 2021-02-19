package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController = mock(OrderController.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);



    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        Cart cart = new Cart();

        Item item = new Item();
        BigDecimal bigDecimal = new BigDecimal(2.99);
        item.setPrice(bigDecimal);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setId(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        cart.setItems(itemList);
        cart.setTotal(bigDecimal);
        cart.setId(1L);

        User user = new User();
        user.setId(0);
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setCart(cart);

        cart.setUser(user);

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        ArrayList<UserOrder> userOrderArrayList= new ArrayList<>();
        userOrderArrayList.add( UserOrder.createFromCart(user.getCart()));
        when(orderRepository.findByUser(user)).thenReturn(userOrderArrayList);

    }


    @Test
    public void submitTest(){
        ResponseEntity<UserOrder> responseEntity = orderController.submit("testUser");
        assertEquals(200, responseEntity.getStatusCodeValue());
        UserOrder userOrder = responseEntity.getBody();

        Item item = new Item();
        BigDecimal bigDecimal = new BigDecimal(2.99);
        item.setPrice(bigDecimal);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setId(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        assertEquals(userOrder.getUser().getUsername(),"testUser");
        assertEquals(userOrder.getItems().get(0).getName(), itemList.get(0).getName());
        assertEquals(userOrder.getItems().get(0).getDescription(), itemList.get(0).getDescription());
        assertEquals(userOrder.getItems().get(0).getPrice(), itemList.get(0).getPrice());
        assertEquals(userOrder.getItems().get(0).getId(), itemList.get(0).getId());
    }

    @Test
    public void getOrdersForUserTest(){
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser("testUser");
        assertEquals(200, responseEntity.getStatusCodeValue());
        List<UserOrder> userOrderList = responseEntity.getBody();
        UserOrder userOrder = userOrderList.get(0);
        assertEquals(1,userOrderList.size());

        Item item = new Item();
        BigDecimal bigDecimal = new BigDecimal(2.99);
        item.setPrice(bigDecimal);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setId(1L);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        assertEquals(userOrder.getUser().getUsername(),"testUser");
        assertEquals(userOrder.getItems().get(0).getName(), itemList.get(0).getName());
        assertEquals(userOrder.getItems().get(0).getDescription(), itemList.get(0).getDescription());
        assertEquals(userOrder.getItems().get(0).getPrice(), itemList.get(0).getPrice());
        assertEquals(userOrder.getItems().get(0).getId(), itemList.get(0).getId());

    }


}
