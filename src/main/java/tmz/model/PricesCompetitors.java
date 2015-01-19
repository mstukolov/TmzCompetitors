package tmz.model;


import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

import javax.persistence.*;
import java.util.Date;

@Entity
@Indexed
@Table(name = "prices")
public class PricesCompetitors {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "competitor", nullable = false)
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES)
    private String competitor;

    @Column(name = "SCU", nullable = false)
    @Field(index = Index.YES, store = Store.NO, analyze = Analyze.YES)
    private String scu;

    @Column(name = "priceDate", nullable = false)
    private Date priceDate;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "priceFirst", nullable = false)
    private Integer priceFirst;


    public PricesCompetitors() {
    }

    public PricesCompetitors(String competitor,
                             String scu,
                             Date priceDate,
                             Integer price) {
        this.competitor = competitor;
        this.scu = scu;
        this.priceDate = priceDate;
        this.price = price;
    }

    public PricesCompetitors(String competitor,
                             String scu,
                             Date priceDate,
                             Integer price,
                             Integer priceFirst) {
        this.competitor = competitor;
        this.scu = scu;
        this.priceDate = priceDate;
        this.price = price;
        this.priceFirst = priceFirst;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScu() {
        return scu;
    }

    public void setScu(String scu) {
        this.scu = scu;
    }

    public Date getPriceDate() {
        return priceDate;
    }

    public void setPriceDate(Date priceDate) {
        this.priceDate = priceDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getCompetitor() {
        return competitor;
    }

    public void setCompetitor(String competitor) {
        this.competitor = competitor;
    }

    public Integer getPriceFirst() {
        return priceFirst;
    }

    public void setPriceFirst(Integer priceFirst) {
        this.priceFirst = priceFirst;
    }
}
