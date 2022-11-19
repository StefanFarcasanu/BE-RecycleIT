package com.domain.entity;

import com.domain.enums.VoucherStatusEnum;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "vouchers")
public class VoucherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private UserEntity client;

    @ManyToOne
    @JoinColumn(name = "retailer_id", referencedColumnName = "id")
    private UserEntity retailer;

    @Column(name = "value")
    private Double value;

    @Column(name = "details")
    private String details;

    @Column(name = "code")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VoucherStatusEnum status;

    @Column(name = "valid_until")
    private LocalDateTime vaildUntil;

    // Getters are manually created, due to bugs/issues regarding @Entities and Enums.
    public Integer getId() {
        return id;
    }

    public UserEntity getClient() {
        return client;
    }

    public UserEntity getRetailer() {
        return retailer;
    }

    public Double getValue() {
        return value;
    }

    public String getDetails() {
        return details;
    }

    public String getCode() {
        return code;
    }

    public VoucherStatusEnum getStatus() {
        return status;
    }

    public LocalDateTime getVaildUntil() {
        return vaildUntil;
    }
}
