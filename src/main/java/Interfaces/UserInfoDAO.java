package Interfaces;

public interface UserInfoDAO {

    void displayInfo();
    boolean updatePhone(String newPhone);
    boolean updateAddress(String newAddress);
    boolean updateEmail(String newEmail);
    boolean updatePassword();
}
