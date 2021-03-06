/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpd414.restful.pk.service;

import cpd414.restful.pk.Product;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author Hongliang Zhang
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/Queue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})

public class ProductListener implements MessageListener {
    @EJB
    private ProductList productList;
    
    public ProductListener() {
       
        
    }
    
    @Override
    public void onMessage(Message message) {
        
        if (!(message instanceof TextMessage)) return;
        try {
            String m = ((TextMessage)message).getText();
            
            
            JsonReader jsonReader = Json.createReader(new StringReader(m));
            JsonObject json = jsonReader.readObject();
            Product p = new Product();
            p.setName(json.getString("Name"));
            p.setQuantity(json.getInt("Quantity"));
            p.setDescription("Description");
            
            
            productList.add(p);
            
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(ProductListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
