package lk.ijse.dep.fx.util;

import lk.ijse.dep.fx.DB.DatabaseConnection;
import lk.ijse.dep.fx.model.Customer;
import lk.ijse.dep.fx.view.util.CustomerTM;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageCustomers {

    // Database
//    private static ArrayList<Customer> customersDB = new ArrayList<>();
//
//    public static ArrayList<Customer> getCustomersDB(){
//        return customersDB;
//    }

//    public static void setCustomersDB(ArrayList<Customer> customers){
//        customersDB = customers;
//    }

//    // Dummy Data
//    static{
//        customersDB.add(new Customer("C001","Kasun","Galle"));
//        customersDB.add(new Customer("C002","Ranga","Panadura"));
//        customersDB.add(new Customer("C003","Nuwan","Kagalle"));
//        customersDB.add(new Customer("C004","Kasun","Matara"));
//    }
    public static ArrayList<Customer> getCustomerDB() throws SQLException {
        ArrayList<Customer> customerArrayList = new ArrayList<>();
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("SELECT *FROM customer");
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()){
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            Customer customerArray = new Customer(id, name, address);

            customerArrayList.add(customerArray);
        }return customerArrayList;
    }

    public static void createCustomer(String customerID,String customerName, String customerAddress) throws SQLException {
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("INSERT INTO customer VALUES (?,?,?)");
            pstm.setObject(1,customerID);
            pstm.setObject(2,customerID);
            pstm.setObject(3,customerAddress);

            pstm.executeUpdate();
    }

    public static void updateCustomer(String customerID,String customerName,String customerAddress) throws SQLException {
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("UPDATE customer SET cus_name=?,cus_address=? WHERE cus_id=?");
        pstm.setObject(1,customerName);
        pstm.setObject(2,customerAddress);
        pstm.setObject(3,customerID);

        pstm.executeUpdate();
    }

    public static void deleteCustomer(CustomerTM customerTM) throws SQLException {

        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("DELETE FROM customer WHERE cus_id=?");
        pstm.setObject(1,customerTM.getId());

        int affectRow = pstm.executeUpdate();

    }

    public static Customer findCustomer(String txtid) throws SQLException {
        for (Customer customer : getCustomerDB()) {
            if(customer.getId().equals(txtid)){
                return customer;
            }

        }return null;

        }

    }



