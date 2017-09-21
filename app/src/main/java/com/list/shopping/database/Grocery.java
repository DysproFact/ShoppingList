package com.list.shopping.database;

import java.util.List;

/**
 * Created by Mathieu on 21/09/2017.
 */

public class Grocery {
    public String id, user_id, item_id, creation_date, shop_date;
    public User user;
    public List<Item> T_items;
}
