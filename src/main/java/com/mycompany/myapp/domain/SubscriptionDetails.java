package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SubscriptionDetails.
 */
@Entity
@Table(name = "subscription_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubscriptionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "subscription_name", nullable = false)
    private String subscriptionName;

    @NotNull
    @Column(name = "subscription_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal subscriptionAmount;

    @NotNull
    @Column(name = "tax_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal taxAmount;

    @NotNull
    @Column(name = "total_amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @NotNull
    @Column(name = "subscription_start_date", nullable = false)
    private LocalDate subscriptionStartDate;

    @NotNull
    @Column(name = "subscription_expiry_date", nullable = false)
    private LocalDate subscriptionExpiryDate;

    @NotNull
    @Column(name = "additional_comments", nullable = false)
    private String additionalComments;

    @NotNull
    @Column(name = "notification_before_expiry", nullable = false)
    private Integer notificationBeforeExpiry;

    @NotNull
    @Column(name = "notification_mute_flag", nullable = false)
    private Boolean notificationMuteFlag;

    @NotNull
    @Column(name = "notification_to", nullable = false)
    private String notificationTo;

    @Column(name = "notification_cc")
    private String notificationCc;

    @Column(name = "notification_bcc")
    private String notificationBcc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subscriptionDetails" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SubscriptionDetails id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubscriptionName() {
        return this.subscriptionName;
    }

    public SubscriptionDetails subscriptionName(String subscriptionName) {
        this.setSubscriptionName(subscriptionName);
        return this;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public BigDecimal getSubscriptionAmount() {
        return this.subscriptionAmount;
    }

    public SubscriptionDetails subscriptionAmount(BigDecimal subscriptionAmount) {
        this.setSubscriptionAmount(subscriptionAmount);
        return this;
    }

    public void setSubscriptionAmount(BigDecimal subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public SubscriptionDetails taxAmount(BigDecimal taxAmount) {
        this.setTaxAmount(taxAmount);
        return this;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public SubscriptionDetails totalAmount(BigDecimal totalAmount) {
        this.setTotalAmount(totalAmount);
        return this;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getSubscriptionStartDate() {
        return this.subscriptionStartDate;
    }

    public SubscriptionDetails subscriptionStartDate(LocalDate subscriptionStartDate) {
        this.setSubscriptionStartDate(subscriptionStartDate);
        return this;
    }

    public void setSubscriptionStartDate(LocalDate subscriptionStartDate) {
        this.subscriptionStartDate = subscriptionStartDate;
    }

    public LocalDate getSubscriptionExpiryDate() {
        return this.subscriptionExpiryDate;
    }

    public SubscriptionDetails subscriptionExpiryDate(LocalDate subscriptionExpiryDate) {
        this.setSubscriptionExpiryDate(subscriptionExpiryDate);
        return this;
    }

    public void setSubscriptionExpiryDate(LocalDate subscriptionExpiryDate) {
        this.subscriptionExpiryDate = subscriptionExpiryDate;
    }

    public String getAdditionalComments() {
        return this.additionalComments;
    }

    public SubscriptionDetails additionalComments(String additionalComments) {
        this.setAdditionalComments(additionalComments);
        return this;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public Integer getNotificationBeforeExpiry() {
        return this.notificationBeforeExpiry;
    }

    public SubscriptionDetails notificationBeforeExpiry(Integer notificationBeforeExpiry) {
        this.setNotificationBeforeExpiry(notificationBeforeExpiry);
        return this;
    }

    public void setNotificationBeforeExpiry(Integer notificationBeforeExpiry) {
        this.notificationBeforeExpiry = notificationBeforeExpiry;
    }

    public Boolean getNotificationMuteFlag() {
        return this.notificationMuteFlag;
    }

    public SubscriptionDetails notificationMuteFlag(Boolean notificationMuteFlag) {
        this.setNotificationMuteFlag(notificationMuteFlag);
        return this;
    }

    public void setNotificationMuteFlag(Boolean notificationMuteFlag) {
        this.notificationMuteFlag = notificationMuteFlag;
    }

    public String getNotificationTo() {
        return this.notificationTo;
    }

    public SubscriptionDetails notificationTo(String notificationTo) {
        this.setNotificationTo(notificationTo);
        return this;
    }

    public void setNotificationTo(String notificationTo) {
        this.notificationTo = notificationTo;
    }

    public String getNotificationCc() {
        return this.notificationCc;
    }

    public SubscriptionDetails notificationCc(String notificationCc) {
        this.setNotificationCc(notificationCc);
        return this;
    }

    public void setNotificationCc(String notificationCc) {
        this.notificationCc = notificationCc;
    }

    public String getNotificationBcc() {
        return this.notificationBcc;
    }

    public SubscriptionDetails notificationBcc(String notificationBcc) {
        this.setNotificationBcc(notificationBcc);
        return this;
    }

    public void setNotificationBcc(String notificationBcc) {
        this.notificationBcc = notificationBcc;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubscriptionDetails category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDetails)) {
            return false;
        }
        return getId() != null && getId().equals(((SubscriptionDetails) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionDetails{" +
            "id=" + getId() +
            ", subscriptionName='" + getSubscriptionName() + "'" +
            ", subscriptionAmount=" + getSubscriptionAmount() +
            ", taxAmount=" + getTaxAmount() +
            ", totalAmount=" + getTotalAmount() +
            ", subscriptionStartDate='" + getSubscriptionStartDate() + "'" +
            ", subscriptionExpiryDate='" + getSubscriptionExpiryDate() + "'" +
            ", additionalComments='" + getAdditionalComments() + "'" +
            ", notificationBeforeExpiry=" + getNotificationBeforeExpiry() +
            ", notificationMuteFlag='" + getNotificationMuteFlag() + "'" +
            ", notificationTo='" + getNotificationTo() + "'" +
            ", notificationCc='" + getNotificationCc() + "'" +
            ", notificationBcc='" + getNotificationBcc() + "'" +
            "}";
    }
}
