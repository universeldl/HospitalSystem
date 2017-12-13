package com.hospital.service;

import com.hospital.domain.Authorization;

public interface AuthorizationService {

	
	boolean addAuthorization(Authorization authorization);

	Authorization getAuthorizationByaid(Authorization authorization);

	Authorization updateAuthorization(Authorization authorization);
}
