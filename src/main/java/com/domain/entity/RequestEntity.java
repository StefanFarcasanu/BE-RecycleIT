package com.domain.entity;

import com.domain.enums.StatusEnum;
import com.domain.enums.TypeEnum;
import lombok.*;

import javax.persistence.*;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "requests")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private UserEntity client;

    @ManyToOne
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private UserEntity company;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeEnum type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;

    public Integer getId() {
        return id;
    }

    public UserEntity getClient() {
        return client;
    }

    public UserEntity getCompany() {
        return company;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public TypeEnum getType() {
        return type;
    }

    public StatusEnum getStatus() {
        return status;
    }
}
