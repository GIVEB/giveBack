package ten.give.common.enums;

/**
 * @version 0.0
 * @Author shinywoon
 * Donation Blood Kind
 * if Client input MALE or FEMALE,
 * We Change character M,F
 */

public enum Gender {

    M("MALE"),
    F("FEMALE");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }


}
