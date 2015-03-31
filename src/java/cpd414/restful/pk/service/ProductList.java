/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpd414.restful.pk.service;

import cpd414.restful.pk.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class ProductList extends AbstractFacade<Product>{

    @PersistenceContext(unitName = "WebForRestfulPU")
    private EntityManager em;
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    private HashMap<Integer, Product> productList  = new HashMap();
    private List<Product> plist;

    public ProductList() {
        super(Product.class);
        plist = super.findAll();
        
        for (Product p : plist)
        {
            
          productList.put(p.getProductID(), p);
            
        }
        
    }  

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
    
    public void remove(Product product)
    {
        if (productList.containsKey(product.getProductID()))
        {
            super.remove(product);
            productList.remove(product.getProductID());
           
        }
        
    }
   
   public void remove(int id)
   {
       if (productList.containsKey(id))
        {
           super.remove(productList.get(id));
           productList.remove(id);
           
        }
       
   }
   
   public void set(int id, Product product)
   {
        if (!productList.containsKey(id))
        {
            em.persist(product);
            productList.put(id, product);
           
        }
   }
  
   public Product get(int id)
   {
     return productList.get(id);
   }
   
   public JsonArray toJSON()
   {
      JsonArray jsonArray = (JsonArray) Json.createArrayBuilder();
      
 
      
     for(Product p : plist) {
        JsonObjectBuilder jBuilder = Json.createObjectBuilder();
        jBuilder.add("ProductID", p.getProductID());
        jBuilder.add("Name", p.getName());
        jBuilder.add("Quantity", p.getQuantity());
        jBuilder.add("Description", p.getDescription());
        jsonArray.add(jBuilder.build());
         
        
    }
    
    return jsonArray;
      
      
    
      
   }
    
   
    
    
}
