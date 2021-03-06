package ro.teamnet.ou.web.rest.dto;

import ro.teamnet.bootstrap.web.rest.dto.AccountDTO;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class OrganizationalUnitDTO implements Serializable {

    private Long id;
    private String code;
    private String description;
    private Date validFrom;
    private Date validTo;
    private Boolean active;

    private PerspectiveDTO perspective;
    private OrganizationalUnitDTO parent;
    private Set<OrganizationalUnitDTO> children = new HashSet<>();


    private Set<AccountDTO> accounts = new HashSet<>();

    public OrganizationalUnitDTO(String code, String description, Date validFrom, Date validTo,
                                 Boolean active, Long id, PerspectiveDTO perspective, OrganizationalUnitDTO parent,
                                 Set<OrganizationalUnitDTO> children, Set<AccountDTO> accounts) {

        this.code = code;
        this.description = description;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.active = active;
        this.id = id;
        this.perspective = perspective;
        this.parent = parent;
        this.children = children;
        this.accounts = accounts;
    }

    public OrganizationalUnitDTO() {
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

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PerspectiveDTO getPerspective() {
        return perspective;
    }

    public void setPerspective(PerspectiveDTO perspective) {
        this.perspective = perspective;
    }

    public OrganizationalUnitDTO getParent() {
        return parent;
    }

    public void setParent(OrganizationalUnitDTO parent) {
        this.parent = parent;
    }

    public Set<OrganizationalUnitDTO> getChildren() {
        return children;
    }

    public void setChildren(Set<OrganizationalUnitDTO> children) {
        this.children = children;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<AccountDTO> accounts) {
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "OrganizationalUnitDTO{" +
                ", id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", active=" + active +
                ", perspective=" + perspective +
                ", parent=" + parent +
                ", children=" + children +
                ", accounts=" + accounts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationalUnitDTO that = (OrganizationalUnitDTO) o;

        if (!id.equals(that.id)) return false;
        return code.equals(that.code);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
