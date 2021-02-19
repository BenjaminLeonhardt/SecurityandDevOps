package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController = mock(ItemController.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);


    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);

        Item item = new Item();
        BigDecimal bigDecimal = new BigDecimal(2.99);
        item.setPrice(bigDecimal);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");

        List<Item> itemListRoundWidget= new ArrayList<>();
        itemListRoundWidget.add(item);

        Item item2 = new Item();
        BigDecimal bigDecimal2 = new BigDecimal(1.99);
        item2.setPrice(bigDecimal2);
        item2.setName("Square Widget");
        item2.setDescription("A widget that is square");
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);
        itemList.add(item2);

        Optional<Item> optionalItem;


        when(itemRepository.findAll()).thenReturn(itemList);
        when(itemRepository.findByName("Round Widget")).thenReturn(itemListRoundWidget);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));

    }

    @Test
    public void getItemsTest() {
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItems();
        assertEquals(200, listResponseEntity.getStatusCodeValue());
        List<Item> itemList = listResponseEntity.getBody();

        assertNotNull(itemList);
        assertEquals(2,itemList.size());

        assertEquals("Round Widget",itemList.get(0).getName());
        assertEquals(2.99,itemList.get(0).getPrice().doubleValue(),0);
        assertEquals("A widget that is round",itemList.get(0).getDescription());

        assertEquals("Square Widget",itemList.get(1).getName());
        assertEquals(1.99,itemList.get(1).getPrice().doubleValue(),0);
        assertEquals("A widget that is square",itemList.get(1).getDescription());
    }

    @Test
    public void getItemByNameTest() {
        ResponseEntity<List<Item>> listResponseEntity = itemController.getItemsByName("Round Widget");
        assertEquals(200, listResponseEntity.getStatusCodeValue());
        List<Item> itemList = listResponseEntity.getBody();

        assertNotNull(itemList);
        assertEquals(1,itemList.size());

        assertEquals("Round Widget",itemList.get(0).getName());
        assertEquals(2.99,itemList.get(0).getPrice().doubleValue(),0);
        assertEquals("A widget that is round",itemList.get(0).getDescription());

    }

    @Test
    public void getItemByIdTest() {
        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);
        assertEquals(200, itemResponseEntity.getStatusCodeValue());
        Item item = itemResponseEntity.getBody();

        assertNotNull(item);

        assertEquals("Round Widget",item.getName());
        assertEquals(2.99,item.getPrice().doubleValue(),0);
        assertEquals("A widget that is round",item.getDescription());
    }

}
