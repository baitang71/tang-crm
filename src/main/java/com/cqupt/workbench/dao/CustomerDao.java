package com.cqupt.workbench.dao;

import com.cqupt.workbench.domain.Customer;

public interface CustomerDao {

    Customer selectByName(String name);

    int save(Customer customer);
}
