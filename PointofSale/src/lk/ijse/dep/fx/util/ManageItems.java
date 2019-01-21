package lk.ijse.dep.fx.util;

import lk.ijse.dep.fx.DB.DatabaseConnection;
import lk.ijse.dep.fx.model.Item;
import lk.ijse.dep.fx.view.util.ItemTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageItems {

    // Database
    //private static ArrayList<Item> itemsDB = new ArrayList<>();

    public static ArrayList<Item> getItemsDB() throws SQLException {
        ArrayList<Item> itemArrayList = new ArrayList<>();
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("SELECT *FROM item");
        ResultSet rst = pstm.executeQuery();
        while (rst.next()){
            String code = rst.getString(1);
            String description = rst.getString(2);
            double unitprice = Double.parseDouble(rst.getString(3));
            int quantity = Integer.parseInt(rst.getString(4));

            Item itemarray = new Item(code, description, unitprice, quantity);
            itemArrayList.add(itemarray);
        }

        return itemArrayList;
    }

//    public static void setItemsDB(ArrayList<Item> items){
//        itemsDB = items;
//    }

    // Dummy Data
//    static{
//        itemsDB.add(new Item("I001","Mouse",250,50));
//        itemsDB.add(new Item("I002","Keyboard",350,50));
//        itemsDB.add(new Item("I003","Monitors",5500,50));
//        itemsDB.add(new Item("I004","Subwoofers",3500,50));
//    }

    public static void createItem(String code,String description,double unitprice,int quantity) throws SQLException {
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("INSERT INTO item VALUES (?,?,?,?)");
        pstm.setObject(1,code);
        pstm.setObject(2,description);
        pstm.setObject(3,unitprice);
        pstm.setObject(4,quantity);

        pstm.executeUpdate();

    }

    public static void updateItem(String code,String description ,double unitprice ,int quantity) throws SQLException {
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("UPDATE item SET description=?, unitprice=?,quantity=? WHERE item_code=?");
        pstm.setObject(4,code);
        pstm.setObject(1,description);
        pstm.setObject(2,unitprice);
        pstm.setObject(3,quantity);

        pstm.executeUpdate();
    }

    public static void deleteItem(ItemTM itemTM) throws SQLException {
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("DELETE FROM customer WHERE cus_id=?");
        pstm.setObject(1,itemTM.getCode());

        int affectRow = pstm.executeUpdate();
    }

    public static Item findItem(String itemCode) throws SQLException {
        for (Item item : getItemsDB()) {
            if(item.getCode().equals(itemCode)){
                return item;
            }

        }return null;

        }

    }

