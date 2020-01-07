package com.nsc.Amoski.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

public interface UserManageService{

    String findMathRandom(Serializable name);
    Integer findData(Serializable name);
}