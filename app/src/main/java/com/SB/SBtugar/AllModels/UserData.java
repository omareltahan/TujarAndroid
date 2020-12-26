package com.SB.SBtugar.AllModels;

import android.net.Uri;

import com.SB.SBtugar.AllModels.Orders.SmallMetaDatum;
import com.SB.SBtugar.AllModels.SuperMarket.SuperMarket;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserData implements Serializable {
//    @SerializedName("promo_codes")
//    @Expose
//    private List<PromoCode> promo_codes;
@SerializedName("fav_markets")
@Expose
private List<SuperMarket> fav_markets;

    public List<SuperMarket> getFav_markets() {
        return fav_markets;
    }

    @SerializedName("blocked")
    @Expose
    private boolean Blocked = false;

    public void setBlocked(Boolean blocked) {
        Blocked = blocked;
    }

    public boolean isBlocked() {
        return Blocked;
    }

    @SerializedName("wallet")
    @Expose
    private String wallet;

    public Boolean getPayingCustomer() {
        return isPayingCustomer;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public void setPayingCustomer(Boolean payingCustomer) {
        isPayingCustomer = payingCustomer;
    }

    public String getWallet() {
        return wallet;
    }

//    public List<PromoCode> getPromo_codes() {
//        return promo_codes;
//    }
//
//    public void setPromo_codes(List<PromoCode> promo_codes) {
//        this.promo_codes = promo_codes;
//    }

//    @SerializedName("shopping_lists")
//    @Expose
//    private List<ShoppingList> shopping_list;
//
//    public List<ShoppingList> getShopping_list() {
//        return shopping_list;
//    }
//
//    public void setShopping_list(List<ShoppingList> shopping_list) {
//        this.shopping_list = shopping_list;
//    }
//
//    @SerializedName("fav_markets")
//    @Expose
//    private List<SuperMarket> fav_markets;
//
//    public List<SuperMarket> getFav_markets() {
//        return fav_markets;
//    }
//
//    public void setFav_markets(List<SuperMarket> fav_markets) {
//        this.fav_markets = fav_markets;
//    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("date_created_gmt")
    @Expose
    private String dateCreatedGmt;
    @SerializedName("date_modified")
    @Expose
    private Object dateModified;
    @SerializedName("date_modified_gmt")
    @Expose
    private Object dateModifiedGmt;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("billing")
    @Expose
    private Billing billing;
    @SerializedName("shipping")
    @Expose
    private Shipping shipping;
//
//    @SerializedName("alt_adresses")
//    @Expose
//    private ArrayList<String> alt_adresses;
//
//    public ArrayList<String> getAlt_adresses() {
//        return alt_adresses;
//    }
//
//
//    public void setAlt_adresses(ArrayList<String> alt_adresses) {
//        this.alt_adresses = alt_adresses;
//    }

    @SerializedName("is_paying_customer")
    @Expose
    private Boolean isPayingCustomer;
    @SerializedName("orders_count")
    @Expose
    private Integer ordersCount = 0;
    @SerializedName("total_spent")
    @Expose
    private String totalSpent;
    @SerializedName("avatar_url")
    @Expose
    private String avatarUrl;
    @SerializedName("meta_data")
    @Expose
    private List<SmallMetaDatum> meta_data = new ArrayList<>();

    private Uri profilePicture;

    private final static long serialVersionUID = -4568596712841351333L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateCreatedGmt() {
        return dateCreatedGmt;
    }

    public void setDateCreatedGmt(String dateCreatedGmt) {
        this.dateCreatedGmt = dateCreatedGmt;
    }

    public Object getDateModified() {
        return dateModified;
    }

    public void setDateModified(Object dateModified) {
        this.dateModified = dateModified;
    }

    public Object getDateModifiedGmt() {
        return dateModifiedGmt;
    }

    public void setDateModifiedGmt(Object dateModifiedGmt) {
        this.dateModifiedGmt = dateModifiedGmt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public Boolean getIsPayingCustomer() {
        return isPayingCustomer;
    }

    public void setIsPayingCustomer(Boolean isPayingCustomer) {
        this.isPayingCustomer = isPayingCustomer;
    }

    public Integer getOrdersCount() {
        return ordersCount;
    }

    public void setOrdersCount(Integer ordersCount) {
        this.ordersCount = ordersCount;
    }

    public String getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(String totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public List<SmallMetaDatum> getMeta_data() {
        return meta_data;
    }

    public void setMeta_data(List<SmallMetaDatum> meta_data) {
        this.meta_data = meta_data;
    }

    public String getDisplayName(){
        return (this.firstName + " " + this.lastName);
    }

    public Uri getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Uri profilePicture) {
        this.profilePicture = profilePicture;
    }
}
