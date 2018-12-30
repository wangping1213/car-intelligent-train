package com.wp.car_intelligent_train.util;


/**
 * 缓存管理器
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-07-03  9:49
 */

public class CacheManager {
    private static CacheManager instance;

    private CacheManager(){}

    public static CacheManager getInstance(){
        if (instance == null){
            instance = new CacheManager();
        }
        return instance;
    }

}
