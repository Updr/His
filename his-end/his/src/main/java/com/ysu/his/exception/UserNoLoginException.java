package com.ysu.his.exception;

import com.ysu.his.utils.Constants;

public class UserNoLoginException extends MyException{
  private static final long serialVersionUID = 6445638039342655171L;
  public UserNoLoginException(int code, String message){
    super(code, message);
  }
  public UserNoLoginException(){
    super(Constants.NOT_LOGIN_MSG);
  }
}