package com.cvut.fel.horovtom.data.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

/**
 * Created by Hermes235 on 15.4.2017.
 */
@Entity
public class Ingredient implements Serializable {
    private static final long serialVersionUID = -3172414008259627778L;
    @Id
    @GeneratedValue
    private int idIngredient;
    
    @Basic
    @Column(nullable = false, unique = true)
    private String name;
    
    @Basic
    @Column(nullable = false)
    private String unit;
    
    @Basic
    @Column(nullable = false)
    private BigDecimal pricePerUnit;
    
    @Basic
    @Column(nullable = false)
    private double amount;
    
    @Basic
    @Column(nullable = false)
    private Date expirationDate;
    
    public int getIdIngredient() {
        return idIngredient;
    }
    
    public void setIdIngredient(int idIngredient) {
        this.idIngredient = idIngredient;
    }
    
    public String getName() {
        return name;
    }
    
    @Nonnull
    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getUnit() {
        return unit;
    }
    
    @Nonnull
    public Ingredient setUnit(String unit) {
        this.unit = unit;
        return this;
    }
    
    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }
    
    @Nonnull
    public Ingredient setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
        return this;
    }
    
    public double getAmount() {
        return amount;
    }
    
    @Nonnull
    public Ingredient setAmount(double amount) {
        this.amount = amount;
        return this;
    }
    
    public Date getExpirationDate() {
        return expirationDate;
    }
    
    @Nonnull
    public Ingredient setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }
    
    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Ingredient that = (Ingredient) o;
        return Objects.equals(name, that.name);
        
    }
    
    @Override
    public int hashCode() {
        if (name == null) {
            return 0;
        }
        return name.hashCode();
    }
    
    @Nonnull
    @Override
    public String toString() {
        return "Ingredient{" + "idIngredient=" + idIngredient + ", name='" + name + '\'' + ", unit='" + unit + '\'' + ", pricePerUnit=" + pricePerUnit + ", "
               + "amount=" + amount + ", expirationDate=" + expirationDate + '}';
    }
}
