package lk.ijse.dep.fx.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.dep.fx.DB.DatabaseConnection;
import lk.ijse.dep.fx.model.Order;
import lk.ijse.dep.fx.model.OrderDetail;
import lk.ijse.dep.fx.util.ManageCustomers;
import lk.ijse.dep.fx.util.ManageItems;
import lk.ijse.dep.fx.util.ManageOrders;
import lk.ijse.dep.fx.view.util.OrderDetailTM;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewOrderFormController {
    public JFXButton btnReport;
    @FXML
    private JFXDatePicker txtOrderDate;
    @FXML
    private JFXTextField txtOrderID;
    @FXML
    private JFXTextField txtCustomerID;
    @FXML
    private JFXTextField txtItemCode;
    @FXML
    private JFXTextField txtCustomerName;
    @FXML
    private JFXTextField txtDescription;
    @FXML
    private JFXTextField txtQtyOnHand;
    @FXML
    private JFXTextField txtUnitPrice;
    @FXML
    private JFXTextField txtQty;
    @FXML
    private TableView<OrderDetailTM> tblOrderDetails;
    @FXML
    private Label lblTotal;

    private String orderId;

    @FXML
    private void navigateToMain(MouseEvent mouseEvent) throws IOException {

        Parent root = FXMLLoader.load(this.getClass().getResource("/lk/ijse/dep/fx/view/SearchOrderForm.fxml"));
        Scene mainScene = new Scene(root);
        Stage mainStage = (Stage) lblTotal.getScene().getWindow();
        mainStage.setScene(mainScene);

        TranslateTransition tt1 = new TranslateTransition(Duration.millis(300), root.lookup("AnchorPane"));
        tt1.setToX(0);
        tt1.setFromX(-mainScene.getWidth());
        tt1.play();

        mainStage.centerOnScreen();
    }

    public void initialize() {
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderDetailTM> observable, OrderDetailTM oldValue, OrderDetailTM selectedOrderDetail) {

                if (selectedOrderDetail == null) {
                    // Clear Selection
                    return;
                }

                txtItemCode.setText(selectedOrderDetail.getCode());
                txtDescription.setText(selectedOrderDetail.getDescription());
                txtUnitPrice.setText(selectedOrderDetail.getUnitPrice() + "");
                txtQty.setText(selectedOrderDetail.getQty() + "");
                try {
                    txtQtyOnHand.setText(ManageItems.findItem(txtItemCode.getText()).getQtyOnHand() + "");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void setInitData(String orderId, double orderTotal) throws SQLException {
        this.orderId = orderId;
        lblTotal.setText("Total : " + orderTotal);
        fillData();
    }

    public void fillData() throws SQLException {
        Order order = ManageOrders.findOrder(this.orderId);
        txtCustomerID.setText(order.getCustomerId());
        txtOrderID.setText(order.getId());
        txtOrderDate.setValue(order.getDate());
        txtCustomerName.setText(ManageCustomers.findCustomer(order.getCustomerId()).getName());

        ArrayList<OrderDetail> orderDetails = order.getOrderDetails();
        ObservableList<OrderDetailTM> details = FXCollections.observableArrayList();

        for (OrderDetail orderDetail : orderDetails) {
            details.add(new OrderDetailTM(orderDetail.getCode(),
                    orderDetail.getDescription(),
                    orderDetail.getQty(),
                    orderDetail.getUnitPrice(),
                    orderDetail.getQty() * orderDetail.getUnitPrice()));
        }
        tblOrderDetails.setItems(details);
    }

    public void btnReport_OnAction(ActionEvent actionEvent) throws JRException, SQLException {
        File file = new File("JasperReports/PosBill.jasper");
        JasperReport compilereport = (JasperReport) JRLoader.loadObject(file);

        HashMap<String, Object> para = new HashMap<>();
        para.put("id",txtOrderID.getText());
        para.put("date",txtOrderDate.getValue());
        para.put("cus_id",txtCustomerID.getText());
        para.put("cus_name",txtCustomerName.getText());


        JasperPrint fillReport = JasperFillManager.fillReport(compilereport, para, DatabaseConnection.getConnection());
        JasperViewer.viewReport(fillReport,false);
    }
}
