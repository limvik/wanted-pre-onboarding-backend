package com.limvik.backend.domain;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(of = "post")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state", nullable = false)
    private String state;

    @MapsId
    @OneToOne(optional = false)
    private Post post;

}
