/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.radnoti.studentmanagementsystem.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author matevoros
 */
@Component
public class ResponseFactory {
    public Map<String, Integer> createResponse(String key, Integer value) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
