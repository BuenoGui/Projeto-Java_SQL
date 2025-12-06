package br.com.rpires.domain;

public class Produto {

    private Long id;
    private String product_desc;
    private Long product_price;

    public void setId(Long id) {
        this.id = id;
    }
    public String getProduct_desc() {
        return product_desc;
    }
    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }
    public Long getProduct_price() {
        return product_price;
    }
    public void setProduct_price(Long product_price) {
        this.product_price = product_price;
    }
    public Long getId() {
        return id;
    }

    

}
