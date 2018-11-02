package com.leinao.exception;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.leinao.commons.ResponseMessage;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 404统一返回处理
 * @author  wangshoufa 
 * @date 2018年10月16日 上午11:21:29
 *
 */
@ApiIgnore
@RestController
public class ApiErrorController implements ErrorController {

	private static final String ERROR_PATH="/error";
	
	@RequestMapping(value = ERROR_PATH)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseMessage error() {
		return ResponseMessage.error(404, -4041, null);
	}

	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}
