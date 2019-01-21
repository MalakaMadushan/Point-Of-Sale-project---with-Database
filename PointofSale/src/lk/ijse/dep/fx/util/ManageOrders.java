package lk.ijse.dep.fx.util;

import lk.ijse.dep.fx.DB.DatabaseConnection;
import lk.ijse.dep.fx.model.Item;
import lk.ijse.dep.fx.model.Order;
import lk.ijse.dep.fx.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ManageOrders {

   // private static ArrayList<Order> ordersDB = new ArrayList<>();



//    public static void setOrdersDB(ArrayList<Order> orders){
//        ordersDB  = orders;
//    }

    // Dummy

//    static{
//        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
//        orderDetails.add(new OrderDetail("I001","Mouse",10,250));
//        orderDetails.add(new OrderDetail("I002","Keyboard",10,350));
//        ordersDB.add(new Order("1", LocalDate.now(),"C001",orderDetails));
//    }
    public static ArrayList<Order> getOrdersDB() throws SQLException {
        ArrayList<Order> alorderArrayList = new ArrayList<>();

        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("SELECT *FROM orders");
        ResultSet rst = pstm.executeQuery();

        while (rst.next()){
            String order_id = rst.getString(1);
            LocalDate date = LocalDate.parse(rst.getString(2));
            String cus_id = rst.getString(3);


            PreparedStatement pstm2 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM order_details WHERE  order_id=?");
            pstm2.setObject(1,order_id);
            ResultSet rst2 = pstm2.executeQuery();


            ArrayList<OrderDetail> alorderDetails = new ArrayList<>();

            while (rst2.next()){
                String code = rst2.getString(5);
                String description = rst2.getString(1);
                int qty = Integer.parseInt(rst2.getString(3));
                double unitprice = Double.parseDouble(rst2.getString(2));
                OrderDetail orderDetail = new OrderDetail(code, description, qty, unitprice);
                alorderDetails.add(orderDetail);

            }
            Order order = new Order(order_id,date,cus_id,alorderDetails);
            alorderArrayList.add(order);
        }
        return alorderArrayList;
}



    public static String generateOrderId() throws SQLException {
        return getOrdersDB().size() + 1 + "";
    }

    public static void createOrder(Order order) throws SQLException {
        PreparedStatement pstm = DatabaseConnection.getConnection().prepareStatement("INSERT INTO orders VALUES (?,?,?)");
        pstm.setObject(1,order.getId());
        pstm.setObject(2,order.getDate());
        pstm.setObject(3,order.getCustomerId());
        pstm.executeUpdate();

        PreparedStatement pstm2 = DatabaseConnection.getConnection().prepareStatement("INSERT  INTO  order_details VALUES (?,?,?,?,?)");
        for (OrderDetail orderDetail : order.getOrderDetails() ) {

            pstm2.setObject(1,orderDetail.getDescription());
            pstm2.setObject(2,orderDetail.getUnitPrice());
            pstm2.setObject(3,orderDetail.getQty());
            pstm2.setObject(4,order.getId());
            pstm2.setObject(5,orderDetail.getCode());

            pstm2.executeUpdate();

            int qtyOnhand =ManageItems.findItem(orderDetail.getCode()).getQtyOnHand();
            qtyOnhand -=orderDetail.getQty();

            PreparedStatement pstm3 = DatabaseConnection.getConnection().prepareStatement("UPDATE item SET quantity=? WHERE item_code=?");
            pstm3.setObject(1,qtyOnhand);
            pstm3.setObject(2,orderDetail.getCode());
            pstm3.executeUpdate();


        }


    }

    public static Order findOrder(String orderId) throws SQLException {
        for (Order order : getOrdersDB()) {
            if(order.getId().equals(orderId)){
                return order;
            }

        }
        return null;
        }
}
