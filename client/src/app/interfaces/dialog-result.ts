import {DialogResultStatus} from '../enums/dialog-result-status';

export interface DialogResult {
  message?: string,
  status: DialogResultStatus

}

export class DialogResults {
  public static OK: DialogResult = {status: DialogResultStatus.OK}
  public static CANCEL: DialogResult = {status: DialogResultStatus.CANCEL};

  public static error(error: string): DialogResult {
    return {message: error, status: DialogResultStatus.ERROR};
  }
}
