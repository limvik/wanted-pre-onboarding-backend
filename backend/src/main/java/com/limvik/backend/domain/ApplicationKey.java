package com.limvik.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ApplicationKey implements Serializable {

    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "post_id", updatable = false)
    private Long postId;

}
