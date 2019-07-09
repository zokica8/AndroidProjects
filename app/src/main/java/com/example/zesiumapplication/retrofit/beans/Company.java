
package com.example.zesiumapplication.retrofit.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Set;

public class Company {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("companyName")
    @Expose
    private String companyName;
    @SerializedName("organization_number")
    @Expose
    private String organizationNumber;
    @SerializedName("invoice_address")
    @Expose
    private String invoiceAddress;
    @SerializedName("users")
    @Expose
    private Set<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public String getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(String invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
