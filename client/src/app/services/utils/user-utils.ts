import {FormGroup, ValidatorFn} from '@angular/forms';

export class UserUtils {
  public static passwordCheckValidator(): ValidatorFn {
    return (fg: FormGroup) => {
      const passwordA = fg.get('password');
      const passwordB = fg.get('passwordMatch');
      if (!!passwordA && !!passwordB) {
        if (passwordA.value !== passwordB.value) {
          passwordB.setErrors({...passwordB.errors, notEquivalent: true});
        } else if (!!passwordB.errors) {
          passwordB.setErrors({...passwordB.errors});
        } else {
          passwordB.setErrors(null);
        }
        return passwordB.errors;
      } else {
        return null;
      }
    };
  }
}
