package eu.cartsc.jhipapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

import eu.cartsc.jhipapp.domain.enumeration.Statoauto;

/**
 * A Auto.
 */
@Entity
@Table(name = "auto")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Auto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "([a-zA-Z]{2}[0-9]{3,4}[a-zA-Z]{2})")
    @Column(name = "targa", nullable = false)
    private String targa;

    @NotNull
    @Column(name = "datainserimento", nullable = false)
    private ZonedDateTime datainserimento;

    @Column(name = "lat")
    private Float lat;

    @Column(name = "lng")
    private Float lng;

    @Enumerated(EnumType.STRING)
    @Column(name = "stato")
    private Statoauto stato;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTarga() {
        return targa;
    }

    public Auto targa(String targa) {
        this.targa = targa;
        return this;
    }

    public void setTarga(String targa) {
        this.targa = targa;
    }

    public ZonedDateTime getDatainserimento() {
        return datainserimento;
    }

    public Auto datainserimento(ZonedDateTime datainserimento) {
        this.datainserimento = datainserimento;
        return this;
    }

    public void setDatainserimento(ZonedDateTime datainserimento) {
        this.datainserimento = datainserimento;
    }

    public Float getLat() {
        return lat;
    }

    public Auto lat(Float lat) {
        this.lat = lat;
        return this;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public Auto lng(Float lng) {
        this.lng = lng;
        return this;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public Statoauto getStato() {
        return stato;
    }

    public Auto stato(Statoauto stato) {
        this.stato = stato;
        return this;
    }

    public void setStato(Statoauto stato) {
        this.stato = stato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Auto auto = (Auto) o;
        if (auto.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, auto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Auto{" +
            "id=" + id +
            ", targa='" + targa + "'" +
            ", datainserimento='" + datainserimento + "'" +
            ", lat='" + lat + "'" +
            ", lng='" + lng + "'" +
            ", stato='" + stato + "'" +
            '}';
    }
}
