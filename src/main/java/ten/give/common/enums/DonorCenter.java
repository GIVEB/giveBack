package ten.give.common.enums;

/**
 * @version 0.0
 * @Author shinywoon
 * Blood Center name
 * if Client input Number 0 to 4,
 * We Change String for to input blood center.
 */

public enum DonorCenter {

    GANGWON(0),
    GYEONGNAM(1),
    GYEONGBUK(2),
    JEONBUK(3),
    CHUNGNAM(4);

    private int regionCode;

    DonorCenter(int regionCode) {
        this.regionCode = regionCode;
    }

    public int getRegionCode() {
        return regionCode;
    }
}

