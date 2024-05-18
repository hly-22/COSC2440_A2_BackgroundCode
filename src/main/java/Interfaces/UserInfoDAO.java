package Interfaces;

import Models.Customer.Customer;

public interface UserInfoDAO {

    void displayInfo();
    boolean updatePhone();
    boolean updateAddress();
    boolean updateEmail();
    boolean updatePassword();
}
