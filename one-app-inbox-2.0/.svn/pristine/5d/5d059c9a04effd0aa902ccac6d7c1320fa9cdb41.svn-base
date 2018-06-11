package grails.plugins.springsecurity;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

public class ByteArrayPasswordEncoder implements PasswordEncoder {

	public boolean isPasswordValid(String encPass, String rawPass, Object salt) throws DataAccessException {
		ArrayUtils.isEquals(rawPass.getBytes(), salt);
		return false;
	}

	public String encodePassword(String rawPass, Object salt) throws DataAccessException {
		return rawPass;
	}
}
