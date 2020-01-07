package com.nsc.AmoskiUser.service.impl;


import com.nsc.AmoskiUser.service.ReportingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

@Transactional(transactionManager = "UserTransactionManager")
public class ReportingServiceImpl implements ReportingService {
}
