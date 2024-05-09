package Interfaces;

import Models.Customer.Customer;

public interface UserInfoDAO {
    void displayInfo(Customer customer);
    boolean updatePhone(Customer customer, String phone);
    boolean updateAddress(Customer customer, String address);
    boolean updateEmail(Customer customer, String email);
    boolean updatePassword(Customer customer, String password);
}
