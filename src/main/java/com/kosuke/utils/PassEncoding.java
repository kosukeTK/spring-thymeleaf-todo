/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kosuke.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * The PassEncoding class
 *
 * @author kosuke takeuchi
 * @version 1.0
 * Date 2021/8/15.
 */
public class PassEncoding {

    private static PassEncoding passEncoding = new PassEncoding();
    public BCryptPasswordEncoder passwordEncoder;

    public static PassEncoding getInstance() {
        if (passEncoding != null)
            return passEncoding;
        return new PassEncoding();
    }

    private PassEncoding() {
        passwordEncoder = new BCryptPasswordEncoder();
    }


}
