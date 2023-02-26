package com.ysu.his.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.ysu.his.utils.Constants;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyException extends RuntimeException {
  private static final long serialVersionUID = 6445638039342655172L;
  private Integer code;
  private String message;
  public MyException(int code, String message) {
    super(message);
    this.message = message;
    this.code = code;
  }

  public MyException(String message){
    super(message);
    this.message = message;
    this.code = Constants.DEFAULT_EXCEPTION_CODE;
  }

  public MyException(){
    super(Constants.DEFAULT_EXCEPTION_MSG);
    this.message = Constants.DEFAULT_EXCEPTION_MSG;
    this.code = Constants.DEFAULT_EXCEPTION_CODE;
  }
}