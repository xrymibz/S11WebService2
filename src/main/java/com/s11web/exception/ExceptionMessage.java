package com.s11web.exception;

public enum ExceptionMessage {

  DATEBASE_CALL_EXCEPTION("DATEBASE_CALL_EXCEPTION", "数据库调用异常");


  /*** 错误码 */
  private String code;

  /** 错误描述 **/
  private String description;

  /**
   * 构造函数
   * 
   * @param code 代码
   * @param description 描述
   */
  private ExceptionMessage(String code, String description) {
    this.code = code;
    this.description = description;
  }
}
