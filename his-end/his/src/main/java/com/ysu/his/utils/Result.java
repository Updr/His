package com.ysu.his.utils;

import java.io.Serializable;

import lombok.Data;

@Data
public class Result implements Serializable{
    private static final long serialVersionUID = -6691656851198107463L;
    private int status;
    private String message;
    private Object data;
}