/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package py.com.palermo.service.utils;


/**
 *
 * @author miguel.ojeda
 */
public class ResponseDTO<T> {
    
    private T model;    
    
    private Integer status;
    
    private String message;

    /**
     * @return the model
     */
    public T getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(T model) {
        this.model = model;
    }

    /**
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    
}
