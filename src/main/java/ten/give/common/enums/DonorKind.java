package ten.give.common.enums;

/**
 * @version 0.0
 * @Author shinywoon
 * Donation Blood Kind
 * if Client input Number 0 to 3,
 * We Change String for to input donation blood Kind.
 */

public enum DonorKind {

    WHOLE(0),
    PLASMA(1),
    PLATELETS(2),
    PLATELETSPLASMA(3);

    private int kindCode;

    DonorKind(int kindCode) {
        this.kindCode = kindCode;
    }

    public int getKindCode() {
        return kindCode;
    }
}
