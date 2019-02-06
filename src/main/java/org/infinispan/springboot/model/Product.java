package org.infinispan.springboot.model;

import org.infinispan.protostream.annotations.ProtoDoc;
import org.infinispan.protostream.annotations.ProtoField;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Product object
 */
@Entity
@ProtoDoc("@Indexed")
public class Product  implements java.io.Serializable {

	@Id
	private Long itemId;
	private String name;
	private String description;
	private double price;
	
	public Product() {}

	@ProtoDoc("@Field")
	@ProtoField(number = 1, required = true)
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@ProtoDoc("@Field")
	@ProtoField(number = 2, required = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ProtoDoc("@Field")
	@ProtoField(number = 3, required = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ProtoDoc("@Field")
	@ProtoField(number = 4, required = true)
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	} 
	
	
}
