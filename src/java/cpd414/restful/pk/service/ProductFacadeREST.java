/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpd414.restful.pk.service;

import cpd414.restful.pk.Product;
import java.util.List;
import javax.ejb.Singleton;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Hongliang Zhang
 */
@Stateless
@Path("products")
public class ProductFacadeREST extends AbstractFacade<Product> {
    @PersistenceContext(unitName = "WebForRestfulPU")
    private EntityManager em;

    public ProductFacadeREST() {
        super(Product.class);
    }

    @POST
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Product createWithReturn(Product entity) {
        super.create(entity);
        
        return entity;
       
        
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    public Product editWithReturn(@PathParam("id") Integer id, Product entity) {
    //   if (super.find(id) == null)  Response.status(500).build();
       entity.setProductID(id);
       super.edit(entity);
       return entity;
        
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
       // if (super.find(id) == null)  Response.status(500).build();
        
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/json"})
    public Product find(@PathParam("id") Integer id) {
        Product p = super.find(id);
       
        
        return p;
    }
 
    @GET
    @Override
    @Produces({ "application/json"})
    public List<Product> findAll() {
        return super.findAll();
    }
 /*
    @GET
    @Path("{from}/{to}")
    @Produces({"application/json"})
    public List<Product> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }
    */
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
