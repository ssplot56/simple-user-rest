package project.module.example.config.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.module.example.data.object.DateRange;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, DateRange> {

    @Override
    public boolean isValid(DateRange dateRange, ConstraintValidatorContext context) {
        if (dateRange == null || dateRange.getStartDate() == null || dateRange.getEndDate() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Both start date and end date are required.")
                    .addConstraintViolation();
            return false;
        }

        return dateRange.getStartDate().isBefore(dateRange.getEndDate());
    }

}
