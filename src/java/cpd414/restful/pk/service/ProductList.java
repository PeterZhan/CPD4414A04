/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpd414.restful.pk.service;

import com.mysql.jdbc.Connection;
import cpd414.restful.pk.Product;
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Hongliang Zhang
 */
@Singleton
public class ProductList {// extends AbstractFacade<Product>{

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private HashMap<Integer, Product> productList = new HashMap();
  //  private List<Product> plist;
    //  @PersistenceContext(unitName = "WebForRestfulPU")
//   private EntityManager em;
    private Connection connection = null;

    public ProductList() {

      //   super(Product.class);
        //   intializeit(); 
        connection = getConnection();

        try {

            // processRequest(request, response);
            String query = "SELECT * FROM product";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(query);
            String outStr = "[";
            while (rs.next()) {
                Product p = new Product(rs.getInt("ProductID"), rs.getString("Name"), rs.getString("Description"), rs.getInt("Quantity"));
                //  plist.add(p);
                productList.put(p.getProductID(), p);

            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    protected void finalize() throws Throwable {

        if (connection != null) {
            try {
                connection.close();
                connection = null;

            } catch (SQLException ex) {
                Logger.getLogger(ProductList.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

        super.finalize(); //To change body of generated methods, choose Tools | Templates.
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found exception! " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost:3306/cpd4414assign3";
        try {
            connection = (Connection) DriverManager.getConnection(url,
                    "usertest", "123456");
        } catch (SQLException e) {
            System.out.println("Failed to Connect! " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /* 
     public void intializeit()
     {
        
     plist = super.findAll();
        
     for (Product p : plist)
     {
            
     productList.put(p.getProductID(), p);
            
     } 
        
        
     }
     */
    /*
     @Override
     protected EntityManager getEntityManager() {
     return em; //To change body of generated methods, choose Tools | Templates.
     }
    
     public void add(Product product)
     {
     if (!productList.containsKey(product.getProductID()))
     {
     em.persist(product);
     productList.put(product.getProductID(), product);
           
     }
     }
     */
    public void remove(Product product) {
        if (productList.containsKey(product.getProductID())) {

            try {
                //  super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.

                int productid = product.getProductID();

                String query = "delete from product  WHERE ProductID = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                System.out.println(query);

                pstmt.setInt(1, productid);

                int updates = pstmt.executeUpdate();

            } catch (Exception ex) {
                Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

            // super.remove(product);
            productList.remove(product.getProductID());

        }

    }

    public void remove(int id) {
        if (productList.containsKey(id)) {

            try {
          //  super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.

                String query = "delete from product  WHERE ProductID = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                System.out.println(query);

                pstmt.setInt(1, id);

                int updates = pstmt.executeUpdate();

            } catch (Exception ex) {
                Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

            // super.remove(product);
            productList.remove(id);

        }

    }

    public void add(Product p) {

        try {
          //  super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.

            String query = "insert into product(Name, Description, Quantity) values(?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            System.out.println(query);

            pstmt.setString(1, p.getName());
            pstmt.setString(2, p.getDescription());
            pstmt.setInt(3, p.getQuantity());

            int updates = pstmt.executeUpdate();

          if (updates > 0)
          {
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int productid = rs.getInt(1);
                productList.put(productid, p);
                
            }
            
          }

        } catch (Exception ex) {
            Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }

    }

    public void set(int id, Product product) {
        if (productList.containsKey(id)) {

            try {
                //  super.doPut(req, resp); //To change body of generated methods, choose Tools | Templates.

                int productid = id;

                String query = "update product set Name=?, Description=?, Quantity=?   WHERE ProductID = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                System.out.println(query);

                pstmt.setString(1, product.getName());
                pstmt.setString(2, product.getDescription());
                pstmt.setInt(3, product.getQuantity());
                pstmt.setInt(4, id);

                int updates = pstmt.executeUpdate();

            } catch (Exception ex) {
                Logger.getLogger(ProductList.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }

            productList.put(id, product);

        }
    }

    public Product get(int id) {
        return productList.get(id);
    }

    public JsonArray toJSON() {
        JsonArray jsonArray = (JsonArray) Json.createArrayBuilder();

        for (Product p : productList.values()) {
            JsonObjectBuilder jBuilder = Json.createObjectBuilder();
            jBuilder.add("ProductID", p.getProductID());
            jBuilder.add("Name", p.getName());
            jBuilder.add("Quantity", p.getQuantity());
            jBuilder.add("Description", p.getDescription());
            jsonArray.add(jBuilder.build());

        }

        return jsonArray;

    }

    /* public void persist(Object object) {
     em.persist(object);
     }
    
     */
}
