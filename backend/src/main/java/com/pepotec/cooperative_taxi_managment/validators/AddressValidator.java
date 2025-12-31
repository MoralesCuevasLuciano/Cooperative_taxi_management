package com.pepotec.cooperative_taxi_managment.validators;

import com.pepotec.cooperative_taxi_managment.models.dto.address.AddressDTO;
import com.pepotec.cooperative_taxi_managment.exceptions.InvalidDataException;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class AddressValidator {

    private static final Pattern STREET_PATTERN = Pattern.compile("^[\\p{L} ]+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");

    public void validateAddress(AddressDTO address) {
        validateRequiredFields(address);
        validateFormats(address);
    }

    private void validateRequiredFields(AddressDTO address) {
        if (address == null) {
            throw new InvalidDataException("The address cannot be null");
        }
        
        if (address.getStreet() == null || address.getStreet().trim().isEmpty()) {
            throw new InvalidDataException("The street cannot be empty");
        }
        
        if (address.getNumeral() == null || address.getNumeral().trim().isEmpty()) {
            throw new InvalidDataException("The number cannot be empty");
        }
        
        if (address.getCity() == null || address.getCity().trim().isEmpty()) {
            throw new InvalidDataException("The city cannot be empty");
        }
    }

    private void validateFormats(AddressDTO address) {
        // Validar calle
        if (!STREET_PATTERN.matcher(address.getStreet()).matches()) {
            throw new InvalidDataException("The street can only contain letters and spaces");
        }
        
        if (address.getStreet().length() < 2 || address.getStreet().length() > 50) {
            throw new InvalidDataException("The street must have between 2 and 50 characters");
        }
        
        // Validar número
        if (!NUMBER_PATTERN.matcher(address.getNumeral()).matches()) {
            throw new InvalidDataException("The number must contain only digits");
        }
        
        if (address.getNumeral().length() > 5) {
            throw new InvalidDataException("The number must have a maximum of 5 digits");
        }
        
        // Validar ciudad
        if (!STREET_PATTERN.matcher(address.getCity()).matches()) {
            throw new InvalidDataException("The city can only contain letters and spaces");
        }
        
        if (address.getCity().length() < 2 || address.getCity().length() > 50) {
            throw new InvalidDataException("The city must have between 2 and 50 characters");
        }
        
        // Validar piso (opcional)
        if (address.getFloor() != null && !address.getFloor().trim().isEmpty()) {
            if (!NUMBER_PATTERN.matcher(address.getFloor()).matches()) {
                throw new InvalidDataException("The floor must contain only digits");
            }
            if (address.getFloor().length() > 5) {
                throw new InvalidDataException("The floor must have a maximum of 5 digits");
            }
        }
        
        // Validar apartamento (opcional)
        if (address.getApartment() != null && !address.getApartment().trim().isEmpty()) {
            if (address.getApartment().length() > 5) {
                throw new InvalidDataException("The apartment must have a maximum of 5 characters");
            }
        }
    }
}