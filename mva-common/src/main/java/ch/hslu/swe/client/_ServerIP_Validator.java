/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hslu.swe.client;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import org.apache.commons.validator.routines.InetAddressValidator;

/**
 *
 * @author Dave
 */
public class _ServerIP_Validator implements IParameterValidator {

    @Override
    public void validate(String name, String value) throws ParameterException {
        boolean isValid = InetAddressValidator.getInstance().isValid(value);
        if (!isValid) {
            String message = "Keine gültige IP Adresse! (" + value + ")";
            throw new ParameterException(message);
        }
    }
}
