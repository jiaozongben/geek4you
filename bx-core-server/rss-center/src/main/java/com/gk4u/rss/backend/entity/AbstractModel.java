package com.gk4u.rss.backend.entity;

import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;

/**
 * Abstract model for all entities, defining id and table generator
 */
@SuppressWarnings("serial")

@Getter
@Setter
public abstract class AbstractModel implements Serializable {

    private Long id;
}
