package ch.hslu.swe.client;


import ch.hslu.swe.helper.MoebelherstellerEnum;
import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Dave
 */
public class _moebelhersteller_Converter implements IStringConverter<MoebelherstellerEnum> {

    @Override
    public MoebelherstellerEnum convert(String value) {
        switch (value) {
            case "FISCHER":
            case "Fischer":
            case "fischer":
                return MoebelherstellerEnum.FISCHER;
            case "Walker":
            case "WALKER":
            case "walker":
                return MoebelherstellerEnum.WALKER;
            case "Zwissig":
            case "zwissig":
            case "ZWISSIG":
                return MoebelherstellerEnum.ZWISSIG;
            case "TEST":
            case "Test":
            case "test":
                return MoebelherstellerEnum.TEST;
            default:
                throw new ParameterException("Kein gültiger Möbelhersteller! (" + value + ")");
        }
    }
}
