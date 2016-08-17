package validators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

@FacesValidator("validators.PasswordValidator")
public class PasswordValidator implements Validator {

	@Override
	public void validate(FacesContext context, UIComponent component,
		Object value) throws ValidatorException {

	  String password = value.toString();

	  UIInput uiInputConfirmPassword = (UIInput) component.getAttributes()
		.get("confirmPassword");
	  String confirmPassword = uiInputConfirmPassword.getSubmittedValue()
		.toString();

	  // required="true" will do its job.
	  if (password == null || password.isEmpty() || confirmPassword == null
		|| confirmPassword.isEmpty()) {
			return;
	  }

	  if (!password.equals(confirmPassword)) {
		uiInputConfirmPassword.setValid(false);
		throw new ValidatorException(new FacesMessage(
			"Passwords does not match"));
	  }

	}
}
