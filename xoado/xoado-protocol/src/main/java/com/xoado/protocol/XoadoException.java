package com.xoado.protocol;

public class XoadoException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//异常信息
	private Integer code;
    public String message;
    private Object object;
    
    public XoadoException(Integer code,String message) {
        this.code = code;
        this.message=message;
    }
    public XoadoException(Integer code,String message,Object object) {
        this.code = code;
        this.message=message;
        this.object=object;
    }
    public XoadoException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

	public XoadoException() {
		// TODO Auto-generated constructor stub
	}
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the object
	 */
	public Object getObject() {
		return object;
	}
	/**
	 * @param object the object to set
	 */
	public void setObject(Object object) {
		this.object = object;
	}
	

}