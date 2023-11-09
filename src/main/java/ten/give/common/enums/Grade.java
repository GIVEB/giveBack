package ten.give.common.enums;

/**
 * @version 0.0
 * @Author shinywoon
 * User's Grade
 * @deprecated we will update user's Grade Policy
 */

public enum Grade {

    PAPERCUP("papercup"),
    MUGCUP("mugcup"),
    WINEGLASS("wineglass"),
    MILKCARTON("milkcarton"),
    PLASTICCUP("plasticcup"),
    WATERBOTTLE("waterbottle"),
    OILDRUM("oildrum"),
    PURIFIEDTANK("purifiedtank"),
    OILBARREL("oilbarrel"),
    WATERTANK("wartertank");

    private String grade;

    Grade(String grade) {
        this.grade = grade;
    }

    public String getGrade() {
        return grade;
    }

}
