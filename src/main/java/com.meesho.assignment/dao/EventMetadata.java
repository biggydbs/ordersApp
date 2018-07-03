package com.meesho.assignment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hitesh.jain1 on 03/07/18.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMetadata {
    private long timestamp;
    private String schema;
    private String sender;
}
