package com.dikshya.inventorymanagementsystem;


import com.dikshya.inventorymanagementsystem.classes.API;
import com.dikshya.inventorymanagementsystem.classes.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeController {
    @FXML
    private TableView<Product> tableView;
    @FXML
    private TableColumn<Product, Integer> id;

    @FXML
    private TableColumn<Product, String> name;

    @FXML
    private TableColumn<Product, Integer> quantity;
    @FXML
    private TableColumn<Product, String> description;

    @FXML
    private TextField productName;

    @FXML
    private TextArea productDescription;

    @FXML
    private TextField productQuantity;

    @FXML
    private Label responseLabel;


    private final ObservableList<Product> productList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

        productQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                productQuantity.setText(oldValue);
            }
        });

        id.setCellValueFactory(new PropertyValueFactory<Product,Integer>("id"));
        name.setCellValueFactory(new PropertyValueFactory<Product,String>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<Product,Integer>("quantity"));
        description.setCellValueFactory(new PropertyValueFactory<Product,String>("description"));

        tableView.setItems(productList);

        // Fetch products and populate the table
        try {
            String json = API.getData("http://localhost:8080/inventorymanagement/api/products","GET");
            List<Product> products = parseProducts(json);
            productList.addAll(products);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private List<Product> parseProducts(String json) {
        JSONArray jsonArray = new JSONArray(json);
        List<Product> products = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Product product = new Product(jsonObject.getInt("id"),jsonObject.getString("name"),jsonObject.getInt("quantity"),jsonObject.getString("description"));
            products.add(product);
        }

        return products;
    }
    @FXML
    private void addProduct() {
        String productName = this.productName.getText();
        String productDescription = this.productDescription.getText();
        String productQuantity = this.productQuantity.getText();
        try{
            String postData = "name=" + productName+ "&quantity="+productQuantity + "&description="+ productDescription;

            String response = API.sendPost("http://localhost:8080/inventorymanagement/api/products",postData);
            if(response.equals("product_created_successfully")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Create Product");
                alert.setHeaderText(null);
                alert.setContentText("Product created successfully");
                alert.showAndWait();
                this.productName.clear();
                this.productQuantity.clear();
                this.productDescription.clear();
                refreshTable();
            }else{
                responseLabel.setText(response);
                responseLabel.setStyle("-fx-text-fill: red");
            }
        }catch (Exception e){
            e.printStackTrace();
            responseLabel.setText("Error while creating product");
        }

    }

    private void refreshTable() {
        try {
            String json = API.getData("http://localhost:8080/inventorymanagement/api/products","GET");
            List<Product> products = parseProducts(json);

            // Clear old data and add new data
            productList.clear();
            productList.addAll(products);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
