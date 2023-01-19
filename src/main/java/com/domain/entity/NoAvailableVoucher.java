package com.domain.entity;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "no_available_vouchers")
public class NoAvailableVoucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "value")
    private Double value;
}
