package switchfully.lms.utility.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utility class with static helpers for validating arguments and
 * logging problems. <p>
 *
 * All methods either return the validated value or throw a
 * {@link RuntimeException}.  Logging is done through SLF4J.
 */
public class Validation {

    private static final Logger logger = LoggerFactory.getLogger(Validation.class);

    private Validation() {
        throw new UnsupportedOperationException("Utility class");
    }

    /** validates an argument of any type and either returns that argument or throws an exception
     * @param argumentToValidate the argument that will be tested on for validation, generic T
     * @param exceptionMessage The exception message to pass to the IllegalArgumentException thrown when invalid
     * @param invalidWhen the predicate that checks for validity of {@code argumentToValidate}, exception is thrown when predicate returns true
     * @throws IllegalArgumentException if {@code argumentToValidate} is invalid after {@code invalidWhen} returns true
     * @return valid argument
     * */
    public static <T> T validateArgument(T argumentToValidate, String exceptionMessage, Predicate<T> invalidWhen) throws IllegalArgumentException {
        if(invalidWhen.test(argumentToValidate)){
            logger.error(exceptionMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
        return argumentToValidate;
    }

    /** validates an argument of any type and either returns that argument or throws an exception
     * @param argumentToValidate the argument that will be tested on for validation, generic T
     * @param exceptionMessage The exception message to pass to the IllegalArgumentException when invalid
     * @param invalidWhen the Boolean that checks for validity of {@code argumentToValidate}, exception is thrown when true
     * @throws IllegalArgumentException if {@code argumentToValidate} is invalid after {@code invalidWhen} return true
     * @return valid argument
     * */
    public static <T> T validateArgumentWithBooleanCondition(T argumentToValidate, String exceptionMessage, Boolean invalidWhen) throws IllegalArgumentException {
        if(invalidWhen){
            logger.error(exceptionMessage);
            throw new IllegalArgumentException(exceptionMessage);
        }
        return argumentToValidate;
    }

    /** validates an argument of any type and either returns that argument or throws an exception
     * @param argumentToValidate the argument that will be tested on for validation, generic T
     * @param exceptionMessage The exception message to apply to the child class of RuntimeException in {@code exceptionFunction}
     * @param invalidWhen the Boolean that checks for validity of {@code argumentToValidate}, exception is thrown when true
     * @param exceptionFunction Function that applies {@code exceptionMessage} to the desired Child class of RuntimeException
     * @throws RuntimeException if {@code argumentToValidate} is invalid after {@code invalidWhen} return true
     * @return valid argument
     * */
    public static <T> T validateArgument(T argumentToValidate, String exceptionMessage, Predicate<T> invalidWhen, Function<String,RuntimeException> exceptionFunction) throws RuntimeException {
        if(invalidWhen.test(argumentToValidate)){
            logger.error(exceptionMessage);
            throw exceptionFunction.apply(exceptionMessage);
        }
        return argumentToValidate;
    }

    /** validates an argument of any type and either returns that argument or throws an exception
     * @param argumentToValidate the argument that will be tested on for validation, generic T
     * @param exceptionMessage The exception message to apply to the child class of RuntimeException in {@code exceptionFunction}
     * @param invalidWhen the predicate that checks for validity of {@code argumentToValidate}, exception is thrown when predicate returns true
     * @param exceptionFunction Function that applies {@code exceptionMessage} to the desired Child class of RuntimeException
     * @throws RuntimeException if {@code argumentToValidate} is invalid after {@code invalidWhen} return true
     * @return valid argument
     * */
    public static <T> T validateArgumentWithBooleanCondition(T argumentToValidate, String exceptionMessage, Boolean invalidWhen, Function<String,RuntimeException> exceptionFunction) throws RuntimeException {
        if(invalidWhen){
            logger.error(exceptionMessage);
            throw exceptionFunction.apply(exceptionMessage);
        }
        return argumentToValidate;
    }

    /** validates whether a string is blank or null and either returns that string or throws an exception
     * @param string The string argument to validate
     * @param exceptionMessage The exception message to apply to the child class of RuntimeException in {@code exceptionFunction}
     * @param exceptionFunction Function that applies {@code exceptionMessage} to the desired Child class of RuntimeException
     * @throws RuntimeException if {@code string} is null or blank
     * @return valid argument
     * */
    public static String validateNonBlank(String string, String exceptionMessage, Function<String, RuntimeException> exceptionFunction) throws RuntimeException {
        return validateArgumentWithBooleanCondition(string, exceptionMessage,string==null||string.isBlank(),exceptionFunction);
    }

    /** Checks if an argument of any type meets a certain condition and logs a warning when it does not
     * @param argumentToValidate The argument to validate
     * @param logMessage The warning to log when {@code argumentToValidate} does not meet the condition
     * @param warnWhen the predicate that checks if {@code argumentToValidate} meets the condition, message is logged when predicate returns true
     * @return argument
     */
    public static <T> T checkArgumentAndLogWarn(T argumentToValidate, String logMessage, Predicate<T> warnWhen) {
        if(warnWhen.test(argumentToValidate)){
            logger.warn(logMessage);
        }
        return argumentToValidate;
    }

    /** Checks if an argument of any type meets a certain condition and logs a warning when it does not
     * @param argumentToValidate The argument to validate
     * @param logMessage The warning to log when {@code argumentToValidate} does not meet the condition
     * @param warnWhen the Boolean that checks if {@code argumentToValidate} meets the condition, message is logged when true
     * @return argument
     */
    public static <T> T logWarningIfConditionMet(T argumentToValidate, String logMessage, boolean warnWhen) {
        if(warnWhen){
            logger.warn(logMessage);
        }
        return argumentToValidate;
    }

}

