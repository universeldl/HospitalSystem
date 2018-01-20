package com.hospital.dao;

import com.hospital.domain.Authorization;

public interface AuthorizationDao {

    boolean addAuthorization(Authorization authorization);

    Authorization getAuthorizationByaid(Authorization authorization);

    Authorization updateAuthorization(Authorization authorization);

}
