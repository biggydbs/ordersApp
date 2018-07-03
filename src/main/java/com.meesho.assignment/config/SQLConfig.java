package com.meesho.assignment.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * Created by hitesh.jain1 on 03/07/18.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SQLConfig {
    @Valid
    String database;

    @Valid
    String user;

    @Valid
    String password;

    @Valid
    String url;
}
