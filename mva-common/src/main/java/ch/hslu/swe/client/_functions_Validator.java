/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.client;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

/**
 *
 * @author Dave
 */
public class _functions_Validator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        int valueN = Integer.parseInt(value);
        boolean isValid = between(valueN, 1, 9);
        if (!isValid) {
            throw new ParameterException("Wert nicht im Gültigkeitsbereicht [1:9]");
        }
    }

    public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
        if (i >= minValueInclusive && i <= maxValueInclusive) {
            return true;
        } else {
            return false;
        }
    }
}
