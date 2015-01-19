package tmz.model;

import org.hibernate.search.annotations.*;

import javax.persistence.*;
import java.util.Date;


/**
 * Created by Stukolov on 13.12.2014.
 */
@Entity
@Indexed
@Table(name = "inventtable")
public class InventTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer recid;

    @Column(name = "scu", nullable = false)
    @Field(index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES)
    private String scu;

    @Column(name = "competitor", nullable = false)
    @Field(index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES)
    private String competitor;

    @Column(name = "category", nullable = false)
    @Field(index = org.hibernate.search.annotations.Index.YES, store = Store.NO, analyze = Analyze.YES)
    private String category;

    @Column(name = "kindShoes", nullable = false)
    private String kindshoes;

    @Column(name = "upperMaterial", nullable = false)
    private String upperMaterial;

    @Column(name = "lining", nullable = false)
    private String lining;

    @Column(name = "context", nullable = false)
    private String context;

    @Column(name = "season", nullable = false)
    private String season;

    @Column(name = "sole", nullable = false)
    private String sole;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "createddate", nullable = false)
    private Date createdDate;

    public InventTable() {
    }

    public InventTable(String scu,
                       String competitor,
                       String category,
                       String kindshoes,
                       String upperMaterial,
                       String lining,
                       String context,
                       String season,
                       String sole,
                       String country,
                       Date createdDate) {
        this.scu = scu;
        this.competitor = competitor;
        this.category = category;
        this.kindshoes = kindshoes;
        this.upperMaterial = upperMaterial;
        this.lining = lining;
        this.context = context;
        this.season = season;
        this.sole = sole;
        this.country = country;
        this.createdDate = createdDate;
    }



    public Integer getRecid() {
        return recid;
    }

    public void setRecid(Integer recid) {
        this.recid = recid;
    }

    public String getScu() {
        return scu;
    }

    public void setScu(String scu) {
        this.scu = scu;
    }

    public String getCompetitor() {
        return competitor;
    }

    public void setCompetitor(String competitor) {
        this.competitor = competitor;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKindshoes() {
        return kindshoes;
    }

    public void setKindshoes(String kindshoes) {
        this.kindshoes = kindshoes;
    }

    public String getUpperMaterial() {
        return upperMaterial;
    }

    public void setUpperMaterial(String upperMaterial) {
        this.upperMaterial = upperMaterial;
    }

    public String getLining() {
        return lining;
    }

    public void setLining(String lining) {
        this.lining = lining;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSole() {
        return sole;
    }

    public void setSole(String sole) {
        this.sole = sole;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
