package ro.teamnet.ou.domain.jpa;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.atmosphere.cpr.Action;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by ionut.patrascu on 04.08.2015.
 */
@Entity
@Table(name="T_PERSPECTIVE")
public class Perspective implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name = "CODE", length = 100, unique = true)
    private String code;

    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToOne
    @JoinColumn(name = "ORGANIZATION_ID", referencedColumnName = "ID")
    private Organization organization;


    @OneToMany(mappedBy = "perspective")
    @JsonManagedReference
    private Set<OrganizationalUnit> organizationalUnits = new TreeSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Set<OrganizationalUnit> getOrganizationalUnits() {
        return organizationalUnits;
    }

    public void setOrganizationalUnits(Set<OrganizationalUnit> organizationalUnits) {
        this.organizationalUnits = organizationalUnits;
    }
}
