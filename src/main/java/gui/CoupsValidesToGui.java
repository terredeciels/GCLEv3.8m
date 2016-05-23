package gui;

import java.util.ArrayList;
import position.FenToGPosition;
import position.GCoups;
import position.GPosition;
import position.ICodage;

public class CoupsValidesToGui implements ICodage {

    private final GPosition gp;
    private String message;

    public CoupsValidesToGui(String f) {
        gp = FenToGPosition.toGPosition(f);
    }

    public void start() {
        gp.getCoupsValides();
        message = toString(gp);
    }

    private String toString(GPosition gp) {
        String traitS = gp.getTrait() == BLANC ? "trait aux Blancs" : "trait aux Noirs";
        String str = gp.getFen() + '\n' + traitS + '\n' + gp.toString() + '\n';

        System.out.println(gp.getFen());
        System.out.println(gp.getTrait() == BLANC ? "trait aux Blancs" : "trait aux Noirs");
        System.out.println(gp.print());
        System.out.println(gp);
        System.out.println();

        return str;
    }

    public String getMessage() {
        return message;
    }

}
