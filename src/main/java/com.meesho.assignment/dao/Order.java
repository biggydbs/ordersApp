package com.meesho.assignment.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hitesh.jain1 on 03/07/18.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    public Long orderId;
    public String name;
    public Long userId;
}
